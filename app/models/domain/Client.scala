package models.domain

import akka.actor.ActorRef

case class Client(username: String, actor: ActorRef) {

}
