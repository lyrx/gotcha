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

  override def render(): ReactElement =
    div(className := "col-xl-3 col-md-6 mb-4")(
      div(className := "card shadow h-100 py-2")(
        div(className := "card-body")(
          div(className := "row no-gutters align-items-center")(
            div(className := "col mr-2")(
              div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                div(className := "h5 mb-0 font-weight-bold text-gray-800")(

                  a(href := "#", className := "btn btn-success btn-icon-split")(
                    span(className := "icon text-white-50")(
                      i(className := "fas fa-save")
                    ),
                    span(className := "text")("Save Credentials to Disk!")))

              ))))))

}
