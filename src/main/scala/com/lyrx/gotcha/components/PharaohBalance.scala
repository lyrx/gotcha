package com.lyrx.gotcha.components


import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.util.Implicits._
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import com.lyrx.gotcha.Implicits._


@react class PharaohBalance extends Component {
  case class Props(pyramidOpt: Option[Pyramid],
                   title: String,
                   currency: String,
                   pubKey: String)

  case class State(balance: String, accountId: String,runtimeStatus: RuntimeStatus)

  override def initialState: State = State(
    balance = "",
    accountId = "",
    runtimeStatus = RuntimeStatus(msg="",status = RuntimeStatus.READY)
  )

  def simpleCard(description: String,
                 amount: String,
                 currency: String): ReactElement =
    div(className := "col-xl-3 col-md-6 my-mb-4")(
      div(className := s"card shadow h-100 py-2 ${state.runtimeStatus.blinkMe()}")(
        div(className := "card-body")(
          div(className := "row no-gutters align-items-center")(
            div(className := "col mr-2")(
              div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                s"${description}"),
              /* div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                state.account), */
              div(className := "h5 mb-0 font-weight-bold text-gray-800")(
                if(state.runtimeStatus.isOnGoing()) {
                  "Updating ..."
                }
              else{
                  s"${currency} ${if(amount == "") "??? " else amount}"

                }),
              div()(
                a(
                  href:="#",
                  onClick := (e=>{updateAccountInfo()})
                )("Update")
              ),

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

  def updateAccountInfo() = props.pyramidOpt
      .map(p => {
        if(!state.runtimeStatus.isOnGoing() && props.pyramidOpt.isDefined) {
          setState(state.copy(runtimeStatus = RuntimeStatus(
            msg = ""
            , status = RuntimeStatus.ONGOING)))
          p.stellarAccountInfo(props.pubKey)
            .map(accountData => {
              val newBalance = accountData.balanceOpt.getOrElse("")
              val newId = accountData.idOpt.getOrElse("")
                setState(
                  State(
                    balance = newBalance,
                    accountId = newId,
                    runtimeStatus = RuntimeStatus(msg="",status = RuntimeStatus.DONE)
                  ))
            })
        }}
      )


  override def componentDidMount(): Unit = {
    updateAccountInfo()
  }
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = if(
    (prevProps.pyramidOpt.isEmpty && props.pyramidOpt.isDefined)
    ||
      (prevState.balance != state.balance)
  ) updateAccountInfo()

  override def render(): ReactElement = {
    simpleCard(description = props.title,
               amount = state.balance,
               currency = props.currency)
  }
}
