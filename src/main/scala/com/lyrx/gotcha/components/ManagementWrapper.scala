package com.lyrx.gotcha.components

import com.lyrx.gotcha.{ Main}
import com.lyrx.pyramids.{Config, Pyramid}
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{div, id}
import Main.ec
import scala.concurrent.{ExecutionContext, Future}


@react class ManagementWrapper extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid],
                   renderer: GotchaPyramidRenderer)

  def initPyramid(isTestNet: Boolean)(
    implicit executionContext: ExecutionContext) =
    props.pyramidOpt
      .map(p => Future { p })
      .getOrElse(
        Config
          .createFuture(isTestNet)
          .flatMap(Pyramid(_)
            .loadPharaohKey())
      )

  override def render(): ReactElement = {
    implicit val pyrOpt: Option[Pyramid] = props.pyramidOpt
    div(id := "wrapper")(
      SideBar(pyrOpt),
      ContentWrapper(pyrOpt, (() => props.renderer(pyrOpt)))
    )
  }

  override def componentDidMount(): Unit = {
    initPyramid(false)
      .map(
        p =>
          Main
            .renderAll(
              ManagementWrapper(
                Some(new Pyramid(p.config
                  .withMessage("Eternalize Your Documents!"))),
                props.renderer)))
  }
}

