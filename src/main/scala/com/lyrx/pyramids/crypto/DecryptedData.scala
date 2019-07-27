package com.lyrx.pyramids.crypto

import com.lyrx.pyramids.util.MetaData

import scala.scalajs.js.typedarray.ArrayBuffer


case class DecryptedData(unencrypted: Option[ArrayBuffer] ,
                         metaData:Option[MetaData]
                        ){
  def isEmpty() =(
    unencrypted.isEmpty  &&
     metaData.isEmpty
  )

  def descr()=if(isEmpty()) "no decrypted data" else "decrypted data"

}
