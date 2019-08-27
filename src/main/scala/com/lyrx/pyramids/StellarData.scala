package com.lyrx.pyramids

case class StellarData(
                        transactionIdOpt: Option[String],
                        registrationFeeXLMOpt: Option[String],
                        notarizeFeeXLMOpt: Option[String],
                        testNet: Boolean
                      ){

  def withTID(id:Option[String])=this.copy(transactionIdOpt=id)

  def passwordFieldValueDefault() =
    if(testNet) "SBNW75AAHCQVQLDAAEIZIBMRO3RETCN43FSZRCLU57OJKGUU5ML2F2Y2"
    else
      "SBUFXJHC2LCGJNMC225WA6BSYUAATYSFBNZXFBYD33Q2BSGLFL7YALPY"


  def docsFieldValueDefault() =
    if(testNet)  "GDUWBX2K7PZT5C4YP3QVGF55VSD2HACINWFCAL45UYOD73PS6ICDJTO3"
    else
      "GBZQMAAA5L46IGYZPQ5AV7ZYCY3IQSZMFAQ3WAR4EF7ZCE52AVBEAVFX"


  def idFieldValueDefault() =
    if(testNet)  "GB6JWG7HLUQH3O5S35WK6A7Q2S26IRB2XGV24SQ2K53MCOFTSATDINEY"
    else
      "GBZQMAAA5L46IGYZPQ5AV7ZYCY3IQSZMFAQ3WAR4EF7ZCE52AVBEAVFX"



}