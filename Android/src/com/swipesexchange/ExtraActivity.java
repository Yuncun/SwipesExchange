package com.swipesexchange;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class ExtraActivity extends Activity {
	
	
	ActionBar action_bar;
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mylist2);
	 
	      
	 
	        Intent i = getIntent();
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
