package com.swipesexchange.sharedObjects;

import java.io.Serializable;

public class Venue implements Serializable{
	
	private String name;

	public Venue(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
