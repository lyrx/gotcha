package com.lyrx.pyramids.crypto

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.Dynamic.{literal => l}


case class Identity(name:String){
  def withName(n:String)=this.copy(name=n)
}


case class CryptoConfig(
                         symKeyOpt:Option[PyramidCryptoKey],
                          asymKeyOpt:Option[PyramidCryptoKeyPair],
                          signKeyOpt:Option[PyramidCryptoKeyPair],
                         pharaohKeyOpt:Option[PyramidCryptoKey],
                         identityOpt:Option[Identity]
                        ) extends Crypto {
  private def withName(n:String):Option[Identity] = identityOpt.map(
    _.copy(name=n)
  )


  def withIDName(n:String)=this.copy(identityOpt=withName(n))



  def importSymKey(walletNative: WalletNative)
                  (implicit ctx:ExecutionContext)=
    walletNative.importSymKey().map(o=>this.copy(symKeyOpt=o))

  def importAsymKey(walletNative: WalletNative)(implicit  executionContext: ExecutionContext) =
    walletNative.importAsymKey().map(o=>this.copy(asymKeyOpt=o))

  def importSignKey(walletNative: WalletNative)(implicit  executionContext: ExecutionContext) =
    walletNative.importSignKey().map(o=>this.copy(signKeyOpt=o))

  def importName(walletNative: WalletNative)(implicit  executionContext: ExecutionContext): Future[CryptoConfig] =
    Future{
      walletNative.identity.flatMap(_.name.map(s=>this.copy(identityOpt=withName(s))))
        .getOrElse(this)}






  def importAllKeys(walletNative: WalletNative)(implicit  executionContext: ExecutionContext) =
    importSymKey(walletNative).
      flatMap(_.importAsymKey(walletNative)).
  flatMap(_.importSignKey(walletNative))
    .flatMap(_.importName(walletNative))


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
      "name" -> identityOpt.map(_.name).getOrElse(null),
      "sym"->ko._1.getOrElse(null),
      "asym" -> ko._2.map((kp:JsonKeyPair)=>l("private" -> kp._1,"public" -> kp._2)).getOrElse(null),
      "sign" -> ko._3.map((kp:JsonKeyPair)=>l("private" -> kp._1,"public" -> kp._2)).getOrElse(null),
    ).asInstanceOf[WalletNative])



}
