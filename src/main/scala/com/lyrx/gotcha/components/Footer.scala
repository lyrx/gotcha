package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, footer, id, span}

@react class Footer extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  def render(): ReactElement = footer(className := "sticky-footer bg-white")(
    div(className := "container my-auto")(
      div(className := "my-auto")(
        span(id := "status-messages")(
          props.pyramidOpt
            .map(_.config.frontendData.message)
            .getOrElse("Working, please wait")
            .toString)
      )
    )
  )

}