package com.lyrx.gotcha

import com.lyrx.pyramids.ipfs.PeerSupport

import scala.concurrent.ExecutionContext

trait OrbitDBSupport {

  def orbitDB()(implicit executionContext: ExecutionContext) = {
    PeerSupport
      .orbitDB()
      .onException()
    // ipfs.onException().map(e=>println(e))
    // ipfs.onReady().map(aipfs=>println(s"Yochai: ${aipfs}"))
  }

}
