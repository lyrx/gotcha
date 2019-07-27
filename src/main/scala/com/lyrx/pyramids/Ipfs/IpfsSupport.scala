package com.lyrx.pyramids.Ipfs

import scala.scalajs.js
import js.Dynamic.{literal => l}

object IpfsSupport{




  def genClient(literal:js.Dynamic) = ()=> Some(IpfsHttpClient(
    literal))



  def infura() : Ipfs= new IpfsSupport(genClient(l(
    "host" -> "ipfs.infura.io",
    "port" -> 5001,
    "protocol" -> "https"
  )))


  def macmini() : Ipfs= new IpfsSupport(genClient(l(
    "host" -> "192.168.1.30",
    "port" -> 5001,
    "protocol" -> "http"
  )))

  def temporal(token:String):Ipfs = new IpfsSupport(genClient(l(
    "host" -> "dev.api.ipfs.temporal.cloud",
    "port" -> 443,
    "protocol" -> "https",
    "api-path" -> "/api/v0/",
    "headers" -> l(
      "authorization" -> s"Bearer ${token}"
    )
  )))






  def apply( ipfsClientOpt: ClientConfig): Ipfs = new IpfsSupport(ipfsClientOpt)

  def apply() = new IpfsSupport(()=>None)
}


class IpfsSupport(override val ipfsClientOpt: ClientConfig) extends Ipfs{

}
