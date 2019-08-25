package com.lyrx.gotcha.components

import com.lyrx.gotcha.CContext
import slinky.core.ComponentWrapper
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.scalajs.js

object Page extends ComponentWrapper {
  case class Props(context:CContext)
  case class State(message:String)

  class Def(jsProps: js.Object) extends Definition(jsProps) {
    def initialState = State(message="")

     override def render(): ReactElement =
       div(id := "content-wrapper", className := "")(
        div(id := "content")(
          TopBar(TopBar.Props(context = props.context)),
          BottomBar(BottomBar.Props(context = props.context))
        )
      )






  }



}