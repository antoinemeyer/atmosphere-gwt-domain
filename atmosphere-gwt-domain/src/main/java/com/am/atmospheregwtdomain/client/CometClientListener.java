package com.am.atmospheregwtdomain.client;

import java.io.Serializable;
import java.util.List;


import com.am.atmospheregwtdomain.client.event.ConnectedEvent;
import com.am.atmospheregwtdomain.client.event.AtmosphereGwtEventBus;
import com.am.atmospheregwtdomain.client.util.Logger;
import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.greencat.gwt.comet.client.CometClient;
import com.greencat.gwt.comet.client.CometListener;

/**
 * Listener used by the {@link CometClient}
 */
public class CometClientListener implements CometListener {

	/** The event bus manager to propagate the events received in the messages */
	private AtmosphereGwtEventBus handlerManager;
	
	@Inject
	public CometClientListener(AtmosphereGwtEventBus handlerManager) {
		this.handlerManager = handlerManager;
	}
	
	@Override
	public void onConnected(int heartbeat, int connectionID) {
		Logger.debug("[CometClientListener] connected with connection id " + connectionID);
		handlerManager.fireEvent(new ConnectedEvent(heartbeat, connectionID));
	}

	@Override
	public void onDisconnected() {
		Logger.debug("[CometClientListener] disconnected");
	}

	@Override
	public void onError(Throwable exception, boolean connected) {
		Logger.error("[CometClientListener] error "+exception.getLocalizedMessage());
	}

	@Override
	public void onHeartbeat() {
	}

	@Override
	public void onRefresh() {
		Logger.debug("[CometClientListener] refresh");
	}

	@Override
	public void onMessage(List<? extends Serializable> messages) {
		// for all the messages intercepted
		for (Serializable serializable : messages) {
			// if it is an event
			if (serializable instanceof GwtEvent<?>) {
				//propagate it
				handlerManager.fireEvent((GwtEvent<?>) serializable);
			}
		}
	}

}
