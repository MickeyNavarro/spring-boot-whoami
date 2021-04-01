//Almicke Navarro 
//CST-323
//January 10, 2020 
//I used the source code from the following website: https://github.com/MickeyNavarro/KaraokeMachine 
package com.SeniorCapstone.WhoAmI.model;

public class Image {
	// attributes
	private int ID;
	private String name;
	private String url;

	// default constructor
	public Image() {
	}

	// non-default constructor
	public Image(int iD) {
		super();
		ID = iD;
	}

	// getters & setters
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
