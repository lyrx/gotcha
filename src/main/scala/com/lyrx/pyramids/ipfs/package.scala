package com.lyrx.pyramids

import scala.concurrent
import scala.scalajs.js

package object ipfs {

  implicit class PimpedIpfs(val ipfs: Ipfs){

    def onException() = {
      val promise = concurrent.Promise[js.Error]
      ipfs.on("error",(e)=>promise.success(e))
      promise.future
    }

    def onReady() = {
      val promise = concurrent.Promise[Ipfs]
      ipfs.on("ready",()=>promise.success(ipfs))
      promise.future
    }


  }

}
