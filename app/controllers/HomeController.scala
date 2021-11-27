
package controllers

import javax.inject.Inject
import utils.DateTime

import play.api.mvc._

class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action { implicit request =>
    //request.
    Ok(views.html.login()).withNewSession
  }
}



