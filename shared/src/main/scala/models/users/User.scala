package models.users

import java.time.LocalDateTime

final case class User(
    userId: String,
    userName: String,
    hashedPassword: String,
    mailAddress: String,
    createdOn: LocalDateTime,
    roles: List[Role]
)

object User {

  def empty: User = User(
    "",
    "",
    "",
    "",
    LocalDateTime.now,
    Nil
  )

}
