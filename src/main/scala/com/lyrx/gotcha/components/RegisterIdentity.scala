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

  override def initialState: State =
    State(aIdOpt = None,
          runtimeStatus = RuntimeStatus(msg = "", status = RuntimeStatus.READY))
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(aIdOpt: Option[String], runtimeStatus: RuntimeStatus)

  override def componentDidMount(): Unit = {
    val fee: String = props.pyramidOpt
      .flatMap(
        _.config.blockchainData.stellar.registrationFeeXLMOpt
          .map(fee => s"Registration fee: XLM ${fee}"))
      .getOrElse("")
    setState(
      state.copy(
        runtimeStatus = RuntimeStatus(msg = fee, status = RuntimeStatus.READY)))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    println("Prev state: " + prevState)
    println("state: " + prevState)
  }

  def handleClick(e: SyntheticEvent[Anchor, Event]) =
    if (!state.runtimeStatus.isOnGoing())
      props.pyramidOpt
        .flatMap(p => {
          val aPrivKey = MyComponents.passwordField.current.value
          val aPubKey = MyComponents.idsField.current.value
          p.config.ipfsData.regOpt.flatMap(s => {
            setState(
              state.copy(
                runtimeStatus = RuntimeStatus(msg = "Registration ongoing",
                                              status = RuntimeStatus.ONGOING)))
            p.stellarRegisterByTransaction(aHash = s,
                                            privKey = aPrivKey,
                                            pubKey = aPubKey)(Main.ec,
                                                              Main.timeout)
              .map(_.map((so) => {
                Main.initWithIdentityManagement(props.pyramidOpt)
                setState(
                  state.copy(aIdOpt = so.toOption,
                             runtimeStatus =
                               RuntimeStatus("Registration done",
                                             status = RuntimeStatus.DONE)))
              }))
          })
        })

  def renderIdentity() =
    if (state.aIdOpt.isDefined)
      a(href := s"https://${props.pyramidOpt.steepx}/tx/${state.aIdOpt.get}",
        target := "_blank")(
        img(src := "img/registration.png"),
        span()(state.runtimeStatus.msg)
      )
    else
      span()(state.runtimeStatus.msg)

  override def render(): ReactElement =
    div(
      className := s"card shadow my-mb-4 my-card ${state.runtimeStatus.blinkMe()}")(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Register Your Identity In The Stellar Network"
        )
      ),
      div(className := "my-card-body")(
        div()(
          renderIdentity(),
          if (!state.aIdOpt.isDefined)
            div()(
              a(href := "#",
                className := "btn my-btn btn-icon-split",
                onClick := (handleClick(_)))(
                i(className := "fas fa-registered m-button-label"),
                span(className := "my-label")("Register Identity")
              )
            )
          else
            div()()
        )
      )
    )

}
