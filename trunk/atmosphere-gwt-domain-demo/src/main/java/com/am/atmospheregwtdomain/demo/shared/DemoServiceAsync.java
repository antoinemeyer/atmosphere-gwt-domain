package com.am.atmospheregwtdomain.demo.shared;

import com.am.atmospheregwtdomain.client.domain.Domain;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Demo service async
 * @author Dev1
 */
public interface DemoServiceAsync {

	void sendToDomain(String text, Domain domain, AsyncCallback<Void> callback);
	void broadcast(String text, AsyncCallback<Void> demoCallback);
	void sendToClient(String text, int connectionId, AsyncCallback<Void> demoCallback);

}
