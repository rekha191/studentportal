package dao

import java.sql.Date

import javax.inject.Inject
import model.{Fees, Users}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class FeesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private class StudentTable(tag: Tag) extends Table[Fees](tag, "fee_details") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("user_id")

    def month = column[String]("month")

    def year = column[Int]("year")

    def amount = column[Int]("amount")

    def date = column[Date]("date")

    def * = (id, userId, month, year, amount, date) <> (Fees.tupled, Fees.unapply)
  }

  private val studentTable = TableQuery[StudentTable]

  def insert(fee: Fees): Future[Unit] = {
    val query = studentTable += fee
    db.run(query).map { _ => () }
  }

  def getAll: Future[Seq[Fees]] = {
    val query = studentTable.result
    db.run(query)
  }
}


