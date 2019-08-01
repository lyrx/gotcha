package com.lyrx.gotcha

import com.lyrx.pyramids.stellarsdk.Timeout
import org.scalajs.dom.document
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM

import scala.concurrent.ExecutionContext
import org.scalajs.dom
import MyComponents._
import com.lyrx.pyramids.Pyramid

object Main {

  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)

  def initReactElements(pyramidOpt:Option[Pyramid],renderer:GotchaPyramidRenderer) = renderAll(
    ManagementWrapper(pyramidOpt,renderer))



  def renderAll(p: ReactElement) =
    ReactDOM.render(p, document.getElementById("root"))

  def main(args: Array[String]): Unit = {
    if(dom.window.location.hash == "#landing")
      initWithLanding(None)
      else
    initWithIdentityManagement(None)
  }


  def initWithIdentityManagement(po:Option[Pyramid]): Unit =
    initReactElements(po,
      ((aPyramidOpt) =>IdentityManagement(aPyramidOpt) ))


  def initWithLanding(po:Option[Pyramid]): Unit =
    initReactElements(po,
      ((aPyramidOpt) =>Landing(aPyramidOpt) ))



}
