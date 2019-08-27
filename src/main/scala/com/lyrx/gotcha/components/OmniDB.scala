package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.ipfs.OrbitDB
import slinky.core.{ComponentWrapper, KeyAndRefAddingStage}
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, h6, p, s}

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
        renderDB(db):ReactElement
    }).getOrElse(div()):ReactElement


    private def renderDB(db:OrbitDB) =
      div(
        className := s"card shadow my-mb-4 my-card")(
        div(className := "card-header py-3")(
          h6(className := "m-0 font-weight-bold text-primary")(
            "P2P Log")))





  }













}


