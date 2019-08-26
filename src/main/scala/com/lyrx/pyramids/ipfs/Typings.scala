package com.lyrx.pyramids.ipfs

import scala.concurrent.Future
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
@JSGlobal("OrbitDB")
object OrbitDB  extends js.Object {
  def createInstance(ipfs:Ipfs):js.Promise[OrbitDB] = js.native
}

object PimpedOrbitDB{
  def createInstance(ipfs:Ipfs):Future[OrbitDB] = OrbitDB
    .createInstance(ipfs).toFuture
}


@js.native
trait OrbitDB  extends js.Object {

}

