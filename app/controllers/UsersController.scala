
package controllers
import scala.concurrent.ExecutionContext.Implicits.global
import dao.UsersDao
import javax.inject.Inject
import model.Users
import play.api.mvc._
import scala.concurrent.Future

class UsersController @Inject()(usersDao: UsersDao ,cc: ControllerComponents) extends AbstractController(cc) {

  def registration = Action {implicit request =>
    Ok(views.html.registration())
  }

  def registrationSubmit = Action.async{ implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    val args = bodyOpt.get
    val name = args("name").head
    //println(s"bodyOpt=>${bodyOpt}")
      val email=args("email").head
      println(s"$bodyOpt")
      val password = args("password").head
      val rollno=args("rollno").head.toInt
      val usersData=Users(None,name,email,password ,rollno)
      val insertResult = usersDao.insert(usersData)
      insertResult.map { result =>
        Ok("registered successfuly")
      }
    }
  }





