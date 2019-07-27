package com.lyrx.pyramids

import com.lyrx.pyramids.util.{PyramidJSON, TextEncoder}
import org.scalajs.dom.{crypto => scrypto}
import scrypto.{
  BufferSource,
  CryptoKey,
  CryptoKeyPair,
  JsonWebKey,
  KeyAlgorithmIdentifier,
  KeyFormat,
  KeyUsage
}
import org.scalajs.dom.raw.{Blob, File, FileReader, ProgressEvent}
import typings.nodeLib
import typings.nodeLib.bufferMod

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.typedarray
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array}
import scala.scalajs.js.Dynamic.{literal => l}

package object crypto {

  type PyramidCryptoKey = CryptoKey
  type PyramidCryptoKeyPair = CryptoKeyPair
  type JsonKeyPair = (JsonWebKey, JsonWebKey)
  type JSKeyPairOpt = Option[JsonKeyPair]
  type JSKeyOpt = Option[JsonWebKey]
  type AllJSKeysOpt = (JSKeyOpt, JSKeyPairOpt, JSKeyPairOpt)
  type JsonWebKeyOptPair = (Option[JsonWebKey], Option[JsonWebKey])
  type EncryptionResult = Encrypted







  implicit class PimpedFileReader(f: FileReader) {

    def futureReadArrayBuffer(b: Blob) = {
      val promise = Promise[ArrayBuffer]
      f.readAsArrayBuffer(b)
      f.onloadend = (e: ProgressEvent) =>
        promise.success(f.result.asInstanceOf[ArrayBuffer])
      f.onerror = (e) => promise.failure(new Throwable(e.toString))
      f.onabort = (e) => promise.failure(new Throwable(e.toString))
      promise.future
    }

  }

  implicit class PimpedKeyPairNative(keyPairNative: KeypairNative)
      extends Crypto {

    def importKeyPair(
        privateUsage: js.Array[KeyUsage],
        publicUsage: js.Array[KeyUsage],
        algo: KeyAlgorithmIdentifier
    )(implicit executionContext: ExecutionContext) =
      keyPairNative.`private`
        .map(_.importKey(privateUsage, algo))
        .getOrElse(Future { None })
        .flatMap(
          (privateKeyOpt: Option[CryptoKey]) =>
            keyPairNative.`public`
              .map(_.importKey(publicUsage, algo).map(publicKeyOpt =>
                toKeyPair(privateKeyOpt, publicKeyOpt)))
              .getOrElse(Future { toKeyPair(privateKeyOpt, None) }))

  }

  implicit class PimpedKeyPair(keys: CryptoKeyPair)
      extends PyramidJSON
      with Crypto {

    private def sign(keys: CryptoKeyPair, data: typedarray.ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .sign(
          l(
            "name" -> "ECDSA",
            "hash" -> l("name" -> "SHA-384"),
          ).asInstanceOf[KeyAlgorithmIdentifier],
          keys.privateKey,
          data
        )
        .toFuture
        .map(_.asInstanceOf[typedarray.ArrayBuffer])

    private def sign(data: typedarray.ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .sign(
          l(
            "name" -> "ECDSA",
            "hash" -> l("name" -> "SHA-384"),
          ).asInstanceOf[KeyAlgorithmIdentifier],
          keys.privateKey,
          data
        )
        .toFuture
        .map(_.asInstanceOf[typedarray.ArrayBuffer])

    private def signArrayBufferAddSigner(keys: CryptoKeyPair,
                                         b: typedarray.ArrayBuffer,
                                         signedB: typedarray.ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      exportCryptoKey(keys.publicKey)
        .map(
          jk =>
            bufferMod.Buffer
              .from(stringify(jk))
              .buffer
              .asInstanceOf[typedarray.ArrayBuffer])
        .map(r => (b, signedB, r))

    def signFile(keys: CryptoKeyPair, f: File)(
        implicit executionContext: ExecutionContext) =
      new FileReader()
        .futureReadArrayBuffer(f)
        .flatMap(b => signArrayBuffer(keys, b))

    def signArrayBuffer(keys: CryptoKeyPair, b: typedarray.ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      sign(b).flatMap(signedB => signArrayBufferAddSigner(keys, b, signedB))

  }

  implicit class PimpedJSONKey(jsonWebKey: JsonWebKey) extends Crypto {

    def importKey(usages: js.Array[KeyUsage], algo: KeyAlgorithmIdentifier)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .importKey(KeyFormat.jwk, jsonWebKey, algo, extractable = true, usages)
        .toFuture
        .map(k => Some(k.asInstanceOf[CryptoKey]))

    def importSymetricKey()(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .importKey(KeyFormat.jwk,
                   jsonWebKey,
                   aesGCMIdentifier,
                   extractable = true,
                   js.Array(KeyUsage.encrypt, KeyUsage.decrypt))
        .toFuture
        .map(_.asInstanceOf[CryptoKey])

  }

  implicit class PimpedKeySym(key: CryptoKey) extends Crypto {

    def symDecryptArrayBuffer(data: ArrayBuffer, iv: ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .decrypt(aesGCMIdentifierIV(new Uint8Array(iv)), key, data)
        .toFuture
        .map(_.asInstanceOf[ArrayBuffer])

    def symEncryptArrayBuffer(b: ArrayBuffer)(
        implicit executionContext: ExecutionContext) = {
      val iv = scrypto.crypto.getRandomValues(new Uint8Array(12))
      scrypto.crypto.subtle
        .encrypt(aesGCMIdentifierIV(iv), key, b)
        .toFuture
        .map(_.asInstanceOf[ArrayBuffer])
        .map(b2 => (b2, iv.buffer))
    }

    def symEncryptMetaData(e: EncryptedData, f: File)(
        implicit executionContext: ExecutionContext) =
      symEncryptArrayBuffer(metaDataFrom(f)).map(
        r =>
          e.copy(
            metaData = Some(r._1),
            metaRandom = Some(r._2)
        ))

    def symEncryptFileData(unencryptedData: ArrayBuffer)(
        implicit executionContext: ExecutionContext) = {

      val iv = scrypto.crypto.getRandomValues(new Uint8Array(12))
      scrypto.crypto.subtle
        .encrypt(aesGCMIdentifierIV(iv), key, unencryptedData)
        .toFuture
        .map(
          r =>
            EncryptedData(
              Some(unencryptedData),
              Some(r.asInstanceOf[ArrayBuffer]),
              Some(iv.buffer),
              None,
              None,
              None,
              None
          ))

    }

    def symEncryptFile(f: File)(implicit executionContext: ExecutionContext) =
      new FileReader()
        .futureReadArrayBuffer(f)
        .flatMap(symEncryptFileData(_))
        .flatMap(symEncryptMetaData(_, f))

  }

  implicit class PimpedKeyAsym(key: CryptoKey) extends Crypto {

    def verify(signature: typedarray.ArrayBuffer, data: typedarray.ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .verify(
          ecsda,
          key,
          signature,
          data
        )
        .toFuture
        .map(_.asInstanceOf[Boolean])

    def asymEncryptString(s: String)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .encrypt(rsaOADP, key, new TextEncoder().encode(s))
        .toFuture
        .map(_.asInstanceOf[typedarray.ArrayBuffer])

    def asymDecryptBuffer( b: nodeLib.Buffer)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .decrypt(rsaOADP,
                 key,
                 b.asInstanceOf[org.scalajs.dom.crypto.BufferSource])
        .
        //Buffer.from(f)).
        toFuture
        .map(_.asInstanceOf[typedarray.ArrayBuffer])

    def asymDecryptArrayBuffer(b: typedarray.ArrayBuffer)(
        implicit executionContext: ExecutionContext) =
      scrypto.crypto.subtle
        .decrypt(rsaOADP, key, b)
        .toFuture
        .map(_.asInstanceOf[typedarray.ArrayBuffer])

  }



  implicit class PimpedBufferSource(b: BufferSource){
    def sha356()(
      implicit excecutionContext: ExecutionContext): Future[ArrayBuffer] =
      scrypto.crypto.subtle
        .digest("SHA-256", b)
        .toFuture
        .map(_.asInstanceOf[ArrayBuffer])

  }


  implicit class PimpedWalletNative(walletNative: WalletNative) extends Crypto {
    def importSymKey()
                    (implicit ctx:ExecutionContext)= walletNative.
      sym.
      map(_.importSymetricKey().
        map(k=>Some(k))).getOrElse(Future{None})

    def importAsymKey()(implicit  executionContext: ExecutionContext) = walletNative.
      asym.
      map(_.importKeyPair(
          privateUsage = usageDecrypt,
          publicUsage = usageEncrypt,aHashAlgorithm).
          map(e=>Some(e))).
      getOrElse(Future{None})



    def importSignKey()(implicit  executionContext: ExecutionContext) = walletNative.sign.
      map(_.importKeyPair(
          privateUsage = usageSign,
          publicUsage = usageVerify,
          aSignAlgorithm
        ).
          map(e=>Some(e))).
      getOrElse(Future{None})


    def importAllKeys()(implicit  executionContext: ExecutionContext) = CryptoConfig(
      None,None,None,None).importAllKeys(walletNative)








  }



}
