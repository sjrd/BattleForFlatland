package models.bff

import urldsl.language.PathSegment.dummyErrorImpl._
import urldsl.language.QueryParameters.dummyErrorImpl._

object Routes {

  final val bff = root / "bff"

  final val allGames    = bff / "games" // get
  final val newMenuGame = bff / "new-game" // post

  final val joinGame      = bff / "join-game" // post
  final val joinGameParam = param[String]("gameId")
  final val gameJoined    = bff / "game-joined"
  final val amIPlaying    = bff / "am-i-playing" // get
  final val gameInfo      = bff / "game-info" // get
  final val gameJoinedWS  = bff / "game-anti-chamber" // web socket
  final val cancelGame    = bff / "cancel-game" // post
  final val startGame     = bff / "start-game" // post
  final val iAmStilThere  = bff / "i-am-still-there" // post
  final val leaveGame     = bff / "leave-game" // post

  final val gameIdParam = param[String]("gameId")

}
