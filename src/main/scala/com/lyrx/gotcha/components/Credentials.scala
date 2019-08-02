package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main
import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.{Anchor, Input}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._
import com.lyrx.gotcha._

import scala.concurrent.Future






@react class Credentials extends Component {

  val fileField = React.createRef[dom.html.Input]
  val nameField = React.createRef[dom.html.Input]

  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(
                    name:String,
                  )

  override def initialState: State = State(name = "NOT SPECIFIED")

  override def componentDidMount(): Unit = {
    setState(state.copy(name=name()))
  }
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {

  }

  def handleChange(e: SyntheticEvent[Input, Event])= {
    props.pyramidOpt.map(p=>
      e.target
        .fileOpt()
        .map(p.uploadWallet(_)).getOrElse(Future{p})
    ).map(_.map(p2=> {
      val newP = new Pyramid(p2.config.clearIdentity())
      setState(state.copy(name=pyrName(newP)))
      Main.initWithIdentityManagement(
        Some(new Pyramid(newP.config.withMessage(s"Loaded Identity"))))
    }))

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
          "Load and Save Identity (Local Disc)"
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
            value := state.name,
            onChange:= (e=>{
              e.preventDefault()
              setState(state.copy(name = e.target.value))

            })
          )
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
