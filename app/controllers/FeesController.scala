package controllers

import java.sql.Date

import dao.{FeesDao, UsersDao}
import javax.inject.Inject
import model.{Fees, Users}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FeesController @Inject()(feesDao: FeesDao, usersDao: UsersDao ,cc: ControllerComponents) extends AbstractController(cc) {
  def fees = Action.async { implicit request =>
    val sessionId = request.session.get("connected").get.toLong
    usersDao.getById(sessionId) flatMap { session: Seq[Users] =>

      usersDao.getAll map { users =>
        Ok(views.html.payment(users, session.head))
      }
    }
  }

  def feesSubmit = Action.async { implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    val args = bodyOpt.get
    val userId = args("userId").head.toLong
    println(">>>>>>>>>>>>>>>>=" + userId)
    val month = args("month").head
    val year = args("year").head.toInt
    val amount = args("amount").head.toInt
    //println(s"$amount")
    val date = new Date(System.currentTimeMillis())

    val feesData = Fees(None, userId, month, year, amount, date)

    val insertResult = feesDao.insert(feesData)
    println(s"$insertResult")
    insertResult.map { result =>
      Redirect("/feeDetail")
      //Ok("registered successfully")
    }
  }

  def feeDetail = Action.async { implicit request =>
    val sessionId = request.session.get("connected").get.toLong
    usersDao.getById(sessionId) flatMap { session: Seq[Users] =>


      val futureResult: Future[Seq[(Users, Fees)]] = feesDao.getUserFees()
      futureResult map { res: Seq[(Users, Fees)] =>
        //println("res>>>>>>>>>>"+res)
        Ok(views.html.feeDetail(res, session.head))
      }
    }
  }


  def delete(id: Long) = Action.async { implicit request =>
    val futureResult = feesDao.delete(id)
    futureResult map { res =>
      Redirect("/feeDetail")
    }
  }

  def searchFees = Action.async { implicit request =>
    val bodyOpt: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded
    val args: Map[String, Seq[String]] =bodyOpt.get
    val month = args("month").head
    val year = args("year").head.toInt
    //val feeData=Fees(month,year)
    val sessionId = request.session.get("connected").get.toLong
    usersDao.getById(sessionId) flatMap { session: Seq[Users] =>
      feesDao.searchFees(month, year) map { res =>
        Ok(views.html.feeDetail(res, session.head))
      }
    }
  }
}