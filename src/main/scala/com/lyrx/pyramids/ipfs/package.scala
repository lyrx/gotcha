package com.lyrx.pyramids

import scala.concurrent
import scala.scalajs.js

package object ipfs {


  /*


    // When the database is ready (ie. loaded), display results
    db.events.on('ready', () => queryAndRender(db))
    // When database gets replicated with a peer, display results
    db.events.on('replicated', () => queryAndRender(db))
    // When we update the database, display result
    db.events.on('write', () => queryAndRender(db))

    db.events.on('replicate.progress', () => queryAndRender(db))

    // Hook up to the load progress event and render the progress
    let maxTotal = 0, loaded = 0
    db.events.on('load.progress', (address, hash, entry, progress, total) => {
      loaded ++
      maxTotal = Math.max.apply(null, [maxTotal, progress, 0])
      total = Math.max.apply(null, [progress, maxTotal, total, entry.clock.time, 0])
      statusElm.innerHTML = `Loading database... ${maxTotal} / ${total}`
    })

   */



  implicit class PimpedEvents(val events: Events){

    def onReady() = onn("ready")
    def onReplicated() = onn("replicated")
    def onWrite() = onn("write")
    def onReplicateProgress() = onn("replicate.progress")


    def onn(event:String) ={
      val promise = concurrent.Promise[Unit]
      events.on(event,()=> promise.success(Unit))
      promise.future
    }

    def onLoadProgress() ={
      val promise = concurrent.Promise[LoadProgress]
      events.on("load.progress",(address:js.Any,hash:String,entry:Entry,progress:Double,total:Double)=>
        promise.success(LoadProgress(address:js.Any,hash:String,entry:Entry,progress:Double,total:Double)))
      promise.future
    }
  }



  implicit class PimpedIpfs(val ipfs: Ipfs){

    def onException() = {
      val promise = concurrent.Promise[js.Error]
      ipfs.on("error",(e)=>promise.success(e))
      promise.future
    }

    def onReady() = {
      val promise = concurrent.Promise[Ipfs]
      ipfs.on("ready",()=>promise.success(ipfs))
      promise.future
    }


  }

}
