package com.am.atmospheregwtdomain.client.event;

import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Singleton;

/**
 * The event bus
 * @author Dev1
 */
@Singleton
public class EventBus extends HandlerManager {

	/**
	 * Default constructor
	 */
	public EventBus() {
		super(null);
	}	
	
}
