package com.lyrx.pyramids.crypto

import com.lyrx.pyramids.crypto
import com.lyrx.pyramids.util.PyramidJSON

import scala.concurrent.{ExecutionContext, Future}


object CryptoSupport {


  def createFuture()(implicit executionContext: ExecutionContext): Future[Cryptography] =
    CryptoConfig(symKeyOpt = None,
                 asymKeyOpt = None,
                 signKeyOpt = None,
                 pharaohKeyOpt = None,
      nameOpt = None
    ).
      generateAllKeys().
      map(new CryptoSupport(_))


  def importFuture(walletNative: WalletNative)(implicit executionContext: ExecutionContext): Future[Cryptography] =
    walletNative.importAllKeys().map(new CryptoSupport(_))
}

class CryptoSupport(override val config: CryptoConfig) extends Cryptography{



}
