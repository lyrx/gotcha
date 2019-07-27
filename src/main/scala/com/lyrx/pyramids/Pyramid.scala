package com.lyrx.pyramids

import com.lyrx.pyramids
import com.lyrx.pyramids.Ipfs.IpfsSupport
import com.lyrx.pyramids.crypto.{CryptoSupport, PyramidCryptoKey, WalletNative}
import com.lyrx.pyramids.jszip.ZipSupport
import com.lyrx.pyramids.stellarsdk.{Stellar, Timeout}
import com.lyrx.pyramids.util.Implicits._
import com.lyrx.pyramids.util.PyramidJSON
import org.scalajs.dom.raw
import org.scalajs.dom.raw.{File, FileReader}
import typings.{nodeLib, stdLib}
import typings.stdLib.JsonWebKey

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import js.Dynamic.{literal => l}
import typings.fileDashSaverLib.fileDashSaverMod.{^ => filesaver}

import scala.scalajs.js.JSON
import scala.scalajs.js.typedarray.ArrayBuffer

object Pyramid {

  def apply(config: Config)(implicit executionContext: ExecutionContext) =
    new Pyramid(config)
}

class Pyramid(val config: Config)
    extends PyramidJSON
    with Stellar
    with ZipSupport {

  private def ipfsSupport() = config.ipfsSupport()

  def crypto() = config.cryptoSupport

  def hasIdentity() = config.ipfsData.identityOpt.isDefined

  def onIdentity(h: (Registration) => Unit) =
    config.ipfsData.identityOpt
      .map(h(_))

  def msg(s: String) = new Pyramid(config.withMessage(s))

  private def zipEncrypt(f: File)(implicit executionContext: ExecutionContext) =
    crypto()
      .symEncryptFile(f)
      .fmap(_.zipped())
      .toFutureOption()

  private def ipfsSaveBuffer(b: nodeLib.Buffer)(
      implicit executionContext: ExecutionContext) =
    ipfsSupport()
      .saveBufferToIpfs(b)

  private def ipfsSaveString(s: String)(
      implicit executionContext: ExecutionContext) =
    ipfsSupport()
      .saveStringToIpfs(s)

  private def uploadSymKey()(implicit executionContext: ExecutionContext) =
    crypto()
      .encryptSymKey()
      .fflatMap(b => ipfsSupport().saveArrayBufferToIpfs(b))

  private def withPharaoh(k: PyramidCryptoKey)(
      implicit executionContext: ExecutionContext) =
    new Pyramid(
      config
        .copy(cryptoSupport = this.crypto().withPharao(k)))

  def saveKeys()(implicit executionContext: ExecutionContext) =
    config.cryptoSupport
      .exportFuture()
      .map(walletNative => {
        filesaver.saveAs(new raw.Blob(js.Array(stringify(walletNative)),
                                      raw.BlobPropertyBag("application/json"))
                           .asInstanceOf[stdLib.Blob],
                         "pyramid-keys.json")
        config.withMessage("Your keys have been saved!")
      })
      .map(Pyramid(_))

  def loadIdentity()(implicit executionContext: ExecutionContext) =
    config.ipfsData.regOpt
      .map(hash => ipfsSupport().readIpfsString(hash))
      .getOrElse(Future { None })
      .fmap(
        s =>
          JSON
            .parse(s)
            .asInstanceOf[Registration])
      .fmap(io => new Pyramid(config.withIdentity(Some(io))))

  def loadPharaohKey()(implicit executionContext: ExecutionContext) =
    config.ipfsData.pharaoOpt
      .map(
        h =>
          ipfsSupport()
            .readIpfsString(h)
            .fmap(crypto().importPublicKey(_)))
      .map(_.toFutureOption())
      .getOrElse(Future { None })
      .fmap(withPharaoh(_))
      .map(_.getOrElse(this))

  def ipfsRegister()(implicit executionContext: ExecutionContext) =
    uploadSymKey()
      .map(_.getOrElse(""))
      .flatMap(
        aHash =>
          crypto()
            .identity()
            .flatMap(ipfsSaveString(_))
            .map(identityString => (aHash, identityString.getOrElse(""))))
      .map(
        t =>
          l(
            "symmetricKey" -> t._1,
            "identity" -> t._2
          ).asInstanceOf[Registration])
      .map(stringify(_))
      .flatMap(ipfsSaveString(_))
      .map(so => new Pyramid(config.withRegistration(so)))

  def downloadDecrypt(hash: String)(
      implicit executionContext: ExecutionContext) =
    ipfsSupport()
      .readIpfs(hash)
      .fmap(b => {
        zipFromArrayBuffer(b)
          .map(ze => {
            ze.encrypted.flatMap(
              encrypted =>
                ze.random
                  .map(random =>
                    crypto()
                      .symDecryptArrayBuffer(encrypted, random))
            )
          })
          .toFutureOption()
      })
      .toFutureOption()

  def saveHash(hash: String, f: File)(
      implicit executionContext: ExecutionContext) =
    downloadDecrypt(hash)
      .fmap(b => {
        filesaver.saveAs(
          new raw.Blob(js.Array(b, raw.BlobPropertyBag(f.`type`)))
            .asInstanceOf[stdLib.Blob],
          f.name)
        new Pyramid(config.withMessage(s"Downloaded ${f.name}"))
      })

  def encryptedUpload(f: File)(implicit executionContext: ExecutionContext) =
    zipEncrypt(f)
      .fmap((ipfsSaveBuffer(_)))
      .toFutureOption()
      .fmap(s => new Pyramid(config.withUpload(f, s)))
      .map(_.getOrElse(new Pyramid(config.withMessage(
        s"Oh Humble Tokenizer, I could not upload ${f.name}"))))

  def uploadWallet(f: File)(implicit executionContext: ExecutionContext) =
    if (f.`type` == "application/json")
      new FileReader()
        .futureReadArrayBuffer(f)
        .map(
          arrayBuffer =>
            js.JSON
              .parse(arrayBuffer.toNormalString())
              .asInstanceOf[WalletNative])
        .flatMap(
          _.importAllKeys().map(config.cryptoSupport.withCryptoConfig(_)))
        .map(c => Pyramid(config.copy(cryptoSupport = c)))
    else
      Future {
        Pyramid(config.withMessage("No keys imported (wrong file type)!"))
      }

  def privateStellarAccountId(privateKey: String)(
  implicit executionContext: ExecutionContext,
  isTest: Boolean) = loadPrivateAccount(privateKey).map(_._2.accountId())



  def balanceStellar(privateKey: String)(
      implicit executionContext: ExecutionContext,
      isTest: Boolean) =
    balanceForPrivate(privateKey)


  def balancePharaoh()(
    implicit executionContext: ExecutionContext,
    isTest: Boolean) =
    config
      .blockchainData
      .stellar
      .pharaohPubOpt
        .map(s=>
        balanceForPublic(s)
        ).getOrElse(Future{None})






  private def internalRegister(hash: String, aPrivateKey: String)(
      implicit executionContext: ExecutionContext,
      timeout: Timeout,
      isTest: Boolean) =
    config.blockchainData.stellar.registrationFeeXLMOpt
      .map(
        regFee =>
          config.blockchainData.stellar.pharaohPubOpt
            .map(
              pharaohPub =>
                register(value = hash,
                         privateKey = aPrivateKey,
                         aSendTo = pharaohPub,
                         amount = regFee)))
      .flatten
      .get

  def registerStellar(privateKey: String)(
      implicit executionContext: ExecutionContext,
      timeout: Timeout,
      isTest: Boolean) =
    config.ipfsData.regOpt
      .map(registrationHash => internalRegister(registrationHash, privateKey))
      .flip()
      .fmap(s => new Pyramid(config.withMessage(s)))

  def notarizeStellar(privateKey: String)(
      implicit executionContext: ExecutionContext,
      timeout: Timeout,
      isTest: Boolean) =
    config.ipfsData.uploads.foldLeft(Future { Some(this) }: Future[Option[Pyramid]])(
      (pf: Future[Option[Pyramid]], upload: Upload) =>
        registerUpload(pf, upload, privateKey))
    .fmap(p=>new Pyramid(p.config.withMessage("All uploads are now notarized!")))

  def registerUpload(
      pf: Future[Option[Pyramid]],
      upload: Upload,
      privKey: String
  )(implicit executionContext: ExecutionContext,
    timeout: Timeout,
    isTest: Boolean) =
    pf.fmap(pyr=> config.blockchainData.stellar.pharaohPubOpt
      .flatMap(
        pharaohPub =>
          config.blockchainData.stellar.notarizeFeeXLMOpt
            .map(fee =>  register(value = upload.hash,
              privateKey = privKey,
              aSendTo = pharaohPub,
              amount = fee).map(Some(_).map(s=>pyr)))))
    .map(_.flatten).toFutureOption()

      /*
    .fmap(_.flip()).toFutureOption()
    .fmap(s=>pf).toFutureOption()
  */


    //.fmap(_.getOrElse(Future{""}))



}
