package com.lyrx.pyramids.ipfs

import scala.scalajs.js

import js.Dynamic.{literal => l}

/*
   https://github.com/orbitdb/orbit-db/blob/master/examples/browser/example.js
 */

object PeerSupport {

  /*
    const ipfs = new Ipfs({
    repo: '/orbitdb/examples/browser/new/ipfs/0.33.1',
    start: true,
    preload: {
      enabled: false
    },
    EXPERIMENTAL: {
      pubsub: true,
    },
    config: {
      Addresses: {
        Swarm: [
          // Use IPFS dev signal server
          // '/dns4/star-signal.cloud.ipfs.team/wss/p2p-webrtc-star',
          '/dns4/ws-star.discovery.libp2p.io/tcp/443/wss/p2p-websocket-star',
          // Use local signal server
          // '/ip4/0.0.0.0/tcp/9090/wss/p2p-webrtc-star',
        ]
      },
    }



   */

  def orbitDB() =
    new Ipfs(
      l(
        "repo" -> "/orbitdb/examples/browser/new/ipfs/0.33.1",
        "start" -> true,
        "preload" -> l(
          "enabled" -> false
        ),
        "EXPERIMENTAL" -> l(
          "pubsub" -> true
        ),
        "config" -> l(
          "Addresses" -> js.Array(
            // Use IPFS dev signal server
            // '/dns4/star-signal.cloud.ipfs.team/wss/p2p-webrtc-star',
            "/dns4/ws-star.discovery.libp2p.io/tcp/443/wss/p2p-websocket-star",
            // Use local signal server
            // '/ip4/0.0.0.0/tcp/9090/wss/p2p-webrtc-star',
          )
        )
      ))

}
