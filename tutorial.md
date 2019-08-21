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
and will remain  at least
partly application specific. 

So  a huge effort is required to
set up and maintain client server interaction. The task will return with each
new web application. -- Of course, existing web frameworks can be 
used. But they often impose
new resistrictions and new problems. 

But the main drawback of the conventional approach 
is the amount of programming needed. The work load (and the cost) 
will decrease significantly, as soon as you find a solution that works with 
a browser and p2p networks only! 
Normally, with p2p networks you get a generic and ready to use communication
API. It will never be specific to your new application, but you will soon
learn that it can serve your needs very well. Also modern Javascript 
environments have evolved into a mighty infrastructure as well: They
offer an enormous number of additional libraries. 

Do not forget the portability issues! I our new world,
as there is only JavaScript on the client side, it will be very
easy to integrate our app into all sorts of already existing web content. 
There is no need to port or duplicate a complete infrastructure to 
a new location. It should just be enough to load existing Javascript modules
into a new HTML page and integrate it with the existing HTML.

**Let us start now!**

I want you to glide easily into these new programming paradigms. 





To make that happen as easily as possible, I used 


 
 
 