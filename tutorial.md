# Pyramids! The Tutorial #

## Overview ##

This is an *SPA*, a *single page application*, which means that
there is only one single [HTML-Page](src/main/webapp/index.html)
loaded inside the browser. After loading the Javascript
embedded inside the page, 
any dynamic content and all application 
controls are generated  by Javascript.

The app runs **inside the browser**. So there is no server backend, except
gateways giving access to p2p networks like IPFS or Stellar. The fact
that there is no server backend cannot overestimated, as the advantages
coming from this are really big. 

There will no longer be the need to set up and maintain a server infrastructure. Today
many huge sites work exactly the opposite way: Huge application servers
are providing the needed application logic. Often there are numerous 
databases involved, and plenty of backend services are interacting with
each other in complex ways.

Also, the client server communication in such conventional web applications is never easy. 
There is always the need to 
have one ore more protocols defining that communication process.  These
can never be completely standardized.
They will remain  at least
partly application specific. 

So  a huge effort is required to
set up and maintain client server interaction. The task will return with each
new web application. -- Of course, existing web frameworks can be 
used. But they often impose
new resistrictions and new problems. 

But the main drawback of the conventional approach 
is the amount of programming needed. The work load (and the cost) 
will decrease significantly as soon as you find a solution that works with 
a browser and p2p networks only! 
Normally, with p2p networks you get a generic and ready to use communication
API. It will never be specific to your new application, but you will soon
learn that it can serve your needs very well. Also modern Javascript 
environments have evolved into a mighty infrastructure as well: They
offer an enormous number of additional libraries. 

Do not forget the portability issues! In our new world,
as there is only JavaScript on the client side, it will be very
easy to integrate our app into all sorts of already existing web content. 
There is no need to port or duplicate a complete infrastructure to 
a new location. It should just be enough to load existing Javascript modules
into a new HTML page and integrate it with the existing HTML.

## Let us start now! ##

I want you to glide easily into these new programming paradigms. 
To make that happen, let us set up everything from scratch, so that
you will get the chance to learn about one new framework with each 
new step you do. 

So we will not look at the code inside this application, but instead set up 
a new and blank project and then gradually add the features currently
implemented in this project.
As soon as you will be  familiar with the basics, you 
will be ready to return to this place. 

So, when you went through the tutorial, please note: I did my best to reach a good level in programming, 
but I am very sure that I am far from arriving at the optimum. This project has been
a learning project for me as well, and I tried to improve my skills in functional programming
and web programming as much as I could. I will be very happy if you profit from
this as I could profit from it, and it will make me even happier if your feedback 
helps me to improve even more.

## SBT ##

We will set this up as a Scala project, using the build tool [SBT](https://www.scala-sbt.org/).
Do not worry about SBT! It is a complex tool, although is is based on few (but very abstract)
concepts. Though this tool is powerful, you will often have a hard time trying to accomplish 
easy tasks with it. Luckily you need to do use the tool mainly during project set up.
For new requirements, it is often enough to do a Google search and 
simply copy/paste the stuff you need.. Let us
hope that google can find us the needed code snippets
and  leave the details to SBT experts.

Three files need to be created, and we will copy the to our new project:
 
- In [project/build.properties](project/build.properties) 
  you just write the sbt version: `sbt.version = 0.13.17`
- In [project/plugins.sbt](project/plugins.sbt) you configure the plugins 
  used by SBT
  - `sbt-scala-js` configures the build process, mainly 
     compiling your Scala code into Javascript. 
     It is the most important plugin.
  - `sbt-scalajs-bundler` is used to bundle other dependencies,
     mainly node.js dependencies, together with the compilation
     results from      
  - `sbt-scalablytyped` is an interesting new plugin that enables
    usage of thousands of node.js modules in Scala. With this the
    whole ecosystem of node.js is opening up for Scala developers
   
- [build.sbt](build.sbt) is the most important file, because here
  the complete build process is configured. (In our case, when 
  copying it to our new projects, which just change the artifact id
  *"gotcha"* into something else, say "incredibleindia")
  
## Start Hacking ##

Let us start by printing *"Hello World*. 

We create a new Scala object
as defined in
 [src/test/scala/com/lyrx/examples/Main.scala](src/test/scala/com/lyrx/examples/Main.scala).

For that purpose, we write such a file into our  repository
[IncredibleIndia](https://github.com/deshbandhumishra/euroindian) --- where
we have also put, the sbt files from the previous chapter.

For best results and usability, we will use Intellij as  our development environment. But
of course, you will be able to do everything with another development environment, if
you feel more comfortable with it.

Now this is the right moment to try out if the project (containing only one single Scala file)
can be compiled by Intellij.

Please note: You have to set up the project by using `File -> New -> Project from existing source ...`
and configure it as a project using SBT. This should setup everything correctly, but I do not recommend
using Intellij to work as anm interface  to SBT. Instead, you should just use Intellij to write 
Scala code, compile it (into bytecode), and also to run tests. You can of course also run SBT from
Intellij, but I do not do the on a regular basis, as I feel more comfortable with the command line.

So, for now, let us just try to compile our Scala file the way we would normally do that, if it were
a project targeted to the Java virtual machine. In the next chapter, we will go on and 
introduce the basics of Scala.JS



    





 

 
 
 