package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import models.ChatActor
import play.api.Logger
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Action, Controller, WebSocket}

class ChatController @Inject() (implicit system: ActorSystem, mat: Materializer)
  extends Controller {

  def showRoom(username: String) = Action {
    implicit request =>
      Ok(views.html.index(username))
  }

  def chatSocket(username: String) = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => ChatActor.props(username, out))
  }

}
