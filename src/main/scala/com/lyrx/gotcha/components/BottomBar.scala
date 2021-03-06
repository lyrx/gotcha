package com.lyrx.gotcha.components

import com.lyrx.gotcha.Main
import com.lyrx.pyramids.ipfsapi.IpfsSupport
import com.lyrx.pyramids.{IPFS, Pyramid}
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Select
import slinky.core.annotations.react
import slinky.core.facade.{ErrorBoundaryInfo, ReactElement}
import slinky.core.{StatelessComponent, SyntheticEvent}
import slinky.web.html._

import scala.scalajs.js

@react class BottomBar extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  val navClasses =
    "navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow"

  def initIpfs(p: Pyramid, h: IPFS, msg: String) =
    Main.initWithIdentityManagement(
      Some(new Pyramid(p.config.withIpfssupport(h).withMessage(msg))))

  def handleChange(e: SyntheticEvent[Select, Event]) =
    props.pyramidOpt
      .map(p =>
        e.target.value match {

          case "hosttech" =>
            initIpfs(p,
                     () => IpfsSupport.hosttech(),
                     "Using Pyramids!  for IPFS")

          case "infura" =>
            initIpfs(p,
                     () => IpfsSupport.infura(),
                     "Using Infura gateway for IPFS")

          case "macmini" =>
            initIpfs(p, () => IpfsSupport.macmini(), "Using macmini  for IPFS")


          case _ => initIpfs(p, () => IpfsSupport.hosttech(), "Using Pyramids! for IPFS")

      })

  def isLocal() = {
    val s = dom.window.location.href
    (s.contains("127.")
    ||
    s.contains("localhost"))
  }

  def options() =
    if (isLocal())
      Seq(
        option(value := "hosttech", key:="hosttech")("Pyramids!"),
        option(value := "infura", key:="infura")("Infura"),
        option(value := "macmini", key:="macmini")("Macmini")
      )
    else
      Seq(
        option(value := "hosttech", key:="hosttech")("Pyramids!"),
        option(value := "infura", key:="infura")("Infura")
      )

  def render(): ReactElement = section(
    nav(className := navClasses)(
      a(href := "https://ipfs.io", target := "_blank")(
        img(src := "img/ipfs.png")
      ),
      span(className := "my-label")("Gateway: "),
      select(
        name := "IPFS Gateway",
        defaultValue := "hosttech",
        onChange := (handleChange(_))
      )(options())
    ),
    nav(className := navClasses)(
      a(href := "https://www.ethereum.org/", target := "_blank")(
        img(src := "img/ethereum.png"))
    )
  )
}
