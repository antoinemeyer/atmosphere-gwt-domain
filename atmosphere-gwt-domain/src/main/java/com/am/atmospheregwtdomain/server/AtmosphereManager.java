package com.am.atmospheregwtdomain.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereEventLifecycle;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListener;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.Broadcaster.SCOPE;
import org.atmosphere.util.SimpleBroadcaster;

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

	private Log logger = LogFactory.getLog(getClass());
	
	/** The map of client-specific broadcasters */
	private Map<Integer, GwtAtmosphereResource> clientResources;
	/** The map of domain-specific broadcasters */
	private Map<Domain, Set<Integer>> domainBroadcasters;

	/** The broadcaster <br><br> <i>We use a simple broadcaster for now to guarantee the order of the messages sent</i> */
	private Broadcaster broadcaster = new SimpleBroadcaster();
	
	/** The {@link MappingCleaner} is scheduled to be started repeatedly on this interval */
	public static final int MAPPING_CLEANER_REPEATING_TIME = 60000;
	
	/**
	 * Constructor<br>
	 * Use {@link AtmosphereManagerCreator#getAtmosphereManager()}
	 * */
	AtmosphereManager() {

		// init the client resources map
		clientResources = Collections.synchronizedMap(new HashMap<Integer, GwtAtmosphereResource>());

		// init the domain resources map
		domainBroadcasters = Collections.synchronizedMap(new HashMap<Domain, Set<Integer>>());
		
		//launch the mapping cleaner
		new Timer(true).schedule(new MappingCleaner(), MAPPING_CLEANER_REPEATING_TIME, MAPPING_CLEANER_REPEATING_TIME);

	}

	/**
	 * @return {@link #broadcaster}
	 */
	private Broadcaster getBroadcaster() {
		if (broadcaster == null) {
			broadcaster = new SimpleBroadcaster();
			broadcaster.setScope(SCOPE.APPLICATION);
		}
		return broadcaster;
	}

	/**
	 * Specify the broadcaster to the resource.
	 * @param resource
	 */
	void initBroadcaster(AtmosphereResource<?, ?> resource) {
		resource.setBroadcaster(getBroadcaster());
	}

	/**
	 * Add a {@link AtmosphereResource} specific to a client
	 * 
	 * @param connectionId
	 * @param resource
	 */
	void addClientResource(final Integer connectionId, GwtAtmosphereResource resource) {
		logger.debug("[AtmosphereManager] add client "+resource);

		// add the client resource in the map
		clientResources.put(connectionId, resource);

		//add a listener on the resource 
		AtmosphereResource<?, ?> atmResource = resource.getAtmosphereResource();
		if (atmResource instanceof AtmosphereEventLifecycle) {
             AtmosphereEventLifecycle ael = (AtmosphereEventLifecycle)atmResource;
             ael.addEventListener(new AtmosphereResourceEventListener() {
				public void onSuspend(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
				}
				public void onResume(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
					logger.debug("[AtmosphereManager] "+connectionId+" resumed");
					//on resume, remove the mapping
					removeClient(connectionId);
				}
				public void onDisconnect(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
					logger.debug("[AtmosphereManager]  "+connectionId+" disconnected");
					//on disconnect, remove the mapping
					removeClient(connectionId);
				}
				public void onBroadcast(AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
					logger.trace("[AtmosphereManager] broadcasted "+event.getMessage()+" to "+connectionId);
				}
			});
         }
	}


	/**
	 * Removes the client identified by its connection id
	 * @param connectionId
	 */
	void removeClient(Integer connectionId) {
		removeClient(Arrays.asList(new Integer[] {connectionId}));
	}

	/**
	 * Remove the clients identified by their connection ids
	 * @param clientIds
	 */
	private void removeClient(List<Integer> clientIds) {

		//remove from the domains first
		synchronized (domainBroadcasters) {
			for (Domain domain : domainBroadcasters.keySet()) {
				Set<Integer> set = domainBroadcasters.get(domain);
				//remove all the ids
				for (Integer integer : clientIds) {
					set.remove(integer);
				}
				//if the set is empty, remove it from the map
				if (set.isEmpty())
					domainBroadcasters.remove(domain);
			}
		}

		//then remove from the map
		for (Integer integer : clientIds) {
			logger.debug("[AtmosphereManager] remove client "+integer);
			clientResources.remove(integer);
		}
		
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
		
		// get the set of the clients for this domain
		Set<Integer> set = domainBroadcasters.get(domain);
		
		//if it is not yet created
		if (set == null) {
			//we create it
			set = Collections.synchronizedSet(new HashSet<Integer>());
			//and put it in the map
			domainBroadcasters.put(domain, set);
		}

		// then add the client to the set
		set.add(connectionId);
		
	}

	/**
	 * Remove the client specified by the id from the specified domain
	 * @param domain
	 * @param connectionId
	 */
	public void removeClientFromDomain(Integer connectionId, Domain domain) {
		logger.debug("[AtmosphereManager] remove client "+connectionId+ " from "+domain.getName());
		
		// simply remove it if it exists
		Set<Integer> set = domainBroadcasters.get(domain);
		if (set != null)  {
			set.remove(connectionId);
		}
	
	}

	/**
	 * Checks whether a domain still contains clients listening or not
	 * @param domain
	 * @return
	 */
	public boolean isDomainActive(Domain domain) {
		
		// get the set of resources for this domain
		final Set<Integer> set;
		set = domainBroadcasters.get(domain);

		//if set is null, the domain is inactive
		if (set == null) 
			return false;

		// return true if the set is not empty
		return !set.isEmpty();
	}
	
	/**
	 * Get the resources attached to the specified domain
	 * @param domain
	 * @return
	 */
	private Set<AtmosphereResource<?,?>> getResourcesOfTheDomain(Domain domain) {
		
		//extract the atmosphere resources from the gwt resources
		Set<AtmosphereResource<?, ?>> returnSet = new HashSet<AtmosphereResource<?,?>>();
		if (domainBroadcasters.containsKey(domain)) {
			Set<Integer> set = domainBroadcasters.get(domain);
			synchronized (set) {
				for (Integer integer : set) {
					returnSet.add(clientResources.get(integer).getAtmosphereResource());
				}
			}
		}
		
		//then return it
		return returnSet;
	}
	
	/**
	 * Thread to clean the mappings for the dead resources<br>
	 * <i>Necessary until {@link AtmosphereManagerLink#cometTerminated(GwtAtmosphereResource, boolean)} is called everytime the resource is terminated)</i>
	 * @author Dev1
	 */
	private class MappingCleaner extends TimerTask {
		@Override
		public void run() {
			logger.debug("[AtmosphereManager.MappingClear] cleaning");
			
			//extract the ids we want to delete
			List<Integer> toDelete = new ArrayList<Integer>();
			synchronized (clientResources) {
				//for all the resources
				for (Integer next : clientResources.keySet()) {
					//check if the resource is dead
					//note that we dont actually check .isAlive() because it might be still alive even if the client is disconnected (!)
					//instead, we check the status of the writer
					if (clientResources.get(next).getResponseWriter().isTerminated()) {
						//add it to the remove list
						toDelete.add(next);
					} else {
						logger.trace("[AtmosphereManager.MappingClear] "+next+" is still alive");
					}
				}
			}
			//remove them
			removeClient(toDelete);
		}
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
		logger.debug("[AtmosphereManager] send event "+event+" to domain "+domain.getName());
		logFuture(getBroadcaster().broadcast(event, getResourcesOfTheDomain(domain)));
	}



	/**
	 * Send the specified event to the client identified by its client id
	 * @param connectionId
	 * @param event
	 */
	public <H extends EventHandler> void sendEventToClient(Integer connectionId, PushedEvent<H> event) {
		logger.debug("[AtmosphereManager] send event "+event+" to client "+connectionId);
		logFuture(getBroadcaster().broadcast(event, getClientResource(connectionId).getAtmosphereResource()));
	}
	
	/**
	 * Send the specified event to all the clients connected
	 * @param event
	 */
	public <H extends EventHandler> void sendEventToAll(PushedEvent<H> event) {
		logger.debug("[AtmosphereManager] send event "+event+" to all");
		logFuture(getBroadcaster().broadcast(event));
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Log the future if trace is enabled
	 * @param broadcast
	 */
	private void logFuture(Future<Object> broadcast) {
		if (logger.isTraceEnabled()) {
			new FutureLogger(broadcast).start();
		}		
	}
	
	/**
	 * Daemon thread to log the obtention of the future
	 * @author Dev1
	 */
	private class FutureLogger extends Thread {
		private Future<?> f;
		public FutureLogger(Future<?> f) {
			super();
			this.f = f;
			setDaemon(true);
		}
		@Override
		public void run() {
			try {
				logger.trace("[AtmosphereManager] future obtained "+f.get());
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (ExecutionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}


}
