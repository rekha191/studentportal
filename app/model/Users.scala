package model

import java.sql.Date

case class Users (id:Option[Long], name:String, email:String, password:String, rollNumber:Int)

