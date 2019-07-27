package com.lyrx.pyramids.util

import com.lyrx.pyramids.crypto.EncryptedData
import com.lyrx.pyramids.jszip.ZippableEncrypt

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

object Implicits {

  type FOption[T] = Future[Option[T]]

  implicit class PimpedFFOptionF[T](fo: Future[Option[Future[Option[T]]]]) {
    def toFutureOption()(implicit executionContext: ExecutionContext) =
      fo.map(_.map(_.map(t => Some(t))))
        .map(_.getOrElse(Future { None }))
        .flatten
        .map(_.flatten)
  }

  implicit class PimpedFOptionF[T](fo: Future[Option[Future[T]]]) {
    def toFutureOption()(implicit executionContext: ExecutionContext) =
      fo.map(_.map(_.map(t => Some(t))))
        .map(_.getOrElse(Future { None }))
        .flatten
  }

  implicit class PimpedOption[T](o: Option[Future[T]]) {
    def flip()(implicit executionContext: ExecutionContext) =
      o.map(_.map(Some(_))).getOrElse(Future { None })
  }

  implicit def convertEncryptedData(d: EncryptedData): ZippableEncrypt =
    ZippableEncrypt(
      unencrypted = d.unencrypted,
      encrypted = d.encrypted,
      random = d.random,
      signature = d.signature,
      metaData = d.metaData,
      metaRandom = d.metaRandom,
      signer = d.signer
    )

  implicit def convertZippedData(d: ZippableEncrypt): EncryptedData =
    EncryptedData(
      unencrypted = d.unencrypted,
      encrypted = d.encrypted,
      random = d.random,
      signature = d.signature,
      metaData = d.metaData,
      metaRandom = d.metaRandom,
      signer = d.signer
    )

  implicit class FutureOption[T](val m: FOption[T]) {

    def fmap[U](tf: T => U)(
        implicit executionContext: ExecutionContext): FOption[U] =
      m.map(_.map(tf(_)))

    def fflatMap[U](tf: T => FOption[U])(
        implicit executionContext: ExecutionContext): FOption[U] =
      this.m
        .flatMap(_.map(tf(_)).getOrElse(Future { None }))

  }

}
