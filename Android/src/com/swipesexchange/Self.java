package com.swipesexchange;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Self {
	private static SharedPreferences sharedPref;
	private static String UID;
	private static String FBID;
	/**
	 * @return the sharedPref
	 */
	public static SharedPreferences getSharedPref() {
		return sharedPref;
	}
	/**
	 * @param sharedPref the sharedPref to set
	 */
	public static void setSharedPref(SharedPreferences sharedPrefs) {
		sharedPref = sharedPrefs;
	}
	/**
	 * @return the uID
	 */
	public static String getUID() {
		return UID;
	}
	/**
	 * @param uID the uID to set
	 */
	public static void setUID(String uID) {
		UID = uID;
	}
	/**
	 * @return the fBID
	 */
	public static String getFBID() {
		return FBID;
	}
	/**
	 * @param fBID the fBID to set
	 */
	public static void setFBID(String fBID) {
		FBID = fBID;
	}
	

}
