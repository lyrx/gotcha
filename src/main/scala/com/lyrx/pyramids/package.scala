package com.lyrx

import com.lyrx.pyramids.Ipfs.Ipfs
import com.lyrx.pyramids.stellarsdk.AccountDetail
import org.scalajs.dom.raw.{Blob, FileReader, ProgressEvent}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.implicitConversions
import scala.scalajs.js


package object pyramids {
  type  IPFS = () => Ipfs


  implicit def convertStellarAccount(input:AccountDetail):AccountData=AccountData(
    balanceOpt = input.balances.headOption.map(_.balance),
    idOpt = input.id.toOption)


  implicit def convertStellarAccount(finput:Future[AccountDetail])(
    implicit executionContext: ExecutionContext):Future[AccountData]=
    finput.map(convertStellarAccount(_))




  implicit class PimpedArrayBuffer(b:js.typedarray.ArrayBuffer){

    def toHexString() = new js.typedarray.Uint8Array(b).
      map(c=>c.toHexString).foldLeft("")((a:String,b:String)=>a + b)

    def toNormalString()=new js.typedarray.Uint8Array(b).
      map((c:Short)=>c.toChar).foldLeft(s"":String)((a:String,b:Char)=>a + b)
  }




  implicit class PimpedFileReader(f:FileReader){

    def futureReadArrayBuffer(b:Blob) = {
      val promise = Promise[js.typedarray.ArrayBuffer]
      f.readAsArrayBuffer(b)
      f.onloadend  = (e:ProgressEvent) => promise
        .success(f.result.asInstanceOf[js.typedarray.ArrayBuffer])
      f.onerror = (e) => promise.failure(new Throwable(e.toString))
      f.onabort = (e) => promise.failure(new Throwable(e.toString))
      promise.future
    }
  }


  def registration(d: js.Dynamic) = d.asInstanceOf[Registration]

  implicit class PimpedRegistration(r: Registration) {}

}
