package com.lyrx.gotcha.components


import com.lyrx.gotcha.Implicits._
import com.lyrx.pyramids.util.Implicits._
import com.lyrx.gotcha.{MyComponents, PageOptionTrait}
import com.lyrx.pyramids.{AccountData, Pyramid}
import org.scalajs.dom
import org.scalajs.dom.html.Anchor
import org.scalajs.dom.{Event, File}
import slinky.core.facade.{React, ReactElement}
import slinky.core.{ComponentWrapper, SyntheticEvent}
import slinky.web.html._

import scala.scalajs.js
object IpfsUpload extends ComponentWrapper with PageOptionTrait {

  case class Props(
                    pyramidOpt: Option[Pyramid],
                  )

  case class State(transactionOpt: Option[String],
                   accountData: AccountData,
                   hashOpt: Option[String],
                   fileOpt: Option[File],
                   runtimeStatus: RuntimeStatus
                  )


  val fileSelector = React.createRef[dom.html.Input]

  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState: State =
      State(
        transactionOpt = None,
        accountData = AccountData(None, None),
        hashOpt = None,
        fileOpt = None,
        runtimeStatus =
          RuntimeStatus(msg = "(Unregistered)", status = RuntimeStatus.READY))




    override def componentDidMount(): Unit = {}

    override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {}

    def getFileOpt() = {
      val maybeFile = fileSelector.current.files.item(0)
      if (maybeFile == null)
        None
      else
        Some(maybeFile)
    }




    def renderTransaction(): ReactElement =
      if (state.transactionOpt.isDefined) div()(a(
        href := s"https://${props.pyramidOpt.steepx}/tx/${state.transactionOpt.get}",
        target := "_blank")(
        img(src := "img/registration.png"),
        span(state.runtimeStatus.msg)
      ))
      else
        div()(span(state.runtimeStatus.msg))


    def onUpload(e: SyntheticEvent[Anchor, Event]) =
      if (!state.runtimeStatus.isOnGoing())
        props.pyramidOpt
          .map(p =>
            state.fileOpt.map(f => {
              setState(
                state.copy(
                  runtimeStatus = RuntimeStatus(msg = "Uploading ...",
                    status = RuntimeStatus.ONGOING)))
              p.unencryptedUpload(f).fmap(
                s => {
                  setState(state.copy(
                    hashOpt = Some(s),
                    runtimeStatus = RuntimeStatus(msg = "Done uploading",
                      status = RuntimeStatus.DONE)))

                }
              )
            }))


    def onDownload(e: SyntheticEvent[Anchor, Event]) = if (!state.runtimeStatus.isOnGoing())
      state.hashOpt.map(h =>
        props.pyramidOpt.map(p => {
          setState(
            state.copy(
              runtimeStatus = RuntimeStatus(msg = "Downloading ...",
                status = RuntimeStatus.ONGOING)))
          getFileOpt().map(f =>
            p.saveHashWithFile(h,f).map(p2 => {
              setState(
                state.copy(
                  runtimeStatus = RuntimeStatus(msg = "Finished download",
                    status = RuntimeStatus.DONE)))}))}))






    def onRegister(e: SyntheticEvent[Anchor, Event]) = if (!state.runtimeStatus.isOnGoing()) {
      val aPubKey = MyComponents.docsField.current.value
      val aPrivKey = MyComponents.passwordField.current.value

      state.hashOpt.map(h => props.pyramidOpt.map(p => {
        setState(
          state.copy(
            runtimeStatus = RuntimeStatus(msg = "Registration ongoing ...",
              status = RuntimeStatus.ONGOING)))

        p.stellarRegisterByTransaction(aHash = h,
          privKey = aPrivKey,
          pubKey = aPubKey)(ec, timeout)
          .map(_.map((result: js.UndefOr[String]) => {

            setState(
              state.copy(
                transactionOpt = result.map(s => Some(s)).getOrElse(None),
                runtimeStatus = RuntimeStatus(msg = "Registration done",
                  status = RuntimeStatus.DONE)))
            initWithNotary(props.pyramidOpt)

          }).onComplete(t => t.failed.map(println))
          )
      }
      ))
    }


    def renderFileOpt(): ReactElement = (if (state.fileOpt.isDefined)
      div()(
        a(href := "#",
          className := "btn my-btn btn-icon-split",
          onClick := (onUpload(_)))(
          i(className := "fas fa-upload m-button-label"),
          span(className := "my-label")("Add")
        )
      )
    else div())


    def renderHasOpt(): ReactElement = if (state.hashOpt.isDefined)
      section()(

        div()(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (onDownload(_)))(
            i(className := "fas fa-download m-button-label"),
            span(className := "my-label")("Download")
          ))




        , div()(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (onRegister(_)))(
            i(className := "fas fa-registered m-button-label"),
            span(className := "my-label")("Register")
          )
        )



      )
    else
      div()


    override def render(): ReactElement =
      div(
        className := s"card shadow my-mb-4 my-card  ${state.runtimeStatus.blinkMe()} ")(
        div(className := "card-header py-3")(
          h6(className := "m-0 font-weight-bold text-primary")(
            "Add, Register (unencrypted)"
          )
        ),
        div(className := s"my-card-body")(
          div(
            input(
              `type` := "file",
              ref := fileSelector,
              onChange := (e => {
                setState(state.copy(fileOpt = e.target.fileOpt()))
              })
            ),
            renderFileOpt(),
            renderHasOpt(),
            renderTransaction()
          ))
      )

  }




}
