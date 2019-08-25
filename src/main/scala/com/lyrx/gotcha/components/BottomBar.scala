package com.lyrx.gotcha.components

import com.lyrx.gotcha.CContext
import slinky.core.ComponentWrapper
import slinky.web.html._

import scala.scalajs.js

object BottomBar extends ComponentWrapper {
  case class Props(context:CContext)
  case class State(s:String)

  class Def(jsProps: js.Object) extends Definition(jsProps) {
    def initialState = State("")

    override  def render = nav()()
  }
}