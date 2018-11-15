val V = new {
  val scala = "2.12.7"
  val http4s = "0.20.0-M2"
  val cats = "1.2"
  val catsEffect = "1.0.0"
  val circe = "0.10.0"
  val fuuid = "0.2.0-M2"
  val enumero = "1.3.0"
  val pureconfig = "0.10.0"
  val doobie = "0.6.0"
}

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := V.scala,
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= Seq(
      // for building routes
      "org.http4s" %% "http4s-dsl" % V.http4s,
      // for actually doing something
      "org.http4s" %% "http4s-blaze-server" % V.http4s,
      // for circe-interop
      "org.http4s" %% "http4s-circe" % V.http4s,
      // for the good stuff
      "org.typelevel" %% "cats-core" % V.cats,
      // for IO, Sync, and friends
      "org.typelevel" %% "cats-effect" % V.catsEffect,
      // for json
      "io.circe" %% "circe-core" % V.circe,
      // for deriving json
      "io.circe" %% "circe-generic" % V.circe,
      // for encoding newtypes
      "io.estatico" %% "newtype" % "0.4.2",
      // for purely functional UUIDs
      "io.chrisdavenport" %% "fuuid" % V.fuuid,
      "io.chrisdavenport" %% "fuuid-http4s" % V.fuuid,
      "io.chrisdavenport" %% "fuuid-circe" % V.fuuid,
      // for purely functional logging
      "io.chrisdavenport" %% "log4cats-slf4j" % "0.2.0",
      // for actually logging something
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      // for narrowing Throwable in MonadError[Throwable, F] to an ADT
      "com.olegpy" %% "meow-mtl" % "0.2.0",
      // for enumeration
      "io.buildo" %% "enumero" % V.enumero,
      "io.buildo" %% "enumero-circe-support" % V.enumero,
      // for loading config...
      "com.github.pureconfig" %% "pureconfig" % V.pureconfig,
      // ...in a purely functional way
      "com.github.pureconfig" %% "pureconfig-cats-effect" % V.pureconfig,
      "org.tpolecat" %% "doobie-core" % V.doobie,
      "org.tpolecat" %% "doobie-postgres" % V.doobie,
      "org.flywaydb" % "flyway-core" % "5.0.7",
    ) ++ Seq(
      "io.monix" %% "minitest" % "2.2.2",
      "com.danielasfregola" %% "random-data-generator-magnolia" % "2.6",
    ).map(_ % Test),
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
  )

lazy val wirogen = project.in(file("wirogen")).settings(
    scalaVersion := V.scala,
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "scalameta" % "4.0.0",
      "com.geirsson" %% "scalafmt-core" % "1.6.0-RC4",
      // due to https://github.com/scalameta/scalafmt/issues/1252
      "org.scala-lang" % "scala-reflect" % V.scala
    ),

)
