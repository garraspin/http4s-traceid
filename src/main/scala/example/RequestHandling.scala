package example

import java.util.UUID

import cats.data.{Kleisli, OptionT}
import cats.effect.{Effect, IO}
import cats.implicits._
import io.circe._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.util.CaseInsensitiveString

trait RequestHandling extends Logging {

  def handleRequest[REQ: Decoder, RESP: Encoder](request: Request[IO])(handler: REQ => IO[RESP]): IO[Response[IO]] = (for {
    decodedRequest <- decodeRequest[REQ](request)
    rawResponse    <- handler(decodedRequest)
    response       <- createResponse(Ok, rawResponse.asJson)
    _              <- IO { logger.info(s"${request.method} ${request.uri} ${response.status.code} ${rawResponse.asJson}") }
  } yield response).recoverWith(errorHandler(request))

  private def decodeRequest[REQ: Decoder](request: Request[IO]): IO[REQ] =
    for {
      bodyAsJson <- request.as(Effect[IO], jsonDecoder)
      result     <- bodyAsJson.as[REQ] match {
        case Left(e)  => IO.raiseError(new RuntimeException(e))
        case Right(a) => IO.pure(a)
      }
      _           <- IO { logger.info(s"${request.method} ${request.uri} $bodyAsJson") }
    } yield result

  private def errorHandler(request: Request[IO]): PartialFunction[Throwable, IO[Response[IO]]] = {
    case e               => handleError(request, e)
  }

  private def handleError(request: Request[IO], error: Throwable): IO[Response[IO]] = for {
    response <- createResponse(InternalServerError, error.getMessage.asJson)
    _        <- IO { logger.error(error.getMessage, error) }
    _        <- IO { logger.info(s"${request.method} ${request.uri} ${response.status.code} ${error.getMessage.asJson}") }
  } yield response

  private def createResponse(statusCode: Status, body: Json) = Response[IO](status = statusCode).withBody(body)
}
