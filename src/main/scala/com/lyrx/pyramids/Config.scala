package com.lyrx.pyramids

import com.lyrx.pyramids.ipfsapi.IpfsSupport
import com.lyrx.pyramids.crypto.{CryptoSupport, Cryptography}
import com.lyrx.pyramids.ipfs.{Ipfs, OrbitDB}
import org.scalajs.dom.raw.File

import scala.concurrent.ExecutionContext

object Config {

  val IPFS_DEFAULT:IPFS = () => IpfsSupport.hosttech()

  def createFuture(isTestNet: Boolean)(
      implicit executionContext: ExecutionContext) =
    CryptoSupport
      .createFuture()
      .map(
        cr =>
          Config(
            cryptoSupport = cr,
            frontendData = FrontendData("Welcome, Oh Humble Tokenizer!"),
            p2pData = P2PData(
              stellar = StellarData(
                transactionIdOpt = None,
                registrationFeeXLMOpt =
                  if (isTestNet)
                    Some("4")
                  else
                    Some("0.00001"),
                notarizeFeeXLMOpt =
                  if (isTestNet)
                    Some("4")
                  else
                    Some("0.00001"),
                isTestNet
              ),
              ipfsOpt = None,
              dbOpt = None,
              ipfsData = IpfsData(
                regOpt = None,
                uploads = Nil,
                pharaoOpt =
                  Some("QmUK2hhKzDfEtnetu41AZUjc7CU8EtLn135EVKyHprVVyn"),
                identityOpt = None,
              ),
              ipfsSupport = IPFS_DEFAULT
            )
        ))
}

case class Config(
    cryptoSupport: Cryptography,
    frontendData: FrontendData,
    p2pData: P2PData
) {
  def withIpfssupport(h: IPFS):Config = this.copy(p2pData=this.p2pData.withIpfssupport(h))


  def withTID(id: Option[String]) =
    this.copy(p2pData = this.p2pData.withTID(id))

  def withIdentityName(n: String) =
    this.copy(cryptoSupport = this.cryptoSupport.withName(n))

  def withIpfs(ipfs: Ipfs) = this.copy(p2pData = this.p2pData.withIpfs(ipfs))
  def withOrbit(db: OrbitDB) = this.copy(p2pData = this.p2pData.withOrbit(db))

  def clearIdentity() = this.copy(
    p2pData = p2pData.copy(
      ipfsData = this.p2pData.ipfsData.copy(regOpt = None, identityOpt = None))
  )



  def withMessage(s: String) =
    this.copy(frontendData = this.frontendData.copy(message = s))

  def withRegistration(so: Option[String]) =
    this.copy(
      frontendData = so
        .map(
          s =>
            this.frontendData
              .copy(message = s"Your identity is registered."))
        .getOrElse(this.frontendData
          .copy(message = "Registration failed, no data about you!")),
      p2pData= p2pData.copy(ipfsData = this.p2pData.ipfsData.copy(regOpt = so))
    )

  def withIdentity(registration: Option[Registration]) =
    this.copy(
      frontendData = registration
        .map(
          s =>
            this.frontendData
              .copy(message =
                s"Oh Humble Tokenizer, we have loaded your identity!"))
        .getOrElse(this.frontendData.copy(
          message = "Oh Humble Tokenizer, I have not found your identity!")),
      p2pData =  this.p2pData.copy(ipfsData = this.p2pData.ipfsData.copy(identityOpt = registration))
    )


}
