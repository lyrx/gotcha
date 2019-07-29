package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, i, s}

import scala.concurrent.Future

@react class PharaohBalance extends Component {
  case class Props(
                    pyramidOpt: Option[Pyramid],
                    retriever:(Option[Pyramid]=>Future[Option[String]]),
                    title:String,
                    currency:String)

  case class State(amount: String)

  override def initialState: State = State( amount = "")


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
                s"${currency} ${amount}")
            ),
            div(className := "col-auto")(
              i(className := "fas fa-coins fa-2x text-gray-300")
            )
          )
        )
      )
    )


  def readBalance() = props
    .retriever(props.pyramidOpt)
     .map(_.map(balance => setState(
         state
           .copy(amount = balance)
       )))
     .onComplete(t => t.failed.map(e => println(s"${e}")))


  override def componentDidMount(): Unit = {
    readBalance()
  }
  override def componentDidUpdate(prevProps: Props,
                                  prevState: State): Unit = {
    if (prevProps.pyramidOpt.isEmpty)
      readBalance()
  }

  override def render(): ReactElement = {
    simpleCard(description = props.title,
      amount = state.amount,
      currency = props.currency)
  }
}
