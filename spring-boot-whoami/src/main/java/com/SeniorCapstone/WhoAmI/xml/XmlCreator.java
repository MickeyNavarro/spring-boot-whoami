//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class XmlCreator {
	// method to create a new xml file
	public static <T> void createXml(String path, String nameFile, T toXml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance((Class) toXml);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(toXml, new File(path + "/" + nameFile));
	}
}
