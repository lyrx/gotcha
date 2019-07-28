package com.lyrx

import com.lyrx.pyramids.Pyramid
import slinky.core.facade.ReactElement

package object gotcha {


  type GotchaRenderer = ()=>ReactElement
  type GotchaPyramidRenderer = (Option[Pyramid])=>ReactElement

}
