package com.am.atmospheregwtdomain.client.util;

import com.google.gwt.core.client.GWT;

/**
 * Convenient class for logging
 * @author Dev1
 */
//TODO might be interesting to change the log system and use something like gwt-log 
public class Logger {

	public static void debug(String msg) {
		GWT.log(msg);
	}
	public static void error(String msg, Throwable e) {
		GWT.log(msg, e);
	}
	public static void error(String msg) {
		GWT.log(msg);
	}
	
}
