package com.am.atmospheregwtdomain.client.event;

import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Singleton;

/**
 * The event bus used by {@link PushedEvent}s
 * @author Dev1
 */
@Singleton
public class AtmosphereGwtEventBus extends HandlerManager {

	/**
	 * Default constructor
	 */
	public AtmosphereGwtEventBus() {
		super(null);
	}	
	
}
