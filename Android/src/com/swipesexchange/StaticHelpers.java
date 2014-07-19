package com.swipesexchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.text.format.DateFormat;
import android.util.Log;

public class StaticHelpers {
	
	public static String figureOutExpirationTime(String timeCreated, String endTime) throws ParseException{
		/**
		 * Figure out how much time is left until expiry of listing, based on listing creation time and listing end time
		 * We will receive both times in "Old Format". Returns a simplified string to represent time, like ">2.5 hrs"
		 */
		final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";
		
		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		Date dateCreated = sdf.parse(timeCreated);
		Date endDate = sdf.parse(endTime);
		
		
		int endDateHours = endDate.getHours();
		int dateCreatedHours = dateCreated.getHours();
		int endDateMins = endDate.getMinutes();
		int dateCreatedMins = dateCreated.getMinutes();
		if (endDateHours<dateCreatedHours || (endDateHours==dateCreatedHours && endDateMins < dateCreatedMins)){
			//We picked a time "before", so we need to add 24 hrs to the difference
			endDateHours+=24;
		}
		
		int diffHours = endDateHours - dateCreatedHours;
		int diffMins = endDateMins - dateCreatedMins;
		if (diffMins<0)
			diffMins+=60;
		
		String simple_diffmins = "";
		
		if (diffMins > 30){
			simple_diffmins = ".5";
		}

		Log.d("figureoutime: ", ">"+Integer.toString(diffHours)+simple_diffmins+"hrs");
		return ">"+Integer.toString(diffHours)+simple_diffmins+"hrs";
		
		
		/*
		long duration  = dateCreated.getTime() - endDate.getTime();

		int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
		Log.d("figureOutExpirationTime", "The difference between selected EndTime and timeCreated is " + Integer.toString(diffInMinutes) + " minutes");
		
		int simple_hours_diff = diffInMinutes/60;
		int simple_minutes_diff = diffInMinutes % 60;
		
		String simple_fraction_for_minutes = "";
		if (simple_minutes_diff > 30){
			//If minutes remainder is more than 30, approximate to a .5, otherwise let it be
			simple_fraction_for_minutes = ".5";
		}*/

	}

}