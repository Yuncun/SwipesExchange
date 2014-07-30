package com.swipesexchange;

import java.util.ArrayList;

import sharedObjects.Message;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

public class NewListingFragmentBuy extends FragmentActivity {

	// member variables
		private final int num_parents = 4;
		private ArrayList<ParentRow> parents;
        static MainActivity mActivity;
        private ExpandableListView lv;
        private int page_num;
        ActionBar action_bar;
        
           
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);

        	// set the content view for the new listing fragments
	        setContentView(R.layout.new_listing_fragment_buy);
	        Intent i = getIntent();

	   
        	String listing_type = i.getStringExtra("new_listing_type");
   
	        
	        RelativeLayout close = (RelativeLayout) findViewById(R.id.go_back_bl);
	        
	        close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ClosedInfo.setMinimized(false);
					Intent intent = new Intent(NewListingFragmentBuy.this, MainActivity.class);
					//startActivityForResult(intent, RESULT_OK);
					setResult(RESULT_OK, intent); 
					finish();
				}
			});
	        

        	/*if(listing_type.equals("buy"))
        	{
        		frag = new NLBuy();
		        FragmentManager fm = getSupportFragmentManager();
		        fm.beginTransaction().add(R.id.new_listing_buy_frag, frag).commit();
        	}
	        */
        	
	        action_bar = this.getActionBar();
	        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	       
	        action_bar.show();
	        
    }

        
    

        
       
            
        
}

