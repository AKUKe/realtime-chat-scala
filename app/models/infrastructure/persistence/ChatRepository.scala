package models.infrastructure.persistence

import models.infrastructure.persistence.table.Broadcast
import slick.driver.H2Driver.api._

import scala.concurrent.Future

class ChatRepository {

  private[this] val db = Database.forConfig("h2mem1")
  private[this] val messages = TableQuery[Broadcast]

  private[this] val setup = DBIO.seq(
      messages.schema.create
  )

  private[this] val setupFuture: Future[Unit] = db.run(setup)

  def storeMessage(username: String, message: String, timestamp: String): Unit = {
    val query = messages += (0, username, message, timestamp)
    db.run(query)
  }

  def getAllMessages: Future[Seq[(Int, String, String, String)]] = {
    val query = messages.result
    db.run(query)
  }
}
