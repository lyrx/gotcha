package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.ipfs.PeerSupport

import scala.concurrent.ExecutionContext
import scala.scalajs.js

trait OrbitDBSupport  {

  def orbitDB(p:Pyramid)(implicit executionContext: ExecutionContext) = {
    PeerSupport
      .orbitDB()
      .onException()
    // ipfs.onException().map(e=>println(e))
    // ipfs.onReady().map(aipfs=>println(s"Yochai: ${aipfs}"))
  }

}
