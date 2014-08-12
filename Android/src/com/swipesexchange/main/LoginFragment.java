package com.swipesexchange.main;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.swipesexchange.R;
import com.swipesexchange.R.id;
import com.swipesexchange.R.layout;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.main.MainActivity;
public class LoginFragment extends Fragment {


public static boolean dialogOpened= false;

EditText mEdit;
//Button submitButton;
Button positive;
Button negative;
Dialog loginDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login_splash, 
	            container, false);
	    /*LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
	    authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);*/
	    getActivity().getActionBar().hide();
	    
	    ClosedInfo.setMinimized(false);
	  
        
        
	    Button submitButton = (Button) view.findViewById(R.id.guestButton);
	    submitButton.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	        	 // openGuestLogin();
	        	  Log.d("Clicked", "CLICKED");
	        	  loginDialog = new Dialog(v.getContext());
	        	  loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        	  loginDialog.setContentView(R.layout.dialog_signin);
	        	  
	        	  positive = (Button) loginDialog.findViewById(R.id.Yes_Button);
	        	  negative = (Button) loginDialog.findViewById(R.id.Cancel_Button);
	        	  mEdit =  (EditText) loginDialog.findViewById(R.id.username);

   			   	MainActivity mactivity = (MainActivity) getActivity();
   			   	mEdit.setText(mactivity.retrieveSavedGuestName(), TextView.BufferType.EDITABLE);
   			   
	        	  positive.setOnClickListener(new View.OnClickListener(){
	        		  
	        		  public void onClick(View v) {
	                	   //Dialog f = (Dialog) dialog;
	                       //mEdit = (EditText) f.findViewById(R.id.username);

	                       // sign in the user ...              	
	                	  
	        			  
	        			 
	        		    	
	        		       
	                	   String username = mEdit.getText().toString().trim();
	                	   Log.d("username handling in edittext", "EditText in logindialog accepted the username " + username);
	                	   MainActivity mactivity = (MainActivity) getActivity();
	                	   
	                	   mactivity.handleNewGuest(username);
	                	   loginDialog.dismiss();
	                	   mactivity.showFragment(mactivity.MAIN, false);
	        		  	}
			          }
			      );
	        	  
	        	  negative.setOnClickListener(new View.OnClickListener(){
	        		   public void onClick(View v) {
	                	   LoginFragment.dialogOpened = false;
	                       loginDialog.dismiss();
	                   }
			          }
			      );
	        	  loginDialog.show();
	          }
	          
	          
	     } );
	    return view; 
	}
	
	/*
	  public void openGuestLogin() {
		  if (!dialogOpened){
			  DialogFragment newFragment = new GuestLoginFragment();
		      newFragment.show(getFragmentManager(), "GuestLogin"); 
		  }
	    }
	*/

	
}