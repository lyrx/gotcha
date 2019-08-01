package com.lyrx.gotcha

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, id}

import scala.concurrent.Future
import Main.ec




@react class Notary extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Notary"),
      div(className := "row")(
        PharaohBalance(
          props.pyramidOpt,
          title = "Notary Docs",
          currency = "XLM",
          pubKey = MyComponents.docsField.current.value
        ),
        PharaohBalance(
          props.pyramidOpt,
          title = "Notary Ids",
          currency = "XLM",
          pubKey = MyComponents.idsField.current.value

        )



      )
    )
}