package com.lyrx.gotcha

import com.lyrx.gotcha.components.Page
import com.lyrx.pyramids.stellarsdk.Timeout
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import slinky.web.ReactDOM
import slinky.web.html.h1

import scala.concurrent.ExecutionContext

object Main  {

  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)




  def page()=Page(Page.Props(
        context = CContext("")
      ))



  def main(args: Array[String]): Unit = {

    println("yocha")


    dom.document.addEventListener("DOMContentLoaded", (event:Event) =>{
      println("DOM fully loaded and parsed");

    });


    ReactDOM.render( page(),
      document.getElementById("wrapper"))













  }







}
