package com.lyrx.gotcha

import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.{Component, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.ExecutionContext

object MyComponents {

  @react class Stellar extends Component {

    val pw = "SBSN4GWX4B7ALR5BDYH4VGWUWMAURFG6Y2SHJQL6CP62JT2N3Q42RPHI"

    case class Props(pyramidOpt:Option[Pyramid])
    case class State(password:String)


    override def render(): ReactElement = div(className := "input-group")(
      img(src := "img/stellar.png"),
      input(
        `type` := "password",
        defaultValue:=pw,
        className := "form-control bg-light border-0 small",
        placeholder := "Stellar Private Key",
        id:="stellar-private-key" ,
        onChange:= (e=>{
          e.preventDefault()
          setState(state.copy(password = e.target.value))
        })

      ),
      /*
      <a href="#" class="btn btn-secondary btn-icon-split">
                    <span class="icon text-white-50">
                      <i class="fas fa-arrow-right"></i>
                    </span>
                    <span class="text">Split Button Secondary</span>
                  </a>
       */
      a( href:="#",
        className:="btn btn-secondary btn-icon-split",
        onClick:= ( e=>{
          e.preventDefault()
        })
      )(
        span(className:="icon text-white-50")(
          i(className:="fas fa-arrow-right")
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
      import ReactElements.ec
      initPyramid()
        .map(
          p =>
            ReactElements
              .renderAll(Pyramidal(Some(new Pyramid(p.config
                .withMessage("Eternalize Your Documents In The Pyramid!"))))))
    }
  }

}
