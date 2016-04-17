package models.domain.message

object MessageType extends Enumeration {
  val userJoined, userLeft, listUsers, broadcast = Value
}
