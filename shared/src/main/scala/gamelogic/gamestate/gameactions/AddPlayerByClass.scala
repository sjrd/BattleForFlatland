package gamelogic.gamestate.gameactions

import gamelogic.entities.Entity
import gamelogic.entities.Resource.ResourceAmount
import gamelogic.entities.classes.{Constants, Hexagon, Square}
import gamelogic.gamestate.{GameAction, GameState}
import gamelogic.gamestate.GameAction.Id
import gamelogic.gamestate.statetransformers.{GameStateTransformer, WithPlayer}
import gamelogic.physics.Complex
import models.bff.outofgame.PlayerClasses

final case class AddPlayerByClass(
    id: GameAction.Id,
    time: Long,
    entityId: Entity.Id,
    position: Complex,
    playerClass: PlayerClasses,
    colour: Int
) extends GameAction {
  def createGameStateTransformer(gameState: GameState): GameStateTransformer = new WithPlayer(
    playerClass match {
      case PlayerClasses.Square =>
        Square(
          entityId,
          time,
          position,
          0.0,
          moving = false,
          0.0,
          100,
          colour,
          Map(),
          100,
          Constants.playerSpeed,
          Square.initialResourceAmount
        )
      case PlayerClasses.Hexagon =>
        Hexagon(
          entityId,
          time,
          position,
          0.0,
          moving = false,
          0.0,
          100,
          colour,
          Map(),
          100,
          Constants.playerSpeed,
          Hexagon.initialResourceAmount
        )
    }
  )

  def isLegal(gameState: GameState): Boolean = !gameState.players.isDefinedAt(entityId)

  def changeId(newId: Id): GameAction = copy(id = newId)
}
