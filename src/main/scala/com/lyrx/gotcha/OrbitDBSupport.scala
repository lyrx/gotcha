package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.ipfs.{Ipfs, OrbitDB, PeerSupport, PimpedIpfs}

import scala.concurrent.ExecutionContext

trait OrbitDBSupport extends PageOptionTrait {

  def orbitDB(p:Pyramid)(implicit executionContext: ExecutionContext) = {
    val ipfs:Ipfs = PeerSupport
      .orbitDB()

    ipfs.onReady().map(aipfs=>{

      p.withIpfs(aipfs)
          .withOrbit(
      OrbitDB.createInstance(aipfs))




    })
    ipfs.onException().map(e=>println(e))
  }

}
