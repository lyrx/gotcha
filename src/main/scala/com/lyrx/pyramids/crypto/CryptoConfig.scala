package com.lyrx.pyramids.crypto

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.Dynamic.{literal => l}

case class CryptoConfig(
                         symKeyOpt:Option[PyramidCryptoKey],
                          asymKeyOpt:Option[PyramidCryptoKeyPair],
                          signKeyOpt:Option[PyramidCryptoKeyPair],
                         pharaohKeyOpt:Option[PyramidCryptoKey],
                        ) extends Crypto {
  def importSymKey(walletNative: WalletNative)
                  (implicit ctx:ExecutionContext)=
    walletNative.importSymKey().map(o=>this.copy(symKeyOpt=o))

  def importAsymKey(walletNative: WalletNative)(implicit  executionContext: ExecutionContext) =
    walletNative.importAsymKey().map(o=>this.copy(asymKeyOpt=o))

  def importSignKey(walletNative: WalletNative)(implicit  executionContext: ExecutionContext) =
    walletNative.importSignKey().map(o=>this.copy(signKeyOpt=o))


  def importAllKeys(walletNative: WalletNative)(implicit  executionContext: ExecutionContext) =
    importSymKey(walletNative).
      flatMap(_.importAsymKey(walletNative)).
  flatMap(_.importSignKey(walletNative))


  def generateSignKeyPair()(implicit ctx: ExecutionContext) =
    generateSignKeys().map(k=>this.copy(signKeyOpt=Some(k)))

  def generateAsymKeyPair()(implicit ctx: ExecutionContext) =
    generateASymetricEncryptionKeys().map(k=>this.copy(asymKeyOpt=Some(k)))


  def generateSymKey()(implicit ctx: ExecutionContext) =generateSymmetricKey().
    map(k=>this.copy(symKeyOpt=Some(k)))

  def generateAllKeys()(implicit ctx: ExecutionContext) = generateSignKeyPair().
    flatMap(_.generateAsymKeyPair()).
    flatMap(_.generateSymKey())




  def exportSymKey()(implicit ctx:ExecutionContext)=
    symKeyOpt.
    map(exportCryptoKey(_).
      map(jw=>Some(jw))).
    getOrElse(Future{None})

  def exportASymKeys()(implicit ctx:ExecutionContext)=exportKeys(asymKeyOpt)

  def exportSignKeys()(implicit ctx:ExecutionContext)=exportKeys(signKeyOpt)

  def exportAllKeys()(implicit ctx:ExecutionContext)
  = exportSymKey().
    flatMap(symKeyOpt=>exportASymKeys().map(keysOpt=>(symKeyOpt,keysOpt))).
    flatMap(keysOpt=>exportSignKeys().map(keysOpt2=>(keysOpt._1,keysOpt._2,keysOpt2))).
    map( (ko:AllJSKeysOpt)=>l(
      "sym"->ko._1.getOrElse(null),
      "asym" -> ko._2.map((kp:JsonKeyPair)=>l("private" -> kp._1,"public" -> kp._2)).getOrElse(null),
      "sign" -> ko._3.map((kp:JsonKeyPair)=>l("private" -> kp._1,"public" -> kp._2)).getOrElse(null),

    ).asInstanceOf[WalletNative])



}
