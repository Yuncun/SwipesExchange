package com.swipesexchange;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.swipesexchange.MainActivity.SectionsPagerAdapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBarFragment extends Fragment implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	
	private LoginFragment loginFragment;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ActionBar actionBar;
	public BackendData l;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private UiLifecycleHelper uiHelper;
	
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	
	
	/**
	 * onCreate function for FragmentActivity
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		
		if(savedInstanceState == null)
		{
			loginFragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction().add(android.R.id.content,  loginFragment).commit();
		}
		
		/*
		if(Session.getActiveSession().isOpened())
		{
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		transaction.hide(loginFragment);
		transaction.commit();
		}
		*/
		
		actionBar = getActionBar();
		 
		
		// Set up the action bar.
		
		//actionBar.setIcon(R.drawable.yes);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//tp = (TimePicker) findViewById(R.id.timePicker1);
		
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);;

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		//if(Session.getActiveSession().isOpened())
		//{

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		
		
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			View tabView = this.getLayoutInflater().inflate(R.layout.tab_layout, null);
			
			TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
			tabText.setText(mSectionsPagerAdapter.getPageTitle(i));
			
		
			//the following commented code can be used to create unique images to display on each tab
			
		/*
			ImageView tabImage = (ImageView) tabView.findViewById(R.id.tabIcon);
			
			switch(i)
			{
			case 0:
				tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.shopping_basket_checkmark));
				break;
			case 1:
				tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.currency_sign_dollar_add));
				break;
			case 2:
				tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.hand_handshake_checkmark));
				break;
			case 3:
				tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.hand_handshake_checkmark));
				break;
			case 4:
				tabImage.setImageDrawable(this.getResources().getDrawable(R.drawable.hand_handshake_checkmark));
				break;
			}
			*/
			
			
			//Add the tabs to the action bar
			actionBar.addTab(actionBar.newTab()
					
					
			//Listen for clicks on the tabs
			.setTabListener(this).setCustomView(tabView));
		}
		/*
		// start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {

	          // make request to the /me API
	          Request.newMeRequest(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	                
	                Log.d("facebook", user.getName());
	                Log.d("facebook", user.getId());
	              }
	            }
	          }).executeAsync();
	        }
	      }
	    });
	    */
		
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  uiHelper.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	/**
	 * onSessionStateChange
	 * Performs logout if sesion state changes (ie logged out of Facebook)
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		
		//try to make a request, if it doesnt work (ie we arent logged in), then handle it
		// and call logout
		//also want to try and force the login each time
		//if(session.)
		
		if(state.isOpened() && session.getAccessToken() != null)
			Log.i("facebook", "Logged in...");
		else if(state.isClosed()) 
		{
			
		}
			//this.callFacebookLogout(this);
			
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		Session session = Session.getActiveSession();
		if(session != null && (session.isOpened() || session.isClosed()))
		{
			this.onSessionStateChange(session, session.getState(), null);
		}
		
		uiHelper.onResume();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.layout_item).getActionView();
		View inflatedView = getLayoutInflater().inflate(R.layout.actionbar_top, null);
		inflatedView.findViewById(R.id.new_button).setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
					
					actionBar.setSelectedNavigationItem(2);
				
			}
		});
		
		inflatedView.findViewById(R.id.new_button2).setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
					
					actionBar.setSelectedNavigationItem(3);
				
			}
		});
		
		inflatedView.findViewById(R.id.logout_button).setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
					
					callFacebookLogout(v.getContext());
					
				
			}
		});
		relativeLayout.addView(inflatedView);
		
		
		return (super.onCreateOptionsMenu(menu));
		
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		
	}
	


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		MainActivity mActivity;

		public SectionsPagerAdapter(FragmentManager fm, MainActivity my_activity) {
			super(fm);
			mActivity = my_activity;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch(position)
			{
			case 0:
				return MyList.newInstance(position, mActivity, l);
			case 1:
				return MyList.newInstance(position, mActivity, l);
			case 2:
				return NewListingFragmentBuy.newInstance(position, mActivity);
			case 3:
				return NewListingFragment.newInstance(position);
		
			
			}
				return null;
			}

		@Override
		public int getCount() {
			// Return the number of fragment pages in the application
			return 4;
		}
		public String getPageTitle(int position) {
			
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section4);
			case 3:
				return getString(R.string.title_section5);
			
			}
			return null;
		}
		}

	public void callFacebookLogout(Context context) {
	    Session session = Session.getActiveSession();
	    if (session != null) {

	       
	           
	            
	            Log.d("facebook", "Clearing tokens");
	            
	            
	   
	   
	        session = new Session(context);
	        Session.setActiveSession(session);
	        session.closeAndClearTokenInformation();
	        
	       
	        Session.openActiveSession(this, true, new Session.StatusCallback() {

	  	      // callback when session changes state
	  	      @Override
	  	      public void call(Session session, SessionState state, Exception exception) {
	  	        if (session.isOpened()) {

	  	          // make request to the /me API
	  	          Request.newMeRequest(session, new Request.GraphUserCallback() {

	  	            // callback after Graph API response with user object
	  	            @Override
	  	            public void onCompleted(GraphUser user, Response response) {
	  	              if (user != null) {
	  	                
	  	                Log.d("facebook", user.getName());
	  	              }
	  	            }
	  	          }).executeAsync();
	  	        }
	  	      }
	  	    });

	    }

	}
	
	
	
}

	
	



