package com.am.atmospheregwtdomain.client;

import java.io.Serializable;
import java.util.List;


import com.am.atmospheregwtdomain.client.event.ConnectedEvent;
import com.am.atmospheregwtdomain.client.event.EventBus;
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
	private EventBus handlerManager;
	
	@Inject
	public CometClientListener(EventBus handlerManager) {
		this.handlerManager = handlerManager;
	}
	
	@Override
	public void onConnected(int heartbeat, int connectionID) {
		Logger.debug("[CMCometListener] connected with connection id " + connectionID);
		handlerManager.fireEvent(new ConnectedEvent(heartbeat, connectionID));
	}

	@Override
	public void onDisconnected() {
		Logger.debug("[CMCometListener] disconnected");
	}

	@Override
	public void onError(Throwable exception, boolean connected) {
		Logger.error("[CMCometListener] error "+exception.getLocalizedMessage());
	}

	@Override
	public void onHeartbeat() {
	}

	@Override
	public void onRefresh() {
		Logger.debug("[CMCometListener] refresh");
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
