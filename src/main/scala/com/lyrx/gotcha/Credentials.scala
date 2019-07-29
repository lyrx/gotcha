package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.web.html._

import scala.concurrent.Future

@react class Credentials extends StatelessComponent {

  val fileField = React.createRef[dom.html.Input]

  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  def readBalance(): Unit = {}

  override def componentDidMount(): Unit = {
    readBalance()
  }
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    if (prevProps.pyramidOpt.isEmpty)
      readBalance()
  }

  /*
   <input id="wallet-upload" type="file" name="name" style="display: none;"/>
   */
  override def render(): ReactElement = div()(
    input(
      className :="my-hidden" ,
      `type`:="file",
      ref:=fileField,
      onChange:= (e=>{e.target})
    ),
    div(
      a(href := "#",
        className := "btn btn-light btn-icon-split my-btn",
        onClick:=(e=>props.pyramidOpt.map(_.saveKeys()))
      )(
        span(className := "icon text-white-50")(
          i(className := "fas fa-save")
        ),
        span(className := "text")("Save Credentials"))
    ),
    div(
      a(href := "#", className := "btn btn-light btn-icon-split my-btn")(
        span(className := "icon text-white-50")(
          i(className := "fas fa-upload")
        ),
        span(className := "text")("Upload Credentials"))
    )
  )

}
