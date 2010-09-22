package com.am.atmospheregwtdomain.client;

import com.am.atmospheregwtdomain.server.AtmosphereManagerLink;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.greencat.gwt.comet.client.CometClient;

/**
 * Initializer of the {@link CometClient}
 * <br>
 * <br>
 * <strong>Please note that you have to configure the servlet mapping for {@link AtmosphereManagerLink} accessed via the url {@link #ATMOSPHERE_LINK_URL}</strong><br>
 * Check the web.xml and atmosphere.xml of the demo project to see a configuration example
 * @author Dev1
 */
@Singleton
public class CometClientInitializer {

	/** 
	 * Url of the {@link AtmosphereManagerLink} servlet.
	 * */
	private static final String ATMOSPHERE_LINK_URL = GWT.getModuleBaseURL()+"atmosphere";
	
	/** The comet client */
	private CometClient cometClient;

	/**
	 * Default constructor<br>
	 * Initialize the comet client
	 */
	@Inject
	public CometClientInitializer(CometClientListener cometListener, EventSerializer serializer) {
		cometClient = new CometClient(ATMOSPHERE_LINK_URL, serializer, cometListener);
		cometClient.start();
	}

	public CometClient getCometClient() {
		return cometClient;
	}
	
}
