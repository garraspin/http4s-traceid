package example

import cats.Monad
import cats.effect.IO
import cats.implicits._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.http4s.Method._
import org.http4s.Status.Successful
import org.http4s._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.Client
import org.http4s.headers._

case class HttpClient(client: Client[IO]) extends Logging {

  def get[RESP : Decoder](uri: Uri): IO[RESP] = getWithErrorHandler(uri)(Map.empty)

  def getWithErrorHandler[RESP : Decoder](uri: Uri)(errorHandler: PartialFunction[Throwable, IO[RESP]]): IO[RESP] =
    doRequest(Request[IO](GET, uri))(errorHandler)

  def post[REQ : Encoder, RESP : Decoder](uri: Uri, body: REQ, headers: Headers = Headers.empty): IO[RESP] =
    postWithErrorHandler(uri, body, headers)(Map.empty)

  def postWithErrorHandler[REQ : Encoder, RESP : Decoder](uri: Uri, body: REQ, headers: Headers = Headers.empty)
                                                         (errorHandler: PartialFunction[Throwable, IO[RESP]]): IO[RESP] =
    Request[IO](POST, uri)
      .withHeaders(headers)
      .withBody(body)(Monad[IO], jsonEncoderOf[IO, REQ])
      .flatMap { request =>
        for {
          _      <- IO { logger.info(s"${request.method} ${request.uri} ${body.asJson}") }
          result <- doRequest(request)(errorHandler)
        } yield result
      }

  private def doRequest[RESP : Decoder](request: Request[IO])(errorHandler: PartialFunction[Throwable, IO[RESP]]): IO[RESP] = {
    implicit val entityDecoder: EntityDecoder[IO, RESP] = jsonOf[IO, RESP]
    client.fetch(withJsonHeaders(request)) {
      case Successful(resp)  => entityDecoder.decode(resp, strict = false).fold(throw _, identity)
      case _                 => IO.raiseError(new RuntimeException(request.uri.renderString))
    }
    .recoverWith(errorHandler)
  }

  private def withJsonHeaders(request: Request[IO]): Request[IO] =
    request
    .putHeaders(Accept(MediaType.`application/json`))
    .putHeaders(`Content-Type`(MediaType.`application/json`))


}
