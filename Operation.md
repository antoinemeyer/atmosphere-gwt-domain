On injection of the AtmosphereGwtDomainManager on the client side, the API initiates an atmosphere connection between the client and the server via the AtmosphereManagerLink servlet (extending GwtAtmosphereServlet) (that you have to map in your web.xml (see [configuration](http://code.google.com/p/atmosphere-gwt-domain/wiki/Configuration)))<br>
The servlet adds a mapping in the AtmosphereManager (clientId -> atmosphereresource)<br>
<br>
The AtmosphereGwtDomainManager provides methods to register or unregister the client to/from a specific domain via RPC calls to AtmosphereGwtDomainManager (that you also have to map in your web.xml (see <a href='http://code.google.com/p/atmosphere-gwt-domain/wiki/Configuration'>configuration</a>))<br>
This service adds a mapping in the AtmosphereManager (domain -> clientIds)<br>
<br>
You can then use the AtmosphereManager to push the events to the clients.<br>
It uses a SimpleBroadcaster to push the events to the clients/domains you specified.<br>
These events are handled by the CometClientListener and redispatched on the AtmosphereGwtEventBus on the client-side.<br>
<br>
To send event to a specific client, you will need the connection id that you can obtain on the client-side from AtmosphereGwtDomainManager.<br>

<hr />

You can see an example of a simple application <a href='http://code.google.com/p/atmosphere-gwt-domain/source/browse/#svn/trunk/atmosphere-gwt-domain-demo'>here</a>