package com.lyrx.gotcha

import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}

import org.scalajs.dom
import slinky.web.html._
import Main.ec

import scala.concurrent.{ExecutionContext, Future}

object MyComponents {

  val passwordField = React.createRef[dom.html.Input]

  def pageHeading(title: String) =
    div(
      className := "d-sm-flex align-items-center justify-content-between mb-4")(
      h1(className := "h3 mb-0 text-gray-800")(title)
    )
  @react class IdentityManagement extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def render(): ReactElement =
      div(className := "container-fluid", id := "pyramid-root")(
        pageHeading("Identity Management"),
        div(className := "row")(
          PharaohBalance(
            props.pyramidOpt,
            retriever =
              (_.map(_.balanceStellar(MyComponents.passwordField.current.value))
                .getOrElse(Future { None })),
            title = "Client Account",
            currency = "XLM"
          )
        )
      )

  }


  @react class TopBar extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def render(): ReactElement =
      nav(
        className := "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow")(
        button(id := "sidebarToggleTop",
               className := "btn btn-link d-md-none rounded-circle mr-3")(
          i(className := "fa fa-bars")),
        form(
          className := "d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100")(
          Stellar(props.pyramidOpt)
        )
      )
  }
  @react class BottomBar extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def render(): ReactElement =
      nav(
        className := "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow")(
        button(id := "sidebarToggleTop",
          className := "btn btn-link d-md-none rounded-circle mr-3")(
          i(className := "fa fa-bars")),
        form(
          className := "d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100")(
          Stellar(props.pyramidOpt)
        )
      )
  }


  @react class Footer extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def render(): ReactElement = footer(className := "sticky-footer bg-white")(
      div(className := "container my-auto")(
        div(className := "my-auto")(
          span(id := "status-messages")(
            props.pyramidOpt
              .map(_.config.frontendData.message)
              .getOrElse("Working, please wait")
              .toString)
        )
      )
    )

  }
  @react class ContentWrapper extends StatelessComponent {

    case class Props(pyramidOpt: Option[Pyramid], renderer: GotchaRenderer)

    override def render(): ReactElement =
      div(id := "content-wrapper", className := "d-flex flex-column")(
        div(id := "content")(
          TopBar(props.pyramidOpt),
          props.renderer() //,
          //BottomBar(props.pyramidOpt)
        ),
        Footer(props.pyramidOpt)

      )

  }

  @react class SideBar extends StatelessComponent {

    case class Props(pyramidOpt: Option[Pyramid])

    def brand(): ReactElement =
      a(className := "sidebar-brand d-flex align-items-center justify-content-center",
        href := "index.html")(
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
        navItem(((aPyramidOpt) => IdentityManagement(aPyramidOpt)),
                "Indentity"),
        navItem(((aPyramidOpt) => Notary(aPyramidOpt)), "Notary"),
        hr(className := "sidebar-divider d-none d-md-block"),
        div(className := "text-center d-none d-md-inline")(
          button(className := "rounded-circle border-0", id := "sidebarToggle")
        )
      )

  }




  @react class ManagementWrapper extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid],
                     renderer: GotchaPyramidRenderer)

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

    override def render(): ReactElement = {
      implicit val pyrOpt: Option[Pyramid] = props.pyramidOpt
      div(id := "wrapper")(
        SideBar(pyrOpt),
        ContentWrapper(pyrOpt, (() => props.renderer(pyrOpt)))
      )
    }

    override def componentDidMount(): Unit = {
      initPyramid(true)
        .map(
          p =>
            Main
              .renderAll(
                ManagementWrapper(
                  Some(new Pyramid(p.config
                    .withMessage("Eternalize Your Documents In The Pyramid!"))),
                  props.renderer)))
    }
  }

}
