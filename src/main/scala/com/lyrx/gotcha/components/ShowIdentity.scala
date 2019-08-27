package com.lyrx.gotcha.components

import com.lyrx.gotcha.MyComponents
import com.lyrx.gotcha.components.Wizard.Definition
import com.lyrx.pyramids.Pyramid
import slinky.core.{StatelessComponent, StatelessComponentWrapper}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.scalajs.js



object  ShowIdentity extends StatelessComponentWrapper {

  case class Props(
      pyramidOpt: Option[Pyramid],
  )

  class Def(jsProps: js.Object) extends Definition(jsProps) {

    def identHash() = props
      .pyramidOpt
      .flatMap(_.config.p2pData.ipfsData.identityOpt.map(_.identity.getOrElse("")))
      .getOrElse("")


    override def componentDidMount(): Unit = {}

    override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {}

    override def render(): ReactElement =if (props.pyramidOpt.map(_.hasIdentity()).getOrElse(false))
      div(className := "card shadow my-mb-4 my-card")(
        div(className := "card-header py-3")(
          h6(className := "m-0 font-weight-bold text-primary")(
            "Your Identity"
          )
        ),
        div(className := "my-card-body")(
          div()(
            a(
              href := MyComponents.hashUrl(identHash())
              , target := "_blank"
            )
            (
              //  s"${identHash()}"
              img(src := "img/fingerprint.png")
            )
          )
        )
      ) else div()
  }

}
