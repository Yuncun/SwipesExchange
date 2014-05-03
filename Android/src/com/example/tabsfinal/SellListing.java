package com.example.tabsfinal;

//TODO: Make price a string, and make changes on the server side code too.
//@author Eric April 28

public class SellListing extends Listing{

	private double price;
	
	public void setPrice(double price) {
		
		this.price = price;
	}
	
	public double getPrice() {
		return this.price;
		
	}
	
}
