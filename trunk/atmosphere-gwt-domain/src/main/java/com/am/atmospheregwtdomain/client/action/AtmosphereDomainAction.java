package com.am.atmospheregwtdomain.client.action;

import java.util.List;

import com.am.atmospheregwtdomain.client.domain.Domain;

/**
 * Convenient class for actions taken upon {@link Domain}s <br>
 * Like {@link RegisterDomainAction} or {@link UnregisterDomainAction}
 */
public class AtmosphereDomainAction extends AtmosphereAction {

	private List<Domain> domains;
	
	/**
	 * Default constructor
	 * @deprecated for serialization use only
	 */
	@Deprecated
	public AtmosphereDomainAction() {
		super();
	}
	
	public AtmosphereDomainAction(int connectionId, List<Domain> domains) {
		super(connectionId);
		this.domains = domains;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

}
