//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//March 8, 2021 
//I used the source code from the following website:https://github.com/Artur-Wisniewski/minesweeper, https://github.com/kkedzierskim/chat-app
package com.SeniorCapstone.WhoAmI.model;

public class RoleMessage {
	//attribute to hold the different types of players
    public enum Role {
        OBSERVER, PLAYER1, PLAYER2;
    }
    
    //attributes 
    private Role role;
    private String sender;

    
    //getters & setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
