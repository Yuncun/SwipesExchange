package com.swipesexchange;

import java.util.Arrays;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.swipesexchange.MainActivity;
public class LoginFragment extends Fragment {


public static boolean dialogOpened= false;

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login_splash, 
	            container, false);
	    /*LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
	    authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);*/
	    getActivity().getActionBar().hide();
	    
	    ClosedInfo.setMinimized(false);
	    
	    final Button button = (Button) view.findViewById(R.id.guestButton);
	      button.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	        	  openGuestLogin();
	              // Perform action on click
	          }
	      });
		
	          
	    
	    return view; 
	}
	
	  public void openGuestLogin() {
		  if (!dialogOpened){
			  DialogFragment newFragment = new GuestLoginFragment();
		      newFragment.show(getFragmentManager(), "GuestLogin"); 
		  }
	    }
	
	  

	
}