package sharedObjects;


import java.io.Serializable;

public class User implements Serializable{
	
	private String name;
	private String idNumber;
	private String connections;
//	private List<Listing> listings;
	private String rating;
	
	public User(String name){
		this.name = name;
		this.idNumber = "undefined";
		this.connections = "undefined";
		this.rating = "undefined";
		
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getConnections() {
		return connections;
	}
	public void setConnections(String connections) {
		this.connections = connections;
	}
	/*
	public List<Listing> getListings() {
		return listings;
	}
	public void setListings(List<Listing> listings) {
		this.listings = listings;
	}
	*/
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}

	
	

}
