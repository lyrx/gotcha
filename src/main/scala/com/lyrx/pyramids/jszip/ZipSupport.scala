package com.lyrx.pyramids.jszip

import typings.jszipLib.jszipMod.JSZip
import typings.{nodeLib, stdLib}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.typedarray


trait ZipSupport{


  def zipFromArrayBuffer(b:nodeLib.Buffer)(implicit executionContext: ExecutionContext) =
    zipInstance()
      .loadAsync(b)
      .toFuture
    .flatMap(_.toEncrypted())

}






