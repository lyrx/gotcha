package com.lyrx.gotcha.docs

import com.lyrx.gotcha.Main.initWithIdentityManagement
import com.lyrx.gotcha.{GotchaPyramidRenderer, Main}
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom

import scala.collection.immutable.HashMap
import scala.collection.mutable

object PageOption {

  val map: Map[String, PageOption] = HashMap(
    "landing" -> PageOption(
      german = Some((aPyramidOpt: Option[Pyramid]) => Landing(aPyramidOpt)),
      english = Some((aPyramidOpt: Option[Pyramid]) => LandingEN(aPyramidOpt))
    )
  )

  def fromHash(h:String) = map(h).render(None)

  def fromDefault() =  initWithIdentityManagement(None)


  def withPage() = hashMark().map(
    h=>if(map.contains(h))Some(h)else None).getOrElse(None)

  def init() = withPage()
    .map(fromHash(_))
    .getOrElse(fromDefault())

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

case class PageOption(
    german: Option[GotchaPyramidRenderer],
    english: Option[GotchaPyramidRenderer]
) {

  def renderer() =
    if (PageOption.isGerman())
      german
    else
      english

  def render(pyramidOpt: Option[Pyramid]) =
    renderer().map(r => Main.initReactElements(pyramidOpt, r))

}
