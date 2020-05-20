package gamelogic.buffs

import gamelogic.entities.Entity
import gamelogic.gamestate.GameAction.Id
import gamelogic.gamestate.gameactions.EntityTakesDamage
import gamelogic.gamestate.{GameAction, GameState}
import gamelogic.utils.EntityIdGenerator

trait DoT extends TickerBuff {

  /** Entity responsible for putting the DoT on the target */
  val sourceId: Entity.Id

  /**
    * Function specifying the damage that will be dealt after the `timeSinceBeginning`. We allow for a function so that
    * we can be a little bit more general than a constant damage. This can for example allow to make a DoT which deals
    * damage exponentially with respect to time (forcing player to debuff early)
    */
  def damagePerTick(timeSinceBeginning: Long): Double

  final def tickEffect(gameState: GameState, time: Long, entityIdGenerator: EntityIdGenerator): List[GameAction] =
    List(
      EntityTakesDamage(
        0L,
        time,
        bearerId,
        damagePerTick(
          time - appearanceTime
        ),
        sourceId
      )
    )

}

object DoT {

  def constantDot(
      currentTime: Long,
      targetId: Entity.Id,
      _buffId: Buff.Id,
      _duration: Long,
      _tickRate: Long,
      damageOnTick: Double,
      _sourceId: Entity.Id,
      _appearanceTime: Long
  ): DoT = new DoT {
    val buffId: Id = _buffId

    val sourceId: Id = _sourceId

    def damagePerTick(timeSinceBeginning: Id): Double = damageOnTick

    val tickRate: Long       = _tickRate
    val bearerId: Entity.Id  = targetId
    val duration: Long       = _duration
    val appearanceTime: Long = _appearanceTime
    val lastTickTime: Long   = currentTime // should a ticker tick when it pops? I don't think so
    def changeLastTickTime(time: Id): TickerBuff = constantDot(
      time,
      targetId,
      _buffId,
      _duration,
      _tickRate,
      damageOnTick,
      _sourceId,
      _appearanceTime
    )
  }

}
