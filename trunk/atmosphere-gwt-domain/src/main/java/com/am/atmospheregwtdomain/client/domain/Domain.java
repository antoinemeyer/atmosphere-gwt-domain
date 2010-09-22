package com.am.atmospheregwtdomain.client.domain;

import java.io.Serializable;


import com.am.atmospheregwtdomain.client.AtmosphereGwtDomainManager;
import com.greencat.gwt.comet.server.GwtAtmosphereResource;

/**
 * A {@link Domain} is the key of a mapping containing multiple {@link GwtAtmosphereResource} <br>
 * Basically, this is used to send an event to a specific group of clients<br>
 * Use the {@link AtmosphereGwtDomainManager} to register a client to a {@link Domain}.<br>
 * The name of the {@link Domain} should be unique to avoid collisions.
 * @author Dev1
 */
public interface Domain extends Serializable, Comparable<Domain> {
    
	/**
     * Return the name of the domain.
     * @return domain name
     */
    String getName();

}