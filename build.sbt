val V = new {
  val scala = "2.12.7"
  val http4s = "0.20.0-M2"
  val cats = "1.2"
  val catsEffect = "1.0.0"
  val circe = "0.10.0"
  val fuuid = "0.2.0-M2"
  val enumero = "1.3.0"
  val pureconfig = "0.10.0"
}

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := V.scala,
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % V.http4s,
      "org.http4s" %% "http4s-blaze-server" % V.http4s,
      "org.http4s" %% "http4s-circe" % V.http4s,
      "org.typelevel" %% "cats-core" % V.cats,
      "org.typelevel" %% "cats-effect" % V.catsEffect,
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-generic" % V.circe,
      "io.estatico" %% "newtype" % "0.4.2",
      "io.chrisdavenport" %% "fuuid" % V.fuuid,
      "io.chrisdavenport" %% "fuuid-http4s" % V.fuuid,
      "io.chrisdavenport" %% "fuuid-circe" % V.fuuid,
      "io.chrisdavenport" %% "log4cats-slf4j" % "0.2.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.olegpy" %% "meow-mtl" % "0.2.0",
      "io.buildo" %% "enumero" % V.enumero,
      "io.buildo" %% "enumero-circe-support" % V.enumero,
      "com.github.pureconfig" %% "pureconfig" % V.pureconfig,
      "com.github.pureconfig" %% "pureconfig-cats-effect" % V.pureconfig
    ) ++ Seq(
      "io.monix" %% "minitest" % "2.2.2"
    ).map(_ % Test),
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  )
