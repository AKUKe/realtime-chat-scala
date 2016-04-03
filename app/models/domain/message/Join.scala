package models.domain.message

import akka.actor.ActorRef

case class Join(username: String, actor: ActorRef) {

}
