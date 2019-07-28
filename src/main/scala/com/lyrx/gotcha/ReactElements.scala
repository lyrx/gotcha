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







  def content()(implicit pyramidOpt:Option[Pyramid]) = div(id := "content")(
    nav(
      className := "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow")(
      button(id := "sidebarToggleTop",
        className := "btn btn-link d-md-none rounded-circle mr-3")(
        i(className := "fa fa-bars")),
      form(
        className := "d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search")(
        Stellar(pyramidOpt)
      )
    ),
    identityManagement()
    ,
    a( className:="scroll-to-top rounded", href:="#page-top")(
      i(className:="fas fa-angle-up")
    )
  )


  def identityManagement()(
    implicit pyramidOpt:Option[Pyramid]
  ): ReactElement =
    div(className := "container-fluid" , id :="pyramid-root")(
      pageHeading("Identity Management"),
      div(className:="row")(
        UserBalance(pyramidOpt)
      )
    )

  def pageHeading(title: String)(implicit pyramidOpt:Option[Pyramid])=
    div(className:="d-sm-flex align-items-center justify-content-between mb-4")(
      h1(className:="h3 mb-0 text-gray-800")(title)
    )


  def contentWrapper()(implicit pyramidOpt:Option[Pyramid]): ReactElement =     div( id:= "content-wrapper", className:= "d-flex flex-column")(
    content(),
    footer(className := "sticky-footer bg-white")(
      div(className := "container my-auto")(
        div(className := "my-auto")(
          span(id := "status-messages")(pyramidOpt.map(_.config.frontendData.message).getOrElse("Working, please wait").toString)
        )
      )
    ))



}
