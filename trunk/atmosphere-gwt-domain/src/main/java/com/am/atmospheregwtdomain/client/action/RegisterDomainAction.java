package com.am.atmospheregwtdomain.client.action;

import java.util.List;

import com.am.atmospheregwtdomain.client.AtmosphereGwtDomainManager;
import com.am.atmospheregwtdomain.client.domain.Domain;


/**
 * Action used to register to a domain<br>
 * please use the {@link AtmosphereGwtDomainManager}
 * @author Dev1
 */
public class RegisterDomainAction extends AtmosphereDomainAction {

	/**
	 * Default constructor
	 */
	public RegisterDomainAction() {
		super();
	}
	
	/**
	 * Constructor
	 * @param connectionId
	 * @param domains
	 */
	public RegisterDomainAction(int connectionId, List<Domain> domains) {
		super(connectionId, domains);
	}
	
}
