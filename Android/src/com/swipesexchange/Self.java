package com.swipesexchange;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Self {
	private SharedPreferences sharedPref;
	private String UID;
	private String FBID;
	/**
	 * @return the sharedPref
	 */
	public SharedPreferences getSharedPref() {
		return sharedPref;
	}
	/**
	 * @param sharedPref the sharedPref to set
	 */
	public void setSharedPref(SharedPreferences sharedPref) {
		this.sharedPref = sharedPref;
	}
	/**
	 * @return the uID
	 */
	public String getUID() {
		return UID;
	}
	/**
	 * @param uID the uID to set
	 */
	public void setUID(String uID) {
		UID = uID;
	}
	/**
	 * @return the fBID
	 */
	public String getFBID() {
		return FBID;
	}
	/**
	 * @param fBID the fBID to set
	 */
	public void setFBID(String fBID) {
		FBID = fBID;
	}
	

}
