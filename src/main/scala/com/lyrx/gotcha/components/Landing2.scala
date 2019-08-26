package com.lyrx.gotcha.components

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class Landing2 extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])


  def nav(previous:String,next:String)= PageOption
    .navLinks(previous,next)(props.pyramidOpt)



  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Pyramids!"),
      h2()("Hochverfügbarkeit und ",i()("\"censorship resistance\"")),
      p(className := "my-par")(
        "Aus dem P2P-Ansatz ergeben sich einige frappierende Eigenschaften dieser Anwendung, ",
        "die wohl nicht sofort offensichtlich sind." ,
          ),
      p(className := "my-par")(
        "Da die "
        ,strong("gesamte "),
        "Anwendungslogik in den Browser verlagert ist, kann die Anwendung prinzipiell von ",
        "jedem Webserver zur Verfügung gestellt werden, der ",
        strong("statische"),
        ", also unveränderliche Inhalte zur Verfügung stellen kann. Ein solcher Server benötigt ",
        "nicht mehr die Infrastruktur eines Applikationsservers, wie er bis dato üblich ist.",
        "Generell kann gesagt werden, dass die Zeit der Applikationsserver zu Ende geht. Es gibt ",
        "einen deutlichen Trend, die Programmierlogik in den Browser, also auf die Seite des Anwenders ",
        "zu verlagern."
      ),
      nav(previous = "landing",next="")

    )

}
