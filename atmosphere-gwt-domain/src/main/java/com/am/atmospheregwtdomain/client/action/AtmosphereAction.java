package com.am.atmospheregwtdomain.client.action;


import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A convenient Action class containing the {@link #connectionId} required to identify the good client mapping for client-specific related events<br>
 */
public class AtmosphereAction implements IsSerializable {

	/** The connection id */
	private int connectionId;

	/**
	 * Default constructor
	 * @deprecated for serialization use only
	 */
	@Deprecated
	public AtmosphereAction() {
		super();
	}
	
	/**
	 * Constructor specifying the connection id
	 * @param connectionId
	 */
	public AtmosphereAction(int connectionId) {
		this.connectionId = connectionId;
	}


	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public int getConnectionId() {
		return connectionId;
	}
	
}
