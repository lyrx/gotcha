package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import slinky.core.{ComponentWrapper, KeyAndRefAddingStage}
import slinky.core.facade.ReactElement
import slinky.web.html.p

import scala.scalajs.js

object OmniDB extends ComponentWrapper {

  case class Props(pyramidOpt: Option[Pyramid])
  case class State(s:String)


  def apply(pyramidOpt: Option[Pyramid]): KeyAndRefAddingStage[Def] = OmniDB(Props(pyramidOpt))




  class Def(jsProps: js.Object) extends Definition(jsProps) {

    override def initialState(): State = State("")

    override def render()  = props
      .pyramidOpt
      .dbOpt()
      .map(db=>{
      p("Got DB"):ReactElement
    }).getOrElse(p("No DB")):ReactElement



  }













}


