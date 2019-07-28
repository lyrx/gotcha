package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.gotcha.MyComponents.simpleCard
import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

import scala.concurrent.Future

@react class PharaohBalance extends Component {
  case class Props(
                    pyramidOpt: Option[Pyramid],
                    retriever:(Option[Pyramid]=>Future[Option[String]]),
                    title:String,
                    currency:String)

  case class State(amount: String)

  override def initialState: State = State( amount = "")


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
