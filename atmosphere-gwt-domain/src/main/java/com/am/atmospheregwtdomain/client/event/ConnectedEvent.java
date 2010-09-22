package com.am.atmospheregwtdomain.client.event;

import com.am.atmospheregwtdomain.client.AtmosphereGwtDomainManager;

/**
 * Event sent when the {@link AtmosphereGwtDomainManager} is connected.
 * @author Dev1
 */
public class ConnectedEvent extends PushedEvent<ConnectedHandler> {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 4210089717876545663L;

	private int hearbeat;
	private int connectionID;
	
	/**
	 * Default constructor
	 */
	public ConnectedEvent() {
		super();
	}
	
	public ConnectedEvent(int heartbeat, int connectionID) {
		super();
		this.hearbeat = heartbeat;
		this.connectionID = connectionID;
	}

	@Override
	protected void dispatch(ConnectedHandler arg0) {
		arg0.onEvent(this);
	}
	
	public int getHearbeat() {
		return hearbeat;
	}
	
	public void setHearbeat(int hearbeat) {
		this.hearbeat = hearbeat;
	}
	
	public int getConnectionID() {
		return connectionID;
	}
	
	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}
	
	///////////////////////////////
	//TYPE
	///////////////////////////////


	/** Type */
	private static Type<ConnectedHandler> TYPE;

	/** Get the type - create it if null */
	public static Type<ConnectedHandler> getType() {
    	return TYPE != null ? TYPE : (TYPE = new Type<ConnectedHandler>());
    }
	@Override
	public Type<ConnectedHandler> getAssociatedType() {
		return getType();
	}

}
