package com.swipesexchange.messaging;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.swipesexchange.R;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.sharedObjects.Message;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class ConversationFragment extends ListFragment {

	private ArrayList<Message> passed_messages;
	ConversationAdapter adapter;
	
	public ConversationFragment() {
		this.passed_messages = null;
	}
	
	public ConversationFragment(ArrayList<Message> l) {
		this.passed_messages = l;
	
	}
	
	  private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
		     // Get extra data included in the Intent
		     String message = intent.getStringExtra("message");
		     updateFragmentWithNoMessage();
		     ClosedInfo.setReceivedMessage(true);
		     
		     MediaPlayer player = new MediaPlayer();
		     AssetFileDescriptor afd = null;
			try {
				afd = context.getAssets().openFd("message.mp3");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		     try {
				player.setDataSource(afd.getFileDescriptor());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		     try {
				player.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		     player.start();
		     
		     Log.d("zebra", "Got message: " + message);
		   }
		 };

	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.conversation_fragment_list, container, false);
         getActivity().getActionBar().show();
         LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
   		      new IntentFilter("message_received"));

         return view;
     }
	 
	 public void sortConversationByDate() {
	
	     	if(this.passed_messages != null && this.passed_messages.size()>1) 
	    	{
	     	   Collections.sort(this.passed_messages, new Comparator<Message>(){
	     		   public int compare(Message emp1, Message emp2) 
	     		   {
	     			 Long l1 = getTimeDate(emp1.getTime()).getTime();
        			 Long l2 = getTimeDate(emp2.getTime()).getTime();
	     		     return l1.compareTo(l2);		     
	     		   }
	     	
	     	   });
	     	}
	 }
	 
	 public void updateFragmentWithMessage(Message m, boolean update_locally) {
		 if(this.adapter != null)
			 this.adapter.addAndUpdate(m, update_locally);
	 }
	 
	 public void updateFragmentWithNoMessage() {
		 if(this.adapter != null)
			 this.adapter.addAndUpdateNoMsg();
	 }
	 

     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
   
         this.sortConversationByDate();
         adapter = new ConversationAdapter(this.getActivity(), this.passed_messages);
         setListAdapter(adapter);

     }
     
	@SuppressLint("SimpleDateFormat")
	private Date getTimeDate(String date_str) {
    	
    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";

    	String oldDateString = date_str;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    	Date d = null;
		try {
			d = sdf.parse(oldDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    	return d;
    }
}
