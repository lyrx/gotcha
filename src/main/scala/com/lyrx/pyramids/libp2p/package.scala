package com.lyrx.pyramids

import com.lyrx.pyramids.libp2p.Typings.Protector

package object libp2p {

  implicit class PimpedProtector(p: Protector) {}

}
