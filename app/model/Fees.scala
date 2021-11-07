package model

import java.sql.Date

case class Fees (id: Option[Long], userId:Long, month:String, year:Int , amount:Int, date:Date)