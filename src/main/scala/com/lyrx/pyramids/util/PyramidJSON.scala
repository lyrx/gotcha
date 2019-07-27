package com.lyrx.pyramids.util

import scala.scalajs.js

trait PyramidJSON {

  def stringify(aAny: js.Any) = js.JSON.stringify(aAny: js.Any, null: js.Array[js.Any], 1: js.Any)

}
