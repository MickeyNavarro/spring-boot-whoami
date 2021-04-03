//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.SeniorCapstone.WhoAmI.xml.model.WebsocketMessageBroker;
import com.SeniorCapstone.WhoAmI.xml.model.WebsocketSimpleBroker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlConfigurationParser {
	WebsocketMessageBroker WebsocketMessageBrokerConfig;

	// method to receive & decipher the path; this will help determine what endpoint
	// is being talked to
	public XmlConfigurationParser(String path) throws JAXBException {
		File xmlFile = new File(path);
		JAXBContext jaxbContext = JAXBContext.newInstance(WebsocketMessageBroker.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		WebsocketMessageBrokerConfig = (WebsocketMessageBroker) jaxbUnmarshaller.unmarshal(xmlFile);

	}

	// method to get & return prefix (destination of where the call will go)
	public String getMessageBrokerApplicationDestinationPrefix() {
		return WebsocketMessageBrokerConfig.getApplicationDestinationPrefix();
	}

	// method to get & return the endpoint needed
	public String getStompEndPointPath() {
		return WebsocketMessageBrokerConfig.getWebsocketStompEndPoint().getPath();
	}

	// method to return all the prefixes for a certain endpoints
	public List<String> getSimpleBrokerPrefixs() {
		List<String> prefixs = new ArrayList<>();
		for (WebsocketSimpleBroker websocketSimpleBroker : WebsocketMessageBrokerConfig.getWebsocketSimpleBrokers())
			prefixs.add(websocketSimpleBroker.getPrefix());
		return prefixs;
	}
}
