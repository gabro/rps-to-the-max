package rps

import cats.effect._
import io.buildo.enumero.{CaseEnum, CaseEnumSerialization}
import io.chrisdavenport.fuuid.FUUID

import org.scalacheck.{Arbitrary, Gen}

import java.time.OffsetDateTime

trait ScalacheckInstances {

  implicit val arbFUUID: Arbitrary[FUUID] = Arbitrary(
    Gen.delay(FUUID.randomFUUID[IO].unsafeRunSync))

  implicit def arbId[A]: Arbitrary[Id[A]] =
    Arbitrary(for {
      fuuid <- Arbitrary.arbitrary[FUUID]
    } yield Id[A](fuuid))

  implicit def arbEnum[E <: CaseEnum](implicit s: CaseEnumSerialization[E]): Arbitrary[E] =
    Arbitrary(Gen.oneOf(s.values.toSeq))

  implicit val arbOffsetDateTime: Arbitrary[OffsetDateTime] =
    Arbitrary(Gen.delay(OffsetDateTime.now))

  implicit val arbString: Arbitrary[String] = Arbitrary(Gen.alphaNumStr)

}
