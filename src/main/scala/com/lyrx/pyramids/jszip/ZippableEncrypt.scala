package com.lyrx.pyramids.jszip


import scala.scalajs.js
import js.typedarray.ArrayBuffer
import js.typedarray.Uint8Array
import scala.concurrent.{ExecutionContext, Future}

case class ZippableEncrypt(unencrypted: Option[ArrayBuffer],
                           encrypted: Option[ArrayBuffer],
                           random: Option[ArrayBuffer],
                           signature: Option[ArrayBuffer],
                           metaData: Option[ArrayBuffer],
                           metaRandom: Option[ArrayBuffer],
                           signer: Option[ArrayBuffer]) {

  private def convert(b: ArrayBuffer) =
    new Uint8Array(b).asInstanceOf[typings.stdLib.Uint8Array]

  def zipped()(implicit executionContext: ExecutionContext): Future[typings.nodeLib.Buffer] =
    signature
      .map(
        s =>
          withMetaData()
            .file(SIGNATURE, convert(s))
            .file(SIGNER, convert(signer.get))
      )
      .getOrElse(withMetaData()).dump()




  def orEncrypted() =
    if (encrypted.isDefined)
      this
    else
      ZippableEncrypt(this.unencrypted, None, None, signature, None, None, None)

  private def withMetaData() =
    metaData
      .map(
        md =>
          zippedUnsigned()
            .file(META, convert(md))
            .file(METARANDOM, convert(metaRandom.get))
      )
      .getOrElse(zippedUnsigned())

  private def zippedUnsigned() =
    orEncrypted().encrypted
      .map(
        b =>
          zipInstance()
            .file(ENCRYPTED, convert(b))
            .file(RANDOM, convert(random.get))
      )
      .getOrElse(zipInstance().file(DATA, convert(unencrypted.get)))

}
