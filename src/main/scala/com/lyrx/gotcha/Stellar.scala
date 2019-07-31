package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{`type`, className, defaultValue, div, img, input, onChange, placeholder, ref, span, src}

@react class Stellar extends Component {

  def passwordFieldValueDefault() = props
    .pyramidOpt
    .map(p=>{
      val isTest  = p.config.blockchainData.stellar.testNet
      if(isTest) "SBNW75AAHCQVQLDAAEIZIBMRO3RETCN43FSZRCLU57OJKGUU5ML2F2Y2"
      else
        ""
    }).getOrElse("")

  def docsFieldValueDefault() = props
    .pyramidOpt
    .map(p=>{
      val isTest  = p.config.blockchainData.stellar.testNet
      if(isTest)  "GDUWBX2K7PZT5C4YP3QVGF55VSD2HACINWFCAL45UYOD73PS6ICDJTO3"
      else
        ""
    }).getOrElse("")

  def idFieldValueDefault() = props
    .pyramidOpt
    .map(p=>{
      val isTest  = p.config.blockchainData.stellar.testNet
      if(isTest)  "GB6JWG7HLUQH3O5S35WK6A7Q2S26IRB2XGV24SQ2K53MCOFTSATDINEY"
      else
        ""
    }).getOrElse("")






  case class Props(pyramidOpt: Option[Pyramid])
  case class State(password: String)


  val inputClasses="form-control bg-light border-0 small account "
  override def render(): ReactElement = {
    div(className := "input-group")(
      img(src := "img/stellar.png"),
      span(className:="account")("Private: "),
      input(
        ref := MyComponents.passwordField,
        `type` := "password",
        defaultValue := passwordFieldValueDefault(),
        className := inputClasses,
        placeholder := "Private Key",
        onChange := (e => {
          e.preventDefault()
          setState(state.copy(password = e.target.value))
        })
      ),
      span(className:="account")("Docs: "),
      input(
        className := inputClasses,
        ref := MyComponents.docsField,
        defaultValue := docsFieldValueDefault(),
        placeholder := "Public Key",
        onChange := (e => {
          e.preventDefault()
        })
      ),
      span(className:="account")("IDs: "),
      input(
        className := inputClasses,
        ref := MyComponents.idsField,
        defaultValue := idFieldValueDefault(),
        placeholder := "Public Key",
        onChange := (e => {
          e.preventDefault()
        })
      )

    )
  }

  override def initialState: State = State(passwordFieldValueDefault())
}