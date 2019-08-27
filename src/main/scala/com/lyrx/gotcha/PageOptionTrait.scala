package com.lyrx.gotcha

import com.lyrx.gotcha.components.PageOption
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom

import scala.concurrent.ExecutionContext


trait PageOptionTrait {

  def renderHash(pyramidOpt: Option[Pyramid], h: String) =
    PageOption.map(h).render(pyramidOpt)

  def fromHash(h: String) = PageOption.map(h).render(None)

  def fromDefault() = Main.initWithIdentityManagement(None)

  def withPage() =
    hashMark()
      .map(h => if (PageOption.map.contains(h)) Some(h) else None)
      .getOrElse(None)



  def init()(implicit executionContext: ExecutionContext) = {
    withPage()
      .map(fromHash(_))
      .getOrElse(fromDefault())
  }

  def locale() = {
    val lang = dom.window.navigator.language
    if (lang == null)
      None
    else
      Some(lang)
  }

  def hashMark() = {
    val s = dom.window.location.hash
    if (s != null && s.length > 1)
      Some(s.substring(1))
    else
      None
  }

  def isGerman(): Boolean =
    locale()
      .map(
        s =>
          (
            s.toLowerCase.contains("de")
            ))
      .getOrElse(false)

}

