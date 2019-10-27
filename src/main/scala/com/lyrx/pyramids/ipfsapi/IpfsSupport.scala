package com.lyrx.pyramids.ipfsapi

import com.lyrx.pyramids.IpfsTrait

import scala.scalajs.js
import js.Dynamic.{literal => l}

object IpfsSupport{




  def genClient(literal:js.Dynamic) = ()=> Some(IpfsHttpClient(
    literal))



  def infura() : IpfsTrait= new IpfsSupport(genClient(l(
    "host" -> "ipfs.infura.io",
    "port" -> 5001,
    "protocol" -> "https"
  )))


  def macmini() : IpfsTrait= new IpfsSupport(genClient(l(
    "host" -> "192.168.1.30",
    "port" -> 5001,
    "protocol" -> "http"
  )))

   def aws() : IpfsTrait= new IpfsSupport(genClient(l(
    "host" -> "blockchain.lyrx.de",
    "port" -> 5001,
    "protocol" -> "https"
  )))

  def hosttech() : IpfsTrait= new IpfsSupport(genClient(l(
    "host" -> "ipfsapi.lyrx.de",
    "port" -> 2053,
    "protocol" -> "https"
  )))


  def perfectPrivacy() : IpfsTrait= new IpfsSupport(genClient(l(
    "host" -> "80.255.7.88",
    "port" ->  57788,  //5001,
    "protocol" -> "http"
  )))


  def temporal(token:String,host:String):IpfsTrait = new IpfsSupport(genClient(l(
    "host" -> host,
    "port" -> 443,
    "protocol" -> "https",
    "api-path" -> "/api/v0/",
    "headers" -> l(
      "authorization" -> s"Bearer ${token}"
    )
  )))






  def apply( ipfsClientOpt: ClientConfig): IpfsTrait = new IpfsSupport(ipfsClientOpt)

  def apply() = new IpfsSupport(()=>None)
}


class IpfsSupport(override val ipfsClientOpt: ClientConfig) extends IpfsAPITrait{

}
