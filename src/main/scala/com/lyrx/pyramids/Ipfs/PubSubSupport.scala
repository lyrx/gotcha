package com.lyrx.pyramids.Ipfs

import typings.nodeLib.bufferMod

import scala.concurrent.Future
import scala.scalajs.js

trait PubSubSupport {
  val ipfsClient: IpfsClient

  def pubsubPublish(topic: String, message: String): Future[OrError] =
    ipfsClient.pubsub.publish(topic, bufferMod.Buffer.from(message)).toFuture

  def pubsubSubscribe(topic: String, h: PubSubHandler): Future[OrError] =
    ipfsClient.pubsub.subscribe(topic, h).toFuture

  def pubsubUnsubscribe(topic: String, h: PubSubHandler): Future[OrError] =
    ipfsClient.pubsub.unsubscribe(topic, h).toFuture

  def pubsubLs(): Future[js.Array[String]] = ipfsClient.pubsub.ls().toFuture

  def pubsubPeers(topic: String): Future[js.Array[String]] =
    ipfsClient.pubsub.peers(topic).toFuture
}