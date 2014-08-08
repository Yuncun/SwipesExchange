package com.swipesexchange.main;

import com.swipesexchange.R;
import com.swipesexchange.helpers.ClosedInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GuestLogoutFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.guestlogout_splash, 
	            container, false);
	    /*LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
	    authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);*/
	    getActivity().getActionBar().hide();
	    
	    ClosedInfo.setMinimized(false);
	    
	    final Button button = (Button) view.findViewById(R.id.guestlogoutbutton);
	      button.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	        	  performlogout();
	              // Perform action on click
	          }
	      });
		
	          
	    
	    return view; 
	}
	
	  public void performlogout() {
		  Log.d("Guest Logout ", "Log out button activated");
		  Intent i = getActivity().getBaseContext().getPackageManager()
		             .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		  
	      //  DialogFragment newFragment = new GuestLoginFragment();
	      //  newFragment.show(getFragmentManager(), "GuestLogin");
	    }
	
}
