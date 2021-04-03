//Almicke Navarro (with the mentoring of Isaiah Discipulo) 
//CST-452
//January 12, 2021 
//I used the source code from the following website: https://github.com/MickeyNavarro/KaraokeMachine, https://github.com/Artur-Wisniewski/minesweeper
package com.SeniorCapstone.WhoAmI.model;

public class Bundle {

	//attributes
	Integer size; 
	Integer numOfImages; 
	String linkToImages []; 
	
	//default constructor 
	public Bundle() {}

	//getters & setters
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getNumOfImages() {
		return numOfImages;
	}

	public void setNumOfImages(Integer numOfImages) {
		this.numOfImages = numOfImages;
	}

	public String[] getLinkToImages() {
		return linkToImages;
	}

	public void setLinkToImages(String[] linkToImages) {
		this.linkToImages = linkToImages;
	}

	
}
