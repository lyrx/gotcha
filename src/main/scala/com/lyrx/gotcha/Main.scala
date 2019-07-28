package com.lyrx.gotcha

import com.lyrx.pyramids.stellarsdk.Timeout
import org.scalajs.dom.document
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM

import scala.concurrent.ExecutionContext
import MyComponents._
import com.lyrx.pyramids.Pyramid

object Main {

  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)

  def initReactElements(pyramidOpt:Option[Pyramid]) = renderAll(ManagementWrapper(pyramidOpt,(() =>IdentityManagement(pyramidOpt) )))

  def renderAll(p: ReactElement) =
    ReactDOM.render(p, document.getElementById("root"))

  def main(args: Array[String]): Unit =
    initReactElements(None)
}
