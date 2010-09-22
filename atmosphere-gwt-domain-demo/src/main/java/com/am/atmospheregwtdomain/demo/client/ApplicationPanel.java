package com.am.atmospheregwtdomain.demo.client;


import com.am.atmospheregwtdomain.client.AtmosphereGwtDomainManager;
import com.am.atmospheregwtdomain.client.domain.DomainFactory;
import com.am.atmospheregwtdomain.demo.shared.DemoServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * The application panel
 * @author Dev1
 */
public class ApplicationPanel extends Composite {

	/** Domain manager <br>
	 * When the manager is automatically injected, it is constructed and starts the {@link CometClient}.
	 * */
	@Inject private AtmosphereGwtDomainManager domainManager;
	
	/** The service to send the rpc calls */
	@Inject private DemoServiceAsync service;
	
	/////////////////////////////////////////////////
	
	/** 
	 * The main panel
	 */
	private Panel mainPanel;
	
	/**
	 * Constructor
	 */
	public ApplicationPanel() {
		
		//init widget
		mainPanel=new VerticalPanel();
		initWidget(mainPanel);
		
		//initialize the components
		mainPanel.add(initBroadcast());
		mainPanel.add(initDomain());
		mainPanel.add(initClient());
		
	}
	

	/**
	 * Initialize the broadcast panel
	 */
	private Widget initBroadcast() {
		
		//init components
		DisclosurePanel panel = new DisclosurePanel("Broadcast");
		Panel horizontalPanel = new HorizontalPanel();
		final TextBox textbox = new TextBox();
		Button button = new Button("broadcast text");
		
		//layout
		horizontalPanel.add(textbox);
		horizontalPanel.add(button);
		panel.add(horizontalPanel);
		
		//set panel opened
		panel.setOpen(true);
		
		//add listeners
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//send the text through rpc
				String textToSend = textbox.getText();
				if (!textToSend.trim().isEmpty()) {
					service.broadcast(textToSend, new DemoCallback());
				}
			}
		});
		
		return panel;
	}

	/**
	 * Initialize the domain panel
	 */
	private Widget initDomain() {

		//init components
		DisclosurePanel panel = new DisclosurePanel("Domain");
		Panel verticalPanel = new VerticalPanel();
		HorizontalPanel hPanelTop = new HorizontalPanel();
		Label l0 = new Label("register current client to domain: ");
		final TextBox registerDomain = new TextBox();
		Button register = new Button("register");
		
		HorizontalPanel hPanelBottom = new HorizontalPanel();
		Label l1 = new Label("send: ");
		final TextBox text = new TextBox();
		Label l2 = new Label(" to domain: ");
		final TextBox sendDomain = new TextBox();
		Button send = new Button("send");
		
		//layout
		hPanelTop.add(l0);
		hPanelTop.add(registerDomain);
		hPanelTop.add(register);
		verticalPanel.add(hPanelTop);
		
		hPanelBottom.add(l1);
		hPanelBottom.add(text);
		hPanelBottom.add(l2);
		hPanelBottom.add(sendDomain);
		hPanelBottom.add(send);
		verticalPanel.add(hPanelBottom);
		
		panel.add(verticalPanel);

		//set panel opened
		panel.setOpen(true);

		//add listeners
		register.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//register to the domain specified in the textbox
				String registerDomainText = registerDomain.getText();
				if (!registerDomainText.trim().isEmpty()) {
					domainManager.registerToDomain(DomainFactory.getDomain(registerDomainText));
				}
			}
		});
		
		send.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//send the specified text to the domain
				String textToSend = text.getText();
				String domainToSend = sendDomain.getText();
				if (!textToSend.trim().isEmpty() && !domainToSend.trim().isEmpty()) {
					service.sendToDomain(textToSend, DomainFactory.getDomain(domainToSend), new DemoCallback());
				}
			}
		});
		
		return panel;
	}

	/**
	 * Initialize the client panel
	 */
	private Widget initClient() {

		//init components
		DisclosurePanel panel = new DisclosurePanel("Client");
		HorizontalPanel hPanel= new HorizontalPanel();
		Label l1 = new Label("send: ");
		final TextBox text = new TextBox();
		Label l2 = new Label(" to the current client");
		Button send = new Button("send");
	
		//layout
		hPanel.add(l1);
		hPanel.add(text);
		hPanel.add(l2);
		hPanel.add(send);
		panel.add(hPanel);
		
		//set panel opened
		panel.setOpen(true);
		
		//add listeners
		send.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//send the text through rpc
				String textToSend = text.getText();
				if (!textToSend.trim().isEmpty()) {
					//in order to identify the client, we use a connection ID
					service.sendToClient(textToSend, domainManager.getConnectionID() ,new DemoCallback());
				}
			}
		});
		
		return panel;
		
	}
	
	/**
	 * Convenient call back for our call<br>
	 * Remember, the events are pushed from backend and caught in the handler registered on the {@link #eventBus}
	 * @author Dev1
	 */
	private class DemoCallback implements AsyncCallback<Void> {
		public void onSuccess(Void result) {
			//nothing
		}
		public void onFailure(Throwable caught) {
			Window.alert("oups: "+caught.getLocalizedMessage());
		}
	}
}
