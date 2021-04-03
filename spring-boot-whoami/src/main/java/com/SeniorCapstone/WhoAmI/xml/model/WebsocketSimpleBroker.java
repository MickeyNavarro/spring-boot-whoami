//Almicke Navarro (with the mentoring of Isaiah Discipulo)
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.xml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="websocket-simple-broker")
public class WebsocketSimpleBroker {
	/* This class is used to define the prefix that is needed for the socket connection */ 
	
	//attribute
    private String prefix;
    
    //getter & setter
    @XmlAttribute(name="prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
