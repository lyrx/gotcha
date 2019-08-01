package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import com.lyrx.pyramids.util.Implicits._
import scala.concurrent.Future

@react class PharaohBalance extends Component {
  case class Props(pyramidOpt: Option[Pyramid],
                   title: String,
                   currency: String,
                   pubKey: String)

  case class State(balance: String, accountId: String)

  override def initialState: State = State(balance = "", accountId = "")

  def simpleCard(description: String,
                 amount: String,
                 currency: String): ReactElement =
    div(className := "col-xl-3 col-md-6 mb-4")(
      div(className := "card shadow h-100 py-2")(
        div(className := "card-body")(
          div(className := "row no-gutters align-items-center")(
            div(className := "col mr-2")(
              div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                s"${description}"),
              /* div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                state.account), */
              div(className := "h5 mb-0 font-weight-bold text-gray-800")(
                s"${currency} ${amount}"),
              div(className := "")(
                a(
                  href :=s"https://${props.pyramidOpt.steepx()}/account/${state.accountId}#payments"
                  , target :="_blank"
                )(s"Account Details"))
            ),
            div(className := "col-auto")(
              i(className := "fas fa-coins fa-2x text-gray-300")
            )
          )
        )
      )
    )

  def updateAccountInfo() =if(props.pubKey!="")
    props.pyramidOpt
      .map(p => p.stellarAccountInfo(props.pubKey))
      .flip()
      .map(_.map(accountData => {
        val newBalance = accountData.balanceOpt.getOrElse("")
        val newId = accountData.idOpt.getOrElse("")
        if (state.accountId != newId || state.balance != newBalance)
          setState(
            State(
              balance = newBalance,
              accountId = newId
            ))
      }))

  override def componentDidMount(): Unit = {
    updateAccountInfo()
  }
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    updateAccountInfo()
  }

  override def render(): ReactElement = {
    simpleCard(description = props.title,
               amount = state.balance,
               currency = props.currency)
  }
}
