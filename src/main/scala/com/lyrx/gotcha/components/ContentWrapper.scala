package com.lyrx.gotcha.components

import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, id}

@react class ContentWrapper extends StatelessComponent {

  case class Props(pyramidOpt: Option[Pyramid], renderer: GotchaRenderer)

  override def render(): ReactElement =
    div(id := "content-wrapper", className := "d-flex flex-column")(
      div(id := "content")(
        TopBar(props.pyramidOpt),
        props.renderer() ,
        BottomBar(props.pyramidOpt)
      ),
      Footer(props.pyramidOpt)

    )

}