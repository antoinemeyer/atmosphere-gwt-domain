package com.am.atmospheregwtdomain.client.action;

import java.util.List;

import com.am.atmospheregwtdomain.client.AtmosphereGwtDomainManager;
import com.am.atmospheregwtdomain.client.domain.Domain;


/**
 * Action used to unregister from a domain<br>
 * use the {@link AtmosphereGwtDomainManager}
 * @author Dev1
 */
public class UnregisterDomainAction extends AtmosphereDomainAction {

	/**
	 * Default constructor
	 */
	public UnregisterDomainAction() {
		super();
	}

	/**
	 * Constructor
	 * @param connectionId
	 * @param domains
	 */
	public UnregisterDomainAction(int connectionId, List<Domain> domains) {
		super(connectionId, domains);
	}

	
}
