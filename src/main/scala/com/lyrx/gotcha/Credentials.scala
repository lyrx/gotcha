package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.Future

@react class Credentials extends StatelessComponent {
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

  override def render(): ReactElement = div()(
    div(
    a(href := "#", className := "btn btn-light btn-icon-split my-btn")(
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
