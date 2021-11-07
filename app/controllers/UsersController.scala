
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
    //println(s"$bodyOpt")
    val args = bodyOpt.get
    val name = args("name").head
    val email = args("email").head
    val password = args("password").head
    val rollno = args("rollno").head.toInt
    val usersData = Users(None, name, email, password, rollno)
    //println("userdata...........="+usersData)
    val insertResult = usersDao.insert(usersData)
    insertResult.map { result =>
      //println("varesult>>>>>>>>>>="+result)
      Ok("registered successfully")
    }
  }

  def login = Action { implicit request =>
    Ok(views.html.login())
  }

  def loginValidate = Action.async { implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    val args = bodyOpt.get
    val email = args("email").head
    val password = args("password").head
    val loginData: Future[Seq[Users]] = usersDao.getByUsername(email, password)
    loginData.map { result: Seq[Users] =>
    //  println("loginData map >>>>>>>>>>>>" + result)
      //println("result>>>>>>>>>>>>" + result)
      result.nonEmpty match {
        case true => Redirect("/profile").withSession("connected" -> result.head.id.getOrElse(0).toString)
        case false => NotFound("Users or password is not correct")
      }
    }
  }

  def profile = Action { implicit request =>
    Ok(views.html.profile())
  }

  def update = Action.async { implicit request =>
    val id = request.session.get("connected").get.toLong
    val futureResult = usersDao.getById(id)
    futureResult map { res =>
      println("res>>>>>>>>>>>>" + res)
      Ok(views.html.update(res.head))

    }
  }

  def updateSubmit = Action.async { implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    //val id = request.session.get("connected").get.toLong
    val args = bodyOpt.get
    val id = args("id").head.toLong
    val name = args("name").head
    val email = args("email").head
    val password = args("password").head
    val rollno = args("rollno").head.toInt
    val usersData = Users(Some(id), name, email, password, rollno)
    //println("usersData::::::::::::" + usersData)
    val updateResult = usersDao.update(usersData)
    updateResult map { result =>
      Redirect("/update").withSession("connected" -> id.toString)
    }
  }

  def data = Action.async { implicit request =>
    val futureResult = usersDao.getAll
    futureResult map { res =>
      println("res>>>>>>>>>>"+res)
      Ok(views.html.data(res))
    }
  }

  def delete(id: Long) = Action.async { implicit request =>
    val futureResult = usersDao.delete(id)
    futureResult map { res =>
      //println("res>>>>>>>>>>" + res)
      Ok("Deleted")
    }
  }

}



