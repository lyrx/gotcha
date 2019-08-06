package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main.ec
import com.lyrx.gotcha.{Main, MyComponents}
import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.util.Implicits._
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import org.scalajs.dom.raw.DataTransfer
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._
@react class IpfsEncrypt extends Component {

  val fileSelector = React.createRef[dom.html.Input]

  override def initialState: RuntimeStatus =
    RuntimeStatus(msg = "(Unregistered)", status = RuntimeStatus.READY)
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  type State = RuntimeStatus

  override def componentDidMount(): Unit = {
    setState(RuntimeStatus(msg = registerHash(), status = RuntimeStatus.READY))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    val newHash = registerHash();
    if (prevState.msg != newHash) {
      setState(RuntimeStatus(msg = newHash, status = RuntimeStatus.DONE))
    }
  }

  def registerHash() =
    registerHashOpt()
      .getOrElse("(Unknown)")

  def registerHashOpt() =
    props.pyramidOpt
      .flatMap(p => p.config.ipfsData.uploads.headOption.map(_.hash))

  def fileOpt() = {
    val maybeFile = fileSelector.current.files.item(0)
    if (maybeFile == null)
      None
    else
      Some(maybeFile)
  }

  def handleClick(e: SyntheticEvent[Anchor, Event]) =
    if (!state.isOnGoing())
      props.pyramidOpt
        .map(p => fileOpt().map(f => {
            setState(
              RuntimeStatus(msg = "Uploading ...",
                            status = RuntimeStatus.ONGOING))
            p.encryptedUpload(f)
              .fmap(
                s => {
                  setState(
                    RuntimeStatus(msg = "Done uploading ...",
                      status = RuntimeStatus.DONE))
                  println(s"Uploaded: ${s}")
                }
              )}))

  override def render(): ReactElement =
    div(className := s"card shadow mb-4 my-card  ${state.blinkMe()} ")(
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
            onChange := (e => {})
          ),
          div()(
            a(href := "#",
              className := "btn my-btn btn-icon-split",
              onClick := (handleClick(_)))(
              i(className := "fas fa-upload m-button-label"),
              span(className := "my-label")("Upload File To IPFS")
            )
          )
        ))
    )

}
