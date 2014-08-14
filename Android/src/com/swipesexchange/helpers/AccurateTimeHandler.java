package com.swipesexchange.helpers;

import java.util.Calendar;

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
	
	private static final String NTP_HOST_NAME = "0.us.pool.ntp.org";
	
	public AccurateTimeHandler(){
		SntpClient client = new SntpClient();
		//String dateFromNtpServer = "";
		if (client.requestTime(NTP_HOST_NAME, 30000)) {
		                ntpTimeStampOnBoot = client.getNtpTime();
		                
		                systemElapsedTimeStamp  =  SystemClock.elapsedRealtime();

		                Log.d("AccurateTimeHandler", ntpTimeStampOnBoot + "...ntpTimeStamp on boot");
		                Log.d("AccurateTimeHandler", systemElapsedTimeStamp  + "...systemElapsedTime on boot");
		                
		                Calendar calendar = Calendar.getInstance();
		                try {
		                	//test that our time is valid
		                    calendar.setTimeInMillis(ntpTimeStampOnBoot);
		                    calendar.getTime();
		                } catch (Exception e) {
		                    // TODO: handle exception
		                    Log.wtf("AccurateTimeHandler", "No Response from NTP");
		                    
		                }

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
}
