package frontend.components.login

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import frontend.components.Component
import frontend.components.utils.PrimaryLink
import models.users.RouteDefinitions
import org.scalajs.dom.html

final class PostRegister private (userName: String) extends Component[html.Element] {
  val element: ReactiveHtmlElement[html.Element] = section(
    p(s"Thank you, $userName, for registering to Battle For Flatland!"),
    p("You should soon receive an email with a confirmation link to follow in order to confirm you registration."),
    PrimaryLink(RouteDefinitions.loginRoute)("Login")
  )
}

object PostRegister {
  def apply(userName: String): PostRegister = new PostRegister(userName)
}
