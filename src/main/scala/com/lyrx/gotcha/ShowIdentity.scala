package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.util.Implicits._
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Anchor
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{Component, StatelessComponent, SyntheticEvent}
import slinky.web.html._



@react class ShowIdentity extends StatelessComponent {

  case class Props(
      pyramidOpt: Option[Pyramid],
  )


  def identHash()=props
    .pyramidOpt
    .flatMap(_.config.ipfsData.identityOpt.map(_.identity.getOrElse("")))
    .getOrElse("")


  override def componentDidMount(): Unit = {}

  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {}

  override def render(): ReactElement =
    div(className := "card shadow mb-4 my-card")(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Your Identity"
        )
      ),
      div(className := "my-card-body")(
        div()(
          a(href:=s"https://ipfs.infura.io/ipfs/${identHash()}")(s"${identHash()}")
        )
      )
    )

}
