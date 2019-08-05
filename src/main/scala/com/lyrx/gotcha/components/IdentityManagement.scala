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

  def balance()= PharaohBalance(
    props.pyramidOpt,
    title = "Client Account",
    currency = "XLM",
    pubKey = props.pyramidOpt.clientAccount()

  )


  def renderShowIdentity():ReactElement= if(
    props.pyramidOpt.map(_.hasIdentity()).getOrElse(false))
    ShowIdentity(props.pyramidOpt)
  else
    div()



  def renderRegistry():ReactElement= props
    .pyramidOpt
    .flatMap(_.config.ipfsData.regOpt.map(s=>
      div(className := "row")(
        RegisterIdentity(props.pyramidOpt)
      )))
    .getOrElse(div(className:="row"))

  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Your Trustless Blockchain Notary and Identity Management"),
      div(className := "row")(
        balance(),
        Credentials(props.pyramidOpt)
      ),
      div(className := "row")(
        PublishIdentity(props.pyramidOpt),
          renderShowIdentity()
      ),
      renderRegistry()
    )

}