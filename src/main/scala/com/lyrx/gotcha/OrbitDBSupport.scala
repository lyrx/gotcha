package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import com.lyrx.pyramids.ipfs.{Ipfs, OrbitDB, PeerSupport, PimpedIpfs}

import scala.concurrent.ExecutionContext
import scala.scalajs.js.JSON

trait OrbitDBSupport extends PageOptionTrait {

  def orbitDB(p:Pyramid)(implicit executionContext: ExecutionContext) = {
    val ipfs:Ipfs = PeerSupport
      .orbitDB()
    ipfs.onException().map(e=>println(e))
    ipfs.onReady().map(aipfs=>{
      val db = OrbitDB.createInstance(aipfs)
      p.withIpfs(aipfs)
          .withOrbit(db)

    })

  }

}
