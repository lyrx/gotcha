package com.lyrx.pyramids

import com.lyrx.pyramids.util.Implicits.FOption
import typings.nodeLib
import typings.nodeLib.{Buffer, bufferMod}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.typedarray.ArrayBuffer

trait IpfsTrait {

  def saveBufferToIpfs(b: nodeLib.Buffer)(
      implicit ctx: ExecutionContext): FOption[String]

  def bufferToIpfs(buffer: nodeLib.Buffer)(
      implicit ctx: ExecutionContext): Future[Option[String]]

  def readIpfs(aHash: String)(
      implicit executionContext: ExecutionContext): Future[Option[Buffer]]

  def readIpfsString(aHash: String)(
      implicit executionContext: ExecutionContext): Future[Option[String]]






  def saveArrayBufferToIpfs(b: ArrayBuffer)(
      implicit ctx: ExecutionContext): FOption[String] =
    saveBufferToIpfs(bufferMod.Buffer.from(b))

  def saveStringToIpfs(s: String)(
      implicit ctx: ExecutionContext): FOption[String] =
    saveBufferToIpfs(
      bufferMod.Buffer.from(s)
    )
}
