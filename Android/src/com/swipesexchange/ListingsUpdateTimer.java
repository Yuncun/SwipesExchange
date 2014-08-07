package com.swipesexchange;

import java.util.Calendar;

import android.text.format.Time;
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
	private static long[] timeOfLastUpdate = {Calendar.getInstance().getTimeInMillis(), Calendar.getInstance().getTimeInMillis()};
	
	private static final long periodBetweenUpdatesMs = 1000*15; //30 seconds
	
	private static boolean justSubmittedAListing = false;
	
	
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
				//timeOfLastUpdate = nowMs;
				return false;
			}
		else{
			Log.d("ListingUpdateTimer", "Time elapsed is over a minute, update true");
			timeOfLastUpdate[tabIndex] = nowMs;
			return true;
		}	
	}
	
	public static void toggleJustSubmittedListing(){
		justSubmittedAListing = true;
	}

}