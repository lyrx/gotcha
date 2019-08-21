# Pyramids! The Tutorial #

## The Environment ##

This is an *SPA*, a *single page application*, which means that
there is only one single [HTML-Page](src/main/webapp/index.html)
loaded inside the browser. After loading the Javascript
embedded inside the page, 
any dynamic content and all application 
controls are generated  by Javascript.

The app runs **inside the browser**. So there is no server backend, except
gateways giving access to p2p networks like IPFS or Stellar. The fact
that there is no server backend cannot overestimated, as the advantages
coming from this are really enourmous. 

There is no need to set up and maintain a server infrastructure. Today
many huge sites work exactly the opposite way: Huge application servers
are providing the needed application logic. There are often numerous 
databases involved, plenty of backend services that can interact with
each other in complex ways.

The client server communication in such conventional web applications is never easy. 
There always need to 
be  one ore more protocols defining that communication process, and it
can never be completely standardized. So it will remain  at least
partly application specific. So it is a huge effort to
maintain the client server interaction, and the task returns with each
new web application. Of course, existing web frameworks can be 
used to get over that problem. But those frameworks often impose
other resistrictions and other problems. 

The main drawback of the conventional approach to web applications
is the amount of programming that needs to be done. The work (and the cost) 
isgreatly reduced, as soon as you find a solution that works with 
browser applications and p2p networks only! 
Normally, with p2p networks you get a generic and ready to use communication
API. It will never be specific to your new application, but you will soon
learn that  




    

As there is only JavaScript on the client side, it will be very
easy to integrate the app into all sorts of already existing web content.
There is no need to port or duplicate a complete infrastructure to 
a new location. It should just be enough to load existing Javascript modules
into a new HTML page and integrate it with the existing HTML.

To make that happen as easily as possible, I used 


 
 
 