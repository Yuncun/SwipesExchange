package com.swipesexchange;

import java.util.ArrayList;

import sharedObjects.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ConversationActivity extends FragmentActivity {
	
	
	ActionBar action_bar;
	private ArrayList<Message> passed_messages;
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.conversation_activity);
	 
	      
	 
	        Intent i = getIntent();
	         this.passed_messages = (ArrayList<Message>) i.getSerializableExtra("clicked_messages");
	         Log.d("pig", Integer.toString(this.passed_messages.size()));
	         for(int j=0; j< this.passed_messages.size(); j++) 
	        	 Log.d("pig", this.passed_messages.get(j).getText());
	     
	        
	        RelativeLayout close = (RelativeLayout) findViewById(R.id.go_back);
	        
	        close.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
					overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
					
				}
			});
	        
	        ConversationFragment frag = new ConversationFragment(this.passed_messages);
	        
	        FragmentManager fm = getSupportFragmentManager();
	        fm.beginTransaction().add(R.id.conversation_view, frag).commit();
	        
	        action_bar = this.getActionBar();
	        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	       
	        action_bar.show();
	        
	        
	    /*
	        btnClose.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View arg0) {
	                //Closing SecondScreen Activity
	                finish();
	            }
	        });
	        */
	 
	    }
}
