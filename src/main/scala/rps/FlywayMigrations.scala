package rps

import cats.implicits._
import cats.effect._
import org.flywaydb.core.Flyway
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect._

class FlywayMigrations[F[_]](implicit F: Sync[F]) {
  private val flyway = new Flyway

  def loadMigrationsConfig(): F[Unit] =
    for {
      dbConfig <- loadConfigF[F, DbConfig]("db")
      _ <- F.delay {
        flyway.setDataSource(
          dbConfig.url,
          dbConfig.user,
          dbConfig.password
        )
      }.void
    } yield ()

  def runMigrationsCli(args: List[String] = List.empty): F[Unit] =
    for {
      flywayConfig <- loadConfigF[F, FlywayConfig]("flyway")
      _ <- F.delay {
        if (args.contains("mocked-data") || flywayConfig.mockedData) {
          flyway.setLocations("db/migration", "db/mock")
        }

        if (args.contains("clean") || flywayConfig.clean) {
          flyway.clean
        }

        if (args.contains("validate") || flywayConfig.validate) {
          flyway.validate
        }

        if (args.contains("migrate") || flywayConfig.migrate) {
          flyway.migrate
        }
      }.void
    } yield ()
}
