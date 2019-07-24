package com.lyrx.gotcha

import me.shadaj.slinky.web.ReactDOM
import me.shadaj.slinky.web.html._ // imports tags, such as "h1"

import org.scalajs.dom.{document}

import scala.scalajs.js
object Main {



  def main(args:Array[String]):Unit = {

    val reactContainer = document.createElement("div")
    document.body.appendChild(reactContainer)

    ReactDOM.render(
      h1("hello!"), // this is Scala's version of JSX!
      reactContainer
    )
  }



}
