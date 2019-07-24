package com.lyrx.gotcha

import com.lyrx.pyramids.Ipfs.IpfsSupport
import com.lyrx.pyramids.stellarsdk.Timeout
import com.lyrx.pyramids.{Config, Pyramid}
import me.shadaj.slinky.web.ReactDOM
import me.shadaj.slinky.web.html._
import org.scalajs.dom.document

import scala.concurrent.{ExecutionContext, Future}

object Main {

  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)
  implicit val isTest: Boolean = true


  def initPyramid()(implicit executionContext: ExecutionContext) =
    Config.createFuture()
        .flatMap(
          Pyramid(_)
            .loadPharaohKey())




  def main(args:Array[String]):Unit = {
    val root = document.getElementById("root")
    initPyramid().map(p=>
      ReactDOM.render(h1(s"Hollahei:${p.config.frontendData.message}" ), root)
    )

  }



}
