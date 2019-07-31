package com.lyrx.pyramids.crypto

import org.scalajs.dom.crypto.JsonWebKey

import scala.scalajs.js


@js.native
trait KeypairNative extends js.Object {
  val `private`: js.UndefOr[JsonWebKey] = js.native
  val `public` : js.UndefOr[JsonWebKey] = js.native
}

@js.native
trait IdentityNative extends js.Object {
  val name: js.UndefOr[String] = js.native
}



@js.native
trait WalletNative extends js.Object {
  val identity:js.UndefOr[IdentityNative] = js.native
  val sym:js.UndefOr[JsonWebKey] = js.native
  val asym:js.UndefOr[KeypairNative] = js.native
  val sign:js.UndefOr[KeypairNative] = js.native

}
