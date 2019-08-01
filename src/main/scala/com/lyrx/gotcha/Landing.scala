package com.lyrx.gotcha

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._


@react class Landing extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])






  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Pyramids! Your Trustless Blockchain Notary and Identity Management"),

    )

}
