package com.lyrx.gotcha.components

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.{StatelessComponent, StatelessComponentWrapper}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import com.lyrx.gotcha._
import com.lyrx.gotcha.components.Wizard.Definition

import scala.scalajs.js

object IdentityManagement extends StatelessComponentWrapper {
  case class Props(pyramidOpt: Option[Pyramid])

  class Def(jsProps: js.Object) extends Definition(jsProps) {
    def render(): ReactElement =
      div(className := "container-fluid", id := "pyramid-root")(
        pageHeading("Your Decentralized Identity Management"),
        div(className := "row")(
          PublishIdentity(PublishIdentity.Props(props.pyramidOpt)),
          ShowIdentity(ShowIdentity.Props(props.pyramidOpt)),
          OrbitDBComponent(props.pyramidOpt),
          RegisterIdentity(RegisterIdentity.Props(props.pyramidOpt))
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

}
