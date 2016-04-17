package models

import akka.actor.{ActorRef, Actor}
import models.domain.Client
import models.domain.message._
import net.liftweb.json.JsonDSL._
import net.liftweb.json._
import scala.collection._

class ChatManager extends Actor {

  val clients = mutable.Queue[Client]()
  val chatRepository = new ChatRepository

  override def receive: Receive = {
    case Join(username, actor) =>
      val jsonValue =
        ("username" -> username) ~
        ("messageType" -> MessageType.userJoined.toString)
      val json = compact(render(jsonValue))
      notifyAllClients(json)
      addClient(username, actor)
      val clientJson =
        ("users" -> createUsernamesList()) ~
        ("messageType" -> MessageType.listUsers.toString)
      actor ! compact(render(clientJson))
    case broadcast: Broadcast =>
      val jsonValue =
        ("username" -> broadcast.username) ~ ("message" -> broadcast.message) ~
        ("messageType" -> MessageType.broadcast.toString)
      notifyAllClients(compact(render(jsonValue)))
      chatRepository.storeMessage(broadcast.username, broadcast.message)
    case History =>
      chatRepository.getAllMessages
    case Leave(username, actor) =>
      removeClient(username)
      val jsonValue =
        ("username" -> username) ~
        ("messageType" -> MessageType.userLeft.toString)
      notifyAllClients(compact(render(jsonValue)))
  }

  private def addClient(username: String, actor: ActorRef): Unit = {
    clients.enqueue(Client(username, actor))
  }

  private def removeClient(username: String): Unit = {
    clients.dequeueFirst(client => client.username == username)
  }

  private def notifyAllClients(json: String): Unit = {
    clients.foreach(client => client.actor ! json)
  }

  private def createUsernamesList(): mutable.ListBuffer[String] = {
    val clientList = new mutable.ListBuffer[String]()
    clients.foreach(client => clientList.append(client.username))
    clientList
  }
}
