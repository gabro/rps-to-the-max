package rps

import cats.effect._
import cats.effect.implicits._
import doobie._
import doobie.implicits._
import doobie.util.testing._
import minitest._
import io.chrisdavenport.fuuid.FUUID
import com.danielasfregola.randomdatagenerator.magnolia.RandomDataGenerator._
import pureconfig._
import pureconfig.generic.auto._

object QuerySuite extends DoobieSuite {
  test("queries work") {
    check(DbGameRepositoryImpl.listQuery)
    check(DbGameRepositoryImpl.storeUpdate(random[FUUID], random[Game]))
  }
}

trait DoobieSuite extends SimpleTestSuite with ScalacheckInstances {

  private val dbConfig = loadConfigOrThrow[DbConfig]("testdb")
  implicit private val cs = IO.contextShift(ExecutionContext.global)
  private val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    dbConfig.url,
    dbConfig.user,
    dbConfig.password
  )

  def check[A: Analyzable](a: A) = checkImpl(Analyzable.unpack(a))
  private def checkImpl(args: AnalysisArgs) = {
    val report = analyzeIO(args, xa).unsafeRunSync
    if (!report.succeeded) {
      fail(
        formatReport(args, report)
          .padLeft("  ")
          .toString
      )
    }
  }

}
