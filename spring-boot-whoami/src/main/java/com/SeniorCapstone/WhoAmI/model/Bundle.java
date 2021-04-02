package com.SeniorCapstone.WhoAmI.model;

import java.util.List;

public class Bundle {

	//attributes
	Integer size; 
	Integer numOfImages; 
	String linkToImages []; 
	
	//default constructor 
	public Bundle() {}

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
