package com.lyrx.gotcha.docs

import com.lyrx.gotcha.docs.PageOption.CCreator
import org.scalajs.dom
import slinky.core.Component


object PageOption {

  type CCreator = ()=>Option[Component]

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
                       german:CCreator,
                       english:CCreator
                     ){

def getCreator() = if(PageOption.isGerman())
  german
  else
  english

}
