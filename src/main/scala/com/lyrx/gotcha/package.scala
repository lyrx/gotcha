package com.lyrx

import com.lyrx.pyramids.Pyramid
import org.scalajs.dom.File
import org.scalajs.dom.raw.EventTarget
import slinky.core.facade.ReactElement

package object gotcha {

  type GotchaRenderer = () => ReactElement
  type GotchaPyramidRenderer = (Option[Pyramid]) => ReactElement

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
      po.map(_.config.blockchainData.stellar.testNet).getOrElse(false)

    def steepx() =
      if (isStellarTestNet())
        "testnet.steexp.com"
      else
        "steexp.com"

  }

}
