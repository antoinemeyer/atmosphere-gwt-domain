package com.am.atmospheregwtdomain.demo.shared;

import com.am.atmospheregwtdomain.client.domain.Domain;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Demo service
 * @author Dev1
 */
@RemoteServiceRelativePath("demo")
public interface DemoService extends RemoteService {

	void sendToDomain(String text, Domain domain);
	void broadcast(String text);
	void sendToClient(String text, int connectionId);
	
}
