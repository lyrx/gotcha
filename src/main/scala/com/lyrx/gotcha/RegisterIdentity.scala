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


  override def initialState: State = State( regMessage="")
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(regMessage: String)



  override def componentDidMount(): Unit = {

  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {

  }





  def handleClick(e: SyntheticEvent[Anchor, Event])= props
    .pyramidOpt
    .flatMap(p=>{
      val aPrivKey = MyComponents.passwordField.current.value
      val aPubKey = MyComponents.idsField.current.value
      p.config.ipfsData.regOpt.flatMap(s=> {
        setState(State(regMessage="Registration ongoing"))
        p.stellarRegisterByTransaction(aHash=s,
          privKey=aPrivKey,
          pubKey = aPubKey)(Main.ec,Main.timeout)
        .map(_.map((s:String)=>{
          setState(State(regMessage = "Registered in the stellar network"))
          Main.initWithIdentityManagement(props.pyramidOpt)
        }))
      })})


  def renderIdentity() =span()(state.regMessage)

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
