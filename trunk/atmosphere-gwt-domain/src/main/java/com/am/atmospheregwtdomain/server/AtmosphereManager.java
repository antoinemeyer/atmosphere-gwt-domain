package com.am.atmospheregwtdomain.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.Broadcaster.SCOPE;

import com.am.atmospheregwtdomain.client.domain.Domain;
import com.am.atmospheregwtdomain.client.event.PushedEvent;
import com.google.gwt.event.shared.EventHandler;
import com.greencat.gwt.comet.server.GwtAtmosphereResource;

/**
 * Manager of the broadcasters.<br>
 * <br>
 * You can: <br>
 * <ul>
 * <li>register a client identified by its connectionId to a {@link Domain} with {@link #addClientToDomain(Integer, Domain)} </li>
 * <li>unregister a client identified by its connectionId from a {@link Domain} with {@link #removeClientFromDomain(Integer, Domain)} </li>
 * <li>check if a domain still has clients subscribed to it with {@link #isDomainActive(Domain)} </li>
 * <li>
 * Send an event to:
 * <ul>
 * <li>-Everyone (broadcast) with {@link #sendEventToAll(PushedEvent)}</li>
 * <li>-A {@link Domain} with {@link #sendEventToDomain(Domain, PushedEvent)}</li>
 * <li>-A specific client identified by its connectionId with {@link #sendEventToClient(Integer, PushedEvent)}</li>
 * </ul>
 * </li>
 * </ul>
 * @author Antoine Meyer
 */
public class AtmosphereManager {

    private Log logger = LogFactory.getLog(AtmosphereManager.class.getName());
	
	/** The map of client-specific broadcasters */
	private HashMap<Integer, GwtAtmosphereResource> clientResources;
	/** The map of domain-specific broadcasters */
	private HashMap<Domain, List<Integer>> domainBroadcasters;

	/**
	 * Constructor<br>
	 * Use {@link AtmosphereManagerCreator#getInstance()}
	 * */
	AtmosphereManager() {

		// init the client resources list
		clientResources = new HashMap<Integer, GwtAtmosphereResource>();

		// init the domain resources list
		domainBroadcasters = new HashMap<Domain, List<Integer>>();

	}

	/** identifier of the global broadcaster */
	private static final String GLOBAL_BROADCASTER = "GLOBAL_BROADCASTER";

	/**
	 * @return {@link #globalBroadcaster}
	 */
	private Broadcaster getBroadcaster() {
		return BroadcasterFactory.getDefault().lookup(Broadcaster.class, GLOBAL_BROADCASTER, true);
	}

	/**
	 * Initialize the global broadcaster
	 * 
	 * @param broadcaster
	 */
	void initBroadcaster(Broadcaster broadcaster) {
		broadcaster.setID(GLOBAL_BROADCASTER);
		broadcaster.setScope(SCOPE.APPLICATION);
	}

	/**
	 * Add a {@link AtmosphereResource} specific to a client
	 * 
	 * @param connectionId
	 * @param resource
	 */
	void addClientResource(Integer connectionId, GwtAtmosphereResource resource) {
		logger.debug("[AtmosphereManager] add client "+resource);
		// add the client resource in the map
		clientResources.put(connectionId, resource);
	}

	/**
	 * Get the {@link AtmosphereResource} of the specific client
	 * 
	 * @param connectionId
	 * @return
	 */
	private GwtAtmosphereResource getClientResource(Integer connectionId) {
		return clientResources.get(connectionId);
	}

	/**
  	 * Add a client to a domain
	 * @param connectionId
	 * @param domain
	 */
	public void addClientToDomain(Integer connectionId, Domain domain) {
		logger.debug("[AtmosphereManager] add client "+connectionId+ " to domain "+domain.getName());
		
		// get the list of the clients for this domain
		List<Integer> list = domainBroadcasters.get(domain);
		
		//if it is not yet created
		if (list == null) {
			//we create it
			list = new ArrayList<Integer>();
			//and put it in the map
			domainBroadcasters.put(domain, list);
		}

		// then add the client to the list
		list.add(connectionId);
	}

	/**
	 * Remove the client specified by the id from the specified domain
	 * @param domain
	 * @param connectionId
	 */
	public void removeClientFromDomain(Integer connectionId, Domain domain) {
		// simply remove it if it exists
		logger.debug("[AtmosphereManager] remove client "+connectionId+ " from "+domain.getName());
		List<Integer> list = domainBroadcasters.get(domain);
		if (list != null) 
			list.remove(connectionId);
	}

	/**
	 * Checks whether a domain still contains clients listening or not
	 * @param domain
	 * @return
	 */
	public boolean isDomainActive(Domain domain) {
		// get the list of resources for this domain
		final List<Integer> list = domainBroadcasters.get(domain);
		
		//if list is null, the domain is inactive
		if (list == null) 
			return false;

		// iterate over all the resources to remove the unactive resources
		for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();)
			if (!clientResources.get(iterator.next()).isAlive())
				iterator.remove();

		// return true if the list is not empty
		return !list.isEmpty();
	}
	
	/**
	 * Get the resources attached to the specified domain
	 * @param domain
	 * @return
	 */
	private Set<AtmosphereResource<?,?>> getResourcesOfTheDomain(Domain domain) {
		
		//extract the atmosphere resources from the gwt resources
		Set<AtmosphereResource<?, ?>> returnSet = new HashSet<AtmosphereResource<?,?>>();
		if (domainBroadcasters.containsKey(domain))
			for (Iterator<Integer> iterator = domainBroadcasters.get(domain).iterator(); iterator.hasNext();)
				returnSet.add(clientResources.get(iterator.next()).getAtmosphereResource());
		
		//then return it
		return returnSet;
	}

	//////////////////////////////////////////////////////////////////////////
	// pushers
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * Send the event to the specified domain
	 * @param domain
	 * @param event
	 */
	public <H extends EventHandler> void sendEventToDomain(Domain domain, PushedEvent<H> event) {
		logger.debug("[AtmosphereManager] send event "+event.getClass()+" to domain "+domain.getName());
		getBroadcaster().broadcast(event, getResourcesOfTheDomain(domain));
	}

	
	/**
	 * Send the specified event to the client identified by its client id
	 * @param connectionId
	 * @param event
	 */
	public <H extends EventHandler> void sendEventToClient(Integer connectionId, PushedEvent<H> event) {
		logger.debug("[AtmosphereManager] send event "+event.getClass()+" to client "+connectionId);
		getBroadcaster().broadcast(event, getClientResource(connectionId).getAtmosphereResource());
	}
	
	/**
	 * Send the specified event to all the clients connected
	 * @param event
	 */
	public <H extends EventHandler> void sendEventToAll(PushedEvent<H> event) {
		logger.debug("[AtmosphereManager] send event "+event.getClass()+" to all");
		getBroadcaster().broadcast(event);
	}


}
