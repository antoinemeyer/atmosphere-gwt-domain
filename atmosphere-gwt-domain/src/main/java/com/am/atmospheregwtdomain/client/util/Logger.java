package com.am.atmospheregwtdomain.client.util;

import com.google.gwt.core.client.GWT;

/**
 * Logging
 * @author Dev1
 */
//TODO change the log system, maybe use something like gwt-log 
public class Logger {

	public static void debug(String msg) {
		GWT.log(msg);
	}
	public static void error(String msg, Throwable e) {
		GWT.log(msg, e);
	}
	public static void error(String msg) {
		debug(msg);
	}
	
}
