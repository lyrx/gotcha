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


@react class Pyramidal extends StatelessComponent  {
  case class Props(pyramidOpt: Option[Pyramid])


  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)
  implicit val isTest: Boolean = true

  def initPyramid()(implicit executionContext: ExecutionContext) =
    Config
      .createFuture()
      .flatMap(
        Pyramid(_)
          .loadPharaohKey())

  override def render(): ReactElement ={
    implicit val pyrOpt:Option[Pyramid] = props.pyramidOpt
    div(id:="wrapper")(
      ReactElements.sidebar(),
      ReactElements.contentWrapper()
    )
  }

  override def componentDidMount(): Unit = {
    initPyramid()
      .map(p => ReactElements
        .renderAll(Pyramidal(
          Some(
            new Pyramid(
              p
                .config
                .withMessage
                ("Eternalize Your Documents In The Pyramid!"))))))
  }
}

object Main {
  def main(args: Array[String]): Unit =
    ReactElements.renderAll(Pyramidal(None))
}