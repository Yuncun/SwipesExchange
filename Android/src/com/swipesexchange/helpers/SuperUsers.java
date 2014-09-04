package com.swipesexchange.helpers;

public class SuperUsers {
	
	//SuperUsers contains list of users with Administrative power
	

	//KDP
	public static Boolean checkSuperUser(String fbid){
		String KDP_fbid = "10152553393776844";
		String Yuncun_fbid = "10152153150921342";
		
		if (fbid.equals(KDP_fbid) || fbid.equals(Yuncun_fbid))
			return true;
		else return false;
			
		
	}

}
