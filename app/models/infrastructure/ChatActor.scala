package models.infrastructure

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import models.domain.message.{Broadcast, History, Join, Leave}
import play.api.Logger

object ChatActor {
  val chatManager = ActorSystem().actorOf(Props[ChatManager])

  def props(username: String, out: ActorRef) =
    Props(new ChatActor(username, out, chatManager))
}

class ChatActor(username: String, client: ActorRef, chatManager: ActorRef)
  extends Actor {

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
      if (message.equals("history")) chatManager ! History(client)
      else {
        val time = java.time.LocalDateTime.now()
        chatManager ! Broadcast(username, message, time.getHour + ":" +
          time.getMinute + ":" + time.getSecond)
      }
  }
}
