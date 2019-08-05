package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main.ec
import com.lyrx.gotcha.{Main, MyComponents}
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._
import com.lyrx.gotcha._

@react class RegisterIdentity extends Component {


  override def initialState: State = RuntimeStatus( msg="",status=RuntimeStatus.READY)
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

 type State = RuntimeStatus


  override def componentDidMount(): Unit = {
 val fee: String = props
   .pyramidOpt
   .flatMap(
     _.config
       .blockchainData
       .stellar
       .registrationFeeXLMOpt
       .map(fee=>s"Registration fee: XLM ${fee}")).getOrElse("")
    setState( RuntimeStatus(msg =fee,status = RuntimeStatus.READY))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {

  }





  def handleClick(e: SyntheticEvent[Anchor, Event])=
    if(!state.isOnGoing())
    props
    .pyramidOpt
    .flatMap(p=>{
      val aPrivKey = MyComponents.passwordField.current.value
      val aPubKey = MyComponents.idsField.current.value
      p.config.ipfsData.regOpt.flatMap(s=> {
        setState(RuntimeStatus(msg="Registration ongoing",status = RuntimeStatus.ONGOING))
        p.stellarRegisterByTransaction(aHash=s,
          privKey=aPrivKey,
          pubKey = aPubKey)(Main.ec,Main.timeout)
        .map(_.map((so)=>{
          setState(RuntimeStatus(msg = so.getOrElse(""),status = RuntimeStatus.DONE))
          Main.initWithIdentityManagement(props.pyramidOpt)
        }))
      })})




  def renderIdentity() = if(state.isDone())a(
    href:=s"https://${props.pyramidOpt.steepx}/tx/${state.msg}",
    target:="_blank")(
    img(src:="img/registration.png")
  )
  else
    span()(state.msg)


  override def render(): ReactElement =
    div(className := s"card shadow mb-4 my-card ${state.blinkMe()}" )(
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
