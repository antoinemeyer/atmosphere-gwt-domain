package com.am.atmospheregwtdomain.client.service;

import com.am.atmospheregwtdomain.client.action.RegisterDomainAction;
import com.am.atmospheregwtdomain.client.action.UnregisterDomainAction;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("atmospheregwtservice")
public interface AtmosphereGwtService extends RemoteService {

	public void registerDomains(RegisterDomainAction action);

	public void unregisterDomains(UnregisterDomainAction action);

}
