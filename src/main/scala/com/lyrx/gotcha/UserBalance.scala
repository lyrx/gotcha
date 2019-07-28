package com.lyrx.gotcha

import com.lyrx.gotcha.MyComponents.{simpleCard}
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import Main.ec

@react class UserBalance extends Component {
  case class Props(pyramidOpt: Option[Pyramid])
  case class State(description: String,
                   currency: String,
                   amount: String)

  override def initialState: State = State(
    description = "Client account balance",
    currency = "XLM",
    amount = "")


  def readBalance() = {
    val pw = MyComponents.passwordField.current.value
    props.pyramidOpt
      .map(
        p =>
          p.balanceStellar(pw)
            .map(_.map(balance => {
              setState(
                state
                  .copy(amount = balance)
              )
            }))
            .onComplete(t => t.failed.map(e => println(s"${e}"))))
  }
  override def componentDidMount(): Unit = {
    readBalance()
  }
  override def componentDidUpdate(prevProps: Props,
                                  prevState: State): Unit = {
    if (prevProps.pyramidOpt.isEmpty)
      readBalance()
  }

  override def render(): ReactElement = {
    simpleCard(description = state.description,
      amount = state.amount,
      currency = state.currency)
  }
}
