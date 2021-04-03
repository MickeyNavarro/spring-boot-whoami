//Almicke Navarro (with the mentoring of Isaiah Discipulo)
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.SeniorCapstone.WhoAmI.xml.XmlConfigurationParser;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	  XmlConfigurationParser xmlConfig = new XmlConfigurationParser("src/main/resources/webSocketConfiguration.xml");
	
	  //constructor 
	  public WebSocketConfig() throws Exception {}
	
	  //method to create a new stomp endpoint for the socket connection 
	  @Override
	  public void registerStompEndpoints(StompEndpointRegistry registry) {
	    registry.addEndpoint(xmlConfig.getStompEndPointPath()).withSockJS();
	
	  }
	
	  //method to set the broker, the platform that will be used to process the communication between apps, in its designated endpoint
	  @Override
	  public void configureMessageBroker(MessageBrokerRegistry registry) {
	    registry.setApplicationDestinationPrefixes( xmlConfig.getMessageBrokerApplicationDestinationPrefix());
	    registry.enableSimpleBroker(xmlConfig.getSimpleBrokerPrefixs().get(0), xmlConfig.getSimpleBrokerPrefixs().get(1));
	  }
}
