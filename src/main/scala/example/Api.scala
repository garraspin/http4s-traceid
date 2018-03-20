package example

import cats.effect.IO
import example.Main.port
import io.circe.generic.auto._
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, EntityEncoder, HttpService, Status, Uri}
import org.http4s.circe._

class Api(client: HttpClient) extends Logging {

  def endpoints: HttpService[IO] =
    HttpService {
      case req@POST -> Root / "pong" =>
        implicit val decoder: EntityDecoder[IO, Ping] = jsonOf[IO, Ping]
        implicit val encoder: EntityEncoder[IO, Pong] = jsonEncoderOf[IO, Pong]
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
          _        <- IO { logger.info("Pang HS: " + req.headers.map(_.value).mkString(",")) }
          response <- Ok(pang)
        } yield response

      case req@POST -> Root / "peng" =>
        implicit val decoder: EntityDecoder[IO, Ping] = jsonOf[IO, Ping]
        implicit val encoder: EntityEncoder[IO, Peng] = jsonEncoderOf[IO, Peng]
        for {
          peng     <- IO { Peng("peng") }
          _        <- IO { logger.info("Peng HS: " + req.headers.map(_.value).mkString(",")) }
          response <- Ok(peng)
        } yield response
    }

}
