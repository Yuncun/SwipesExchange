package sharedObjects;


import java.io.Serializable;

public class User implements Serializable{
	
	private String name;
	private String uid;
	private String regid;
	private String connections;
//	private List<Listing> listings;
	private String rating;
	
	public User(String name){
		this.name = name;
		this.uid = null;
		this.connections = null;
		this.rating = null;
		
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNumber() {
		return uid;
	}
	public void setIdNumber(String idNumber) {
		this.uid = idNumber;
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
	/**
	 * @return the regid
	 */
	public String getRegid() {
		return regid;
	}
	/**
	 * @param regid the regid to set
	 */
	public void setRegid(String regid) {
		this.regid = regid;
	}

	
	

}
