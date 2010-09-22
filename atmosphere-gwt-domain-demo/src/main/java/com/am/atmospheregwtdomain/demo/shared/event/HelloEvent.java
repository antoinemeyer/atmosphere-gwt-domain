package com.am.atmospheregwtdomain.demo.shared.event;

import com.am.atmospheregwtdomain.client.event.PushedEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * An event that can be pushed from backend to front end.
 * @author Dev1
 */
public class HelloEvent extends PushedEvent<HelloEventHandler> {

	/** The hello message to display */
	private String text;

	/**
	 * Default constructor
	 */
	public HelloEvent() {
		super();
	}
	
	/**
	 * Constructor specifying the message
	 * @param text
	 */
	public HelloEvent(String text) {
		this();
		this.text = text;
	}

	@Override
	protected void dispatch(HelloEventHandler arg0) {
		arg0.onEvent(this);
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	///////////////////////////////
	//TYPE
	///////////////////////////////


	/** Type */
	private static Type<HelloEventHandler> TYPE;

	/** Get the type - create it if null */
	public static Type<HelloEventHandler> getType() {
    	return TYPE != null ? TYPE : (TYPE = new Type<HelloEventHandler>());
    }
	@Override
	public Type<HelloEventHandler> getAssociatedType() {
		return getType();
	}


}
