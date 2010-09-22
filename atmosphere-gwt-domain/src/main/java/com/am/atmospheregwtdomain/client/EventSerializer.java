package com.am.atmospheregwtdomain.client;

import com.am.atmospheregwtdomain.client.event.PushedEvent;
import com.greencat.gwt.comet.client.CometSerializer;
import com.greencat.gwt.comet.client.SerialTypes;

@SerialTypes(value = { PushedEvent.class })
public abstract class EventSerializer extends CometSerializer {
}
