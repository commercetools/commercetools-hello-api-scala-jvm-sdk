import java.util.Locale._

import com.typesafe.config.ConfigFactory
import io.sphere.sdk.client.{ScalaSphereClient, SphereClientConfig, SphereClientFactory}
import io.sphere.sdk.products.ProductProjection
import io.sphere.sdk.products.queries.ProductProjectionQuery
import io.sphere.sdk.queries.Implicits._
import io.sphere.sdk.queries.PagedQueryResult

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends App {
  val scalaSphereClient = initializeClient()
  val searchRequest = ProductProjectionQuery.ofCurrent
    .withPredicatesScala(_.name.locale(ENGLISH).isPresent)
    .withExpansionPathsScala(_.categories)
    .withLimit(10)
    .withSortScala(_.createdAt.sort.desc)
  val queryResultFuture: Future[PagedQueryResult[ProductProjection]] = scalaSphereClient(searchRequest)

  val queryResult = Await.result(queryResultFuture, 5 seconds)

  for (product <- queryResult.getResults) {
    val output: String = productProjectionToString(product)
    System.out.println("found product " + output)
  }

  scalaSphereClient.close()

  def initializeClient() = {
    val sphereClientConfig = getSphereClientConfig()
    val factory = SphereClientFactory.of()
    val javaClient = factory.createClient(sphereClientConfig)
    ScalaSphereClient(javaClient)
  }

  def getSphereClientConfig(): SphereClientConfig = {
    val conf = ConfigFactory.load().getConfig("commercetools")
    val projectKey = conf.getString("projectKey")
    val clientId = conf.getString("clientId")
    val clientSecret = conf.getString("clientSecret")
    val authUrl = conf.getString("authUrl")
    val apiUrl = conf.getString("apiUrl")
    SphereClientConfig.of(projectKey, clientId, clientSecret, authUrl, apiUrl)
  }

  def productProjectionToString(product: ProductProjection) = {
    import scala.collection.JavaConversions._

    val name = product.getName.get(ENGLISH)
    val categoryNamesString = product.getCategories
      .filter(_.getObj != null)
      .map(_.getObj.getName.find(ENGLISH).orElse("name unknown"))
      .mkString(", ")
    categoryNamesString

    name + " in categories " + categoryNamesString
  }
}
