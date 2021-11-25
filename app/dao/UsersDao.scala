package dao

import java.sql.Date

import javax.inject.Inject
import model.{Users}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UsersDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class UsersTable(tag: Tag) extends Table[Users](tag, "users") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def email = column[String]("email")

    def password = column[String]("password")

    def rollNumber = column[Int]("roll_number")

    def * = (id, name, email, password, rollNumber) <> (Users.tupled, Users.unapply)
  }


  private val usersTable = TableQuery[UsersTable]

  def insert(user: Users): Future[Unit] = {
    val query = usersTable += user
    db.run(query).map { x => () }
  }

  def getByUsername(email: String, password: String): Future[Seq[Users]] = {
    val query = usersTable.filter(usersT => usersT.email === email && usersT.password === password).result
   db.run(query)
  }

  def update(user: Users): Future[Unit] = {
    val query = usersTable.filter(usersT => usersT.id === user.id).update(user)
    db.run(query).map(_ => ())
  }

  def getById(id: Long): Future[Seq[Users]] = {
    //seect * from users from id = "1";
    val query = usersTable.filter(_.id === id).result
    db.run(query)
  }

  def getAll: Future[Seq[Users]] = {
    // seect * from users;
    val query = usersTable.result
    db.run(query)
  }

  def delete(id: Long): Future[Unit] = {
    val query = usersTable.filter(_.id === id).delete
    db.run(query).map(_ => ())
  }
}


