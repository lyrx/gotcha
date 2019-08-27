package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.ipfs.{LoadProgress, OrbitDB}
import slinky.core.{ComponentWrapper, KeyAndRefAddingStage}
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, h6, p, s}
import com.lyrx.pyramids.ipfs.{OrbitDB, PimpedEvents}

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import com.lyrx.gotcha.Implicits._


object OrbitDBComponent extends ComponentWrapper {

  case class Props(pyramidOpt: Option[Pyramid])
  case class State(loadProgressOpt:Option[LoadProgress])


  def apply(pyramidOpt: Option[Pyramid]): KeyAndRefAddingStage[Def] = OrbitDBComponent(Props(pyramidOpt))




  class Def(jsProps: js.Object) extends Definition(jsProps) {

    override def initialState(): State = State(loadProgressOpt = None)

    override def render()  = props
      .pyramidOpt
      .dbOpt()
      .map(db=>{
        renderDB(db):ReactElement
    }).getOrElse(div()):ReactElement

    override def componentDidMount(): Unit = {

    }

    def init(db: OrbitDB)(implicit executionContext: ExecutionContext) = {
     // db.events.onReady().map((r)=>println("Yochaiii"))

    }

    override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
      val prevDBOpt = prevProps.pyramidOpt.dbOpt()
      val dbOpt = props.pyramidOpt.dbOpt()
      if(prevDBOpt.isEmpty && ! dbOpt.isEmpty){
        init(dbOpt.get)
      }
      /*
      prevState.loadProgressOpt match {
        case state.loadProgressOpt =>
        case _ =>
      }

       */



    }


    private def renderDB(db:OrbitDB) =
      div(
        className := s"card shadow my-mb-4 my-card")(
        div(className := "card-header py-3")(
          h6(className := "m-0 font-weight-bold text-primary")(
            "P2P Log")))





  }













}


