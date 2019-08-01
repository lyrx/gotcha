package com.lyrx.pyramids

import com.lyrx.pyramids.Ipfs.{IpfsSupport}
import com.lyrx.pyramids.crypto.{CryptoSupport, Cryptography}
import org.scalajs.dom.raw.File

import scala.concurrent.ExecutionContext

object Config {

  val IPFS_DEFAULT = () => IpfsSupport.infura()



  /*

  stellar test accounts:

private:
Public Key	GAH3OFRA4DWRZDQH4DWWPEIJ2ZXHDLSHE5ECY6VUHEFSNEHCF6KNF3OO
Secret Key	SBNW75AAHCQVQLDAAEIZIBMRO3RETCN43FSZRCLU57OJKGUU5ML2F2Y2

docs:
Public Key	GDUWBX2K7PZT5C4YP3QVGF55VSD2HACINWFCAL45UYOD73PS6ICDJTO3
Secret Key	SDLZWRFJBZMF4PULSZXKQ5U7DXI5HV7ZHWFPVPJLTOIV67P75YA4CHJK

ids:
Public Key	GB6JWG7HLUQH3O5S35WK6A7Q2S26IRB2XGV24SQ2K53MCOFTSATDINEY
Secret Key	SCSIPIXZ4Y7X7IX4ZTTHVGDCXFJMFL62TRT4DFS3NSK3G3KMKJPKNYSY













   */







  def createFuture(isTestNet: Boolean)(
      implicit executionContext: ExecutionContext) =
    CryptoSupport
      .createFuture()
      .map(
        cr =>
          Config(
            cryptoSupport = cr,
            frontendData = FrontendData("Welcome, Oh Humble Tokenizer!"),
            ipfsData =
              IpfsData(
                regOpt = None,
                uploads = Nil,
                pharaoOpt =
                  Some("QmUK2hhKzDfEtnetu41AZUjc7CU8EtLn135EVKyHprVVyn"),
                identityOpt = None,
              ),
            blockchainData = BlockchainData(
              StellarData(
                docsPubObt = Some(
                  "GDY7YWJF6F7W7EIQP5UDWYXNBC62JUSGJOLM2VWRQGY7RZ5SDYRZOZNT"),
                idPubObt = Some(
                  "GDY7YWJF6F7W7EIQP5UDWYXNBC62JUSGJOLM2VWRQGY7RZ5SDYRZOZNT"),
                registrationFeeXLMOpt = Some("4"),
                notarizeFeeXLMOpt = Some("4"),
                isTestNet
              )
            ),
            ipfsSupport = IPFS_DEFAULT
        ))
}

case class StellarData(
    docsPubObt: Option[String],
    idPubObt: Option[String],
    registrationFeeXLMOpt: Option[String],
    notarizeFeeXLMOpt: Option[String],
    testNet: Boolean
){

  def passwordFieldValueDefault() =
      if(testNet) "SBNW75AAHCQVQLDAAEIZIBMRO3RETCN43FSZRCLU57OJKGUU5ML2F2Y2"
      else
        ""


  def docsFieldValueDefault() =
      if(testNet)  "GDUWBX2K7PZT5C4YP3QVGF55VSD2HACINWFCAL45UYOD73PS6ICDJTO3"
      else
        ""


  def idFieldValueDefault() =
      if(testNet)  "GB6JWG7HLUQH3O5S35WK6A7Q2S26IRB2XGV24SQ2K53MCOFTSATDINEY"
      else
        ""
   


}







case class BlockchainData(stellar: StellarData)

case class Config(
    cryptoSupport: Cryptography,
    frontendData: FrontendData,
    ipfsData: IpfsData,
    blockchainData: BlockchainData,
    ipfsSupport: IPFS
) {

  def withIdentityName(n:String) = this.copy(cryptoSupport = this.cryptoSupport.withName(n))

  def clearIdentity() = this.copy(
    ipfsData=this.ipfsData.copy(regOpt = None,identityOpt = None)
  )

  def withIpfssupport(aIpfsSupport: IPFS) =
    this.copy(ipfsSupport = aIpfsSupport)

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
      ipfsData = this.ipfsData.copy(regOpt = so)
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
      ipfsData = this.ipfsData.copy(identityOpt = registration)
    )

  def withUpload(f: File, hash: String) =
    this.copy(
      frontendData =
        this.frontendData
          .copy(
            message =
              s"Oh Humble Tokenizer, you have uploaded ${f.name} to Ipfs!"),
      ipfsData =
        this.ipfsData.copy(uploads = ipfsData.uploads :+ Upload(f, hash))
    )
}
