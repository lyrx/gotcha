package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Wizard extends Component {


  case class Props(pyramidOpt: Option[Pyramid],title:String,frags:Seq[String])
  case class State(pageOpt:Option[String])



  def renderContents():ReactElement = if(state.pageOpt.isDefined)
    iframe(src:=state.pageOpt.get)
  else div()



  def render(): ReactElement =
    div(className := "container-fluid")(
      h1()(props.title),
      renderContents()
    )



  override def initialState(): State = State(pageOpt=None)


}


