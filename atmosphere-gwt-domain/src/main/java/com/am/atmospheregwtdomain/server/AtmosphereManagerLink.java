package com.am.atmospheregwtdomain.server;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.am.atmospheregwtdomain.client.CometClientInitializer;
import com.greencat.gwt.comet.client.CometClient;
import com.greencat.gwt.comet.server.GwtAtmosphereResource;
import com.greencat.gwt.comet.server.GwtAtmosphereServlet;

/**
 * Servlet called from {@link CometClient} to create the link with the back-end.
 * @see CometClientInitializer
 * @author Dev1
 */
public class AtmosphereManagerLink extends GwtAtmosphereServlet {

	private Log logger = LogFactory.getLog(AtmosphereManagerLink.class.getName());
	
    @Override
    public int doComet(GwtAtmosphereResource resource) throws ServletException, IOException {

        //get the atmosphere manager singleton
        AtmosphereManager atmosphereManager = new AtmosphereManagerCreator().getAtmosphereManager();

        //synchronize the resource and broadcaster
        atmosphereManager.initBroadcaster(resource.getAtmosphereResource());
        
        //keep and map the session id with the atmosphere resource
        int connectionId = (int) resource.getConnectionID();
        atmosphereManager.addClientResource(connectionId, resource);

        return super.doComet(resource);
    }
    
    @Override
	public void cometTerminated(GwtAtmosphereResource cometResponse, boolean serverInitiated) {
    	//TODO this method is not called on heartbeat failed, so we might be never aware of client disconnection.
    	//so for now, we just pass a recurrent thread in AtmosphereManager that checks the aliveness of resources to remove the mappings...
    	super.cometTerminated(cometResponse, serverInitiated);
    }

}