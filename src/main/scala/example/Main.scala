package example

import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, TimeUnit}

import cats.effect.IO
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kamon.executors.util.ContextAwareExecutorService
import kamon.http4s.middleware._
import org.http4s.client.blaze.Http1Client
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object Main extends StreamApp[IO] with Logging {
  val port = 9000

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

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    ContextAwareExecutorService(ExecutionContextExecutorServiceBridge(ExecutionContext.global))
  )

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    for {
      http4sclient <- Http1Client.stream[IO]()
      client        = HttpClient(kamon.http4s.middleware.client.KamonSupport(http4sclient))
      api           = new Api(client)
      router        = Router("/" -> api.endpoints)
      exitCode     <- BlazeBuilder[IO]
        .bindHttp(port, "0.0.0.0")
        .mountService(server.KamonSupport(router))
        .serve
    } yield exitCode
  }
}
