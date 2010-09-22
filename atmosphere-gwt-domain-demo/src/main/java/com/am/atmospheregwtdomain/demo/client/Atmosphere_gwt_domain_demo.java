package com.am.atmospheregwtdomain.demo.client;


import com.am.atmospheregwtdomain.demo.shared.event.HelloEvent;
import com.am.atmospheregwtdomain.demo.shared.event.HelloEventHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entrypoint of the demo
 * @author Dev1
 */
public class Atmosphere_gwt_domain_demo implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		//init injector
		ApplicationInjector injector = GWT.create(ApplicationInjector.class);

		//create the application
		RootPanel.get().add(injector.getApplicationPanel());
		
		//register the listener triggered on the hello event on the event bus
		injector.getEventBus().addHandler(HelloEvent.getType(), new HelloEventHandler() {
			public void onEvent(HelloEvent helloEvent) {
				//we simply display the text in a popup
				PopupPanel popupPanel = new PopupPanel(true);
				popupPanel.setWidget(new Label(helloEvent.getText()));
				popupPanel.center();
			}
		});
	}

	
}
