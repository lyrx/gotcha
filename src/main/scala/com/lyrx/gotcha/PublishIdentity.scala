package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{Component, StatelessComponent, SyntheticEvent}
import slinky.web.html._

import scala.concurrent.Future


@react class PublishIdentity extends Component {


  override def initialState: State = State( regHash = "(Unregistered)")
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(regHash: String)



  override def componentDidMount(): Unit = {
    setState(State(regHash = registerHash()))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    val newHash= registerHash();
    if(prevState.regHash != newHash){
      setState(State(regHash = newHash))
    }
  }


  def registerHash()=props.pyramidOpt
    .flatMap(p=>
      p.config
        .ipfsData
        .regOpt
      )
    .getOrElse("(Unknown)")

  def handleClick(e: SyntheticEvent[Anchor, Event])= props
    .pyramidOpt
    .map(p=>{
      setState(State(regHash="Publishing ..."))
      p.ipfsRegister()
        .map(p2=>Main
          .initWithIdentityManagement(
            Some(p2))
        )
    })

  override def render(): ReactElement =
    div(className := "card shadow mb-4 my-card" )(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Publish Your Identity"
        )
      ),
      div(className := "my-card-body")(
        div()(
          if(state.regHash.startsWith("Qm"))
          a(href:=s"https://ipfs.infura.io/ipfs/${state.regHash}"
            ,target:="_blank")(
            state.regHash)
        else
         span(state.regHash)),
        div( )(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (handleClick(_)))(
            i(className := "fas fa-upload m-button-label"),
            span(className:="my-label")("Publish Identity")
          )
        )
      )
    )



}
