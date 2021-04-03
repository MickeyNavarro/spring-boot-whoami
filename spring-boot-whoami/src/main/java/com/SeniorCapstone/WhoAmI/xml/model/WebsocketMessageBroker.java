//Almicke Navarro (with the mentoring of Isaiah Discipulo)
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.xml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="websocket-message-broker")
public class WebsocketMessageBroker {
	/* This class is used to get the prefixes determined in the webSocketConfiguration.xml - this will help to determine the destination of the endpoints */
	
	//attributes
    private String applicationDestinationPrefix; 
    private WebsocketStompEndPoint websocketStompEndPoint;
    private List<WebsocketSimpleBroker> WebsocketSimpleBrokers  = new ArrayList<>();

    //getters & setters
    @XmlElements(@XmlElement(name="websocket-simple-broker"))
    public List<WebsocketSimpleBroker> getWebsocketSimpleBrokers() {
        return WebsocketSimpleBrokers;
    }

    public void setWebsocketSimpleBrokers(List<WebsocketSimpleBroker> websocketSimpleBrokers) {
        WebsocketSimpleBrokers = websocketSimpleBrokers;
    }

    @XmlElement(name="websocket-stomp-endpoint")
    public WebsocketStompEndPoint getWebsocketStompEndPoint() {
        return websocketStompEndPoint;
    }

    public void setWebsocketStompEndPoint(WebsocketStompEndPoint websocketStompEndPoint) {
        this.websocketStompEndPoint = websocketStompEndPoint;
    }

    @XmlAttribute(name="application-destination-prefix")
    public String getApplicationDestinationPrefix() {
        return applicationDestinationPrefix;
    }
    public void setApplicationDestinationPrefix(String applicationDestinationPrefix) {
        this.applicationDestinationPrefix = applicationDestinationPrefix;
    }
}
