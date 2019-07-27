package com.lyrx.gotcha

import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import ReactElements.ec
import ReactElements.isTest
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import slinky.web.html._

import scala.concurrent.ExecutionContext

object MyComponents {

  val stellarPasswordFieldId="stellar-private-key"



  @react class Balance extends Component {
    case class Props(pyramidOpt: Option[Pyramid])
    case class State(description: String,currency:String, amount:String)

    override def initialState: State = State(
     description="Your stellar account balance",
      currency="XLM",
      amount ="..."
    )

    override def componentDidUpdate(prevProps: Props, prevState: State): Unit = if( prevProps.pyramidOpt.isEmpty) {
      val pw = document.
        getElementById(stellarPasswordFieldId).
        asInstanceOf[HTMLInputElement]
        .value
      props
      .pyramidOpt
        .map(p => p.balanceStellar(
          pw
         ).map(_.map(s=>setState(
          state
            .copy(
              amount=s)
        )))
          .onComplete(t=>t.failed.map(e=>println(s"${e}")))
        )
    }


    override def render(): ReactElement = {
      div(className := "col-xl-3 col-md-6 mb-4")(
        div(className := "card border-left-primary shadow h-100 py-2")(
          div(className := "card-body")(
            div(className := "row no-gutters align-items-center")(
              div(className := "col mr-2")(
                div(className := "text-xs font-weight-bold text-primary text-uppercase mb-1")(
                  state.description),
                div(className := "h5 mb-0 font-weight-bold text-gray-800")(
                  s"${state.currency} ${state.amount}")
              ),
              div(className := "col-auto")(
                i(className := "fas fa-calendar fa-2x text-gray-300")
              )
            )
          )
        )
      )
    }
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
  @react class Pyramidal extends StatelessComponent {
    case class Props(pyramidOpt: Option[Pyramid])

    def initPyramid()(implicit executionContext: ExecutionContext) =
      Config
        .createFuture()
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
      initPyramid()
        .map(
          p =>
            ReactElements
              .renderAll(Pyramidal(Some(new Pyramid(p.config
                .withMessage("Eternalize Your Documents In The Pyramid!"))))))
    }
  }

}
