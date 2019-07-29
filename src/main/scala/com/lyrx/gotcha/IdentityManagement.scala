package com.lyrx.gotcha



import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.Future
import Main.ec


@react class IdentityManagement extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Your Trustless Blockchain Notary"),
      div(className := "row")(
        PharaohBalance(
          props.pyramidOpt,
          retriever =
            (_.map(_.balanceStellar(MyComponents.passwordField.current.value))
              .getOrElse(Future { None })),
          title = "Client Account",
          currency = "XLM"
        ),
        Credentials(props.pyramidOpt)

      )
    )

}
