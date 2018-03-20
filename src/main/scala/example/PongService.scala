package example

import cats.effect.IO
import example.Main.port
import io.circe.generic.auto._
import org.http4s.dsl.io.{->, /, POST, Root}
import org.http4s.{HttpService, Uri}

class PongService(client: HttpClient) extends RequestHandling {

  def endpoints: HttpService[IO] =
    HttpService {
      case req@POST -> Root => handleRequest[Ping, Pong](req)(pong)
    }

  def pong(ping: Ping): IO[Pong] = for {
    pang <- client.post[Ping, Pang](Uri.unsafeFromString(s"http://localhost:$port/pang"), ping)
    peng <- client.post[Ping, Peng](Uri.unsafeFromString(s"http://localhost:$port/peng"), ping)
    pong <- IO { Pong(s"${ping.msg} ${pang.msg} ${peng.msg}") }
  } yield pong

}
