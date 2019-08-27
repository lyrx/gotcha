package com.lyrx.gotcha.components

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import com.lyrx.gotcha._

@react class IdentityManagement extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Your Decentralized Identity Management"),
      div(className := "row")(

        PublishIdentity(PublishIdentity.Props(props.pyramidOpt))

        ,(if (props.pyramidOpt.map(_.hasIdentity()).getOrElse(false))
          ShowIdentity(props.pyramidOpt)
        else
          div())

        ,(if (props.pyramidOpt
               .map(p => p.config.p2pData.ipfsData.regOpt.isDefined)
               .getOrElse(false)) {
           RegisterIdentity(props.pyramidOpt)
         } else div())
      ),
      div(className := "row")(
        Credentials(props.pyramidOpt)
      ),
      div(className := "row")(
        PharaohBalance(
          props.pyramidOpt,
          title = "Client Account",
          currency = "XLM",
          pubKey = props.pyramidOpt.clientAccount()
        )
      )
    )

}
