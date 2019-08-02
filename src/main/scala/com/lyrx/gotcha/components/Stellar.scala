package com.lyrx.gotcha.components

import com.lyrx.gotcha.MyComponents
import com.lyrx.pyramids.Pyramid
import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{`type`, className, defaultValue, div, img, input, onChange, placeholder, ref, span, src}

import com.lyrx.gotcha._



@react class Stellar extends Component {






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
        defaultValue :=   props.pyramidOpt.stellarPassword(),
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
        defaultValue := props.pyramidOpt.stellarData().map(_.docsFieldValueDefault()).getOrElse("") ,
        placeholder := "Public Key",
        onChange := (e => {
          e.preventDefault()
        })
      ),
      span(className:="account")("IDs: "),
      input(
        className := inputClasses,
        ref := MyComponents.idsField,
        defaultValue := props.pyramidOpt.stellarData().map(_.idFieldValueDefault()).getOrElse("") ,
        placeholder := "Public Key",
        onChange := (e => {
          e.preventDefault()
        })
      )

    )
  }

  override def initialState: State = State(  props.pyramidOpt.stellarPassword() )
}