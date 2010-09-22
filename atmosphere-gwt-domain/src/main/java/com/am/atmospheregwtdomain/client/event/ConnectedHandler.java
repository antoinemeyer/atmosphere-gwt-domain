package com.am.atmospheregwtdomain.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link ConnectedEvent}
 * @author Dev1
 */
public interface ConnectedHandler extends EventHandler {

    void onEvent(ConnectedEvent connectedEvent);
    
}
