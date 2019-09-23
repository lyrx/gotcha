package com.lyrx.pyramids.libp2p

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import typings.nodeLib
import typings.nodeLib.bufferMod

object Typings {

  def protectorFrom(s: String) = new Protector(bufferMod.Buffer.from(s))

  @JSImport("libp2p-pnet", "Protector")
  @js.native
  class Protector(b: nodeLib.Buffer) extends js.Object {}

}
