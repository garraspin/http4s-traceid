package example

import cats.effect.IO
import example.Main.port
import io.circe.generic.auto._
import org.http4s.Status.Ok
import org.http4s.dsl.io.{->, /, POST, Root}
import org.http4s.{EntityDecoder, EntityEncoder, HttpService, Status, Uri}
import org.http4s.circe._

class PongService(client: HttpClient) extends RequestHandling {

  def endpoints: HttpService[IO] =
    HttpService {
      case req@POST -> Root / "ping" =>
        implicit val decoder: EntityDecoder[IO, Ping] = jsonOf[IO, Ping]
        implicit val encoder = jsonEncoderOf[IO, Pong]
        for {
          ping     <- req.as[Ping]
          _        <- IO { logger.info(s"Got $ping") }
          pang     <- client.post[Ping, Pang](Uri.unsafeFromString(s"http://localhost:$port/pang"), ping)
          _        <- IO { logger.info(s"Got $pang") }
          peng     <- client.post[Ping, Peng](Uri.unsafeFromString(s"http://localhost:$port/peng"), ping)
          _        <- IO { logger.info(s"Got $peng") }
          pong     <- IO { Pong(s"${ping.msg} ${pang.msg} ${peng.msg}") }
          _        <- IO { logger.info(s"Responding $pong") }
          response <- Ok(pong)
        } yield response

      case req@POST -> Root / "pang" =>
        implicit val decoder: EntityDecoder[IO, Ping] = jsonOf[IO, Ping]
        implicit val encoder: EntityEncoder[IO, Pang] = jsonEncoderOf[IO, Pang]
        for {
          pang     <- IO { Pang("pang") }
          response <- Ok(pang)
        } yield response

      case req@POST -> Root / "peng" =>
        implicit val decoder: EntityDecoder[IO, Ping] = jsonOf[IO, Ping]
        implicit val encoder: EntityEncoder[IO, Peng] = jsonEncoderOf[IO, Peng]
        for {
          peng     <- IO { Peng("peng") }
          response <- Ok(peng)
        } yield response
    }

}
