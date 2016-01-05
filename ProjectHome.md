### Notice: project not maintained anymore, please check the core atmosphere project. ###

API inspired by [gwteventservice](http://code.google.com/p/gwteventservice/) to provide high level functionalities to [atmosphere-gwt-comet](http://code.google.com/p/atmosphere-gwt-comet/)

## What does it do? ##

The API initiates a connection between the client and the server via atmosphere.

The server can then push gwt events through this connection via a SimpleBroadcaster using 3 differents ways:
  * broadcast: all the clients connected to the server will receive the event
  * domain: only the clients previously registered to the domain will receive the event
  * client: only the specified client will receive the event.

When the event is pushed to the client-side, it is dispatched using an event bus (extending HandlerManager)
<br><br>
<b>You can see a complete example <a href='http://code.google.com/p/atmosphere-gwt-domain/source/browse/#svn/trunk/atmosphere-gwt-domain-demo'>here</a> or check the <a href='http://code.google.com/p/atmosphere-gwt-domain/wiki/Configuration'>wiki</a></b>

<hr />

current version: v0.1.2<br>
using atmosphere-gwt-comet: v0.1.7<br>
using atmosphere: v0.6.2<br>
<br>
<hr />