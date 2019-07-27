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


  def initReactElements()= renderAll(Pyramidal(None))

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
      a(className:="nav-link", href := "index.html")(
        i(className:="fas fa-fw fa-tachometer-alt"),
        span()("Dashboard")
      )),
    hr(className:="sidebar-divider d-none d-md-block"),
    div(className:="text-center d-none d-md-inline")(
      button( className:="rounded-circle border-0", id:="sidebarToggle")
    )
  )


  /*

  <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
            <div class="sidebar-brand-icon rotate-n-15">
                <img src="img/ETER-Logo-small.png">

                <!--i class="fas fa-laugh-wink"></i-->
            </div>
            <div class="sidebar-brand-text mx-3">Pyramids!</div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0">

        <!-- Nav Item - Dashboard -->
        <li class="nav-item">
            <a class="nav-link" href="index.html">
                <i class="fas fa-fw fa-tachometer-alt"></i>
                <span>Dashboard</span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Interface
        </div>

        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-cog"></i>
                <span>Components</span>
            </a>
            <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom Components:</h6>
                    <a class="collapse-item" href="buttons.html">Buttons</a>
                    <a class="collapse-item" href="cards.html">Cards</a>
                </div>
            </div>
        </li>

        <!-- Nav Item - Utilities Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities"
               aria-expanded="true" aria-controls="collapseUtilities">
                <i class="fas fa-fw fa-wrench"></i>
                <span>Utilities</span>
            </a>
            <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
                 data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom Utilities:</h6>
                    <a class="collapse-item" href="utilities-color.html">Colors</a>
                    <a class="collapse-item" href="utilities-border.html">Borders</a>
                    <a class="collapse-item" href="utilities-animation.html">Animations</a>
                    <a class="collapse-item" href="utilities-other.html">Other</a>
                </div>
            </div>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Addons
        </div>

        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item active">
            <a class="nav-link" href="#" data-toggle="collapse" data-target="#collapsePages" aria-expanded="true"
               aria-controls="collapsePages">
                <i class="fas fa-fw fa-folder"></i>
                <span>Pages</span>
            </a>
            <div id="collapsePages" class="collapse show" aria-labelledby="headingPages"
                 data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Login Screens:</h6>
                    <a class="collapse-item" href="login.html">Login</a>
                    <a class="collapse-item" href="register.html">Register</a>
                    <a class="collapse-item" href="forgot-password.html">Forgot Password</a>
                    <div class="collapse-divider"></div>
                    <h6 class="collapse-header">Other Pages:</h6>
                    <a class="collapse-item" href="404.html">404 Page</a>
                    <a class="collapse-item active" href="blank.html">Blank Page</a>
                </div>
            </div>
        </li>

        <!-- Nav Item - Charts -->
        <li class="nav-item">
            <a class="nav-link" href="charts.html">
                <i class="fas fa-fw fa-chart-area"></i>
                <span>Charts</span></a>
        </li>

        <!-- Nav Item - Tables -->
        <li class="nav-item">
            <a class="nav-link" href="tables.html">
                <i class="fas fa-fw fa-table"></i>
                <span>Tables</span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider d-none d-md-block">

        <!-- Sidebar Toggler (Sidebar) -->
        <div class="text-center d-none d-md-inline">
            <button class="rounded-circle border-0" id="sidebarToggle"></button>
        </div>

    </ul>
    <!-- End of Sidebar -->



   */



  def handleChange(e: SyntheticEvent[html.Input, Event]): Unit = {

  }


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
    dashBoard()
    ,

    /*
    <a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

     */
    a( className:="scroll-to-top rounded", href:="#page-top")(
      i(className:="fas fa-angle-up")
    )
  )


  def dashBoard()(implicit pyramidOpt:Option[Pyramid]): ReactElement =
    div(className := "container-fluid" , id :="pyramid-root")(
      pageHeading("Dashboard"),
      div(className:="row")
    )

  def pageHeading(title: String)(implicit pyramidOpt:Option[Pyramid])=
    div(className:="d-sm-flex align-items-center justify-content-between mb-4")(
      h1(className:="h3 mb-0 text-gray-800")(title)
    )
    /*

  <footer class="sticky-footer bg-white">
              <div class="container my-auto">
                  <div class="my-auto">
                      <span id="status-messages">fsdfsdfsdsf</span>
                  </div>
              </div>
          </footer>


     */

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
