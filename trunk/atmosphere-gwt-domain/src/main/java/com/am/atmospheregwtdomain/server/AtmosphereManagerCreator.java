package com.am.atmospheregwtdomain.server;

/**
 * Factory to create and get the {@link AtmosphereManager} instance
 * @author Dev1
 */
public class AtmosphereManagerCreator {

	/** 
	 * Instance of the {@link AtmosphereManager} 
	 * */
	private static AtmosphereManager instance;
	
	/**
	 * Default constructor<br>
	 * Build the instance of {@link AtmosphereManager} if it is null
	 */
	public AtmosphereManagerCreator() {
		if (instance == null)
			instance = new AtmosphereManager();
	}
	
	/**
	 * Get the instance of the {@link AtmosphereManager}
	 * @return
	 */
	public AtmosphereManager getAtmosphereManager() {
		return instance;
	}
	
}
