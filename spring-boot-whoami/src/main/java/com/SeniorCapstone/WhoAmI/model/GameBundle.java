package com.SeniorCapstone.WhoAmI.model;

import java.util.List;

public class GameBundle {

	//attributes
	private int ID;
	private int size;
	private List<Image> images; 
	
	//default constructor 
	public GameBundle() {}
	
	public GameBundle(int size) {
		this.size = size; 
	}

	// getters & setters 
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}	
	
}
