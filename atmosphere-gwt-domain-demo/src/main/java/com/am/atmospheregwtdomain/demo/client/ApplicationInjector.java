package com.am.atmospheregwtdomain.demo.client;

import com.am.atmospheregwtdomain.client.event.EventBus;
import com.google.gwt.inject.client.Ginjector;


/**
 * Injector for application demo
 * @author Dev1
 */
public interface ApplicationInjector extends Ginjector {

	/**
	 * @return the application panel
	 */
	ApplicationPanel getApplicationPanel();

	/**
	 * @return the event bus
	 */
	EventBus getEventBus();

}
