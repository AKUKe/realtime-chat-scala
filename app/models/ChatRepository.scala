package models

import models.persistence.BroadcastTableDef
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ChatRepository {

  val db = Database.forConfig("h2mem1")
  val messages = TableQuery[BroadcastTableDef]

  val setup = DBIO.seq(
      messages.schema.create
  )

  val setupFuture: Future[Unit] = db.run(setup)

  def storeMessage(username: String, message: String): Unit = {
    val query = messages += (0, username, message)
    val result = db.run(query)
    println("Message Stored in H2 " + result)
    getAllMessages
  }

  def getAllMessages: Unit = {
    val query = messages.result
    val results: Future[Seq[(Int, String, String)]] = db.run(query)
    results.onSuccess {
      case s => println(s"Results: $s")
    }
  }
}
