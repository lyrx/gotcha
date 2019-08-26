package com.lyrx.pyramids.ipfs

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

@js.native
@JSGlobal
class Ipfs extends js.Object {

  def this(config: js.Dynamic) = this()

  def on(event:String,cb:js.Function1[js.Error,Unit]):Unit = js.native

  def on(event:String,cb:js.Function0[Unit]):Unit = js.native


}


@js.native
@JSGlobal
class OrbitDB  extends js.Object {
  def createInstance(ipfs:Ipfs):OrbitDB = js.native
}



