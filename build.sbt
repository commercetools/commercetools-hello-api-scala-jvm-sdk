scalaVersion := "2.11.7"

val commercetoolsJvmSdkCoreVersion = "1.0.0"

val commercetoolsScalaAddOnsVersion = "1.0.0-RC3"

libraryDependencies ++=
  "com.commercetools.sdk.jvm.core" % "commercetools-models" % commercetoolsJvmSdkCoreVersion :: //models like categories and products
  "com.commercetools.sdk.jvm.core" % "commercetools-java-client" % commercetoolsJvmSdkCoreVersion :: //the underlying async Java client
  "com.commercetools.sdk.jvm.core" % "commercetools-convenience" % commercetoolsJvmSdkCoreVersion :: //utility library
  "com.commercetools.sdk.jvm.scala-add-ons" %% "commercetools-scala-client" % commercetoolsScalaAddOnsVersion :: //client which provides Scala Future
  "com.commercetools.sdk.jvm.scala-add-ons" %% "commercetools-scala-models" % commercetoolsScalaAddOnsVersion :: //use Scala lambda function in DSLs
  "com.typesafe" % "config" % "1.3.0" :: //Akka/Play typical configuration system as alternative to Java properties
  Nil