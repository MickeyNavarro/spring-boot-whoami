//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 7, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.model;

public class ChatMessage {

	//attribute to hold the different types of messages 
	public enum MessageType {
		CHAT, JOIN, LEAVE
	}

	//attributes
	private MessageType messageType;
	private String content;
	private String sender;

	//getters & setters
	public MessageType getType() {
		return messageType;
	}

	public void setType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
