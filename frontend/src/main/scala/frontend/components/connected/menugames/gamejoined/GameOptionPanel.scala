package frontend.components.connected.menugames.gamejoined

import frontend.components.Component
import org.scalajs.dom.html
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.laminar.api.L._
import frontend.components.utils.tailwind.{primaryColour, primaryColourDark}
import gamelogic.entities.boss.Boss101
import gamelogic.entities.boss.BossEntity
import models.bff.gameantichamber.WebSocketProtocol

final class GameOptionPanel private (socketOutWriter: Observer[WebSocketProtocol]) extends Component[html.Element] {

  val element: ReactiveHtmlElement[html.Element] = section(
    h2(
      className := "text-2xl",
      className := s"text-$primaryColour-$primaryColourDark",
      "Game Options"
    ),
    select(
      BossEntity.allBossesNames.map(name => option(value := name, name)),
      onMountSet(_ => {
        socketOutWriter.onNext(WebSocketProtocol.UpdateBossName(BossEntity.allBossesNames.head))
        value := BossEntity.allBossesNames.head
      }),
      inContext(elem => onChange.mapTo(elem.ref.value).map(WebSocketProtocol.UpdateBossName) --> socketOutWriter)
    )
  )

}

object GameOptionPanel {
  def apply(socketOutWriter: Observer[WebSocketProtocol]): GameOptionPanel = new GameOptionPanel(socketOutWriter)
}
