package example

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.HttpService
import org.http4s.dsl.io.{->, POST, Root}

class PengService extends RequestHandling {

  def endpoints: HttpService[IO] =
    HttpService {
      case req@POST -> Root => handleRequest[Ping, Peng](req)(pang)
    }

  def pang(ping: Ping): IO[Peng] = IO { Peng("peng") }

}
