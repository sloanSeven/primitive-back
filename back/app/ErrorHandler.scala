
import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._
import javax.inject.Singleton

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    val info = "\nTry,\nchat/\n  list/ (for current users).\n    userNameA (for list of logs)\n  userNameA/log/userNameB (for a log)\n  userNameA/sendTo/userNameB/message (to send a single word message) (yeah i know)"
    Future.successful(
      Status(statusCode)("A client error occurred: " + message + info))
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage))
  }
}