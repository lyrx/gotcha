package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main
import com.lyrx.gotcha.components.Wizard.Definition
import com.lyrx.pyramids.Pyramid
import slinky.core.{StatelessComponent, StatelessComponentWrapper}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{a, button, className, div, hr, href, i, id, img, li, onClick, span, src, target, ul}

import scala.scalajs.js


object  SideBar extends StatelessComponentWrapper {

  case class Props(pyramidOpt: Option[Pyramid])


  class Def(jsProps: js.Object) extends Definition(jsProps) {

    def brand(): ReactElement =
      a(className := "sidebar-brand d-flex align-items-center justify-content-center",
        href := "https://github.com/lyrx/gotcha"
        , target := "_blank")(
        div(className := "sidebar-brand-icon rotate-n-15")(
          img(src := "img/ETER-Logo-small.png")
        ),
        div(className := "sidebar-brand-text mx-3")("Pyramids!")
      )

    def navItem(renderer: GotchaPyramidRenderer, name: String) = {

      def initMainPanel(po: Option[Pyramid]) =
        Main
          .initReactElements(po, renderer)

      li(className := "nav-item")(
        a(
          className := "nav-link",
          href := "#",
          onClick := (e => {
            e.preventDefault()
            initMainPanel(props.pyramidOpt)
          })
        )(
          i(className := "fas fa-fw fa-tachometer-alt"),
          span()(name)
        ))
    }

    override def render(): ReactElement =
      ul(
        className := "navbar-nav bg-gradient-primary sidebar sidebar-dark accordion\" id=\"accordionSidebar")(
        brand(),
        hr(className := "sidebar-divider my-0"),
        navItem(((aPyramidOpt) => IdentityManagement(IdentityManagement.Props(aPyramidOpt))),
          "Identity"),
        navItem(((aPyramidOpt) => Notary(aPyramidOpt)), "Documents"),
        hr(className := "sidebar-divider d-none d-md-block"),
        div(className := "text-center d-none d-md-inline")(
          button(className := "rounded-circle border-0", id := "sidebarToggle")
        )
      )
  }

}


