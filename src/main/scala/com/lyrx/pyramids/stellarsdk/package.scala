package com.lyrx.pyramids
import typings.nodeLib
import nodeLib.bufferMod
import typings.bs58Lib.bs58Mod
import typings.nodeLib.fsMod.{PathLike, ^ => Fs}

package object stellarsdk {

  implicit class PimpedString(s: String) {

    val prefix = "1220"
    def split2() = ((s.substring(0, s.length / 2), s.substring(s.length / 2)))

    def encodebs58(): String = {
      bs58Mod.encode(bufferMod.Buffer.from(s).asInstanceOf[bufferMod.Buffer])
    }

    def decodebs58(): String = {
      bs58Mod.decode(s).toString("hex")
    }
    def forStellarMemo(): String = decodebs58().substring(4)

  }

  implicit class PimpedServer(override val server: Server) extends ServerTrait {}

  implicit class PimpedBuffer(b: nodeLib.Buffer) {

    def myToString() =
      b.toString("utf8") //new TextDecoder().decode(new Uint8Array(js.Array(b)))

  }

  def readFileFuture(path: PathLike, encoding: String = "utf8") = {
    val p = concurrent.Promise[nodeLib.Buffer]
    Fs.readFile(path, (e, b) => {
      if (e == null)
        p.success(b)
      else p.failure(new Throwable(e.toString))
      ()
    })
    p.future
  }

  def readFileFutureString(path: PathLike)(
      implicit executionContext: concurrent.ExecutionContext) =
    readFileFuture(path).map(_.myToString())

}
