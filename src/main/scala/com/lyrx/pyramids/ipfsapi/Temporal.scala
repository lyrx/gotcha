package com.lyrx.pyramids.ipfsapi


import org.scalajs.dom.experimental.{Fetch, RequestInit, Response}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData
import typings.nodeLib

import scalajs.js
import js.JSON
import js.Dynamic.{literal => l}
import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js.typedarray.Uint8Array




class TemporalCredentials(
                           val username: String,
val password: String,aurl:String){

  def loginFetch() =
    Fetch
      .fetch(
        aurl,
        l(
          "method" -> "POST",
          "headers" -> l(
            "Content-Type" -> "text/plain"
          ),
          "body" -> JSON.stringify(
            l(
              "username" -> username,
              "password" -> password
            ))
        ).asInstanceOf[RequestInit]
      )
      .toFuture

  def loginAjax() = Ajax.post(
    url = aurl,
    data = JSON.stringify(
      l(
        "username" -> username,
        "password" -> password
      )),
    headers = Map("Content-Type" -> "text/plain"),
    timeout = 0,
    withCredentials = false
  )

  def jsw()(implicit executionContext: ExecutionContext) =
    loginAjax().map(_.responseText)


}


object Temporal {
  val DEV_HOST = "dev.api.temporal.cloud"
  val DEV_LOGIN = s"https://${DEV_HOST}/v2/auth/login"
  val DEV_IPFS_HOST= "dev.api.ipfs.temporal.cloud"
}
