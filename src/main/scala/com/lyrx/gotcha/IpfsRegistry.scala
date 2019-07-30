package com.lyrx.gotcha

import com.lyrx.gotcha.Main.ec
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.html.Input
import slinky.core.annotations.react
import slinky.core.facade.{React, ReactElement}
import slinky.core.{StatelessComponent, SyntheticEvent}
import slinky.web.html._

import scala.concurrent.Future


@react class IpfsRegistry extends StatelessComponent {

  val fileField = React.createRef[dom.html.Input]

  case class Props(
      pyramidOpt: Option[Pyramid],
  )



  override def componentDidMount(): Unit = {


  }
  override def componentDidUpdate(prevProps: Props, prevState: State): Unit = {
    if (prevProps.pyramidOpt.isEmpty){

    }

  }

  def handleChange(e: SyntheticEvent[Input, Event])= {


  }

  override def render(): ReactElement =
    div(className := "card shadow mb-4 my-card" )(
      div(className := "card-header py-3")(
        h6(className := "m-0 font-weight-bold text-primary")(
          "Publish Your Identity"
        )
      ),
      div(className := "my-card-body")(
        div( )(
          a(href := "#",
            className := "btn my-btn btn-icon-split",
            onClick := (e =>{}))(
            i(className := "fas fa-upload m-button-label"),
            span(className:="my-label")("Publish Identity")
          )
        )
      )
    )



}
