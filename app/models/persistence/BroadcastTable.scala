package models.persistence

import slick.driver.MySQLDriver.api._

class BroadcastTable(tag: Tag) extends Table[(Int, String, String)](tag, "BROADCAST") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def username = column[String]("USERNAME")
  def message = column[String]("MESSAGE")

  def * = (id, username, message)

}