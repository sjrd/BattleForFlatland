package models.users

import urldsl.language.PathSegment.dummyErrorImpl._

object Routes {

  private val users = root / "users"

  final val donwloadUsers       = users / "users"
  final val confirmRegistration = users / "confirm-registration"
  final val login               = users / "login"
  final val register            = users / "register"
  final val me                  = users / "me"
  final val superUser           = users / "am-i-super-user"
  final val logout              = users / "logout"

}
