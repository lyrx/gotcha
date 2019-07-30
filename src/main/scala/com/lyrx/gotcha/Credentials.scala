package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import org.scalajs.dom.html.Input
import org.scalajs.dom.{Event, File}
import org.scalajs.dom.raw.{Blob, EventTarget}
import slinky.core.{Component, StatelessComponent, SyntheticEvent}
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

  def handleChange(e: SyntheticEvent[Input, Event])= {
    props.pyramidOpt.map(p=>
      e.target
        .fileOpt()
        .map(p.uploadWallet(_)).getOrElse(Future{p})
    ).map(_.map(p2=>
      Main.initWithIdentityManagement(
        Some(new Pyramid(
          p2.config.withMessage("Loaded Credentials"))))
    ))

  }

  override def render(): ReactElement =
    div(className := "card shadow mb-4")(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Load and save credentials"
        )
      ),
      div(className := "my-card-body")(
        div(
          input(
            className := "my-fileselector",
            `type` := "file",
            ref := fileField,
            onChange := (handleChange(_)))
        ),
        div(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (e => props.pyramidOpt.map(_.saveKeys())))(
            i(className := "fas fa-save"),
            span(className:="my-label")("Save credentials")
          )
        )
      )
    )



}
