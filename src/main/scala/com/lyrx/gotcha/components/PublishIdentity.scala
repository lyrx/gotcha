package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main.ec
import com.lyrx.gotcha.components.ManagementWrapper.Definition
import com.lyrx.gotcha.{MyComponents, PageOptionTrait}
import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.util.Implicits._
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{Component, ComponentWrapper, SyntheticEvent}
import slinky.web.html._

import scala.scalajs.js
object PublishIdentity extends ComponentWrapper with PageOptionTrait{

  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(aHashOpt: Option[String], runtimeStatus: RuntimeStatus)

  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState: State =
      State(aHashOpt = None,
        runtimeStatus = RuntimeStatus(msg = "", status = RuntimeStatus.READY))

    override def componentDidMount(): Unit = {
      if (props.pyramidOpt.isEmpty)
        setState(
          state.copy(runtimeStatus =
            RuntimeStatus(msg = "Initializing", status = RuntimeStatus.ONGOING)))
      else
        setState(
          state.copy(runtimeStatus =
            RuntimeStatus(msg = "Ready", status = RuntimeStatus.READY)))

    }

    override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
      if (prevProps.pyramidOpt.isEmpty && props.pyramidOpt.isDefined) {
        setState(
          state.copy(runtimeStatus =
            RuntimeStatus(msg = "Ready", status = RuntimeStatus.READY)))
      } else onNewHash(prevState)
    }

    private def onNewHash(prevState: State) = {
      val newOpt = registerHashOpt()
      val aa = prevState.aHashOpt.getOrElse("")
      val nn = newOpt.getOrElse("")
      if (aa != nn) {
        setState(
          state.copy(aHashOpt = newOpt,
            runtimeStatus = RuntimeStatus(msg = "Publish complete!",
              status = RuntimeStatus.READY)))
      }
    }

    def registerHashOpt() =
      props.pyramidOpt
        .flatMap(p => p.config.p2pData.ipfsData.regOpt)

    def handleClick(e: SyntheticEvent[Anchor, Event]) =
      if (!state.runtimeStatus.isOnGoing())
        props.pyramidOpt
          .map(p => {
            setState(
              state.copy(
                runtimeStatus = RuntimeStatus(msg = "Publishing ...",
                  status = RuntimeStatus.ONGOING)))
            p.ipfsRegister()
              .map(_.loadIdentity()
                .fmap(p3 => {
                  initWithIdentityManagement(Some(p3))

                  setState(
                    state.copy(
                      runtimeStatus = RuntimeStatus(msg = "Publish complete",
                        status = RuntimeStatus.DONE)))

                }))
          })

    override def render(): ReactElement =
      div(
        className := s"card shadow my-mb-4 my-card  ${state.runtimeStatus.blinkMe()} ")(
        div(className := "card-header py-3")(
          h6(className := "m-0 font-weight-bold text-primary")(
            "Publish Your Identity"
          )
        ),
        div(className := s"my-card-body")(
          div()(
            if (state.aHashOpt.isDefined)
              a(href := MyComponents.hashUrl(state.aHashOpt.get),
                target := "_blank")(
                img(src := "img/published.png"),
                span(state.runtimeStatus.msg)
              )
            else
              span(state.runtimeStatus.msg)),
          if (!state.aHashOpt.isDefined)
            div()(
              a(href := "#",
                className := "btn my-btn btn-icon-split",
                onClick := (handleClick(_)))(
                i(className := "fas fa-upload m-button-label"),
                span(className := "my-label")("Publish Identity")
              )
            )
          else
            div()
        )
      )


  }



}
