package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.stellarsdk.Timeout
import org.scalajs.dom.{Event, document,html}
import slinky.core.SyntheticEvent
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM
import slinky.web.html._

import scala.concurrent.ExecutionContext
import MyComponents._
object ReactElements  {


  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)
  implicit val isTest: Boolean = true


  def initReactElements()= renderAll(IdentityManagement(None))

  def renderAll(p: ReactElement) = ReactDOM.render(p, document.getElementById("root"))











}
