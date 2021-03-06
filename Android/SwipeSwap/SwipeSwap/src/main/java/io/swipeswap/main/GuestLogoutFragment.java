package io.swipeswap.main;

import io.swipeswap.helpers.ClosedInfo;
import io.swipeswap.network.ConnectToServlet;
import io.swipeswap.sharedObjects.Self;

import android.content.Intent;
import android.os.Bundle;
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
	    View view = inflater.inflate(io.swipeswap.R.layout.guestlogout_splash,
	            container, false);

	    getActivity().getActionBar().hide();
	    
	    ClosedInfo.setMinimized(false);
	    
	    final Button button = (Button) view.findViewById(io.swipeswap.R.id.guestlogoutbutton);
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
		
		//Log out of database, so that this phone does not receive GCM notifications while not logged in
		ConnectToServlet.logoutRemoveUIDRegIDPair(Self.getUser().getUID(), Self.getUser().getRegid());
		 
		 
		startActivity(i);
	    }
	
}
