package com.lyrx.gotcha

import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{div, id}

import scala.concurrent.ExecutionContext

object MyComponents {





  @react class Pyramidal extends StatelessComponent  {
    case class Props(pyramidOpt: Option[Pyramid])


    def initPyramid()(implicit executionContext: ExecutionContext) =
      Config
        .createFuture()
        .flatMap(
          Pyramid(_)
            .loadPharaohKey())

    override def render(): ReactElement ={
      implicit val pyrOpt:Option[Pyramid] = props.pyramidOpt
      div(id:="wrapper")(
        ReactElements.sidebar(),
        ReactElements.contentWrapper()
      )
    }

    override def componentDidMount(): Unit = {
      import ReactElements.ec
      initPyramid()
        .map(p => ReactElements
          .renderAll(Pyramidal(
            Some(
              new Pyramid(
                p
                  .config
                  .withMessage
                  ("Eternalize Your Documents In The Pyramid!"))))))
    }
  }

}
