package com.swipesexchange;

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
	private static int minuteSinceLastUpdate = new Time().minute;
	private static int secondSinceLastUpdate = new Time().second;
	
	private static boolean justSubmittedAListing = false;
	
	
	public static boolean shouldListBeUpdatedAgain()
	{
		Time timeNow = new Time();
		int minuteNow = timeNow.minute;
		int secondsNow = timeNow.second;
		
		if (justSubmittedAListing==true){
			Log.d("ListingUpdateTimer", "Updating because recent submission locally");
			justSubmittedAListing = false;
			minuteSinceLastUpdate = minuteNow;
			secondSinceLastUpdate = secondsNow;
			return true;
		}
		
		if (minuteNow >= minuteSinceLastUpdate){
			if (((minuteNow-minuteSinceLastUpdate == 1)&&((60-secondSinceLastUpdate)+secondsNow)<60) || (minuteNow-minuteSinceLastUpdate == 0)){
				//if the minute difference is only 1, check if 60 seconds has elapsed. 
				Log.d("ListingUpdateTimer", "Time elapsed between last update and now is under 60 second, update false");
				minuteSinceLastUpdate = minuteNow;
				secondSinceLastUpdate = secondsNow;
				return false;
			}
		}
		
		Log.d("ListingUpdateTimer", "Time elapsed is over a minute, update true");
		minuteSinceLastUpdate = minuteNow;
		secondSinceLastUpdate = secondsNow;
		return true;


	}
	
	public static void toggleJustSubmittedListing(){
		justSubmittedAListing = true;
	}

}
