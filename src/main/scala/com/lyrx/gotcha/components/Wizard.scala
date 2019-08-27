package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import slinky.core.ComponentWrapper
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.scalajs.js

object Wizard extends ComponentWrapper {


  case class Props(pyramidOpt: Option[Pyramid])
  case class State(s:String)



  class Def(jsProps: js.Object) extends Definition(jsProps) {

    override def initialState(): State = ???

    override def render(): ReactElement  = ???



  }













}


