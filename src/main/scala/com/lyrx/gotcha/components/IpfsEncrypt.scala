package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.{AccountData, Pyramid}
import com.lyrx.pyramids.util.Implicits._
import org.scalajs.dom
import org.scalajs.dom.{Event, File}
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._
import com.lyrx.gotcha._
@react class IpfsEncrypt extends Component {

  val fileSelector = React.createRef[dom.html.Input]

  override def initialState: State =
    State(accountData = AccountData(None, None),
          hashOpt = None,
          fileOpt = None,
          runtimeStatus =
            RuntimeStatus(msg = "(Unregistered)", status = RuntimeStatus.READY))
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(
      accountData: AccountData,
      hashOpt: Option[String],
      fileOpt: Option[File],
      runtimeStatus: RuntimeStatus
  )

  override def componentDidMount(): Unit = {}

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {}

  def getFileOpt() = {
    val maybeFile = fileSelector.current.files.item(0)
    if (maybeFile == null)
      None
    else
      Some(maybeFile)
  }

  def onUpload(e: SyntheticEvent[Anchor, Event]) =
    if (!state.runtimeStatus.isOnGoing())
      props.pyramidOpt
        .map(p =>
          state.fileOpt.map(f => {
            setState(
              state.copy(
                runtimeStatus = RuntimeStatus(msg = "Uploading ...",
                                              status = RuntimeStatus.ONGOING)))
            p.encryptedUpload(f)
              .fmap(
                s => {
                  setState(state.copy(
                    hashOpt=Some(s),
                    runtimeStatus = RuntimeStatus(msg = "Done uploading ...",
                                                  status = RuntimeStatus.DONE)))

                }
              )
          }))

  def onDownload(e: SyntheticEvent[Anchor, Event]) =  if (!state.runtimeStatus.isOnGoing())
    state.hashOpt.map(h=>getFileOpt().map(f=>
      props.pyramidOpt.map(p=>{
        setState(
          state.copy(
            runtimeStatus = RuntimeStatus(msg = "Downloading ...",
              status = RuntimeStatus.ONGOING)))
        p.saveHash(h, f).map(p2=>{
          setState(
            state.copy(
              runtimeStatus = RuntimeStatus(msg = "Finished Download",
                status = RuntimeStatus.DONE)))
        })
      })
    ))






  def onRegister(e: SyntheticEvent[Anchor, Event]) =  if (!state.runtimeStatus.isOnGoing()){
    val aPubKey = MyComponents.docsField.current.value
    val aPrivKey = MyComponents.passwordField.current.value

    state.hashOpt.map(h=> props.pyramidOpt.map(p=>{
        setState(
          state.copy(
            runtimeStatus = RuntimeStatus(msg = "Downloading ...",
              status = RuntimeStatus.ONGOING)))

      p.stellarRegisterByTransaction(aHash=h,
        privKey=aPrivKey,
        pubKey = aPubKey)(Main.ec,Main.timeout)
        .map(_.map((so)=>{


        }))



  }))}



  override def render(): ReactElement =
    div(
      className := s"card shadow mb-4 my-card  ${state.runtimeStatus.blinkMe()} ")(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Encrypt And Upload A File"
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
          (if (state.fileOpt.isDefined)
             div()(
               a(href := "#",
                 className := "btn my-btn btn-icon-split",
                 onClick := (onUpload(_)))(
                 i(className := "fas fa-upload m-button-label"),
                 span(className := "my-label")("Upload File To IPFS")
               )
             )
           else div()),
          if (state.hashOpt.isDefined)
            div()(
              a(href := "#",
                className := "btn my-btn btn-icon-split",
                onClick := (onDownload(_)))(
                i(className := "fas fa-download m-button-label"),
                span(className := "my-label")("Download File")
              )
            )
          else
            div(),
          if (state.hashOpt.isDefined)
            div()(
              a(href := "#",
                className := "btn my-btn btn-icon-split",
                onClick := (onRegister(_)))(
                i(className := "fas fa-registered m-button-label"),
                span(className := "my-label")("Register File")
              )
            )
          else
            div()
        ))
    )

}
