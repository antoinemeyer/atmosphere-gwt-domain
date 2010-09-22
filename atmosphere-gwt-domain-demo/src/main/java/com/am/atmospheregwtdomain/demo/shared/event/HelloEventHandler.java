package com.am.atmospheregwtdomain.demo.shared.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * {@link HelloEvent} handler
 * @author Dev1
 */
public interface HelloEventHandler extends EventHandler {

	/**
	 * @param helloEvent
	 */
	void onEvent(HelloEvent helloEvent);

}
