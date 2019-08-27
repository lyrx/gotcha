package com.lyrx.pyramids.ipfs

import scala.scalajs.js

case class LoadProgress(address:js.Any,hash:String,entry:Entry,progress:Double,total:Double)