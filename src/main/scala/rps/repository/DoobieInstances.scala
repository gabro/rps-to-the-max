package rps

import doobie.postgres.implicits._
import io.chrisdavenport.fuuid.FUUID
import io.buildo.enumero._
import doobie._
import java.util.UUID

object DoobieInstances {

  implicit def enumeroMeta[E <: CaseEnum](implicit s: CaseEnumSerialization[E]): Meta[E] =
    Meta[String].imap(s.caseFromString(_).get)(s.caseToString)

  implicit val fuuidMeta: Meta[FUUID] =
    Meta[UUID].timap[FUUID](FUUID.fromUUID)(fuuid => FUUID.Unsafe.toUUID(fuuid))

}
