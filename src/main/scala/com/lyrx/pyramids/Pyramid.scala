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

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.scalajs.js
import js.Dynamic.{literal => l}
import typings.fileDashSaverLib.fileDashSaverMod.{^ => filesaver}

import scala.scalajs.js.JSON
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.timers._
object Pyramid {

  def apply(config: Config)(implicit executionContext: ExecutionContext) =
    new Pyramid(config)
}

class Pyramid(val config: Config)
    extends PyramidJSON
    with Stellar
    with ZipSupport {


  def withTID(id:Option[String])=new Pyramid(config.withTID(id))

  def clearIdentity() = new Pyramid(config.clearIdentity())

  def hasIdentity()=config.ipfsData.regOpt.isDefined

  private def ipfsSupport() = config.ipfsSupport()

  def crypto() = config.cryptoSupport



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

  def delay[T](milliseconds: Int,f:()=>Future[Option[T]])
              (implicit executionContext: ExecutionContext):
  Future[Option[T]] = {
    val p = Promise[Unit]()
    js.timers.setTimeout(milliseconds) {
      p.success(())
    }
    p.future
      .flatMap(r=>f())
  }

  private def ipfsSaveString(s: String)(
      implicit executionContext: ExecutionContext) =  ipfsSupport()
      .saveStringToIpfs(s)
      .fmap((s:String)=>s)




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

  def ipfsRegister()(implicit executionContext: ExecutionContext) = {
    val delayy = 1000
    uploadSymKey()
      .map(_.getOrElse(""))
      .flatMap(
        aHash =>
          crypto()
            .identity()
            .flatMap(r=>delay(delayy,()=>ipfsSaveString(r)))
            .map(identityString => (aHash, identityString.getOrElse(""))))
      .map(
        t =>
          l(
            "symmetricKey" -> t._1,
            "identity" -> t._2
          ).asInstanceOf[Registration])
      .map(stringify(_))
      .flatMap(r2=>delay(delayy,()=>ipfsSaveString(r2)))
      .map(so => new Pyramid(config.withRegistration(so)))
  }

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





  def saveDecryptHash(hash: String, f: File)(
      implicit executionContext: ExecutionContext) =
    downloadDecrypt(hash)
      .fmap((b:ArrayBuffer)=> {
        filesaver.saveAs(b.toBlob(f.`type`)
          ,f.name)
        new Pyramid(config.withMessage(s"Downloaded ${f.name}"))
      })



  def saveHash(hash: String)(
    implicit executionContext: ExecutionContext) =
    ipfsSupport().readIpfs(hash)
    .fmap((b:nodeLib.Buffer)=>
      filesaver
        .saveAs(
          b.toBlob(
            "application/zip")
          ,"data.zip"))

  def saveHashWithFile(hash: String, f: File)(
    implicit executionContext: ExecutionContext) =
    ipfsSupport().readIpfs(hash)
      .fmap((b:nodeLib.Buffer)=>
        filesaver
          .saveAs(
            b.toBlob(
              f.`type`)
            ,f.name))







  def encryptedUpload(f: File)(implicit executionContext: ExecutionContext) =
    zipEncrypt(f)
      .fmap((ipfsSaveBuffer(_)))
      .toFutureOption()


  def unencryptedUpload(f: File)(implicit executionContext: ExecutionContext) = new FileReader()
      .futureReadArrayBuffer(f)
      .map(  (b:ArrayBuffer)=>ipfsSaveBuffer(b.toNodeLib8uffer())).flatten



  def uploadWallet(f: File)(implicit executionContext: ExecutionContext) =
    if (f.`type` == "application/json") {
      val pharaohKeyOpt = config.cryptoSupport.config.pharaohKeyOpt

      def fillPharaoh(p:Pyramid) = if(pharaohKeyOpt.isDefined)
        p.withPharaoh(pharaohKeyOpt.get)
      else
      p

      new FileReader()
        .futureReadArrayBuffer(f)
        .map(
          arrayBuffer =>
            js.JSON
              .parse(arrayBuffer.toNormalString())
              .asInstanceOf[WalletNative])
        .flatMap(
          _.importAllKeys().map(config.cryptoSupport.withCryptoConfig(_)))
        .map(c => fillPharaoh(Pyramid(config.copy(cryptoSupport = c) )    ))
    } else
      Future {
        Pyramid(config.withMessage("No keys imported (wrong file type)!"))
      }

  def privateStellarAccountId(privateKey: String, isTestNet: Boolean)(
      implicit executionContext: ExecutionContext) =
    loadPrivateAccount(privateKey, isTestNet).map(_._2.accountId())

  def balanceStellar(privateKey: String)(
      implicit executionContext: ExecutionContext) =
    balanceForPrivate(privateKey, config.blockchainData.stellar.testNet)




  def balanceForAccount(s: String)(
      implicit executionContext: ExecutionContext) =
    balanceForPublic(s, config.blockchainData.stellar.testNet)

  def stellarFromSecret(privateKey: String)(
    implicit executionContext: ExecutionContext) =if ("".equals(privateKey))
    ""
    else
    fromSecret(privateKey, config.blockchainData.stellar.testNet)



  def stellarAccountInfo(s: String)(
    implicit executionContext: ExecutionContext):Future[AccountData] =
    accountData(s, config.blockchainData.stellar.testNet)








  def stellarRegisterByTransaction(aHash: String,
                                   privKey: String,
                                   pubKey: String)(
      implicit executionContext: ExecutionContext,
      timeout: Timeout) =
    config.blockchainData.stellar.notarizeFeeXLMOpt
      .map(
        fee =>{
          register(value = aHash,
            privateKey = privKey,
            aSendTo = pubKey,
            amount = fee,
            isTestNet = config.blockchainData.stellar.testNet)
        })


}
