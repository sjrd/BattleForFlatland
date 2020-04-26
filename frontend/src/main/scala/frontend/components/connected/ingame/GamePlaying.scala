package frontend.components.connected.ingame

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import frontend.components.LifecycleComponent
import io.circe.syntax._
import models.bff.Routes._
import models.bff.ingame.InGameWSProtocol
import models.bff.ingame.InGameWSProtocol.{Ping, Pong}
import models.users.User
import org.scalajs.dom.html
import utils.websocket.JsonWebSocket
import zio.{Task, UIO, ZIO}
import utils.laminarzio.Implicits._

final class GamePlaying private (gameId: String, user: User, token: String) extends LifecycleComponent[html.Div] {

  private val layer = zio.clock.Clock.live

  final val gameSocket = JsonWebSocket[InGameWSProtocol, InGameWSProtocol, (String, String)](
    joinGameServer,
    userIdAndTokenParams,
    (user.userId, token),
    host = "localhost:22222" // todo: change this!
  )

  def sendPing(ping: Ping)(implicit owner: Owner): UIO[Pong] =
    for {
      pongFiber <- ZIO
        .effectAsync[Any, Nothing, Pong](
          callback => gameSocket.$in.collect { case pong: Pong => pong }.map(UIO(_)).foreach(callback)
        )
        .fork
      _ <- ZIO.effectTotal(gameSocket.outWriter.onNext(ping))
      pong <- pongFiber.join
    } yield pong

  val elem: ReactiveHtmlElement[html.Div] = div(
    pre(
      child.text <-- gameSocket.$in.fold(List[InGameWSProtocol]())(_ :+ _)
        .map(_.map(_.asJson.spaces2))
        .map(_.mkString("\n"))
    )
  )

  override def componentDidMount(): Unit = {
    println(s"Game id: $gameId.")
    gameSocket
      .open()(elem)

    gameSocket.$open.flatMap(
      _ =>
        EventStream.fromZIOEffect(
          programs.frontend.ingame
            .synchronizeClock(sendPing(_)(elem))
            .provideLayer(layer)
        )
    ).foreach(delta => println(s"Delta is: $delta"))(elem)

  }

}

object GamePlaying {
  def apply(gameId: String, user: User, token: String) = new GamePlaying(gameId, user, token)
}
