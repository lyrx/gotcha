package com.lyrx.gotcha.components


import com.lyrx.gotcha.{ PageOptionTrait, components}
import com.lyrx.pyramids.Pyramid
import org.scalajs.dom
import slinky.web.html._

import scala.collection.immutable.HashMap
import scala.concurrent.ExecutionContext

object PageOption extends PageOptionTrait {

  def navLinks(previous: String, next: String)(
      implicit pyramidOpt: Option[Pyramid]) =
    PageOption
      .nav(
        nextOpt =
          (if (next != "")
             Some(next)
           else
             None),
        prevOpt =
          (if (previous != "")
             Some(previous)
           else
             None)
      )

  def nav(
      nextOpt: Option[String],
      prevOpt: Option[String]
  )(implicit pyramidOpt: Option[Pyramid]) = p(
    nextOpt
      .map(
        next =>
          a(
            href := "#",
            className := "btn btn-light btn-icon-split",
            onClick := (e => PageOption.renderHash(pyramidOpt, next))
          )(
            span(className := "icon text-black-50")(
              i(className := "fas fa-arrow-right"),
              span(className := "text")(
                if (PageOption.isGerman()) "Weiter" else "Next"))))
      .getOrElse(span())
    ,prevOpt
      .map(
        next =>
          a(
            href := "#",
            className := "btn btn-light btn-icon-split",
            onClick := (e => PageOption.renderHash(pyramidOpt, next))
          )(
            span(className := "icon text-black-50")(
              i(className := "fas fa-arrow-left"),
              span(className := "text")(
                if (PageOption.isGerman()) "Zurück" else "Previous"))))
      .getOrElse(span())



  )

  val map: Map[String, PageOption] = HashMap(
    "landing" -> components.PageOption(
      german = Some((aPyramidOpt: Option[Pyramid]) => Landing(aPyramidOpt)),
      english = Some((aPyramidOpt: Option[Pyramid]) => LandingEN(aPyramidOpt))
    ),
    "highavailability" -> components.PageOption(
      german = Some((aPyramidOpt: Option[Pyramid]) => Landing2(aPyramidOpt)),
      english = Some((aPyramidOpt: Option[Pyramid]) => Landing2(aPyramidOpt))
    ),
  )
}


case class PageOption(
    german: Option[GotchaPyramidRenderer],
    english: Option[GotchaPyramidRenderer]
) extends PageOptionTrait {

  def renderer() =
    if (isGerman())
      german
    else
      english

  def render(pyramidOpt: Option[Pyramid]) =
    renderer().map(r => initReactElements(pyramidOpt, r))

}
