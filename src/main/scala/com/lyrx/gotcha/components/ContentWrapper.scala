package com.lyrx.gotcha.components

import com.lyrx.gotcha.components.Wizard.Definition
import com.lyrx.pyramids.Pyramid
import slinky.core.{StatelessComponent, StatelessComponentWrapper}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div, id}

import scala.scalajs.js

object ContentWrapper extends StatelessComponentWrapper {

  case class Props(pyramidOpt: Option[Pyramid], renderer: GotchaRenderer)


  class Def(jsProps: js.Object) extends Definition(jsProps) {

    override def render(): ReactElement =
      div(id := "content-wrapper", className := "d-flex flex-column")(
        div(id := "content")(
          TopBar(props.pyramidOpt),
          props.renderer(),
          BottomBar(props.pyramidOpt)
        ),
        Footer(props.pyramidOpt)

      )
  }

}