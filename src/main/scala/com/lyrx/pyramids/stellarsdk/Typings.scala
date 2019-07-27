package com.lyrx.pyramids.stellarsdk

import typings.nodeLib

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("stellar-sdk", "Network")
@js.native
object Network extends js.Object {
  def current(): StellarNetwork = js.native
  def use(network: StellarNetwork): scala.Unit = js.native
  def usePublicNetwork(): Unit = js.native
  def useTestNetwork(): Unit = js.native
}

@JSImport("stellar-sdk", "Operation")
@js.native
object Operation extends js.Object {

  def accountMerge(options: js.Dynamic): Operation = js.native
  def allowTrust(options: js.Dynamic): Operation = js.native
  def bumpSequence(options:js.Dynamic): Operation = js.native
  def changeTrust(options:js.Dynamic):Operation = js.native
  def createAccount(options:js.Dynamic): Operation = js.native
  def createPassiveOffer(options:js.Dynamic): Operation = js.native
  def inflation(options: js.Dynamic): Operation = js.native
  def manageData(options: js.Dynamic): Operation = js.native
  def manageOffer(options: js.Dynamic): Operation = js.native
  def pathPayment(options: js.Dynamic): Operation = js.native
  def payment(options: js.Dynamic): Operation = js.native

}


@js.native
trait Operation extends js.Object {


}

@js.native
trait Memo extends js.Object
   {

}

/* static members */
@JSImport("stellar-sdk", "Memo")
@js.native
object Memo extends js.Object {
  def hash(hash: java.lang.String): Memo = js.native

}






@JSImport("stellar-base", "Network")
@js.native
class StellarNetwork extends js.Object {
  def networkId(): java.lang.String = js.native
  def networkPassphrase(): java.lang.String = js.native
}



@js.native
trait Account extends js.Object {
  def accountId():String = js.native

}
@js.native
trait Accounts extends js.Object {
  def accountId(s:String):AccountDetails = js.native


}

@js.native
trait AccountDetails extends js.Object {
  def call():js.Promise[AccountDetail]  = js.native

}
/*
{
    "balance": "0.0000000",
    "limit": "10000.0000000",
    "asset_type": "credit_alphanum4",
    "asset_code": "PHP",
    "asset_issuer": "GDENQQVYYVD4DWAF5243RLRW4GS7H2LAUM4VVRA2KDERFKP4N2XATCIJ"
  },

 */


@js.native
trait Balance extends js.Object {
  val balance:String = js.native

}

@js.native
trait AccountDetail extends js.Object {

  def balances:js.Array[Balance] = js.native


}



@JSImport("stellar-sdk", "Server")
@js.native
class Server extends js.Object {
  def this(serverURL: java.lang.String) = this()
  var serverURL: js.Any = js.native
  def fetchBaseFee():js.Promise[Double] = js.native

  def loadAccount(accountId: java.lang.String): js.Promise[Account] = js.native

  def accounts():Accounts = js.native

  def submitTransaction(t:Transaction):js.Promise[Operation]=js.native

}



@JSImport("stellar-sdk", "TransactionBuilder")
@js.native
class TransactionBuilder extends js.Object {
  def this(sourceAccount: Account) = this()
  def this(sourcAccount:Account,options:js.Dynamic) = this()

  def addOperation(o:Operation):TransactionBuilder = js.native

  def setTimeout(second:Int):TransactionBuilder = js.native
  def  build():Transaction = js.native
  def addMemo(m:Memo):TransactionBuilder = js.native
}

@js.native
trait Transaction extends js.Object{
  def sign(keypairs: Keypair*): scala.Unit = js.native
}


@js.native
trait Keypair extends js.Object {
  def canSign(): scala.Boolean = js.native
  def publicKey(): java.lang.String = js.native
  def rawPublicKey(): nodeLib.Buffer = js.native
  def rawSecretKey(): nodeLib.Buffer = js.native
  def secret(): java.lang.String = js.native

}


/* static members */
@JSImport("stellar-sdk", "Keypair")
@js.native
object Keypair extends js.Object {
  def fromBase58Seed(secretSeed: java.lang.String): Keypair = js.native
  def fromPublicKey(publicKey: java.lang.String): Keypair = js.native
  def fromRawEd25519Seed(secretSeed: nodeLib.Buffer): Keypair = js.native
  def fromSecret(secretKey: java.lang.String): Keypair = js.native
  def master(): Keypair = js.native
  def random(): Keypair = js.native
}


@js.native
trait Asset extends js.Object {

}

/* static members */
@JSImport("stellar-sdk", "Asset")
@js.native
object Asset extends js.Object {
  def native(): Asset = js.native
}


