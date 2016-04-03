package models.domain.message

import akka.actor.ActorRef

case class Leave(username: String, actor: ActorRef) {

}
