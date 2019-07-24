import org.scalajs.core.tools.linker.ModuleInitializer

enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ScalablyTypedPlugin)


name := "gotcha"
version := "0.0.1"
scalaVersion := "2.12.8"

resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped")

// This is an application with a main method

/*
scalaJSModuleInitializers += ModuleInitializer
  .mainMethod(
    "com.lyrx.pyramids.frontend.Main"
    ,"init")
*/

scalaJSUseMainModuleInitializer := true



libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.7",
  "me.shadaj" %%% "slinky-web" % "0.1.0",
  "default" %%% "humbletokenizer" % "0.0.3",
  ScalablyTyped.R.react,
  ScalablyTyped.R.`react-dom`
)

npmDependencies in Compile ++= Seq(
  "react" -> "16.8",
  "react-dom" -> "16.8"


)

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
