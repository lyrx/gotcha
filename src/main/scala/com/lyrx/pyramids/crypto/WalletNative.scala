package com.lyrx.pyramids.crypto

import org.scalajs.dom.crypto.JsonWebKey

import scala.scalajs.js


@js.native
trait KeypairNative extends js.Object {
  val `private`: js.UndefOr[JsonWebKey] = js.native
  val `public` : js.UndefOr[JsonWebKey] = js.native
}



@js.native
trait WalletNative extends js.Object {
  val sym:js.UndefOr[JsonWebKey] = js.native
  val asym:js.UndefOr[KeypairNative] = js.native
  val sign:js.UndefOr[KeypairNative] = js.native

}
