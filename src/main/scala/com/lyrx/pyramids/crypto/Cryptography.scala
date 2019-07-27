package com.lyrx.pyramids.crypto

import com.lyrx.pyramids.util.Implicits._
import com.lyrx.pyramids.util.PyramidJSON
import org.scalajs.dom.crypto.JsonWebKey
import org.scalajs.dom.raw.File
import typings.nodeLib

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import js.typedarray.ArrayBuffer
import js.{JSON, typedarray}
import js.Dynamic.{literal => l}

trait Cryptography extends WalletHandling with PyramidJSON with Crypto {

  val config: CryptoConfig

  def withPharao(ph: PyramidCryptoKey) =
    new CryptoSupport(config.copy(pharaohKeyOpt = Some(ph)))

  def withCryptoConfig(c: CryptoConfig) = new CryptoSupport(c)

  implicit class PimpedKeyOpt(o: Option[PyramidCryptoKey]) {

    def exportKeyJ()(implicit executionContext: ExecutionContext) =
      o.map(config.exportCryptoKey(_))
        .flip()

    def exportKey()(implicit executionContext: ExecutionContext) =
      exportKeyJ()
        .fmap(stringify(_))
  }

  def importPublicKey(s: String)(implicit executionContext: ExecutionContext) =
    JSON
      .parse(s)
      .asym
      .public
      .asInstanceOf[JsonWebKey]
      .importKey(usageEncrypt, aHashAlgorithm)

  def encryptSymKey()(implicit executionContext: ExecutionContext)
    : Future[Option[ArrayBuffer]] =
    config
      .exportSymKey()
      .fmap(stringify(_))
      .fflatMap(pharaohEncryptString(_))

  def pharaohKey()(
      implicit executionContext: ExecutionContext): FOption[String] =
    config.pharaohKeyOpt.exportKey()

  def pubKeyJ()(implicit executionContext: ExecutionContext) = // : Future[Option[String]] =
    config.asymKeyOpt
      .map(kp => Some(kp.publicKey).exportKeyJ())
      .getOrElse(Future { None })

  def signKeyJ()(implicit executionContext: ExecutionContext) = // : Future[Option[String]] =
    config.signKeyOpt
      .map(kp => Some(kp.publicKey).exportKeyJ())
      .getOrElse(Future { None })

  def identity()(implicit executionContext: ExecutionContext) =
    pubKeyJ()
      .flatMap(
        pubKeyOpt =>
          signKeyJ()
            .map(signKeyOpt => (pubKeyOpt, signKeyOpt)))
      .map(
        t =>
          stringify(
            l(
              "asym" -> t._1.getOrElse(null),
              "sign" -> t._2.getOrElse(null)
            )))


  //.fmap(o=>signKeyJ())

  def symKey()(implicit executionContext: ExecutionContext): FOption[String] =
    config.symKeyOpt.exportKey()

  def exportFuture()(
      implicit executionContext: ExecutionContext): Future[WalletNative] =
    config.exportAllKeys()

  def symDecryptArrayBuffer(data: ArrayBuffer, iv: ArrayBuffer)(
      implicit executionContext: ExecutionContext)
    : Future[Option[ArrayBuffer]] =
    config.symKeyOpt.map(_.symDecryptArrayBuffer(data, iv)).flip()

  def symEncryptArrayBuffer(b: ArrayBuffer)(
      implicit executionContext: ExecutionContext)
    : Future[Option[(ArrayBuffer, ArrayBuffer)]] =
    config.symKeyOpt.map(_.symEncryptArrayBuffer(b)).flip()

  def symEncryptMetaData(e: EncryptedData, f: File)(
      implicit executionContext: ExecutionContext)
    : Future[Option[EncryptedData]] =
    config.symKeyOpt.map(_.symEncryptMetaData(e, f)).flip()

  def symEncryptFileData(unencryptedData: ArrayBuffer)(
      implicit executionContext: ExecutionContext)
    : Future[Option[EncryptedData]] =
    config.symKeyOpt.map(_.symEncryptFileData(unencryptedData)).flip()

  def symEncryptFile(f: File)(implicit executionContext: ExecutionContext)
    : Future[Option[EncryptedData]] =
    config.symKeyOpt.map(_.symEncryptFile(f)).flip()

  def verify(signature: typedarray.ArrayBuffer, data: typedarray.ArrayBuffer)(
      implicit executionContext: ExecutionContext): Future[Option[Boolean]] =
    config.signKeyOpt.map(_.publicKey.verify(signature, data)).flip()

  def asymEncryptString(s: String)(implicit executionContext: ExecutionContext)
    : Future[Option[ArrayBuffer]] =
    config.asymKeyOpt.map(_.publicKey.asymEncryptString(s)).flip()

  def pharaohEncryptString(s: String)(
      implicit executionContext: ExecutionContext)
    : Future[Option[ArrayBuffer]] =
    config.pharaohKeyOpt.map(_.asymEncryptString(s)).flip()

  def asymDecryptBuffer(b: nodeLib.Buffer)(
      implicit executionContext: ExecutionContext)
    : Future[Option[ArrayBuffer]] =
    config.asymKeyOpt
      .map(
        _.privateKey.asymDecryptBuffer(
          b
        )
      )
      .flip()

  def asymDecryptArrayBuffer(b: typedarray.ArrayBuffer)(
      implicit executionContext: ExecutionContext)
    : Future[Option[ArrayBuffer]] =
    config.asymKeyOpt.map(_.privateKey.asymDecryptArrayBuffer(b)).flip()
}
