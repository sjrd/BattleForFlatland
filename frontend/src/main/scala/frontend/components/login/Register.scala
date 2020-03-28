package frontend.components.login

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import frontend.components.Component
import frontend.router.{Link, RouteDefinitions}
import org.scalajs.dom.html
import org.scalajs.dom.html.Div

final class Register private () extends Component[html.Div] {
  override val element: ReactiveHtmlElement[Div] = div(
    className := "Register",
    h1("Sign-up"),
    RegisterForm(),
    Link(RouteDefinitions.loginRoute)("Login")
  )
}

object Register {
  def apply(): Register = new Register
}
