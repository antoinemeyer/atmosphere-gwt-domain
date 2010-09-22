package com.am.atmospheregwtdomain.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.am.atmospheregwtdomain.client.action.RegisterDomainAction;
import com.am.atmospheregwtdomain.client.action.UnregisterDomainAction;
import com.am.atmospheregwtdomain.client.domain.Domain;
import com.am.atmospheregwtdomain.client.event.ConnectedEvent;
import com.am.atmospheregwtdomain.client.event.ConnectedHandler;
import com.am.atmospheregwtdomain.client.event.AtmosphereGwtEventBus;
import com.am.atmospheregwtdomain.client.service.AtmosphereGwtServiceAsync;
import com.am.atmospheregwtdomain.client.util.Logger;
import com.am.atmospheregwtdomain.server.ClientId;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.greencat.gwt.comet.client.CometClient;

/**
 * Main manager class on the client-side<br>
 * On injection: it instantiates {@link CometClientInitializer} to initiate the {@link CometClient} connection<br>
 * <br>
 * This class provides a bunch of utility class to register and unregister to and from domains:
 * <ul>
 * <li>{@link #registerToDomain(Domain)}</li>
 * <li>{@link #registerToDomains(List)}</li>
 * <li>{@link #registerToDomain(Domain, AsyncCallback)}</li>
 * <li>{@link #registerToDomains(List, AsyncCallback)}</li>
 * <li>{@link #unregisterFromDomain(Domain)}</li>
 * <li>{@link #unregisterFromDomains(List)}</li>
 * <li>{@link #unregisterFromDomain(Domain, AsyncCallback)}</li>
 * <li>{@link #unregisterFromDomains(List, AsyncCallback)}</li>
 * </ul>
 * <i>note that the domain registration can be performed on the application initialization, as the registration will be sent to the server only once the connection is established</i></br>
 * <br>
 * Also it always keeps uptodate the current {@link #connectionID} (that is used to generate the {@link ClientId} to identify a client on the server-side)<br>
 * @author Antoine Meyer
 */
@Singleton
public class AtmosphereGwtDomainManager {

	/** The connection ID */
	private int connectionID;
	
	/** The list of register actions stacked while the connection is not established */
	private Map<Domain, AsyncCallback<Void>> registerActionsToSend;

	/** The list of unregister actions stacked while the connection is not established */
	private Map<Domain, AsyncCallback<Void>> unregisterActionsToSend;

	//////////////
	//Injected dependencies
		
	/** the service for rpc calls */
	private AtmosphereGwtServiceAsync service;
	/** the event bus */
	private AtmosphereGwtEventBus eventbus;
	
	//////////////
	
	@Inject
	public AtmosphereGwtDomainManager(AtmosphereGwtServiceAsync service, AtmosphereGwtEventBus eventbuss, CometClientInitializer cometClientInitializer) {
		this.service=service;
		this.eventbus=eventbuss;
		eventbus.addHandler(ConnectedEvent.getType(), new ConnectedHandler() {
			@Override
			public void onEvent(ConnectedEvent connectedEvent) {
				//get the connection id
				connectionID = connectedEvent.getConnectionID();
				//register to the domains
				if (registerActionsToSend != null) {
					registerToDomains(new ArrayList<Domain>(registerActionsToSend.keySet()), new DeferredProcessCallback(registerActionsToSend, "Registration for multiple on connection"));
					registerActionsToSend = null;
				}
				//unregister to the domains
				if (unregisterActionsToSend != null) {
					unregisterFromDomains(new ArrayList<Domain>(unregisterActionsToSend.keySet()), new DeferredProcessCallback(unregisterActionsToSend, "Unregistration for multiple on connection"));
					unregisterActionsToSend = null;
				}
				//remove the listener
				eventbus.removeHandler(ConnectedEvent.getType(), this);
			}
		});
	}
 
	/**
	 * Callback propagating all the registered callbacks
	 * @author Dev1
	 */
	private class DeferredProcessCallback extends DomainCallBack {
		private Map<Domain, AsyncCallback<Void>> map;
		public DeferredProcessCallback(final Map<Domain, AsyncCallback<Void>> map, String msg) {
			super(msg);
			this.map = map;
		}
		public void onSuccess(Void result) {
			super.onSuccess(result);
			//trigger all the callbacks
			for (Domain domain : map.keySet()) {
				AsyncCallback<Void> asyncCallback = map.get(domain);
				if (asyncCallback != null)
					asyncCallback.onSuccess(result);
			}
		}
		public void onFailure(Throwable caught) {
			super.onFailure(caught);
			//trigger all the callbacks
			for (Domain domain : map.keySet()) {
				AsyncCallback<Void> asyncCallback = map.get(domain);
				if (asyncCallback != null)
					asyncCallback.onFailure(caught);
			}
		}
	}
	

	/**
	 * @return true if the connection has been established ({@link #connectionID} != 0)
	 */
	private boolean isConnectionEstablished() {
		return connectionID != 0;
	}
	
	/**
	 * @return the connectionID
	 */
	public int getConnectionID() {
		return connectionID;
	}
	
	//////////////////////////
	// Registrators
	//////////////////////////
	

	/**
	 * Stack the domain for registration
	 * @param domain
	 * @param callback
	 */
	private void stackRegister(Domain domain, AsyncCallback<Void> callback) {
		//we add the domain to the list of domains that will have to be registered on connection
		if (registerActionsToSend == null)
			registerActionsToSend = new HashMap<Domain, AsyncCallback<Void>>();
		registerActionsToSend.put(domain, callback);
	}
	
	/**
	 * Register to multiple domains
	 * @param domains
	 * @param callback
	 */
	public void registerToDomains(List<Domain> domains, AsyncCallback<Void> callback) {
		if (isConnectionEstablished()) {
			service.registerDomains(new RegisterDomainAction(connectionID, domains), callback);
		} else {
			//we just have one callback
			if (!domains.isEmpty()) {
				Iterator<Domain> iterator = domains.iterator();
				stackRegister(iterator.next(), callback);
				while (iterator.hasNext()) {
					stackRegister(iterator.next(), null);
				}
			}
		}
	}

	/**
	 * Register to multiple domains
	 * @param domains
	 */
	public void registerToDomains(List<Domain> domains) {
		registerToDomains(domains, new DomainCallBack("registration to multiple domains"));
	}

	/**
	 * Register to a specific domain
	 * @param domain
	 * @param callback
	 */
	public void registerToDomain(Domain domain, AsyncCallback<Void> callback) {
		if (isConnectionEstablished()) {
			service.registerDomains(new RegisterDomainAction(connectionID, Arrays.asList(new Domain[] {domain})), callback);
		} else {
			stackRegister(domain, callback);
		}
	}


	/**
	 * Register to a specific domain
	 * @param domain
	 */
	public void registerToDomain(Domain domain) {
		registerToDomain(domain, new DomainCallBack("registration to domain"+domain.getName()));
	}

	//////////////////////////
	// Unregistrators
	//////////////////////////

	/**
	 * Stack the domain for unregistration
	 * @param domain
	 * @param callback
	 */
	private void stackUnregister(Domain domain, AsyncCallback<Void> callback) {
		//we add the domain to the list of domains that will have to be registered on connection
		if (unregisterActionsToSend == null)
			unregisterActionsToSend = new HashMap<Domain, AsyncCallback<Void>>();
		unregisterActionsToSend.put(domain, callback);
	}
	
	/**
	 * Unregister from multiple domains
	 * @param domains
	 * @param callback
	 */
	public void unregisterFromDomains(List<Domain> domains, AsyncCallback<Void> callback) {
		if (isConnectionEstablished()) {
			service.unregisterDomains(new UnregisterDomainAction(connectionID, domains), callback);
		} else {
			//we just have one callback
			if (!domains.isEmpty()) {
				Iterator<Domain> iterator = domains.iterator();
				stackUnregister(iterator.next(), callback);
				while (iterator.hasNext()) {
					stackUnregister(iterator.next(), null);
				}
			}
		}
	}
	
	/**
	 * Unregister from multiple domains
	 * @param domains
	 */
	public void unregisterFromDomains(List<Domain> domains) {
		unregisterFromDomains(domains, new DomainCallBack("unregistration from multiple domains"));
	}
	
	/**
	 * Unregister from a specific domain
	 * @param domain
	 * @param callback
	 */
	public void unregisterFromDomain(Domain domain, AsyncCallback<Void> callback) {
		if (isConnectionEstablished()) {
			service.unregisterDomains(new UnregisterDomainAction(connectionID, Arrays.asList(new Domain[] {domain})), callback);
		} else {
			stackUnregister(domain, callback);
		}
	}
	
	/**
	 * Unregister from a specific domain
	 * @param domain
	 */
	public void unregisterFromDomain(Domain domain) {
		unregisterFromDomain(domain, new DomainCallBack("unregistration from domain"+domain.getName()));
	}
	
	////////////////////////////////////////////////////////////
	
	/**
	 * Convenient callback for {@link AtmosphereGwtDomainManager}
	 * @author Dev1
	 */
	private class DomainCallBack implements AsyncCallback<Void> {
		private String message;
		public DomainCallBack(String message) {
			this.message = message;
		}
		public void onSuccess(Void result) {
			Logger.debug("[AtmosphereGwtDomainManager] "+message+" successful.");
			//nothing
		}
		public void onFailure(Throwable caught) {
			Logger.error("[AtmosphereGwtDomainManager] "+message+" failed.", caught);
			caught.printStackTrace();
			Window.alert(caught.getLocalizedMessage());
		}
	}
	
}
