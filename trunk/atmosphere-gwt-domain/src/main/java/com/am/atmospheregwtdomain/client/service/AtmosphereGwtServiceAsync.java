package com.am.atmospheregwtdomain.client.service;


import com.am.atmospheregwtdomain.client.action.RegisterDomainAction;
import com.am.atmospheregwtdomain.client.action.UnregisterDomainAction;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of {@link AtmosphereGwtService}
 * @author Dev1
 */
public interface AtmosphereGwtServiceAsync {

	void registerDomains(RegisterDomainAction action, AsyncCallback<Void> callback);
	void unregisterDomains(UnregisterDomainAction action, AsyncCallback<Void> callback);

}
