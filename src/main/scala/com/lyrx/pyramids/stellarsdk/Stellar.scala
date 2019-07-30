package com.lyrx.pyramids.stellarsdk



import scala.concurrent.ExecutionContext
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object StellarObject {

  val TESTNET = "https://horizon-testnet.stellar.org"
  val MAINNET = "https://horizon.stellar.org"
  //val pyramidAccess = new stellarSdk.Asset("pyramid","pharaoh")

}

trait Stellar {


  def initStellar(isTest:Boolean)
                 (implicit executionContext: ExecutionContext) = {
    // Uncomment the following line to build transactions for the live network. Be
    // sure to also change the horizon hostname.
    // StellarSdk.Network.usePublicNetwork();
    if(isTest){
      Network.useTestNetwork()
      new Server(StellarObject.TESTNET)
    }
    else{
      Network.usePublicNetwork();
      new Server(StellarObject.MAINNET)
    }

  }

  def loadPrivateAccount(privateKey: String,isTestNet:Boolean)(
      implicit executionContext: ExecutionContext) =
    initStellar(isTestNet).loadPrivateAcount(privateKey)


  def register(value:String,privateKey: String,aSendTo:String,amount:String,isTestNet:Boolean)(
    implicit executionContext: ExecutionContext,t:Timeout) =
    initStellar(isTestNet)
      .register(aaHash = value,
        aPrivateKey = privateKey,
        sendTo = aSendTo,
        aamount = amount
      )
    .map(t=>value)



  def balanceForPrivate(privateKey:String,isTestNet:Boolean)(
    implicit executionContext: ExecutionContext)=
    initStellar(isTestNet).balanceForPrivate(privateKey)


  def balanceForPublic(pubKey:String,isTestNet:Boolean)(
    implicit executionContext: ExecutionContext)=
    initStellar(isTestNet).balanceForPublic(pubKey)



}

