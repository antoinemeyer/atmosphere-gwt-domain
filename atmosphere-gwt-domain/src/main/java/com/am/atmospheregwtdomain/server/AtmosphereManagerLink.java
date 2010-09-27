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
    public void doComet(GwtAtmosphereResource resource) throws ServletException, IOException {
        super.doComet(resource);

        //get the atmosphere manager singleton
        AtmosphereManager atmosphereManager = new AtmosphereManagerCreator().getAtmosphereManager();
        	
        //init the broadcaster of the resource as global
        atmosphereManager.initBroadcaster(resource.getBroadcaster());
        
        //keep and map the session id with the atmosphere resource
        int connectionId = (int) resource.getConnectionID();
        atmosphereManager.addClientResource(connectionId, resource);

        logger.debug("[AtmosphereManagerLink] resource "+resource+" registered with id : "+connectionId);

    }
    
    

}