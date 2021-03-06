package io.swipeswap.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.os.SystemClock;
import android.util.Log;

public class AccurateTimeHandler {
	/**
	 * This class provides accurate NTP time. It should be created on startup.
	 * 
	 * "Cache that value once and the value of SystemClock.elapsedRealtime() at the time you cached the NTP timestamp. Then make a method that just returns the NTP timestamp plus the difference between the elapsed millis now and when cached."
	 * 
	 */

	//TODO: This is not the best way of fixing our problem of times not lining up. 
	// Quest: Implement a better way to ensure that our times don't collide between different devices.
	// Hint: See if there is a way to use time received as a metric, so that time is relative to each device's system. However, this causes issues when we first pull ALL Messages
	
	private static long ntpTimeStampOnBoot;
	private static long systemElapsedTimeStamp;

	private static SntpClient client;
	
	public AccurateTimeHandler(){
		client = new SntpClient();
		
		//	Log.d("AccurateTimeHandler", "sntpCLient detected no network; stopping application");
		//	DisplayExceptionAlertDialog error = new DisplayExceptionAlertDialog();
		//	error.showAlert(this, "No Network Detected", true);
			client.requestTime();
		
		
	}
	
	public static void completeTimeRetrieval(Boolean sntpSuccess){
		if (sntpSuccess){
			ntpTimeStampOnBoot = client.getNtpTime();
		}
		else if (!sntpSuccess){
			ntpTimeStampOnBoot = Calendar.getInstance().getTimeInMillis();
			 Log.d("AccurateTimeHandler", "sntpSuccess - FAILED, using Calendar time");
		}
	        
	        systemElapsedTimeStamp  =  SystemClock.elapsedRealtime();
	
	        Log.d("AccurateTimeHandler", "Successful time retrieval: " + ntpTimeStampOnBoot + "...ntpTimeStamp on boot");
	        Log.d("AccurateTimeHandler", "Successful time retrieval: " + systemElapsedTimeStamp  + "...systemElapsedTime on boot");
	        
	        Calendar calendar = Calendar.getInstance();
	        try {
	        	//test that our time is valid
	            calendar.setTimeInMillis(ntpTimeStampOnBoot);
	            calendar.getTime();
	        } catch (Exception e) {
	            // TODO: handle exception
	            Log.d("AccurateTimeHandler", "No Response from NTP");
	            
	        }
		
	}
	
	public static long getAccurateTime(){
		/*
		 * Returns updated "universal" time in milliseconds
		 */
		long updatedSystemElapsedTime = SystemClock.elapsedRealtime();
		long time = ntpTimeStampOnBoot + (updatedSystemElapsedTime - systemElapsedTimeStamp);
		return time;
	}
	
	public static long getAccurateTime_adjustedForPST(){
		//This is kind of a hack to get the correct time to be displayed; Usually, you would get the ms time since epoch and then convert into timezone
		//However, since we ended up formatting our time using the Time object, the time zone conversion is FUBAR, so for now I'm jsut going to manually
		//subtract the PST/PDT offset from epoch time. This is terrible, but will work until I start using JodaTime.
		//PST is UTC-8, PDT is UTC-7
		long PDT_ms = 60000*60*7;
		long PST_ms = 60000*60*8;
		
		TimeZone tz = TimeZone.getTimeZone("EST");
		boolean inDs = tz.inDaylightTime(new Date());
		long PT;
		if (inDs){
			PT = PDT_ms;
		 //7 hrs
		}else{
			PT = PST_ms;
		}
		return (getAccurateTime()-PT);
	}
}
