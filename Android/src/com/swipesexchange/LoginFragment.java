package com.swipesexchange;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {


	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.login_splash, 
	            container, false);
	    /*LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
	    authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);*/
	    getActivity().getActionBar().hide();
	    
	    ClosedInfo.setMinimized(false);
	    
	    
	    return view;
	    
	}
	

	
}