package com.lyrx.gotcha.docs

import com.lyrx.gotcha.{GotchaPyramidRenderer, Main}
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom



object PageOption {


  def locale() =  {
    val lang = dom.window.navigator.language
    if(lang == null)
      None
    else
      Some(lang)
  }

  def hashMark() ={
    val s = dom.window.location.hash
    if( s!=null && s.length > 1)
      Some(s.substring(1))
    else
      None
  }

  def isGerman(): Boolean = hashMark()
    .map(s=>(
      s.toLowerCase.contains("de")
      )
    )
  .getOrElse(false)

}


case class PageOption(
                       german:GotchaPyramidRenderer,
                       english:GotchaPyramidRenderer
                     ){

def renderer() = if(PageOption.isGerman())
  german
  else
  english

  def render(pyramidOpt:Option[Pyramid]) =
    Main.initReactElements(pyramidOpt,renderer())


}
