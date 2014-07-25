package com.swipesexchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.text.format.DateFormat;
import android.util.Log;

public class StaticHelpers {
	
	public static String figureOutExpirationTime(String timeCreated, String endTime) throws ParseException{
				/*
				 * Figure out how much time is left until expiry of listing, based on listing creation time and listing end time
				 * We will receive both times in "Old Format". Returns a simplified string to represent time, like ">2.5 hrs"
				 */
				final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";
				
				SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
				Date dateCreated = sdf.parse(timeCreated);
				Date endDate = sdf.parse(endTime);
				
				Calendar calCreated = Calendar.getInstance();
				calCreated.setTime(dateCreated);
				
				Calendar calEnded = Calendar.getInstance();
				calEnded.setTime(endDate);

				
				int endDateHours = calEnded.get(Calendar.HOUR_OF_DAY);
				int dateCreatedHours = calCreated.get(Calendar.HOUR_OF_DAY);
				int endDateMins = calEnded.get(Calendar.MINUTE);
				int dateCreatedMins = calCreated.get(Calendar.MINUTE);
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

				Log.d("figureoutime: ", ">"+Integer.toString(diffHours)+simple_diffmins+"h");
				return ">"+Integer.toString(diffHours)+simple_diffmins+"h";
		
		

	}
	
   public static String getTimeText(String date_str) {
    	
    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";
    	final String NEW_FORMAT = "EEE, MMM dd, hh:mm aaa";

    	String oldDateString = date_str;
    	String newDateString;

    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    	Date d = null;
		try {
			d = sdf.parse(oldDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return date_str;
		}
    	sdf.applyPattern(NEW_FORMAT);
    	newDateString = sdf.format(d);
    	
        return newDateString;
    }

}