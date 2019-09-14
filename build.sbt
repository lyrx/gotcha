import org.scalajs.core.tools.linker.ModuleInitializer

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ScalablyTypedPlugin)

name := "gotcha"
version := "0.0.1"
scalaVersion := "2.12.8"

resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped")

scalaJSUseMainModuleInitializer := true

scalacOptions += "-P:scalajs:sjsDefinedByDefault"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.7",
  "me.shadaj" %%% "slinky-core" % "0.6.2",
  "me.shadaj" %%% "slinky-web" % "0.6.2",
  "me.shadaj" %%% "slinky-native" % "0.6.2",
  "me.shadaj" %%% "slinky-hot" % "0.6.2",
  "me.shadaj" %%% "slinky-scalajsreact-interop" % "0.6.2",
  ScalablyTyped.J.jquery,
  ScalablyTyped.J.jszip,
  ScalablyTyped.F.`file-saver`,
  ScalablyTyped.W.`web3`,
  ScalablyTyped.B.`bs58`

)


npmDependencies in Compile ++= Seq(
  "react" -> "16.8", "react-dom" -> "16.8",
  "web3" -> "1.0.0-beta.55",
  "ipfs-http-client" -> "32.0.1",
  "ipfs" -> "0.37.0",
  "buffer" -> "3.5.5",
  "jszip" -> "3.2.1",
  "file-saver" -> "2.0",
  "bs58" -> "4.0.1",
  "stellar-sdk" -> "2.3.0"
  ,"libp2p" -> "0.26.1"
)




// optional, but recommended; enables the @react macro annotation API
addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

testFrameworks += new TestFramework("utest.runner.Framework")

lazy val copyjs = TaskKey[Unit]("copyjs", "Copy bundle files to websapp")
copyjs := {
  val outDir = new File("src/main/webapp/js")
  val inDir = new File(s"target/scala-2.12/scalajs-bundler/main")
  val files = Seq(
    s"${name.value}-opt-bundle.js",
    s"${name.value}-opt-bundle.js.map"
  ) map { p =>
    (inDir / p, outDir / p)
  }
  IO.copy(files, true)
}

addCommandAlias("mywebpack", ";fullOptJS::webpack;copyjs")
