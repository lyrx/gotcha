package com.lyrx.pyramids

import org.scalajs.dom.raw.File


case class Upload(f:File,hash:String)

case class IpfsData(
                     regOpt:Option[String],
                     uploads:Seq[Upload] ,
                     pharaoOpt:Option[String],
                     identityOpt:Option[Registration]
                   )
