package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.ipfs.{Ipfs, PeerSupport, PimpedIpfs}

import scala.concurrent.ExecutionContext

trait OrbitDBSupport  {

  def orbitDB(p:Pyramid)(implicit executionContext: ExecutionContext) = {
    val ipfs:Ipfs = PeerSupport
      .orbitDB()

    ipfs.onReady().map(aipfs=>{})
    ipfs.onException().map(e=>println(e))
  }

}
