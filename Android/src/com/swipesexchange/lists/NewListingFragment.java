package com.swipesexchange.lists;

import com.swipesexchange.R;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.main.MainActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class NewListingFragment extends FragmentActivity{

	static MainActivity mActivity;
	        ActionBar action_bar;
	        
	    	Button yes_button;
	    	Button cancel_button;
	    	Dialog submit_dialog;
	    	
	    	Button ok_button;
	    	Dialog time_error_dialog;
	        
	           
	        @Override
	        protected void onCreate(Bundle savedInstanceState) {
	        	super.onCreate(savedInstanceState);

	        	// set the content view for the new listing fragments
		        setContentView(R.layout.new_listing_fragment);
		        Intent i = getIntent();

		   
	        	i.getStringExtra("new_listing_type");
	   
		        
		        RelativeLayout close = (RelativeLayout) findViewById(R.id.go_back_sl);
		        
		        close.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ClosedInfo.setMinimized(false);
						Intent intent = new Intent(NewListingFragment.this, MainActivity.class);
						setResult(RESULT_OK, intent); 
						finish();
					}
				});
		        

		        action_bar = this.getActionBar();
		        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		       
		        action_bar.show();
		        
	    }

	        
	    

	        
	       
	            
	        
	}

