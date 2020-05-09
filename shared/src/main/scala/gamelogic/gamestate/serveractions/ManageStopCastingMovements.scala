package gamelogic.gamestate.serveractions
import gamelogic.gamestate.ImmutableActionCollector
import gamelogic.gamestate.gameactions.EntityCastingInterrupted
import gamelogic.utils.{AbilityUseIdGenerator, EntityIdGenerator, GameActionIdGenerator}

/**
  * Entities that are casting, but moving, should stop casting.
  */
final class ManageStopCastingMovements extends ServerAction {
  def apply(
      currentState: ImmutableActionCollector,
      gameActionIdGenerator: GameActionIdGenerator,
      entityIdGenerator: EntityIdGenerator,
      abilityUseIdGenerator: AbilityUseIdGenerator,
      nowGenerator: () => Long
  ): (ImmutableActionCollector, ServerAction.ServerActionOutput) = {
    val startTime = nowGenerator()
    val gameState = currentState.currentGameState

    val entityStoppedCastingActions = gameState.castingEntityInfo
      .map { case (id, info) => (id, gameState.withAbilityEntitiesById(id), info.positionWhenStarted) }
      .flatMap {
        case (id, None, _)                                                    => Some(id)
        case (id, Some(withAbility), position) if position != withAbility.pos => Some(id)
        case _                                                                => None
      }
      .map(id => EntityCastingInterrupted(gameActionIdGenerator(), startTime, id))
      .toList

    val (nextCollector, oldestTime, idsToRemove) = currentState.masterAddAndRemoveActions(entityStoppedCastingActions)

    (nextCollector, ServerAction.ServerActionOutput(entityStoppedCastingActions, oldestTime, idsToRemove))
  }
}