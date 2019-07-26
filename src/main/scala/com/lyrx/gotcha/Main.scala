package com.lyrx.gotcha

import com.lyrx.pyramids.stellarsdk.Timeout
import com.lyrx.pyramids.{Config, Pyramid}
import org.scalajs.dom.document
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM
import slinky.web.html._

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import js.Dynamic.{literal => l}
import scala.scalajs.js.annotation.ScalaJSDefined
@react class Pyramidal extends Component {
  case class State(pyramidOpt: Option[Pyramid])
  type Props = Unit

  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)
  implicit val isTest: Boolean = true

  def initPyramid()(implicit executionContext: ExecutionContext) =
    Config
      .createFuture()
      .flatMap(
        Pyramid(_)
          .loadPharaohKey())

  override def initialState: State = State(None)

  def msg() = state.pyramidOpt.map(_.config.frontendData.message).getOrElse("")

  def content() = div(id := "content")(
    nav(
      className := "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow")(
      button(id := "sidebarToggleTop",
        className := "btn btn-link d-md-none rounded-circle mr-3")(
        i(className := "fa fa-bars")),
      form(
        className := "d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search")(
        div(className := "input-group")(
          img(src := "img/stellar.png"),
          input(
            `type` := "password",
            className := "form-control bg-light border-0 small",
            placeholder := "Stellar Private Key"
          )
        )
      )
    ),
    div(className := "container-fluid")
  )

  /*

<footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="my-auto">
                    <span id="status-messages">fsdfsdfsdsf</span>
                </div>
            </div>
        </footer>


   */

  def contentWrapper() =     div( id:= "content-wrapper", className:= "d-flex flex-column")(
    content(),
    footer(className := "sticky-footer bg-white")(
      div(className := "container my-auto")(
        div(className := "my-auto")(
          span(id := "status-messages")("FDSdsfsdffsdsdf")
        )
      )
    ))



  override def render(): ReactElement = h1(s"${msg()}")

  override def componentDidMount(): Unit =
    initPyramid()
      .map(p => setState(State(Some(new Pyramid(p.config.withMessage("Eternalize Your Documents In The Pyramid!"))))))
}

object Main {
  def main(args: Array[String]): Unit =
    ReactDOM.render(Pyramidal(), document.getElementById("root"))
}
