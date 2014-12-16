package io.swipeswap.lists;

import io.swipeswap.helpers.ClosedInfo;
import io.swipeswap.main.MainActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

public class NewListingFragmentBuy extends FragmentActivity {

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
        	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        	// set the content view for the new listing fragments
	        setContentView(io.swipeswap.R.layout.new_listing_fragment_buy);
	        Intent i = getIntent();
	   
        	i.getStringExtra("new_listing_type");
   
	        
	        RelativeLayout close = (RelativeLayout) findViewById(io.swipeswap.R.id.go_back_bl);
	        
	        close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ClosedInfo.setMinimized(false);
					Intent intent = new Intent(NewListingFragmentBuy.this, MainActivity.class);
					setResult(RESULT_OK, intent); 
					finish();
				}
			});
	        
	        
	        action_bar = this.getActionBar();
	        action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	       
	        action_bar.show();
	        
    }

        
    

        
       
            
        
}

