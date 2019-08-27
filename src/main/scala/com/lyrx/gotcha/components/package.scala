package com.lyrx.gotcha

import com.lyrx.pyramids.{AccountData, Pyramid}
import org.scalajs.dom.File
import org.scalajs.dom.raw.EventTarget
import slinky.core.facade.ReactElement

import scala.concurrent.Future
import Main.ec


package object components {
  type GotchaRenderer = () => ReactElement
  type GotchaPyramidRenderer = (Option[Pyramid]) => ReactElement
  type FAccountData = Future[AccountData]


  implicit class PimpedEvent(target: EventTarget) {

    def fileOpt() =
      target
        .asInstanceOf[Typings.DataTransferTarget]
        .files
        .map(_.headOption.map(_.asInstanceOf[File]))
        .getOrElse(None)
  }

  implicit class PimpedPyramidOpt(po: Option[Pyramid]) {

    def isStellarTestNet() =
      po.map(_.config.p2pData.stellar.testNet).getOrElse(false)

    def steepx() =
      if (isStellarTestNet())
        "testnet.steexp.com"
      else
        "steexp.com"

    def stellarData()=po.map(_.config.p2pData.stellar)

    def stellarPassword() = stellarData().map(_.passwordFieldValueDefault()).getOrElse("")


    def clientAccount() =  po.map(_.stellarFromSecret(po.stellarPassword())).getOrElse("")



  }

}
