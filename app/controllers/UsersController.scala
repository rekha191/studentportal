
package controllers
import scala.concurrent.ExecutionContext.Implicits.global
import dao.UsersDao
import javax.inject.Inject
import model.Users
import play.api.mvc._
import scala.concurrent.Future

class UsersController @Inject()(usersDao: UsersDao ,cc: ControllerComponents) extends AbstractController(cc) {

  def registration = Action { implicit request =>
    Ok(views.html.registration())
  }

  def registrationSubmit = Action.async { implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    println(s"$bodyOpt")
    val args = bodyOpt.get
    val name = args("name").head
    val email = args("email").head
    val password = args("password").head
    val rollno = args("rollno").head.toInt
    val usersData = Users(None, name, email, password, rollno)
    val insertResult = usersDao.insert(usersData)
    insertResult.map { result =>
      Ok("registered successfully")
    }
  }

  def login = Action { implicit request =>
    Ok(views.html.login())

  }
  def loginValidate = Action.async { implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    println("login bodyOpt::::::::::::"+bodyOpt)
    val args = bodyOpt.get
    val email = args("email").head
    val password = args("password").head
    println("password>>>>>>>>"+password)
    val loginData: Future[Seq[Users]] = usersDao.getByUsername(email, password)
    loginData.map { result =>
      println("result>>>>>>>>>>>>"+result)
      result.nonEmpty match{
        case true =>
          Redirect("/profie").withSession("connected" -> result.head.id.getOrElse(0).toString)
        case false =>
         NotFound("Users or password is not correct")
      }

    }
  }
}





