
package controllers

import javax.inject.Inject
import utils.DateTime

import play.api.mvc._

class HomeController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action { implicit request =>
    //request.
    Ok("It works!").withNewSession
  }

  def studentDetail = Action {
    var x = 12
    var y = 40
    var z = x * y

    Ok(s"value of x is: $z")


  }

  def studentName(name: String) = Action {

    //Ok(s"value of x is: $name")
    val currentdate = DateTime.currentDate()

    Ok(s"value of x is: $currentdate")
  }

  def sum(x: Int, y: Int) = Action {



    val z = x + y

    Ok("value of x and y:" + z)

  }

  def studentNames(name: String, roll: Int) = Action {
    Ok(s"value of name and roll no: $name,$roll")

  }

  def hello(name: String) = Action {
    Ok("Hello =" + name)
  }



  def validateLoginGet(username: String, password: String) = Action {
    Ok(s"$username ,$password")
  }



  def registrationSubmit = Action { request =>
    val postvals  = request.body.asFormUrlEncoded
    println(s"postvals=>${postvals}")
    postvals.map { args =>
      val username = args("username").head
      val password = args("password").head
      Ok(s"$username , $password ")
    }.getOrElse(Ok("Oops"))
  }

}



