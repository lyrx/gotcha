package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import org.scalajs.dom.{Event, File}
import org.scalajs.dom.raw.{Blob, EventTarget}
import slinky.core.{Component, StatelessComponent, SyntheticEvent}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.web.html._






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


  override def render(): ReactElement =
    div(className := "card shadow mb-4")(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Upload and save credentials"
        )
      ),
      div(className := "my-card-body")(
        div(
          input(
            className := "my-fileselector",
            `type` := "file",
            ref := fileField,
            onChange := (
            _.target
              .fileOpt()
              .map(f=>println(s"File: ${f.name}"))))
        ),
        div(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (e => props.pyramidOpt.map(_.saveKeys())))(
            span()(
              i(className := "fas fa-save"),
              "Save credentials"
            ))
        )
      )
    )

  /*

    div()(
    div(
      a(href := "#", className:="btn my-btn btn-icon-split",
        onClick:=(e=>props.pyramidOpt.map(_.saveKeys()))
      )(
        span()(
          i(className := "fas fa-save"),
          "Save credentials"
        ))
    ),
    div(
      input(
          className :="my-fileselector" ,
          `type`:="file",
          ref:=fileField,
          onChange:= (e=>{e.target})
        )
        )

  )

 */

}
