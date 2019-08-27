package com.lyrx.pyramids

import com.lyrx.pyramids.ipfs.{Ipfs, OrbitDB}


case class P2PData(
                    stellar: StellarData
                  , ipfsOpt:Option[Ipfs]
                    , dbOpt:Option[OrbitDB]
                  ){
  def withTID(id:Option[String])=this.copy(stellar=this.stellar.withTID(id))


  def withIpfs(ipfs:Ipfs)=this.copy(ipfsOpt=Some(ipfs))


  def withOrbit(db:OrbitDB)=this.copy(dbOpt=Some(db))





}