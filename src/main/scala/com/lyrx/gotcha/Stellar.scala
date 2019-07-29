package com.lyrx.gotcha

import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{`type`, className, defaultValue, div, img, input, onChange, placeholder, ref, span, src}

@react class Stellar extends Component {

  def passwordFieldValue() = props
    .pyramidOpt
    .map(p=>{
      val isTest  = p.config.blockchainData.stellar.testNet
      if(isTest) "SBSN4GWX4B7ALR5BDYH4VGWUWMAURFG6Y2SHJQL6CP62JT2N3Q42RPHI"
      else
        ""
    }).getOrElse("")

  case class Props(pyramidOpt: Option[Pyramid])
  case class State(password: String)

  override def render(): ReactElement = {
    div(className := "input-group")(
      img(src := "img/stellar.png"),
      span(className:="account")("Private: "),
      input(
        ref := MyComponents.passwordField,
        `type` := "password",
        defaultValue := passwordFieldValue(),
        className := "form-control bg-light border-0 small account ",
        placeholder := "Private Key",
        onChange := (e => {
          e.preventDefault()
          setState(state.copy(password = e.target.value))
        })
      ),
      span(className:="account")("Docs: "),
      input(
        className := "form-control bg-light border-0 small account ",
        placeholder := "Public Key",
        onChange := (e => {
          e.preventDefault()
        })
      ),
      span(className:="account")("IDs: "),
      input(
        className := "form-control bg-light border-0 small account ",
        placeholder := "Public Key",
        onChange := (e => {
          e.preventDefault()
        })
      )

    )
  }

  override def initialState: State = State(passwordFieldValue())
}