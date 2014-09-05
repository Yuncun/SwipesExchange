package com.swipesexchange.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.swipesexchange.main.MainActivity;

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
		calCreated.setTimeInMillis(AccurateTimeHandler.getAccurateTime());
		Date timeNowDate = calCreated.getTime();
		//timeNowDate is current time
		//endDate is date when listing should expire
		
		long differenceMs = endDate.getTime() - timeNowDate.getTime();
		long differenceMin = differenceMs/(1000*60);
		long differenceHr = differenceMin / 60 + (differenceMin % 60 == 0 ? 0 : 1);
	//	Log.d("long differenceHr = differenceMin / 60 + (differenceMin % 60 == 0 ? 0 : 1);", "Differencemin = " + Long.toString(differenceMin) + " and endDate was " + endDate.getTime() + " and nowDate is " + timeNowDate.getTime());
		
		String re = "<" + Long.toString(differenceHr) + "h";
		
		if (differenceHr < 0){
			re = "EXP";
		}
		return re;
		

	}
	
	public static Calendar convertToPST(Calendar cal) {

		//PST = -8hr
		//PDT = -7hr
		long PST_ms = 60000*60*8;
		long PDT_ms = 60000*60*7;
		
	//	log.debug("Created GMT cal with date [" + gmtCal.getTime() + "]");
//		long timezoneAlteredTime = cal.getTimeInMillis()+ TimeZone.getTimeZone("PDT").getRawOffset();
		Calendar cSchedStartCal1 = Calendar.getInstance(TimeZone.getTimeZone("PDT"));
		cSchedStartCal1.setTimeInMillis(cal.getTimeInMillis() - PDT_ms);
		return cSchedStartCal1;
	}
	
	
   public static String getTimeText(String date_str) {
    	
    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";
    	final String NEW_FORMAT = "MMM dd, hh:mm aaa";

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
   
	 public static int fixHours(int hours_24)
	 {
		 if (hours_24 == 0)
			 return 12;
		 else if(hours_24 <= 12)
			 return hours_24;
		 else
		 {
			return hours_24 - 12;
			 
		 }
	 }
	 
	 public static boolean isPM(int hours_24)
	 {
		 if (hours_24>=12)
			 return true;
		 
		 return false;
	 }
	

}

