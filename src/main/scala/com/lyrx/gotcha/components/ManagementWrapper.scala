package com.lyrx.gotcha.components

import com.lyrx.gotcha.{Main, OrbitDBSupport}
import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.ComponentWrapper
import slinky.web.html.{div, id}
import Main.ec
import slinky.core.facade.ReactElement

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js




object ManagementWrapper extends ComponentWrapper with OrbitDBSupport {
  case class Props(pyramidOpt: Option[Pyramid],
                   renderer: GotchaPyramidRenderer)

  case class State(s:String)




  class Def(jsProps: js.Object) extends Definition(jsProps) {
    def initialState = State("")



    override def render(): ReactElement = {
      implicit val pyrOpt: Option[Pyramid] = props.pyramidOpt
      div(id := "wrapper")(
        SideBar(pyrOpt),
        ContentWrapper(pyrOpt, (() => props.renderer(pyrOpt)))
      )
    }
    override def componentDidMount(): Unit = {
      initPyramid(false,props.pyramidOpt)
        .map(
          p => initWithPyramid(new Pyramid(p.config
            .withMessage("Free Your Documents!")))
        )
    }

    private def initWithPyramid(p: Pyramid) = {
      Main
        .renderAll(
          ManagementWrapper(
            ManagementWrapper.Props(Some(p),
              props.renderer))
        )
      orbitDB(p)
    }
  }






}

