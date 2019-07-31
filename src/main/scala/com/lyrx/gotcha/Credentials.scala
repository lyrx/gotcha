package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import org.scalajs.dom.html.Input
import org.scalajs.dom.html.Anchor

import org.scalajs.dom.{Event, File}
import org.scalajs.dom.raw.{Blob, EventTarget}
import slinky.core.{Component, StatelessComponent, SyntheticEvent}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.web.html._

import scala.concurrent.Future






@react class Credentials extends StatelessComponent {

  val fileField = React.createRef[dom.html.Input]
  val nameField = React.createRef[dom.html.Input]

  case class Props(
      pyramidOpt: Option[Pyramid],
  )



  override def componentDidMount(): Unit = {

  }
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {

  }

  def handleChange(e: SyntheticEvent[Input, Event])= {
    props.pyramidOpt.map(p=>
      e.target
        .fileOpt()
        .map(p.uploadWallet(_)).getOrElse(Future{p})
    ).map(_.map(p2=>
      Main.initWithIdentityManagement(
        Some(new Pyramid(
          p2.config
            .clearIdentity()
            //.withIdentityName(p2.config.cryptoSupport.config.nameOpt.getOrElse("NO NAME!!"))
            .withMessage(s"Loaded Identity: ${pyrName(new Pyramid(p2.config.clearIdentity))}"))))
    ))

  }


  def handleClick(e: SyntheticEvent[Anchor, Event])= {
    props
      .pyramidOpt
      .map(
        p=>
          new Pyramid(p.config.withIdentityName(nameField.current.value))
            .saveKeys()
          .map(p3=>Main.initWithIdentityManagement(Some(p3)))

      )

  }

  def name():String = props.pyramidOpt.
    map(pyrName(_)).getOrElse("NO NAME")



  def pyrName(p:Pyramid):String = p.config.cryptoSupport.config.nameOpt.getOrElse("No name")


  override def render(): ReactElement =
    div(className := "card shadow mb-4")(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Load and Save Your Identity"
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
          input(
            `type` := "text",
            ref := nameField,
            defaultValue := name())
        ),
        div(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (handleClick(_)))(
            i(className := "fas fa-save"),
            span(className:="my-label")("Save Identity")
          )
        )
      )
    )



}
