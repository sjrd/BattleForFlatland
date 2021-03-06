package frontend

import assets.Asset
import com.raquo.laminar.api.L._
import org.scalajs.dom
import zio.{UIO, ZIO}

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}

@JSImport("resources/index.css", JSImport.Default)
@js.native
object IndexCSS extends js.Object

@JSImport("resources/tailwind-index.css", JSImport.Default)
@js.native
object Tailwind extends js.Object

object Main {

  println("css", IndexCSS)
  println("Tailwind", Tailwind)

  Asset

  final val addPageTitle = ZIO.effectTotal {
    dom.document.title = globals.projectName
  }

  final val makeCSS = for {
    head <- ZIO.effectTotal(dom.document.getElementsByTagName("head")(0))
    css <- UIO(CSS) *> UIO(GlobalStyleSheet.textStyleSheet) // touching CSS object
    style <- ZIO.effectTotal(dom.document.createElement("style"))
    _ <- ZIO.effectTotal(style.innerText = css)
    _ <- ZIO.effectTotal(head.appendChild(style))
  } yield ()

  final val createElement = ZIO
    .fromOption(Option(dom.document.getElementById("root")))
    .catchAll { _ =>
      val elem = dom.document.createElement("div")
      elem.id = "root"
      dom.document.body.appendChild(elem)
      UIO(elem)
    }

  final val emptyContainer = ZIO.effect {
    if (scala.scalajs.LinkingInfo.developmentMode) {
      Option(dom.window.asInstanceOf[js.Dynamic].selectDynamic("__laminar_root_unmount"))
        .filter(x => !js.isUndefined(x))
        .map(_.asInstanceOf[js.Function0[Unit]])
        .foreach { _.apply() }
    }
  }

  final val renderAppInContainer = for {
    container <- ZIO.environment[dom.Element]
    reactiveElement <- ZIO.effect(render(container, frontend.components.App()))
    _ <- ZIO.effect(render(container, frontend.components.utils.bootstrap.ModalWindow.modalContainer))
    unmountFunction <- UIO {
      val unmount: js.Function0[js.Any] = () => {
        println("Unmounting previous element...")
        reactiveElement.unmount()
      }

      unmount
    }
    _ <- ZIO.effect(dom.window.asInstanceOf[js.Dynamic].__laminar_root_unmount = unmountFunction)
  } yield reactiveElement

  final val program = for {
    _ <- addPageTitle
    container <- createElement
    _ <- emptyContainer
    _ <- renderAppInContainer.provide(container)
  } yield ()
  @JSExportTopLevel("main")
  def main(): Unit =
    zio.Runtime.default.unsafeRun(program.orDie)
}
