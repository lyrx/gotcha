package com.lyrx.gotcha

import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import ReactElements.{ec}
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import slinky.web.html._

import scala.concurrent.ExecutionContext

object MyComponents {

  val stellarPasswordFieldId = "stellar-private-key"

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
          UserBalance(props.pyramidOpt)
        )
      )

  }
  @react class ContentWrapper extends StatelessComponent {

    case class Props(pyramidOpt: Option[Pyramid], renderer: ()=>ReactElement)

    def content() = div(id := "content")(
      nav(
        className := "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow")(
        button(id := "sidebarToggleTop",
               className := "btn btn-link d-md-none rounded-circle mr-3")(
          i(className := "fa fa-bars")),
        form(
          className := "d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search")(
          Stellar(props.pyramidOpt)
        )
      ),
      //IdentityManagement(props.pyramidOpt),
      props.renderer(),
      a(className := "scroll-to-top rounded", href := "#page-top")(
        i(className := "fas fa-angle-up")
      )
    )

    override def render(): ReactElement =
      div(id := "content-wrapper", className := "d-flex flex-column")(
        content(),
        footer(className := "sticky-footer bg-white")(
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

    override def render(): ReactElement =
      ul(
        className := "navbar-nav bg-gradient-primary sidebar sidebar-dark accordion\" id=\"accordionSidebar")(
        brand(),
        hr(className := "sidebar-divider my-0"),
        li(className := "nav-item")(
          a(className := "nav-link", href := "#", onClick := (e => {
            e.preventDefault()
          }))(
            i(className := "fas fa-fw fa-tachometer-alt"),
            span()("Indentity Management")
          )),
        hr(className := "sidebar-divider d-none d-md-block"),
        div(className := "text-center d-none d-md-inline")(
          button(className := "rounded-circle border-0", id := "sidebarToggle")
        )
      )

  }
  @react class UserBalance extends Component {
    case class Props(pyramidOpt: Option[Pyramid])
    case class State(description: String,
                     currency: String,
                     amount: String,
                     account: String)

    override def initialState: State = State(
      description = "Your stellar account balance",
      currency = "XLM",
      amount = "",
      account = ""
    )
    def simpleCard(description: String,
                   amount: String,
                   currency: String): ReactElement =
      div(className := "col-xl-3 col-md-6 mb-4")(
        div(className := "card border-left-primary shadow h-100 py-2")(
          div(className := "card-body")(
            div(className := "row no-gutters align-items-center")(
              div(className := "col mr-2")(
                div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                  s"${description}"),
                /* div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                  state.account), */
                div(className := "h5 mb-0 font-weight-bold text-gray-800")(
                  s"${currency} ${amount}")
              ),
              div(className := "col-auto")(
                i(className := "fas fa-calendar fa-2x text-gray-300")
              )
            )
          )
        )
      )

    override def componentDidUpdate(prevProps: Props,
                                    prevState: State): Unit = {
      val pw = document
        .getElementById(stellarPasswordFieldId)
        .asInstanceOf[HTMLInputElement]
        .value
      if (prevProps.pyramidOpt.isEmpty)
        props.pyramidOpt
          .map(
            p =>
              p.balanceStellar(pw, p.config.blockchainData.stellar.testNet)
                .map(_.map(balance => {
                  p.privateStellarAccountId(
                      pw,
                      p.config.blockchainData.stellar.testNet)
                    .map(accountId => {
                      setState(
                        state
                          .copy(amount = balance, account = accountId)
                      )
                    })

                }))
                .onComplete(t => t.failed.map(e => println(s"${e}"))))
    }

    override def render(): ReactElement =
      simpleCard(description = state.description,
                 amount = state.amount,
                 currency = state.currency)
  }

  @react class Stellar extends Component {

    val pw = "SBSN4GWX4B7ALR5BDYH4VGWUWMAURFG6Y2SHJQL6CP62JT2N3Q42RPHI"

    case class Props(pyramidOpt: Option[Pyramid])
    case class State(password: String)

    override def render(): ReactElement = div(className := "input-group")(
      img(src := "img/stellar.png"),
      input(
        `type` := "password",
        defaultValue := pw,
        className := "form-control bg-light border-0 small",
        placeholder := "Stellar Private Key",
        id := stellarPasswordFieldId,
        onChange := (e => {
          e.preventDefault()
          setState(state.copy(password = e.target.value))
        })
      ),
      a(href := "#",
        className := "btn btn-secondary btn-icon-split",
        onClick := (e => {
          e.preventDefault()
        }))(
        span(className := "icon text-white-50")(
          i(className := "fas fa-arrow-right")
        )
      )
    )

    override def initialState: State = State(pw)
  }
  @react class IdentityManagementWrapper extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def initPyramid(isTestNet: Boolean)(
        implicit executionContext: ExecutionContext) =
      Config
        .createFuture(isTestNet)
        .flatMap(
          Pyramid(_)
            .loadPharaohKey())

    override def render(): ReactElement = {
      implicit val pyrOpt: Option[Pyramid] = props.pyramidOpt
      div(id := "wrapper")(
        SideBar(pyrOpt),
        ContentWrapper(pyrOpt, (() =>IdentityManagement(pyrOpt) ))
      )
    }

    override def componentDidMount(): Unit = {
      initPyramid(true)
        .map(
          p =>
            ReactElements
              .renderAll(IdentityManagementWrapper(Some(new Pyramid(p.config
                .withMessage("Eternalize Your Documents In The Pyramid!"))))))
    }
  }

}
