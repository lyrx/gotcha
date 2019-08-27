package com.lyrx.pyramids


case class BlockchainData(stellar: StellarData){
  def withTID(id:Option[String])=this.copy(stellar=this.stellar.withTID(id))
}