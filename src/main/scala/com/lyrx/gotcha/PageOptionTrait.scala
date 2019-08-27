package com.lyrx.gotcha

import com.lyrx.gotcha.components.{GotchaPyramidRenderer, IdentityManagement, ManagementWrapper, PageOption,Notary}
import com.lyrx.pyramids.{Config, Pyramid}
import org.scalajs.dom
import org.scalajs.dom.document
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM

import scala.concurrent.{ExecutionContext, Future}



trait PageOptionTrait {




  def initPyramid(isTestNet: Boolean,pyramidOpt:Option[Pyramid])(
    implicit executionContext: ExecutionContext) =
    pyramidOpt
      .map(p => Future { p })
      .getOrElse(
        Config
          .createFuture(isTestNet)
          .flatMap(Pyramid(_)
            .loadPharaohKey())
      )





  def initReactElements(pyramidOpt:Option[Pyramid],renderer:GotchaPyramidRenderer) = renderAll(
    ManagementWrapper(  ManagementWrapper.Props(pyramidOpt,renderer)))



  def initWithIdentityManagement(po:Option[Pyramid]): Unit =
    initReactElements(po,
      ((aPyramidOpt) =>IdentityManagement(IdentityManagement.Props(aPyramidOpt) )))

  def initWithNotary(po:Option[Pyramid]): Unit =
    initReactElements(po,
      ((aPyramidOpt) =>Notary(aPyramidOpt) ))




  def renderAll(p: ReactElement) =
    ReactDOM.render(p, document.getElementById("root"))


  def renderHash(pyramidOpt: Option[Pyramid], h: String) =
    PageOption.map(h).render(pyramidOpt)

  def fromHash(h: String) = PageOption.map(h).render(None)

  def fromDefault() =initWithIdentityManagement(None)

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

