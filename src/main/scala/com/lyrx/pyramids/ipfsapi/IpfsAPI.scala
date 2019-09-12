package com.lyrx.pyramids.ipfsapi

import com.lyrx.pyramids.IpfsTrait
import com.lyrx.pyramids.util.Implicits._
import typings.nodeLib
import typings.nodeLib.{Buffer, bufferMod}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.typedarray.ArrayBuffer

trait IpfsAPI extends IpfsTrait{

  val ipfsClientOpt: ClientConfig

  override def saveBufferToIpfs(b:nodeLib.Buffer)(
      implicit ctx: ExecutionContext): FOption[String] =
       ipfsClientOpt()
          .map(_.futureAdd(b).map(Some(_)))
          .getOrElse(Future { None }).fmap(_.head.hash)



  override def bufferToIpfs(buffer: nodeLib.Buffer)(implicit ctx: ExecutionContext): Future[Option[String]] =
    ipfsClientOpt()
      .map(_.futureAdd(buffer).map(l => Some(l.head.hash)))
      .getOrElse(Future { None })

  override def readIpfs(aHash: String)(implicit executionContext: ExecutionContext): Future[Option[Buffer]] =
    ipfsClientOpt()
      .map(_.futureCat(aHash).map(Some(_)))
      .getOrElse(Future { None })

  override def readIpfsString(aHash: String)(
      implicit executionContext: ExecutionContext): Future[Option[String]] =
    ipfsClientOpt()
      .map(_.futureCatString(aHash).map(Some(_)))
      .getOrElse(Future { None })

}
