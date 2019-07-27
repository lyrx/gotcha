package com.lyrx.pyramids.Ipfs

import typings.nodeLib
import typings.nodeLib.bufferMod

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer
import com.lyrx.pyramids.util.Implicits._

trait Ipfs {

  val ipfsClientOpt: ClientConfig

  def saveBufferToIpfs(b:nodeLib.Buffer)(
      implicit ctx: ExecutionContext) =
       ipfsClientOpt()
          .map(_.futureAdd(b).map(Some(_)))
          .getOrElse(Future { None }).fmap(_.head.hash)

  def saveArrayBufferToIpfs(b: ArrayBuffer)(
      implicit ctx: ExecutionContext)=
    saveBufferToIpfs(bufferMod.Buffer.from(b))             //.fmap(_.headOption)

  def saveStringToIpfs(s: String)(implicit ctx: ExecutionContext) =
    saveBufferToIpfs(
       bufferMod.Buffer.from(s)
    )

  def bufferToIpfs(buffer: nodeLib.Buffer)(implicit ctx: ExecutionContext) =
    ipfsClientOpt()
      .map(_.futureAdd(buffer).map(l => Some(l.head.hash)))
      .getOrElse(Future { None })

  def readIpfs(aHash: String)(implicit executionContext: ExecutionContext) =
    ipfsClientOpt()
      .map(_.futureCat(aHash).map(Some(_)))
      .getOrElse(Future { None })

  def readIpfsString(aHash: String)(
      implicit executionContext: ExecutionContext) =
    ipfsClientOpt()
      .map(_.futureCatString(aHash).map(Some(_)))
      .getOrElse(Future { None })

}
