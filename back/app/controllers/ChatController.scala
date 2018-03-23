package controllers

import javax.inject._

import play.api.mvc._
import services.Chat

/**
 * This controller demonstrates how to use dependency injection to
 * bind a component into a controller class. The class creates an
 * `Action` that shows an incrementing count to users. The [[Board]]
 * object is injected by the Guice dependency injection system.
 */
@Singleton
class ChatController @Inject() (
  cc: ControllerComponents,
  d:  Chat) extends AbstractController(cc) {

  /**
   * Create an action that responds with the [[Board]]'s current
   * count. The result is plain text. This `Action` is mapped to
   * `GET /count` requests by an entry in the `routes` config file.
   */
  def chat = Action {
    Ok(views.html.chat("foo"))
  }

  def send(userA: String, userB: String, message: String) = Action {
    d.send(userA, userB, message)
    Ok(d.getLog(userA, userB).mkString("\n"))
  }

  def log(userA: String, userB: String) = Action {
    Ok(d.getLog(userA, userB) + "")
  }

  def list() = Action {
    Ok(d.getUserList().mkString("\n"))
  }

  def listUser(person: String) = Action {
    Ok(d.getUserList(person).mkString("\n"))
  }

  //def next = Action { Ok(d.nextMove().toString) }

}
