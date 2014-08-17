package com.swipesexchange.sharedObjects;


public class SellListing extends Listing{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * SellListing MUST BE IDENTICAL to the SellListing class in the clientSide code. 
	 * The communication between server and client passes serialized listing objects using objectOutputStream and ObjectInputStream
	 * 
	 * @Author Eric
	 */

	private double price;
	
	public void setPrice(double price) {
		
		this.price = price;
	}
	
	public double getPrice() {
		return this.price;
		
	}
	
}
