package com.am.atmospheregwtdomain.demo.server;


import com.am.atmospheregwtdomain.client.domain.Domain;
import com.am.atmospheregwtdomain.demo.shared.DemoService;
import com.am.atmospheregwtdomain.demo.shared.event.HelloEvent;
import com.am.atmospheregwtdomain.server.AtmosphereManager;
import com.am.atmospheregwtdomain.server.AtmosphereManagerCreator;
import com.am.atmospheregwtdomain.server.ClientId;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of the {@link DemoService}
 * @author Dev1
 */
public class DemoServiceImpl extends RemoteServiceServlet implements DemoService {

	@Override
	public void sendToDomain(String text, Domain domain) {
		
		//get the instance of the manager
		AtmosphereManager atmosphereManager = new AtmosphereManagerCreator().getAtmosphereManager();
		
		//send an event to the domain
		atmosphereManager.sendEventToDomain(domain, new HelloEvent(text));
		
	}
	
	@Override
	public void broadcast(String text) {

		//get the instance of the manager
		AtmosphereManager atmosphereManager = new AtmosphereManagerCreator().getAtmosphereManager();
		
		//send an event to the domain
		atmosphereManager.sendEventToAll(new HelloEvent(text));
		
	}

	@Override
	public void sendToClient(String text, int connectionId) {

		//get the instance of the manager
		AtmosphereManager atmosphereManager = new AtmosphereManagerCreator().getAtmosphereManager();

		//get the client id from the manager with the session id and the connection id
		ClientId clientId = ClientId.getClientId(this.getThreadLocalRequest().getSession().getId(), connectionId);
		
		//with this client id, we can send a user specific event
		atmosphereManager.sendEventToClient(clientId, new HelloEvent(text));
		
	}

}
