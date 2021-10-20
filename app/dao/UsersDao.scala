package dao

import javax.inject.Inject
import model.Users
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UsersDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

    import profile.api._

    private class UsersTable(tag: Tag) extends Table[Users](tag, "users") {
      def id = column[Option[String]]("id", O.PrimaryKey , O.AutoInc)

      def name = column[String]("name")

      def email = column[String]("email")
      def password = column[String]("password")
      def rollno = column[Int]("password")
      def * = (id, name, email, password ,rollno) <> (Users.tupled, Users.unapply)
    }


  private val usersTable = TableQuery[UsersTable]

  def insert(user: Users): Future[Unit] = {

    //db.run(usersTable += user).map { _ => () }
    Future(())
    //Future(2)
  }
}

