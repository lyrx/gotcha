package com.lyrx.pyramids.crypto


import com.lyrx.pyramids.util.PyramidJSON
import org.scalajs.dom.raw.File
import org.scalajs.dom.{crypto => scrypto}
import scrypto.{AlgorithmIdentifier, CryptoKey, CryptoKeyPair, HashAlgorithm, JsonWebKey, KeyAlgorithmIdentifier, KeyFormat, KeyUsage, RsaHashedKeyAlgorithm}
import typings.nodeLib.bufferMod.Buffer

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.typedarray
import scala.scalajs.js.Dynamic.{literal => l}
import scala.scalajs.js.typedarray.{ArrayBuffer, ArrayBufferView}



trait Crypto extends PyramidJSON {
  val aesGCM = "AES-GCM"


  val rsaOADP: KeyAlgorithmIdentifier = js.Dynamic
    .literal("name" -> "RSA-OAEP")
    .asInstanceOf[KeyAlgorithmIdentifier]

  val aHashAlgorithm:KeyAlgorithmIdentifier =  RsaHashedKeyAlgorithm.`RSA-OAEP`(modulusLength = 4096,
    publicExponent = new typedarray.Uint8Array( js.Array(1,0,1)),
    hash = HashAlgorithm.`SHA-256`)




  val ecsda = l(
    "name" -> "ECDSA",
    "hash" -> l("name" -> "SHA-384"),
  ).asInstanceOf[KeyAlgorithmIdentifier]


  def metaDataFrom(f: File) = Buffer.
    from(stringify(
      l("name"  -> f.name,
        "type" -> f.`type`,
        "size"  -> f.size
      ))).
    buffer.asInstanceOf[ArrayBuffer]



  def aesGCMIdentifierIV(iv:ArrayBufferView):AlgorithmIdentifier={
    l(
      "name" -> aesGCM,
      "iv" -> iv ,

    ).asInstanceOf[AlgorithmIdentifier]
  }
  val aesGCMIdentifier:KeyAlgorithmIdentifier= l(
    "name" -> aesGCM,
    "length" -> 256).asInstanceOf[KeyAlgorithmIdentifier]



  val usageDecrypt=js.Array(KeyUsage.decrypt)
  val usageEncrypt=js.Array(KeyUsage.encrypt)
  val usageSign=js.Array(KeyUsage.sign)
  val usageVerify=js.Array(KeyUsage.verify)


  val aSignAlgorithm:KeyAlgorithmIdentifier  = l(
    "name" ->"ECDSA",
    "namedCurve" -> "P-384"
  ).asInstanceOf[KeyAlgorithmIdentifier]



  def generateSymmetricKey()(implicit ctx:ExecutionContext)= scrypto.crypto.
    subtle.
    generateKey(
      aesGCMIdentifier,
      extractable = true,
      js.Array(
        KeyUsage.encrypt,
        KeyUsage.decrypt)).
    toFuture.
    map(_.asInstanceOf[CryptoKey])



  def generateASymetricEncryptionKeys()(implicit ctx:ExecutionContext):Future[CryptoKeyPair]
  = generateKeysFor( js.Array(KeyUsage.encrypt, KeyUsage.decrypt),aHashAlgorithm)


  def generateSignKeys()(implicit ctx:ExecutionContext):Future[CryptoKeyPair]
  = generateKeysFor(js.Array(KeyUsage.sign, KeyUsage.verify), aSignAlgorithm)



   def generateKeysFor(aUsage:js.Array[KeyUsage],alg:KeyAlgorithmIdentifier)
                             (implicit ctx:ExecutionContext):Future[CryptoKeyPair]
  = scrypto.crypto.subtle.generateKey(
    algorithm = alg,
    extractable = true,
    keyUsages = aUsage).
    toFuture.map(_.asInstanceOf[CryptoKeyPair])






  def toKeyPair(privateKey: Option[CryptoKey], publicKey: Option[CryptoKey]) = js.Dictionary(
    "publicKey" -> publicKey.getOrElse(null),
    "privateKey" -> privateKey.getOrElse(null)
  ).asInstanceOf[CryptoKeyPair]






  def exportCryptoKey(key: CryptoKey)(implicit ctx: ExecutionContext) =
    scrypto.crypto.subtle
      .exportKey(KeyFormat.jwk, key)
      .toFuture
      .map((a: Any) => a.asInstanceOf[JsonWebKey])

  def exportKeys(keyPairOpt: Option[CryptoKeyPair])(
      implicit ctx: ExecutionContext) =
    keyPairOpt
      .map(
        keys =>
          exportCryptoKey(keys.publicKey).flatMap(
            pubKeyJsonWeb =>
              exportCryptoKey(
                keys.privateKey
              ).flatMap(privKeyJsonWeb =>
                Future { Some((privKeyJsonWeb, pubKeyJsonWeb)) })))
      .getOrElse(Future { None })

  def exportPublicKey(keyPairOpt: Option[CryptoKeyPair])(
      implicit ctx: ExecutionContext) =
    keyPairOpt
      .map(keys =>
        exportCryptoKey(keys.publicKey).map((k: JsonWebKey) => Some(k)))
      .getOrElse(Future { None })
  //.getOrElse(Future{None})

}
