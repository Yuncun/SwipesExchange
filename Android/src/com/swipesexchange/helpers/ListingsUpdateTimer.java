package com.swipesexchange.helpers;

import java.util.Calendar;

import android.util.Log;

public class ListingsUpdateTimer {
	/**
	 * This is a class containing static functions that will check if it is time to update Listings. 
	 * Ostensibly, I have set the timer to 1 minute, meaning that if the Listing was refreshed under a minute ago, it will not refresh again
	 *  - If we have a recently submitted a llisting, this will not hold true, to ensure that listings are updated after we submit them
	 *  
	 *  This circumvents the nuisance of the half second wait every time we switch tabs
	 *  
	 */
	//{bl, sl}
	private static long[] timeOfLastUpdate = {Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis()};
	
	private static final long periodBetweenUpdatesMs = 1000*16; //16 seconds
	
	private static boolean justSubmittedAListing = false;
	
	private static boolean forceRepeatedUpdates_BL = false;
	private static boolean forceRepeatedUpdates_SL = false;
	
	public static boolean shouldListBeUpdatedAgain(int tabIndex)
	{
   	 
	   	 Calendar now = Calendar.getInstance();
	   	 long nowMs = now.getTimeInMillis();
		
		if (justSubmittedAListing==true){
			Log.d("ListingUpdateTimer", "Updating because recent submission locally");
			justSubmittedAListing = false;
			timeOfLastUpdate[tabIndex] = nowMs;
			return true;
		}
		
		if (nowMs <= periodBetweenUpdatesMs + timeOfLastUpdate[tabIndex]){
				Log.d("ListingUpdateTimer", "Time elapsed between last update and now is under 30 second, update false");
				return false;
			}
		else{
			Log.d("ListingUpdateTimer", "Time elapsed is over a minute, update true");
			timeOfLastUpdate[tabIndex] = nowMs;
			return true;
		}	
	}
	
	public static void resetUpdateCountdown(int tabindex){
		//Reset update countdown by adding period to our last update time
		 Calendar now = Calendar.getInstance();
	   	 long nowMs = now.getTimeInMillis();
		timeOfLastUpdate[tabindex] = nowMs;
	}
	
	public static void toggleJustSubmittedListing(){
		justSubmittedAListing = true;
	}

	/**
	 * @return the forceRepeatedUpdates_SL
	 */
	public static boolean isForceRepeatedUpdates_SL() {
		return forceRepeatedUpdates_SL;
	}

	/**
	 * @param forceRepeatedUpdates_SL the forceRepeatedUpdates_SL to set
	 */
	public static void setForceRepeatedUpdates_SL(boolean forceRepeatedUpdates_SL) {
		ListingsUpdateTimer.forceRepeatedUpdates_SL = forceRepeatedUpdates_SL;
	}

	/**
	 * @return the forceRepeatedUpdates_BL
	 */
	public static boolean isForceRepeatedUpdates_BL() {
		return forceRepeatedUpdates_BL;
	}

	/**
	 * @param forceRepeatedUpdates_BL the forceRepeatedUpdates_BL to set
	 */
	public static void setForceRepeatedUpdates_BL(boolean forceRepeatedUpdates_BL) {
		ListingsUpdateTimer.forceRepeatedUpdates_BL = forceRepeatedUpdates_BL;
	}

}