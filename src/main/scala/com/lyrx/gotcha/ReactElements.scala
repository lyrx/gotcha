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


  def brand()(implicit pyramidOpt:Option[Pyramid]):ReactElement =  a(className := "sidebar-brand d-flex align-items-center justify-content-center", href := "index.html")(
    div(className:="sidebar-brand-icon rotate-n-15")(
      img(src := "img/ETER-Logo-small.png")
    ),
    div(className := "sidebar-brand-text mx-3" )("Pyramids!")
  )

  def sidebar()(implicit pyramidOpt:Option[Pyramid]):ReactElement = ul( className := "navbar-nav bg-gradient-primary sidebar sidebar-dark accordion\" id=\"accordionSidebar")(
    brand(),
    hr(className:="sidebar-divider my-0"),
    li(className:="nav-item")(
      a(className:="nav-link",
        href := "index.html")(
        i(className:="fas fa-fw fa-tachometer-alt"),
        span()("Indentity Management")
      )),
    hr(className:="sidebar-divider d-none d-md-block"),
    div(className:="text-center d-none d-md-inline")(
      button( className:="rounded-circle border-0", id:="sidebarToggle")
    )
  )





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
