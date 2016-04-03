package models

import akka.actor.{ActorSystem, Props, ActorRef, Actor}
import models.domain.message.{Leave, Broadcast, Join}
import play.api.Logger

object ChatActor {
  val chatManager = ActorSystem().actorOf(Props[ChatManager])

  def props(username: String, out: ActorRef) = Props(new ChatActor(username, out, chatManager))
}

class ChatActor(username: String, client: ActorRef, chatManager: ActorRef) extends Actor {

  override def preStart() = {
    Logger.info(s"$username connected")
    chatManager ! Join(username, client)
  }

  override def postStop() = {
    Logger.info(s"$username disconnected")
    chatManager ! Leave(username, client)
  }

  def receive = {
    case message: String =>
      chatManager ! Broadcast(username, message)
  }
}
