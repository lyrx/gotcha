package com.lyrx.pyramids.stellarsdk

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Promise
import js.Dynamic.{literal => l}
import com.lyrx.pyramids.util.Implicits._

trait ServerTrait {

  val server: Server

  def futureLoadAccount(publicKey: String)(
      implicit executionContext: concurrent.ExecutionContext) =
    server
      .loadAccount(publicKey)
      .toFuture

  def loadPrivateAcount(privateKey: String)(
      implicit executionContext: concurrent.ExecutionContext) = {
    val trimmed = privateKey.trim()
    futureLoadAccount(Keypair.fromSecret(trimmed).publicKey())
      .map(account => (trimmed, account))
  }

  /*

  {
  "_links": {
    "self": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N"
    },
    "transactions": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/transactions{?cursor,limit,order}",
      "templated": true
    },
    "operations": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/operations{?cursor,limit,order}",
      "templated": true
    },
    "payments": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/payments{?cursor,limit,order}",
      "templated": true
    },
    "effects": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/effects{?cursor,limit,order}",
      "templated": true
    },
    "offers": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/offers{?cursor,limit,order}",
      "templated": true
    },
    "trades": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/trades{?cursor,limit,order}",
      "templated": true
    },
    "data": {
      "href": "https://horizon-testnet.stellar.org/accounts/GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N/data/{key}",
      "templated": true
    }
  },
  "id": "GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N",
  "account_id": "GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N",
  "sequence": "3756872318320648",
  "subentry_count": 4,
  "last_modified_ledger": 1283378,
  "thresholds": {
    "low_threshold": 0,
    "med_threshold": 0,
    "high_threshold": 0
  },
  "flags": {
    "auth_required": false,
    "auth_revocable": false,
    "auth_immutable": false
  },
  "balances": [
    {
      "balance": "9999.9999200",
      "buying_liabilities": "0.0000000",
      "selling_liabilities": "0.0000000",
      "asset_type": "native"
    }
  ],
  "signers": [
    {
      "weight": 1,
      "key": "GCHUIVJN4GT7MDYG2552XPRB2NSX6DZNIVYI3JXB3A4XS6XKD3EXL62N",
      "type": "ed25519_public_key"
    }
  ],
  "data": {
    "QmSeUEei1fBexYJNAxZCJ2imkpxuWUDMqoV4TGF8YHSjN7": "cmVnaXN0cmF0aW9u",
    "QmXCHn9UUptWnSifu5X1iXb6k56sEcLw4ZXGJfKaEVH3ZD": "cmVnaXN0cmF0aW9u",
    "QmZEu8gM2cjJKaECMbtSNCwVDbNNSjfz47GzYiTDmnEhSg": "cmVnaXN0cmF0aW9u",
    "registration": "VEhJU0lTVEVTVA=="
  }
}
   */

  def balanceForPrivate(privateKey: String)(
      implicit executionContext: concurrent.ExecutionContext) =
    balanceForPublic(Keypair.fromSecret(privateKey).publicKey())


  def balanceForPublic(pubKey: String)(
    implicit executionContext: concurrent.ExecutionContext) =server
    .accounts()
    .accountId(pubKey)
    .call()
    .toFuture
    .map(_.balances.headOption)
    .fmap(_.balance)



  def registrationTransaction(aHash: String,
                              name: String,
                              privateKey: String)()(
      implicit executionContext: concurrent.ExecutionContext,
      timeoutSeconds: Timeout) =
    server
      .fetchBaseFee()
      .toFuture
      .flatMap(aFee =>
        loadPrivateAcount(privateKey).map((t: (String, Account)) => {
          val transaction =
            new TransactionBuilder(t._2, l("fee" -> aFee))
              .addOperation(
                Operation.manageData(l("name" -> name, "value" -> aHash)))
              .setTimeout(timeoutSeconds.seconds)
              .build()
          transaction.sign(Keypair.fromSecret(t._1))
          (transaction, t._2)
        }))
  def registrationSendTransaction(privateKey: String,
                                  sendTo: String,
                                  aHash: String,
                                  amount:String
                                 )()(
                               implicit executionContext: concurrent.ExecutionContext,
                               timeoutSeconds: Timeout) =
    server
      .fetchBaseFee()
      .toFuture
      .flatMap(aFee =>
        loadPrivateAcount(privateKey).map((t: (String, Account)) => {
          val transaction =
            new TransactionBuilder(t._2, l("fee" -> aFee))
              .addOperation(
                Operation.payment(l(
                  "destination" -> sendTo,
                  "asset" -> Asset.native(),
                  "amount" -> amount)))
            .addMemo(Memo.hash(aHash.forStellarMemo()))
              .setTimeout(timeoutSeconds.seconds)
              .build()
          transaction.sign(Keypair.fromSecret(t._1))
          (transaction, t._2)
        }))





  def register(aaHash: String, aPrivateKey: String,sendTo:String,aamount:String)(
      implicit executionContext: concurrent.ExecutionContext,
      timeoutSeconds: Timeout) =
    registrationSendTransaction(
      privateKey= aPrivateKey,
      sendTo=sendTo,
      aHash = aaHash,
      amount = aamount
    )
      .flatMap((t) => {
        val account: Account = t._2;
        server
          .submitTransaction(t._1)
          .toFuture
          .map((_, account))
      })

}
