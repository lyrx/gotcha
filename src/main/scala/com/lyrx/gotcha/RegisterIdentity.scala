package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.{Pyramid, Registration}
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._


@react class RegisterIdentity extends Component {


  override def initialState: State = State( registrationOpt = None)
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(registrationOpt: Option[Registration])



  override def componentDidMount(): Unit = {
    setState(State(registrationOpt = registerHash()))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    val newRegOpt= registerHash();
    if(prevState.registrationOpt != newRegOpt){
      setState(State(registrationOpt = newRegOpt))
    }
  }


  def registerHash()=props.pyramidOpt
    .flatMap(_.config
        .ipfsData
        .identityOpt
      )


  def handleClick(e: SyntheticEvent[Anchor, Event])= props
    .pyramidOpt
    .map(p=>{



      ???

    })


  def renderIdentity() = state
    .registrationOpt
    .map(_.identity
    .map(hash=>
      a(href:=s"https://ipfs.infura.io/ipfs/${hash}"
        ,target:="_blank")(
        hash)).toOption)
    .flatten
    .getOrElse(span()("(Not Registered)"))

  override def render(): ReactElement =
    div(className := "card shadow mb-4 my-card" )(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Register Your Identity In The Stellar Network"
        )
      ),
      div(className := "my-card-body")(
        div()(renderIdentity(),
        div( )(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (handleClick(_)))(
            i(className := "fas fa-registered m-button-label"),
            span(className:="my-label")("Register Identity")
          )
        )
      )
    ))



}
