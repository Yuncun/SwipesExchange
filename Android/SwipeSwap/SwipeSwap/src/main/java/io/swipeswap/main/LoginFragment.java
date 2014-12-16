package io.swipeswap.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.swipeswap.R;
import io.swipeswap.helpers.ClosedInfo;

public class LoginFragment extends Fragment {


public static boolean dialogOpened= false;

EditText mEdit;
Button positive;
Button negative;
Dialog loginDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login_splash, 
	            container, false);

	    getActivity().getActionBar().hide();
	    
	    ClosedInfo.setMinimized(false);

	    Button submitButton = (Button) view.findViewById(R.id.guestButton);
	    submitButton.setOnClickListener(new View.OnClickListener() {
	          public void onClick(View v) {
	     
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

	                	   String username = mEdit.getText().toString().trim();
	                	   Log.d("username handling in edittext", "EditText in logindialog accepted the username " + username);
	                	   MainActivity mactivity = (MainActivity) getActivity();
	                	   
	                	   mactivity.handleNewGuest(username);
	                	   loginDialog.dismiss();
	                	   mactivity.showFragment(MainActivity.MAIN, false);
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
	 @Override
	public void onResume() {
		 super.onResume();
	getActivity().getActionBar().hide();
	}*/

	 @Override
     public void setUserVisibleHint(boolean isVisibleToUser) {
         super.setUserVisibleHint(isVisibleToUser);
         if (isVisibleToUser) 
         { 
  
         getActivity().getActionBar().hide();
         
                  
         }
         else {}
     }

	
	

}