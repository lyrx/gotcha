package com.lyrx.gotcha.docs

import com.lyrx.gotcha.MyComponents.pageHeading
import com.lyrx.pyramids.Pyramid
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class LandingEN extends StatelessComponent {
  case class Props(pyramidOpt: Option[Pyramid])

  def render(): ReactElement =
    div(className := "container-fluid", id := "pyramid-root")(
      pageHeading("Pyramids! "),
      h2()("A Decentralized Notary And Passport Office"),
      p(className := "my-par")(
        "This application demonstrates, how P2P Techology" ,
          i(" (especially blockchain technology) ") ,
          "can be used to register documents and manage identities."),
      p(className := "my-par")(
        "The following is implemented: "),
      ul( className:="my-list")(
        li(
           "Register your Identity",
             ul()(
             li("Create personal digital signatures"),
             li("Encrypt without external tools"),
               li("Create a digital passport, not involving any state or authority whatsoever")
           )
        ),
        li("Encrypt and decrypt documents"),
        li("Upload (encrypted) documents to the P2P network IPFS"),
        li("Register documents the  blockchain (Stellar)")
      ),
      p(className := "my-par")("This application needs ",
                               strong("NO SERVER! "),
                               "It is completely based on  ",
        strong("P2P networks"),
        ", severely lowering development and maintenance costs."
      )

    )

}
