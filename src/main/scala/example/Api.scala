package example

import cats.effect.IO
import example.Main.port
import io.circe.generic.auto._
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, EntityEncoder, HttpService, Request, Status, Uri}
import org.http4s.circe._
import org.http4s.client.Client

class Api(client: Client[IO]) extends Logging {
  implicit val pingDecoder: EntityDecoder[IO, Ping] = jsonOf[IO, Ping]
  implicit val pingEncoder: EntityEncoder[IO, Ping] = jsonEncoderOf[IO, Ping]
  implicit val pangDecoder: EntityDecoder[IO, Pang] = jsonOf[IO, Pang]
  implicit val pangEncoder: EntityEncoder[IO, Pang] = jsonEncoderOf[IO, Pang]
  implicit val pengDecoder: EntityDecoder[IO, Peng] = jsonOf[IO, Peng]
  implicit val pengEncoder: EntityEncoder[IO, Peng] = jsonEncoderOf[IO, Peng]
  implicit val pongEncoder: EntityEncoder[IO, Pong] = jsonEncoderOf[IO, Pong]

  def endpoints: HttpService[IO] =
    HttpService {
      case req@POST -> Root / "pong" =>
        for {
          ping     <- req.as[Ping]
          _        <- IO { logger.info(s"Got $ping") }
          pang     <- client.expect[Pang](Request[IO](POST, Uri.unsafeFromString(s"http://localhost:$port/pang")).withBody(ping))
          _        <- IO { logger.info(s"Got $pang") }
          peng     <- client.expect[Peng](Request[IO](POST, Uri.unsafeFromString(s"http://localhost:$port/peng")).withBody(ping))
          _        <- IO { logger.info(s"Got $peng") }
          pong     <- IO { Pong(s"${ping.msg} ${pang.msg} ${peng.msg}") }
          _        <- IO { logger.info(s"Responding $pong") }
          response <- Ok(pong)
        } yield response

      case req@POST -> Root / "pang" =>
        for {
          pang     <- IO { Pang("pang") }
          _        <- IO { logger.info("Pang HS: " + req.headers.map(_.value).mkString(",")) }
          response <- Ok(pang)
        } yield response

      case req@POST -> Root / "peng" =>
        for {
          peng     <- IO { Peng("peng") }
          _        <- IO { logger.info("Peng HS: " + req.headers.map(_.value).mkString(",")) }
          response <- Ok(peng)
        } yield response
    }

}
