package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import dom.Event
import dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.{ReactElement}
import slinky.core.{Component, SyntheticEvent}
import slinky.web.html._
import com.lyrx.pyramids.util.Implicits._



@react class PublishIdentity extends Component {

  val READY="ready"
  val ONGOING = "ongoing"
  val DONE = "done"

  override def initialState: State = State( regHash = "(Unregistered)",status=READY)
  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  case class State(regHash: String,status:String)



  override def componentDidMount(): Unit = {
    setState(State(regHash = registerHash(),status = READY))
  }

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    val newHash= registerHash();
    if(prevState.regHash != newHash){
      setState(State(regHash = newHash,status = DONE))
    }
  }

  def isOnGoing():Boolean = (state.status==ONGOING)

  def registerHash()= registerHashOpt()
    .getOrElse("(Unknown)")


  def registerHashOpt()=props.pyramidOpt
    .flatMap(p=>
      p.config
        .ipfsData
        .regOpt
    )


  def handleClick(e: SyntheticEvent[Anchor, Event])=
    if(!isOnGoing())props
    .pyramidOpt
    .map(p=>{
      setState(State(regHash="Publishing ...",status=ONGOING))
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