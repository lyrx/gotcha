package com.lyrx.gotcha

import com.lyrx.pyramids.stellarsdk.Timeout

import scala.concurrent.ExecutionContext

object Main  {

  implicit val ec = ExecutionContext.global
  implicit val timeout: Timeout = new Timeout(30)


  def main(args: Array[String]): Unit = {
    println("Yohai!")

  }







}
