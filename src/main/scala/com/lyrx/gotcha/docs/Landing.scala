package com.lyrx.gotcha.docs

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
@react class Landing extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])


  def nav(nextOpt:Option[String])=p(

    nextOpt.map(next =>
    a(
    href:="#" ,
    className:="btn btn-light btn-icon-split",
    onClick:= (e=>PageOption.renderHash(props.pyramidOpt,next))
  )(
    span( className:="icon text-black-50")(
      i (className:="fas fa-arrow-right"),
      span (className:="text")(if(PageOption.isGerman()) "Weiter" else "Next"))))
      .getOrElse(span())





  )




  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Pyramids! "),
      h2()("Ein dezentrales Notariat und ein dezentrales Passamt"),
      p(className := "my-par")(
        "Diese Anwendung demonstriert beispielhaft, wie mit modernen P2P-Techologien" ,
          i(" (insbesondere mit Blockchain-Technologien) ") ,
          "Dokumente registriert und Identitäten verwaltet werden können."),
      p(className := "my-par")(
        "Folgender Funktionsumfang ist implementiert: "),
      ul( className:="my-list")(
        li(
           "Erstellen einer Identität",
             ul()(
             li("Persönliche Signaturen (digitale Unterschriften) erstellen"),
             li("Dokumente selbst verschlüsseln"),
               li("Einen digitalen Ausweis haben, der nicht an Institutionen gebunden ist.")
           )
        ),
        li("Verschlüsseln von Dokumenten"),
        li("Upload von Dokumenten in das dezentrale Netzwerk (IPFS)"),
        li("Registrieren von Dokumenten in der Blockchain (Zeitstempel)")
      ),
      p(className := "my-par")("Diese Anwendung benötigt ",
                               strong("keinen Server."),
                               "Sie basiert stattdessen vollständig auf ",
        strong("P2P-Netzwerken"),
        ", wodurch der Entwicklungsaufwand deutlich gesenkt wird."
      ),

      nav(nextOpt=Some("highavailability"))


    )

}
