package com.swipesexchange.network;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.swipesexchange.R;
import com.swipesexchange.main.MainActivity;
import com.swipesexchange.messaging.ConversationList;
import com.swipesexchange.sharedObjects.Message;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

	public class GcmIntentService extends IntentService {
	    public static final int NOTIFICATION_ID = 1;
	    private Context mContext; 
	    NotificationCompat.Builder builder;

	    public GcmIntentService() {
	        super("GcmIntentService");
	    }
	    
	    public void setContext(Context context)
	    {
	    	mContext = context;
	    }

	    @Override
	    protected void onHandleIntent(Intent intent) {
	        Bundle extras = intent.getExtras();
	        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
	        // The getMessageType() intent parameter must be the intent you received
	        // in your BroadcastReceiver.
	        String messageType = gcm.getMessageType(intent);
	        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
	            /*
	             * Filter messages based on message type. Since it is likely that GCM
	             * will be extended in the future with new message types, just ignore
	             * any message types you're not interested in, or that you don't
	             * recognize.
	             */
	            if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
	                sendNotification("Send error: " + extras.toString());
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_DELETED.equals(messageType)) {
	                sendNotification("Deleted messages on server: " +
	                        extras.toString());
	            // If it's a regular GCM message, do some work.
	            } else if (GoogleCloudMessaging.
	                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
	                // This loop represents the service doing some work.
	                for (int i=0; i<5; i++) {
	                    Log.i("LOUD AND CLEAR", "Working... " + (i+1)
	                            + "/5 @ " + SystemClock.elapsedRealtime());
	                    try {
	                        Thread.sleep(5000);
	                    } catch (InterruptedException e) {
	                    }
	                }
	                Log.i("LOUD AND CLEAR", "Completed work @ " + SystemClock.elapsedRealtime());
	                // Post notification of received message.
	                String gsondMessage = extras.getString("message");
	                sendNotification(gsondMessage);
	                updateLocal(gsondMessage);
	            }
	        }
	        // Release the wake lock provided by the WakefulBroadcastReceiver.
	        GcmBroadcastReceiver.completeWakefulIntent(intent);
	    }
	    
	    private void updateLocal(String msg)
	    {
	    	
	    	Gson gson = new Gson();
	    	Message received = gson.fromJson(msg, Message.class);
	    	String payload = received.getText();
	    	String senderName = received.getSender().getName();
	    	Log.d("GCM", "Updating local message lists with message " + payload + "from user" + senderName);
	    	ConversationList.addMessage(received);
    	    Intent bIntent = new Intent("message_received");
    	    // You can also include some extra data.
    	    bIntent.putExtra("message", received.getText());
    	    LocalBroadcastManager.getInstance(mContext).sendBroadcast(bIntent);
	    	
	    }
	    
	    
	    
	    

	    // Put the message into a notification and post it.
	    // This is just one simple example of what you might choose to do with
	    // a GCM message.
	    private void sendNotification(String msg) {
	    	Log.d("GCM", "Sending a new notification");
	    	Gson gson = new Gson();
	    	Message received = gson.fromJson(msg, Message.class);
	    	String payload = received.getText();
	    	String senderName = received.getSender().getName();
	    	/*
	        mNotificationManager = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);

	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, MainActivity.class), 0);

	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.icon)
	        .setContentTitle("GCM Notification")
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setContentText(msg);

	        mBuilder.setContentIntent(contentIntent);
	        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	        */
	    	NotificationCompat.Builder mBuilder =
	    	        new NotificationCompat.Builder(this)
	    	        .setSmallIcon(R.drawable.icon)
	    	        .setContentTitle(payload)
	    	        .setContentText("New Message from " + senderName);
	    	// Creates an explicit intent for an Activity in your app
	    	Intent resultIntent = new Intent(this, MainActivity.class);

	    	// The stack builder object will contain an artificial back stack for the
	    	// started Activity.
	    	// This ensures that navigating backward from the Activity leads out of
	    	// your application to the Home screen.
	    	//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	    	//stackBuilder.addParentStack(MainActivity.class);
	    	//stackBuilder.addNextIntent(resultIntent);
	    	PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    	/*PendingIntent resultPendingIntent =
	    	        stackBuilder.getPendingIntent(
	    	            0,
	    	            PendingIntent.FLAG_UPDATE_CURRENT
	    	        );*/
	    	mBuilder.setContentIntent(resultPendingIntent);
	    	NotificationManager mNotificationManager =
	    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    	// mId allows you to update the notification later on.
	    	int mId = 0;
	    	mNotificationManager.notify(mId, mBuilder.build());
	    }
}


