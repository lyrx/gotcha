package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main
import com.lyrx.pyramids.Ipfs.IpfsSupport
import com.lyrx.pyramids.{IPFS, Pyramid}
import org.scalajs.dom.Event
import org.scalajs.dom.html.Select
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{StatelessComponent, SyntheticEvent}
import slinky.web.html._

@react class BottomBar extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  val navClasses =
    "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow"

  def initIpfs(p: Pyramid, h: IPFS,msg:String) =
    Main.initWithIdentityManagement(
      Some(
        new Pyramid(
          p.config.withIpfssupport(h).withMessage(msg))))


  def handleChange(e: SyntheticEvent[Select, Event]) = props
    .pyramidOpt
    .map(p=>e.target.value match {

    case "local" => initIpfs(p, () => IpfsSupport.macmini(),"Using Pyramids! gateway for IPFS")

    case "infura" => initIpfs(p, () => IpfsSupport.infura(),"Using Infura gateway for IPFS")

    case "aws" => initIpfs(p, () => IpfsSupport.aws(),"Using AWS  for IPFS")

    case _ => initIpfs(p, () => IpfsSupport.infura(),"Using Default gateway (Infura) for IPFS")


    })

  def render(): ReactElement = section(
    nav(className := navClasses)(
      img(src := "img/ipfs.png"),
      span(className := "my-label")("Gateway: "),
      select(
        name := "IPFS Gateway"
        ,defaultValue:="infura"
        ,onChange:= (handleChange(_))
      )(
        option(value := "infura")("Infura"),
        option(value := "local")("Pyramids!"),
        option(value := "aws")("AWS")
      )
    ),
    nav(className := navClasses)(
      img(src := "img/ethereum.png")
    )
  )
}
