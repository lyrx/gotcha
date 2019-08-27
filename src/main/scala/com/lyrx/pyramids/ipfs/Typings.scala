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
object OrbitDB  extends js.Object {
  def createInstance(ipfs:Ipfs):OrbitDB = js.native
}

@js.native
trait OrbitDB  extends js.Object {


}


@js.native
trait OrbitDBInstance  extends js.Object {
  val events:Events = js.native

}
@js.native
trait Events  extends js.Object {

  def on(event:String,cb:js.Function0[Unit]):Unit=js.native
  def on(event:String,cb:js.Function5[js.Any,String,Entry,Double,Double,Unit]):Unit=js.native

}

@js.native
trait Entry  extends js.Object {
  val clock:Clock = js.native

}
@js.native
trait Clock  extends js.Object {
  val time:js.Any  = js.native

}




