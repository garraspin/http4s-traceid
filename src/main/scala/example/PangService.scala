package example

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.dsl.io.{->, POST, Root}

class PangService extends RequestHandling {

  def endpoints: HttpService[IO] =
    HttpService {
      case req@POST -> Root => handleRequest[Ping, Pang](req)(pang)
    }

  def pang(ping: Ping): IO[Pang] = IO { Pang("pang") }

}
