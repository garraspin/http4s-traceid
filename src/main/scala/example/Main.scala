import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, TimeUnit}

import cats.effect.{Effect, IO}
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.executors.util.ContextAwareExecutorService
import org.http4s.client.blaze.Http1Client
import org.http4s.dsl.io._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeBuilder
import kamon.http4s.middleware._
import org.http4s.HttpService
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object Main extends StreamApp[IO] {
  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
      ContextAwareExecutorService(ExecutionContextExecutorServiceBridge(ExecutionContext.global))
    )
    for {
      client <- Http1Client.stream[IO]().map(kamon.http4s.middleware.client.KamonSupport(_))
      logger  = LoggerFactory.getLogger(this.getClass)
      router  = Router(
        "/" -> HttpService[IO] {
          case req@GET -> Root / "foo" =>
            for {
              _   <- IO.pure(1)
              _    = logger.info("foo hs: " + req.headers.map(h => h.name -> h.value).mkString(", "))
              res <- client.expect[String]("http://localhost:9000/bar")
              r   <- Ok(s"foo and $res")
            } yield r

          case req@GET -> Root / "bar" =>
            logger.info("bar hs: " + req.headers.map(h => h.name -> h.value).mkString(", "))
            Ok("bar")
        }
      )

      exitCode <- BlazeBuilder[IO]
        .bindHttp(9000, "0.0.0.0")
        .mountService(server.KamonSupport(router))
        .serve
    } yield exitCode
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
