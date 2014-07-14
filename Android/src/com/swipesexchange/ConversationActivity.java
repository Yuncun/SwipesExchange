package com.swipesexchange;

import java.util.ArrayList;

import sharedObjects.Message;
import sharedObjects.Self;
import sharedObjects.User;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ConversationActivity extends FragmentActivity {
	
	//private User self;
	ActionBar action_bar;
	private Button submit_message;
	private Time now;
	private ArrayList<Message> passed_messages;
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.conversation_activity);
	 
	        now = new Time();
	 
	        Intent i = getIntent();
	
	        int pos = i.getIntExtra("clicked_messages_position", 0);
	        this.passed_messages = (ArrayList<Message>) ConversationList.getConversations().get(pos).getAllMessages();
	       
	        Log.d("pig", Integer.toString(this.passed_messages.size()));
	        for(int j=0; j< this.passed_messages.size(); j++) 
	        	Log.d("pig", this.passed_messages.get(j).getText());
	     
	        
	        RelativeLayout close = (RelativeLayout) findViewById(R.id.go_back);
	        
	        close.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ClosedInfo.setMinimized(false);
					Intent intent = new Intent(ConversationActivity.this, MainActivity.class);
					//startActivityForResult(intent, RESULT_OK);
					setResult(RESULT_OK, intent); 
					finish();
					overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
					
				}
			});
	        
	        final EditText message_content_holder = (EditText) findViewById(R.id.messageEdit);
	     	submit_message = (Button) findViewById(R.id.chatSendButton);
	     	
	     	
	     	
			submit_message.setOnClickListener(new View.OnClickListener() {
				 
	            @Override
	            public void onClick(View view) {
	            	// get the contents of the EditText field holding the message string to be sent
	                String message_contents = message_content_holder.getText().toString();
	                if(message_contents == null || (message_contents.length() == 0))
	                	return;
	                // create the message to be sent
	                // TODO: make this accurate, check if EditText view is null
	                //These IDs have to be real now - @ES
	                
	                User sender = Self.getUser();
	                //sender.setRegid("10001");
	                //sender.setUID("10152153150921342");
	                User receiver = Self.getUser(); //TODO: resolve target
	                //receiver.setRegid("10002");
	                //receiver.setUID("10152153150921342");
	                String lid = passed_messages.get(0).getListing_id();
	                now.setToNow();

	                
	                String time = now.format2445();
	                
	                Log.d("TIME", "Time_plus at ConversationActivity is " + time);
	                
	                Message msg = new Message(sender, receiver, lid, time, message_contents);
	                ConnectToServlet.sendMessage(msg);
	                // clear the edittext
	                message_content_holder.getText().clear();
	                
	                // hide the keyboard
	                InputMethodManager imm = (InputMethodManager)getSystemService(
	                	      Context.INPUT_METHOD_SERVICE);
	                imm.hideSoftInputFromWindow(message_content_holder.getWindowToken(), 0);
	                
	                refreshConversationFragment(msg);
	                

	            }
	        });
	        
	        ConversationFragment frag = new ConversationFragment(this.passed_messages);
	        
	        FragmentManager fm = getSupportFragmentManager();
	        fm.beginTransaction().add(R.id.conversation_view, frag).commit();
	        
	        action_bar = this.getActionBar();
	        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	       
	        action_bar.show();
	        
	    }
	   
	   public void refreshConversationFragment(Message m) {
		   Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.conversation_view);
		   if(fragment != null)
		   {
			   ConversationFragment c_frag = (ConversationFragment) fragment;
			   c_frag.updateFragmentWithMessage(m);
			  
		   }
	   }
	   
	   public void refreshConversationFragmentNoMsg() {
		   Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.conversation_view);
		   if(fragment != null)
		   {
			   ConversationFragment c_frag = (ConversationFragment) fragment;
			   c_frag.updateFragmentWithNoMessage();
			  
		   }
	   }
	   
		 public int fixHours(int hours_24)
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
		 
		 public boolean isPM(int hours_24)
		 {
			 if (hours_24>=12)
				 return true;
			 
			 return false;
		 }
}
