package com.lyrx.pyramids


case class P2PData(stellar: StellarData){
  def withTID(id:Option[String])=this.copy(stellar=this.stellar.withTID(id))
}