package io.swipeswap.sharedObjects;

public class Self {
	public final static int max_listings_allowed = 5;
	private static User user;
	/**
	 * @return the myuser
	 */
	
	public static User getUser() {
		return user;
	}

	/**
	 * @param myuser the myuser to set
	 */
	public static void setUser(User myuser) {
		Self.user = myuser;
	}
	
	
	

}
