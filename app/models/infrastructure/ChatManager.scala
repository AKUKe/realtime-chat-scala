package models.infrastructure

import akka.actor.{Actor, ActorRef}
import models.domain.Client
import models.domain.message._
import models.infrastructure.persistence.ChatRepository
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

import scala.collection._
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class ChatManager extends Actor {

  private[this] val clients = mutable.Queue[Client]()
  private[this] val chatRepository = new ChatRepository

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
          ("messageType" -> MessageType.broadcast.toString) ~
          ("timestamp" -> broadcast.timestamp)
      notifyAllClients(compact(render(jsonValue)))
      chatRepository.storeMessage(broadcast.username, broadcast.message, broadcast.timestamp)
    case History(actor) =>
      val historyFuture = chatRepository.getAllMessages
      historyFuture.onComplete {
        case Success(history) =>
          val jsonValue = ("history" -> createMessageHistoryList(history)) ~
            ("messageType" -> MessageType.history.toString)
          actor ! compact(render(jsonValue))
        case Failure(f) =>
          val jsonValue = ("history" -> f.toString) ~
            ("messageType" -> MessageType.history.toString)
          actor ! compact(render(jsonValue))
      }
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

  private def createMessageHistoryList(messages: Seq[(Int, String, String, String)]): mutable.ListBuffer[String] = {
    val messageList = new ListBuffer[String]()
    messages.foreach(message => messageList.append(message._2 + " " + message._4 + " said: " + message._3))
    messageList
  }

  private def createUsernamesList(): mutable.ListBuffer[String] = {
    val clientList = new mutable.ListBuffer[String]()
    clients.foreach(client => clientList.append(client.username))
    clientList
  }
}
