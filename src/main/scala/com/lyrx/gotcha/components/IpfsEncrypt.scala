package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main.ec
import com.lyrx.gotcha.{Main, MyComponents}
import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.util.Implicits._
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._


@react class IpfsEncrypt extends Component {


  override def initialState: RuntimeStatus= RuntimeStatus(msg= "(Unregistered)",status=RuntimeStatus.READY)
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

 type State = RuntimeStatus



  override def componentDidMount(): Unit = {
    setState(RuntimeStatus(msg = registerHash(),status = RuntimeStatus.READY))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    val newHash= registerHash();
    if(prevState.msg != newHash){
      setState(RuntimeStatus(msg = newHash,status = RuntimeStatus.DONE))
    }
  }

  def registerHash()= registerHashOpt()
    .getOrElse("(Unknown)")


  def registerHashOpt()=props.pyramidOpt
    .flatMap(p=>
      p.config
        .ipfsData.uploads.headOption.map(_.hash)
    )


  def handleClick(e: SyntheticEvent[Anchor, Event])=
    if(!state.isOnGoing())props
    .pyramidOpt
    .map(p=>{
      setState(RuntimeStatus(msg="Publishing ...",status=RuntimeStatus.ONGOING))
      p.ipfsRegister()
        .map(_.loadIdentity()
              .fmap(p3=>{
                Main
                  .initWithIdentityManagement(
                    Some(p3))
              })
        )
    })



  override def render(): ReactElement =
    div(className := s"card shadow mb-4 my-card  ${state.blinkMe()} " )(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Encrypt And Upload Your"
        )
      ),
      div(className := s"my-card-body")(
        div()(
          if(state.msg.startsWith("Qm"))
          a(href:=s"https://ipfs.infura.io/ipfs/${state.msg}"
            ,target:="_blank")(
            //state.regHash
           img(src:="img/published.png")

          )
        else
         span(state.msg)),
        div( )(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (handleClick(_)))(
            i(className := "fas fa-upload m-button-label"),
            span(className:="my-label")("Upload File To IPFS")
          )
        )
      )
    )



}
