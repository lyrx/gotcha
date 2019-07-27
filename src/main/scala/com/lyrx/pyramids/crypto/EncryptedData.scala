package com.lyrx.pyramids.crypto

import scala.scalajs.js.typedarray.ArrayBuffer

object EncryptedData{
  def apply():EncryptedData=EncryptedData(
    None,None,None,None,None,None,None
  )
}


case class EncryptedData(unencrypted: Option[ArrayBuffer],
                         encrypted:Option[ArrayBuffer],
                         random:Option[ArrayBuffer],
                         signature:Option[ArrayBuffer],
                         metaData:Option[ArrayBuffer],
                         metaRandom:Option[ArrayBuffer],
                         signer:Option[ArrayBuffer]
                        )
  extends Encrypted

