package models.infrastructure.persistence.table

import slick.driver.MySQLDriver.api._

class Broadcast(tag: Tag) extends Table[(Int, String, String, String)](tag, "BROADCAST") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def username = column[String]("USERNAME")
  def message = column[String]("MESSAGE")
  def timestamp = column[String]("TIMESTAMP")

  def * = (id, username, message, timestamp)

}