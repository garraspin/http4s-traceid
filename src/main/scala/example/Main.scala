import java.util.{Collections, UUID}
import java.util.concurrent.{AbstractExecutorService, TimeUnit}

import cats.effect.IO
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.syntax._
import kamon.executors.util.ContextAwareExecutorService
import kamon.http4s.middleware._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.blaze.Http1Client
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.{Headers, HttpService, Request, Response}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

case class Ping(msg: String)
case class Pong(msg: String)

trait Logging {
  lazy val logger = LoggerFactory.getLogger(this.getClass)
}

object Main extends StreamApp[IO] with Logging {
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    ContextAwareExecutorService(ExecutionContextExecutorServiceBridge(ExecutionContext.global))
  )

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    for {
      client  <- Http1Client.stream[IO]()
      kamonClient = kamon.http4s.middleware.client.KamonSupport(client)
      pingResource = new PingResource(kamon.http4s.middleware.client.KamonSupport(client))
      router  = Router(
        "/" -> HttpService[IO] {
          case req@GET -> Root / "foo" =>
            for {
              _   <- IO { 42 }
              t1   = Thread.currentThread().getName
              res <- kamonClient.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
              t2   = Thread.currentThread().getName
              _    = if (t1 != t2) logger.info(s"No TraceID when jumping from $t1 to $t2")
                r   <- Ok(s"GET foo and $res")
            } yield r

          case request@POST -> Root / "foo" =>
            implicit val requestDecoder = jsonOf[IO, Ping]
            implicit val responseEncoder = jsonEncoderOf[IO, Pong]
            for {
              _   <- IO { logger.info("POST foo") }
              req <- request.as[Ping]
              _    = logger.info("POST foo headers: " + request.headers.map(h => h.name -> h.value).mkString(", "))
              res <- client.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
              r   <- Ok(s"POST foo ${req.msg} and $res")
            } yield r

          case req@GET -> Root / "bar" =>
//            logger.info("bar hs: " + req.headers.map(h => h.name -> h.value).mkString(", "))
            Ok("bar")
        },
        "/ping" -> pingResource.endpoints
      )

      exitCode <- BlazeBuilder[IO]
        .bindHttp(9001, "0.0.0.0")
        .mountService(server.KamonSupport(router))
        .serve
    } yield exitCode
  }
}

class PingResource(client: Client[IO]) extends RequestHandling2 { //with Logging {

  def endpoints: HttpService[IO] = HttpService[IO] {
    case request@POST -> Root => handleRequest(request)(postPing)

    case request@POST -> Root / "pass0" =>
      implicit val requestDecoder = jsonOf[IO, Ping]
      implicit val responseEncoder = jsonEncoderOf[IO, Pong]
      for {
        _   <- IO { logger.info("POST foo") }
        req <- request.as[Ping]
        _    = logger.info("POST foo headers: " + request.headers.map(h => h.name -> h.value).mkString(", "))
        res <- client.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
        _    = logger.info("ping post client call")
        r   <- Ok(s"POST foo ${req.msg} and $res")
      } yield r

    case request@POST -> Root / "pass1" =>
      handleRequestInternal(request)

    case request@POST -> Root / "fail0" =>
      handleRequestGeneric[Ping, Pong](request)

    case request@POST -> Root / "fail1" =>
      handleRequestWithHandler[Ping, Pong](request) { ping =>
        for {
          _   <- IO { 42 }
          _    = logger.info("ping pre client call")
          res <- client.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
          _    = logger.info("ping post client call")
        } yield Pong(res)
      }
  }

  def handleRequestInternal(request: Request[IO]): IO[Response[IO]] = {
    implicit val requestDecoder = jsonOf[IO, Ping]
    implicit val responseEncoder = jsonEncoderOf[IO, Pong]
    for {
      _   <- IO { logger.info("POST foo") }
      req <- request.as[Ping]
      _    = logger.info("POST foo headers: " + request.headers.map(h => h.name -> h.value).mkString(", "))
      res <- client.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
      _    = logger.info("POST foo post client call")
      r   <- Ok(s"POST foo ${req.msg} and $res")
    } yield r
  }

  def handleRequestGeneric[REQ : Decoder, RESP : Encoder](request: Request[IO]): IO[Response[IO]] = {
    implicit val requestDecoder = jsonOf[IO, REQ]
    implicit val responseEncoder = jsonEncoderOf[IO, RESP]
    for {
      _   <- IO { logger.info("POST foo") }
      req <- request.as[REQ]
      _    = logger.info("POST foo headers: " + request.headers.map(h => h.name -> h.value).mkString(", "))
      res <- client.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
      _    = logger.info("POST foo post client call")
      r   <- Ok(s"POST foo $req and $res")
    } yield r
  }

  def handleRequestWithHandler[REQ : Decoder, RESP : Encoder](request: Request[IO])(handler: REQ => IO[RESP]): IO[Response[IO]] = {
    implicit val requestDecoder = jsonOf[IO, REQ]
    implicit val responseEncoder = jsonEncoderOf[IO, RESP]
    for {
      _   <- IO { logger.info("POST foo") }
      req <- request.as[REQ]
      _    = logger.info("POST foo headers: " + request.headers.map(h => h.name -> h.value).mkString(", "))
      res <- handler(req)
      r   <- Ok.apply(res)
    } yield r
  }

  def postPing(ping: Ping): IO[Pong] = {
    for {
      _   <- IO { 42 }
      _    = logger.info("pre bar call")
      bar <- client.expect[String](Request[IO](method = GET, uri = uri("http://localhost:9001/bar"), headers = Headers.empty))
      _    = logger.info("post bar call")
      res <- IO { Pong(ping.msg + " and " + bar) }
      _    = logger.info("post res")
    } yield res
  }
}

trait RequestHandling {
  this: Logging =>

  def handleRequest[REQ : Decoder, RESP : Encoder](request: Request[IO])(handler: REQ => IO[RESP]): IO[Response[IO]] = {
    implicit val requestDecoder = jsonOf[IO, REQ]
    implicit val responseEncoder = jsonEncoderOf[IO, RESP]

    for {
      _    <- IO { 42 }
      _     = logger.info("pre POST")
      req  <- request.as[REQ]
      _     = logger.info("POST")
      resp <- handler(req)
      _     = logger.info("post POST")
      r    <- Ok(resp.asJson)
    } yield r
  }

}

trait RequestHandling2 extends Logging {

  def handleRequest[REQ : Decoder, RESP : Encoder](request: Request[IO])(handler: REQ => IO[RESP]): IO[Response[IO]] = {
    implicit val requestDecoder = jsonOf[IO, REQ]
    implicit val responseEncoder = jsonEncoderOf[IO, RESP]

    for {
      _    <- IO { 42 }
      _     = logger.info("pre POST")
      req  <- request.as[REQ]
      _     = logger.info("POST")
      resp <- handler(req)
      _     = logger.info("post POST")
      r    <- Ok(resp.asJson)
    } yield r
  }

}

object ExecutionContextExecutorServiceBridge {
  def apply(ec: ExecutionContext): ExecutionContextExecutorService = ec match {
    case null => throw null
    case eces: ExecutionContextExecutorService => eces
    case other => new AbstractExecutorService with ExecutionContextExecutorService {
      override def prepare(): ExecutionContext = other
      override def isShutdown = false
      override def isTerminated = false
      override def shutdown() = ()
      override def shutdownNow() = Collections.emptyList[Runnable]
      override def execute(runnable: Runnable): Unit = other execute runnable
      override def reportFailure(t: Throwable): Unit = other reportFailure t
      override def awaitTermination(length: Long,unit: TimeUnit): Boolean = false
    }
  }
}
