package dao

import java.sql.Date

import javax.inject.Inject
import model.{Fees, Users}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class FeesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, usersDao: UsersDao)(implicit executionContext: ExecutionContext)
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

  private val feesTable = TableQuery[StudentTable]
  private val usersTable = TableQuery[usersDao.UsersTable]

  def insert(fee: Fees): Future[Unit] = {
    val query = feesTable += fee
    db.run(query).map { _ => () }
  }

  def getAll: Future[Seq[Fees]] = {
    val query = feesTable.result
    db.run(query)
  }

  def getUserFees(): Future[Seq[(Users, Fees)]] = {
    val query = usersTable.join(feesTable).on(_.id === _.userId).result
    db.run(query)
  }


  def delete(id: Long): Future[Unit] = {
    val query = feesTable.filter(_.id === id).delete
    db.run(query).map(_ => ())
  }

  def searchFees(month: String, year: Int): Future[Seq[(Users, Fees)]] = {
    val feesF = feesTable.filter(feesT => feesT.month === month && feesT.year === year)
    val query = usersTable.join(feesF ).on(_.id === _.userId).result
     // .filter(x => x._2.month === month && x._2.year === year).
    db.run(query)
  }
}

