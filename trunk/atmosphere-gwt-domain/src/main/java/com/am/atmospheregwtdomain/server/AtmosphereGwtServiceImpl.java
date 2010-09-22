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
	
	/* (non-Javadoc)
	 * @see antoinemeyer.atmosphere.gwt.shared.service.AtmosphereGwtService#registerDomains(antoinemeyer.atmosphere.gwt.shared.action.RegisterDomainAction)
	 */
	@Override
	public void registerDomains(RegisterDomainAction action) {
		
		//get the client id
		ClientId clientId = getClientId(action.getConnectionId());
		
		//register on the domains
		for (Domain domain : action.getDomains()) {
			new AtmosphereManagerCreator().getAtmosphereManager().addClientToDomain(clientId, domain);
			logger.debug("[AtmosphereGwtServiceImpl] client with id "+clientId+ " registered on domain "+domain.getName());
		}
		
	}

	/* (non-Javadoc)
	 * @see antoinemeyer.atmosphere.gwt.shared.service.AtmosphereGwtService#unregisterDomains(antoinemeyer.atmosphere.gwt.shared.action.UnregisterDomainAction)
	 */
	@Override
	public void unregisterDomains(UnregisterDomainAction action) {
		
		//get the client id
		ClientId clientId = getClientId(action.getConnectionId());
		
		//unregister
		for (Domain domain : action.getDomains()) {
			new AtmosphereManagerCreator().getAtmosphereManager().removeClientFromDomain(clientId, domain);
			logger.debug("[AtmosphereGwtServiceImpl] client with id "+clientId+ " unregistered from domain "+domain.getName());
		}
		
	}
	
	/**
	 * @param connectionId
	 * @return the clientId from the connection id
	 */
	private ClientId getClientId(Integer connectionId) {
		logger.debug("[AtmosphereGwtServiceImpl] getting client id of connection id "+connectionId);
		return ClientId.getClientId(this.getThreadLocalRequest().getSession().getId(), connectionId);
	}

}
