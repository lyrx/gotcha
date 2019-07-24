package com.lyrx.gotcha

import me.shadaj.slinky.web.ReactDOM
import me.shadaj.slinky.web.html._ // imports tags, such as "h1"

import org.scalajs.dom.{document}

object Main {



  def main(args:Array[String]):Unit = {
    val root = document.getElementById("root")
    println("Jocha hei: "+root)
    ReactDOM.render(h1("Hollahei!" ), root)
  }



}
