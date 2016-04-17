package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

class LoginController extends Controller {

  case class LoginData(username: String)

  val loginForm = Form(
      mapping("username" -> text)(LoginData.apply)(LoginData.unapply))

  def showLogin() = Action {
    Ok(views.html.login())
  }

  def login() = Action { implicit request =>
    loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest,
        user => Ok(views.html.main(user.username))
    )
  }
}
