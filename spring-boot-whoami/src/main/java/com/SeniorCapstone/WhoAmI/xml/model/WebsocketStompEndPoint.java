//Almicke Navarro (with the mentoring of Isaiah Discipulo)
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.xml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="websocket-stomp-endpoint")
public class WebsocketStompEndPoint {
	/* This class is used to just determine the stomp endpoint in the socket connection */ 
	
	//attribute
    private String path;

    //getter & setter 
    @XmlAttribute(name="path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
