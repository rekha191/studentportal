
package controllers
import scala.concurrent.ExecutionContext.Implicits.global
import dao.UsersDao
import javax.inject.Inject
import model.Users
import play.api.mvc._
import scala.concurrent.Future

class UsersController @Inject()(usersDao: UsersDao ,cc: ControllerComponents) extends AbstractController(cc) {

  def registration = Action.async { implicit request =>
    val id = request.session.get("connected").get.toLong
    val futureResult = usersDao.getById(id)
    futureResult map { res: Seq[Users] =>
      Ok(views.html.registration(res.head))
    }
  }
  def registrationSubmit = Action.async { implicit request =>
    val bodyOpt = request.body.asFormUrlEncoded
    println(s"$bodyOpt")
    val args = bodyOpt.get
    val name = args("name").head
    val email = args("email").head
    val password = args("password").head
    val rollno = args("rollno").head.toInt
    val usersData: Users = Users(None, name, email, password, rollno)
    val insertResult: Future[Unit] = usersDao.insert(usersData)
    insertResult.map { result: Unit =>
      //Ok("registered successfully")
      Redirect("/studentDetail")
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
    val loginData= usersDao.getByUsername(email, password)
    loginData.map { result: Seq[Users] =>
      result.nonEmpty match {
        case true => Redirect("/dashboard").withSession("connected" -> result.head.id.getOrElse(0).toString)
        case false => NotFound("Users or password is not correct")
      }
    }
  }

  def dashboard = Action.async { implicit request =>
    val id = request.session.get("connected").get.toLong
    val futureResult = usersDao.getById(id)
    futureResult map { res: Seq[Users] =>
      Ok(views.html.dashboard(res.head))
    }
  }


  def update(id:Long) = Action.async { implicit request =>
    //val id = request.session.get("connected").get.toLong
    val futureResult = usersDao.getById(id)
    futureResult map { res =>
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
    val updateResult = usersDao.update(usersData)
    updateResult map { result =>
      Redirect("/studentDetail").withSession("connected" -> id.toString)
    }
  }

  def studentDetail = Action.async { implicit request =>
    val sessionId=request.session.get("connected").get.toLong

    /*for {
      session <-  usersDao.getById(sessionId)
      res <- usersDao.getAll
    } yield  Ok(views.html.studentDetail(res, session.head))*/


    usersDao.getById(sessionId) flatMap { session =>

      usersDao.getAll map { res =>
        Ok(views.html.studentDetail(res, session.head))
      }

    }



  }

  def delete(id: Long) = Action.async { implicit request =>
    val futureResult = usersDao.delete(id)
    futureResult map { res =>
      Redirect("/studentDetail")
    }
  }

}



