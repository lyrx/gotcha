package com.lyrx.pyramids.util

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import scala.scalajs.js.typedarray

@js.native
@JSGlobal
class TextEncoder(utfLabel: js.UndefOr[String]= "utf-8" ) extends js.Object {
  def encode(buffer: String): typedarray.Uint8Array = js.native
}

@js.native
@JSGlobal
class TextDecoder(utfLabel: js.UndefOr[String] = "utf-8") extends js.Object {
  def decode(buffer: typedarray.Uint8Array): String = js.native
}



@js.native
trait MetaData extends js.Object{
  val name:String = js.native
  val `type`:String = js.native
  val size:Int = js.native

}
