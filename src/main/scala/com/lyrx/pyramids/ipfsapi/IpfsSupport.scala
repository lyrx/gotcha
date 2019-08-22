package com.lyrx.pyramids.ipfsapi

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

   def aws() : Ipfs= new IpfsSupport(genClient(l(
    "host" -> "blockchain.lyrx.de",
    "port" -> 5001,
    "protocol" -> "https"
  )))

  def temporal(token:String,host:String):Ipfs = new IpfsSupport(genClient(l(
    "host" -> host,
    "port" -> 443,
    "protocol" -> "https",
    "api-path" -> "/api/v0/",
    "headers" -> l(
      "authorization" -> s"Bearer ${token}"
    )
  )))

  /*
    https://github.com/orbitdb/orbit-db/blob/master/examples/browser/example.js
   */
  def orbitDB(): Ipfs = new IpfsSupport(genClient(
    ???
  ))





  def apply( ipfsClientOpt: ClientConfig): Ipfs = new IpfsSupport(ipfsClientOpt)

  def apply() = new IpfsSupport(()=>None)
}


class IpfsSupport(override val ipfsClientOpt: ClientConfig) extends Ipfs{

}
