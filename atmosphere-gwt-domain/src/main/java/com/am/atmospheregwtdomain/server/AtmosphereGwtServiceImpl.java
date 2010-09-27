package com.am.atmospheregwtdomain.server;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.am.atmospheregwtdomain.client.action.RegisterDomainAction;
import com.am.atmospheregwtdomain.client.action.UnregisterDomainAction;
import com.am.atmospheregwtdomain.client.domain.Domain;
import com.am.atmospheregwtdomain.client.service.AtmosphereGwtService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of the RPC services.
 * @author Dev1
 */
public class AtmosphereGwtServiceImpl extends RemoteServiceServlet implements AtmosphereGwtService {

    private Log logger = LogFactory.getLog(AtmosphereGwtServiceImpl.class.getName());
	
	@Override
	public void registerDomains(RegisterDomainAction action) {
		
		//get the connection id
		int connectionId = action.getConnectionId();
		
		//register on the domains
		for (Domain domain : action.getDomains()) {
			new AtmosphereManagerCreator().getAtmosphereManager().addClientToDomain(connectionId, domain);
			logger.debug("[AtmosphereGwtServiceImpl] client with id "+connectionId+ " registered on domain "+domain.getName());
		}
		
	}

	@Override
	public void unregisterDomains(UnregisterDomainAction action) {
		
		//get the connection id
		int connectionId = action.getConnectionId();
		
		//unregister
		for (Domain domain : action.getDomains()) {
			new AtmosphereManagerCreator().getAtmosphereManager().removeClientFromDomain(connectionId, domain);
			logger.debug("[AtmosphereGwtServiceImpl] client with id "+connectionId+ " unregistered from domain "+domain.getName());
		}
		
	}
	
}
