package models

import akka.actor.Actor
import models.domain.Client
import models.domain.message.{Leave, Broadcast, Join}

class ChatManager extends Actor {

  val clients = collection.mutable.Queue[Client]()

  override def receive: Receive = {
    case Join(username, actor) =>
      clients.enqueue(Client(username, actor))
    case Broadcast(username, message) =>
      clients.foreach(client => client.actor ! message)
    case Leave(username, actor) =>
      clients.dequeueFirst(_ == username)
  }

}
