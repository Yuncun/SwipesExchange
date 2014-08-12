package com.swipesexchange.helpers;

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
	//	Log.d("timeCreated is ", "TIME CREATED : " + timeCreated + " and TIME ENDED :  "+ endTime);
		SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
		//Date dateCreated = sdf.parse(timeCreated);
		Date endDate = sdf.parse(endTime);
		
		Calendar calCreated = Calendar.getInstance();
		Date timeNowDate = calCreated.getTime();
		//timeNowDate is current time
		//endDate is date when listing should expire
		
		long differenceMs = endDate.getTime() - timeNowDate.getTime();
		long differenceMin = differenceMs/(1000*60);
		long differenceHr = differenceMin / 60 + (differenceMin % 60 == 0 ? 0 : 1);
	//	Log.d("long differenceHr = differenceMin / 60 + (differenceMin % 60 == 0 ? 0 : 1);", "Differencemin = " + Long.toString(differenceMin) + " and endDate was " + endDate.getTime() + " and nowDate is " + timeNowDate.getTime());
		
		return "<" + Long.toString(differenceHr) + "h";
		

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