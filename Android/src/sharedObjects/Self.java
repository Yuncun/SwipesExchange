package sharedObjects;

public class Self {
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
