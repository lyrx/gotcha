package com.lyrx.gotcha

import org.scalajs.dom
import slinky.core.facade.React
import slinky.web.html._


object MyComponents {

  val passwordField = React.createRef[dom.html.Input]
  val docsField = React.createRef[dom.html.Input]
  val idsField = React.createRef[dom.html.Input]



  def pageHeading(title: String) =
    div(
      className := "d-sm-flex align-items-center justify-content-between mb-4")(
      h1(className := "h3 mb-0 text-gray-800")(title)
    )


  def hashUrl(s:String) = s"https://ipfs.lyrx.de/ipfs/${s}"








}
