package rps

import cats.implicits._
import cats.effect._
import org.http4s.server._
import org.http4s.server.blaze._
import org.http4s.implicits._
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.chrisdavenport.log4cats.Logger
import doobie._

object Boot extends IOApp {

  implicit val logger: Logger[IO] = Slf4jLogger.unsafeCreate[IO]

  val flyway = new FlywayMigrations[IO]

  val service = for {
    dbConfig <- loadConfigF[IO, DbConfig]("db")
  } yield {
    val xa = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver", // driver classname
      dbConfig.url,
      dbConfig.user,
      dbConfig.password
    )
    val gameRepository = new DbGameRepositoryImpl[IO](xa)
    val gameService = new GameServiceImpl[IO](gameRepository)
    val gameRoutes = new GameRoutes(gameService)

    Router(
      "/rps" -> gameRoutes.routes
    ).orNotFound
  }

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- flyway.loadMigrationsConfig
      _ <- flyway.runMigrationsCli(args)
      serverConfig <- loadConfigF[IO, ServerConfig]("server")
      service <- service
      server <- BlazeServerBuilder[IO]
        .bindHttp(serverConfig.port, serverConfig.host)
        .withHttpApp(service)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    } yield server

}
