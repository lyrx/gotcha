package com.lyrx.gotcha

import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import ReactElements.ec
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import slinky.web.html._

import scala.concurrent.ExecutionContext

object MyComponents {

  val stellarPasswordFieldId="stellar-private-key"



  /*


  @react class SideBar extends Component {

    case class Props(pyramidOpt: Option[Pyramid])

    case class State(description: String, currency: String, amount: String, account: String)

  }
*/



  @react class UserBalance extends Component {
    case class Props(pyramidOpt: Option[Pyramid])
    case class State(description: String,currency:String, amount:String, account:String)

    override def initialState: State = State(
     description="Your stellar account balance",
      currency="XLM",
      amount ="",
      account = ""
    )
    def simpleCard(description:String,amount:String,currency:String): ReactElement =
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

    override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
      val pw = document.
        getElementById(stellarPasswordFieldId).
        asInstanceOf[HTMLInputElement]
        .value
      if( prevProps.pyramidOpt.isEmpty)
        props
          .pyramidOpt
          .map(p => p
            .balanceStellar(pw,p.config.blockchainData.stellar.testNet)
            .map(
              _.map(balance=>{
                p.privateStellarAccountId(pw,p.config.blockchainData.stellar.testNet)
                    .map(accountId=>{
                      setState(
                        state
                          .copy(
                            amount=balance,
                            account = accountId)
                      )
                    })

              }))
            .onComplete(t=>t.failed.map(e=>println(s"${e}")))
          )
    }

    override def render(): ReactElement = simpleCard(
      description = state.description,
      amount  = state.amount,
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
        id :=stellarPasswordFieldId,
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
  @react class IdentityManagement extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def initPyramid(isTestNet:Boolean)(implicit executionContext: ExecutionContext) =
      Config
        .createFuture(isTestNet)
        .flatMap(
          Pyramid(_)
            .loadPharaohKey())

    override def render(): ReactElement = {
      implicit val pyrOpt: Option[Pyramid] = props.pyramidOpt
      div(id := "wrapper")(
        ReactElements.sidebar(),
        ReactElements.contentWrapper()
      )
    }

    override def componentDidMount(): Unit = {
      initPyramid(true)
        .map(
          p =>
            ReactElements
              .renderAll(IdentityManagement(Some(new Pyramid(p.config
                .withMessage("Eternalize Your Documents In The Pyramid!"))))))
    }
  }

}
