package com.lyrx.gotcha

import com.lyrx.pyramids.stellarsdk.Timeout

import scala.concurrent.ExecutionContext


object Implicits{
  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)
}