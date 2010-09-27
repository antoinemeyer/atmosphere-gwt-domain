package com.am.atmospheregwtdomain.client.event;

import java.io.Serializable;



import com.am.atmospheregwtdomain.client.CometClientListener;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Extend this class in order to be able to push these events from the back-end to the client-side, they will be dispatched using {@link AtmosphereGwtEventBus} by {@link CometClientListener#onMessage(java.util.List)}
 * @author Dev1
 *
 * @param <H>
 */
public abstract class PushedEvent<H extends EventHandler> extends GwtEvent<H> implements Serializable {
}
