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
  case class Props(name: String)

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

  override def render(): ReactElement = h1(s"${msg()}")

  override def componentDidMount(): Unit =
    initPyramid()
      .map(p => setState(State(Some(p))))
}

object Main {
  def main(args: Array[String]): Unit =
    ReactDOM.render(Pyramidal(name = ""), document.getElementById("root"))
}
