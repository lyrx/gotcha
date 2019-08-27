package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main
import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.{ComponentWrapper}
import slinky.web.html.{div, id}
import Main.ec

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js




object ManagementWrapper extends ComponentWrapper {
  case class Props(pyramidOpt: Option[Pyramid],
                   renderer: GotchaPyramidRenderer)

  case class State(s:String)




  class Def(jsProps: js.Object) extends Definition(jsProps) {
    def initialState = State("")

    def initPyramid(isTestNet: Boolean)(
      implicit executionContext: ExecutionContext) =
      props.pyramidOpt
        .map(p => Future { p })
        .getOrElse(
          Config
            .createFuture(isTestNet)
            .flatMap(Pyramid(_)
              .loadPharaohKey())
        )



    def render = {
      implicit val pyrOpt: Option[Pyramid] = props.pyramidOpt
      div(id := "wrapper")(
        SideBar(pyrOpt),
        ContentWrapper(pyrOpt, (() => props.renderer(pyrOpt)))
      )
    }
    override def componentDidMount(): Unit = {
      initPyramid(false)
        .map(
          p =>{Main
            .renderAll(
              ManagementWrapper(
                ManagementWrapper.Props(Some(new Pyramid(p.config
                  .withMessage("Eternalize Your Documents!"))),
                props.renderer))
            )
          }
        )
    }

  }






}

