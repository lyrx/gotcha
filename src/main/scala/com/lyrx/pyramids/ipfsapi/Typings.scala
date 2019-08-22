package com.lyrx.pyramids.ipfsapi

import typings.nodeLib

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport, JSName}


@js.native
trait IPFSSFile extends js.Object{
  val path:String = js.native
  val hash:String = js.native
  val size:Int = js.native
}


@js.native
trait IPFSSError extends js.Object{

}

@js.native
@JSGlobal
class TextEncoder(utfLabel: js.UndefOr[String]= "utf-8" ) extends js.Object {
  def encode(buffer: String): js.typedarray.Uint8Array = js.native
}

@js.native
@JSGlobal
class TextDecoder(utfLabel: js.UndefOr[String] = "utf-8") extends js.Object {
  def decode(buffer: js.typedarray.Uint8Array): String = js.native
}



@js.native
@JSImport("ipfs-http-client",JSImport.Namespace)
object IpfsHttpClient extends js.Object {

  def apply(lit:js.Dynamic):IpfsClient = js.native

}

@js.native
trait PubSub extends js.Object {

  /*

  ipfs.pubsub.subscribe(topic, handler, [options], [callback])
topic: String
handler: (msg) => {} - Event handler which will be called with a message object everytime one is received. The msg has the format {from: String, seqno: Buffer, data: Buffer, topicIDs: Array<String>}.
options: Object - (Optional) Object containing the following properties:
discover: Boolean - (Default: false) Will use the DHT to find other peers.
callback: (Error) => {} - (Optional) Called once the subscription is established.
If no callback is passed, a [promise][] is returned.

   */

  def subscribe(topic:String,
                handler:PubSubHandler,
                option:PubSubOptions = null,
                discover:Boolean=true,
                callback:ErrorCallback = null
               ):ErrorPromise = js.native

  /*

  ipfs.pubsub.unsubscribe(topic, handler, [callback])
  topic: String - The topic to unsubscribe from
  handler: (msg) => {} - The handler to remove.
  callback: (Error) => {} (Optional) Called once the unsubscribe is done.
  If no callback is passed, a [promise][] is returned.

  If the topic and handler are provided, the handler will no longer receive updates for the topic. This behaves like EventEmitter.removeListener. If the handler is not equivalent to the handler provided on subscribe, no action will be taken.

  If only the topic param is provided, unsubscribe will remove all handlers for the topic. This behaves like EventEmitter.removeAllListeners. Use this if you would like to no longer receive any updates for the topic.

  WARNING: Unsubscribe is an async operation, but removing all handlers for a topic can only be done using the Promises API
  (due to the difficulty in distinguishing between a "handler" and a "callback" - they are both functions).
  If you need to know when unsubscribe has completed you must use await or .then on the return value from

   */

  def unsubscribe(topic:String,
                  handler:PubSubHandler,
                  callback:ErrorCallback = null):ErrorPromise= js.native



  /*
  pubsub.publish
  Publish a data message to a pubsub topic.

  ipfs.pubsub.publish(topic, data, [callback])
  topic: String
  data: Buffer - The message to send
  callback: (Error) => {} - (Optional) Calls back with an error or nothing if the publish was successful.
  If no callback is passed, a promise is returned.


   */

  def publish(topic:String,
              data:nodeLib.Buffer,
              callback:ErrorCallback=null):ErrorPromise = js.native

  /*
    pubsub.ls
    Returns the list of subscriptions the peer is subscribed to.

    ipfs.pubsub.ls([callback])
    callback: (Error, Array<string>) => {} - (Optional) Calls back with an error or a list of topicIDs that this peer is subscribed to.



   */

  def ls(cb:js.Function2[OrError,js.Array[String],Unit]=null):js.Promise[js.Array[String]]

  /*
  pubsub.peers
Returns the peers that are subscribed to one topic.

ipfs.pubsub.peers(topic, [callback])
topic: String
callback: (Error, Array<String>) => {} - (Optional) Calls back with an error or a list of peer IDs subscribed to the topic.
If no callback is passed, a promise is returned.
   */

  def peers(topic:String,callback:js.Function2[OrError,js.Array[String],Unit]=null):js.Promise[js.Array[String]]

}
@js.native
trait Pin extends js.Object {

  def add(s:String):js.Promise[js.Array[PinResult]] = js.native

}

@js.native
trait PinResult extends js.Object {

  val hash:String = js.native
  //val Pins:js.Array[String] = js.native
}

/*
${from: String, seqno: Buffer, data: Buffer, topicIDs: Array<String>}


 */

@js.native
trait PubSubMessage extends js.Object {
  val from:String = js.native
  val seqno:nodeLib.Buffer
  val data:nodeLib.Buffer
  val topicIDs:js.Array[String]
}

/*

options: Object - (Optional) Object containing the following properties:
discover: Boolean - (Default: false) Will use the DHT to find other peers.
 */

@js.native
trait PubSubOptions extends js.Object {
  val discover:Boolean = js.native

}


  /*
 {
 "version": "0.4.21",
 "commit": "",
  "repo": "7"
 }
   */
@js.native
  trait IpfsVersion extends js.Object {
    val version:js.UndefOr[String] = js.native
    val commit:js.UndefOr[String] = js.native
    val repo:js.UndefOr[String] = js.native

  }


@js.native
trait IpfsClient extends js.Object {

  val pubsub:PubSub = js.native
  val pin:Pin = js.native

  def add(content:nodeLib.Buffer,
          option:js.Dynamic,
          c: js.Function2[IPFSSError,js.Array[IPFSSFile],Unit]):Unit =
    js.native

  def version():js.Promise[IpfsVersion] = js.native

  @JSName("cat")
  def cat(s:String):js.Promise[nodeLib.Buffer] = js.native

  @JSName("cat")
  def catString(s:String):js.Promise[String] = js.native



}
