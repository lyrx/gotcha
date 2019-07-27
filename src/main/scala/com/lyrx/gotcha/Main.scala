package com.lyrx.gotcha

import com.lyrx.pyramids.stellarsdk.Timeout
import com.lyrx.pyramids.{Config, Pyramid}
import org.scalajs.dom.document
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.ReactDOM
import slinky.web.html._

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import js.Dynamic.{literal => l}
import scala.scalajs.js.annotation.ScalaJSDefined


object Main {
  def main(args: Array[String]): Unit =
    ReactElements.renderAll(MyComponents.Pyramidal(None))
}