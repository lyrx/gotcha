package com.lyrx.pyramids.crypto


import com.lyrx.pyramids.util.{MetaData, TextDecoder}
import org.scalajs.dom.crypto.CryptoKey

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array}


trait Encrypted {
  val unencrypted: Option[ArrayBuffer]
  val encrypted: Option[ArrayBuffer]
  val random: Option[ArrayBuffer]
  val signature: Option[ArrayBuffer]
  val metaData:Option[ArrayBuffer]
  val metaRandom:Option[ArrayBuffer]
  val signer:Option[ArrayBuffer]


  def decryptData(symKey:CryptoKey)(implicit executionContext: ExecutionContext) =
    encrypted.
      map(data=>symKey.symDecryptArrayBuffer(
        data,
        random.get
      ).map(b=>DecryptedData(Some(b),None))).
      getOrElse(
        Future{DecryptedData(None,None)
        })


  def decrypt(symKey:CryptoKey)(implicit executionContext: ExecutionContext) =
    decryptData(symKey).flatMap(d=>
      metaData.
        map(data=>symKey.symDecryptArrayBuffer(
          data,
          metaRandom.get
        ).map(b=>DecryptedData(
          d.unencrypted,
          Some(js.JSON.parse(
            new TextDecoder().
              decode(
                new Uint8Array(b))).asInstanceOf[MetaData])
        ))).
        getOrElse(
          Future{DecryptedData(d.unencrypted,None)
          }))





  def isEmpty() =(
    unencrypted.isEmpty &&
      encrypted.isEmpty &&
      random.isEmpty &&
      signature.isEmpty &&
      metaData.isEmpty &&
      metaRandom.isEmpty &&
      signer.isEmpty
    )

  def descr()=if(isEmpty()) "no encrypted data" else "encrypted data"
}
