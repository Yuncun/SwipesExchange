package com.swipesexchange;

import android.support.v4.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.*;
import com.swipesexchange.SelectionFragment.SectionsPagerAdapter;

import android.content.Context;
import android.content.Intent;


public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private boolean state_changed;
	private boolean create;
	private LoginFragment login_fragment;
	SectionsPagerAdapter mSectionsPagerAdapter;
	public BackendData l;
	private final String login_tag = "login_fragment";
	private static final int LOGIN_SPLASH = 0;
	private static final int TEST = 1;
	private static final int SETTINGS = 2;
	private MenuItem settings;
	private Menu options_menu;
	
	private final Handler handler = new Handler();
	private Runnable run_pager;
	
	private boolean is_resumed;
	
	private Fragment[] fragments = new Fragment[3];
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		
		FragmentManager fragment_manager = this.getSupportFragmentManager();
		fragments[LOGIN_SPLASH] = fragment_manager.findFragmentById(R.id.splash_fragment);
		fragments[TEST] = fragment_manager.findFragmentById(R.id.selection_fragment);
		fragments[SETTINGS] = fragment_manager.findFragmentById(R.id.userSettingsFragment);
		
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		for(int i=0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		
		transaction.commit();
	        
		this.state_changed = false;
		this.create = false;
	    
		/*
		if(Session.getActiveSession().isOpened())
		{
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		transaction.hide(mainFragment);
		transaction.commit();
		}
		*/
		
		 
		
		// Set up the action bar.
		
		//actionBar.setIcon(R.drawable.yes);
		
		//tp = (TimePicker) findViewById(R.id.timePicker1);
		
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		/*
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);;

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		

		*/
		
		// For each of the sections in the app, add a tab to the action bar.
		
		/*
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			View tabView = this.getLayoutInflater().inflate(R.layout.tab_layout, null);
			
			TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
			tabText.setText(mSectionsPagerAdapter.getPageTitle(i));
			
		
			//the following commented code can be used to create unique images to display on each tab
			
		
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
			
			
			
			//Add the tabs to the action bar

					
					
			//Listen for clicks on the tabs
			
		}
	*/
		
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
	public boolean onPrepareOptionsMenu(Menu menu) {
	    // only add the menu when the selection fragment is showing
		this.options_menu = menu;
		super.onCreateOptionsMenu(this.options_menu);
	    if (fragments[TEST].isVisible() || create) {
	        if (this.options_menu.size() == 0) {
	            settings = this.options_menu.add(R.string.settings);
	        }
	        return true;
	    } else {
	        this.options_menu.clear();
	        settings = null;
	    }
	    return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.equals(settings)) {
	        showFragment(SETTINGS, true);
	        return true;
	    }
	    return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  uiHelper.onActivityResult(requestCode, resultCode, data);
	  //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		is_resumed = false;
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
	
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
	    boolean update_menu = false;
		FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    for (int i = 0; i < fragments.length; i++) {
	        if (i == fragmentIndex) {
	        	if(i==1)
	        	{
	        		update_menu = true;
	        		getActionBar().show();
	        	}
	        	else
	        		getActionBar().hide();
	        
	        	transaction.show(fragments[i]);
	        
	        	
	        } else {
	        	
	            transaction.hide(fragments[i]);
	        }
	    }
	    if (addToBackStack) {
	        transaction.addToBackStack(null);
	    }
	    transaction.commit();
	    this.create = true;
	    if(update_menu)
	    	this.onCreateOptionsMenu(this.options_menu);
	    	
	}
	/**
	 * onSessionStateChange
	 * Performs logout if sesion state changes (ie logged out of Facebook)
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
	    if (is_resumed) {
	        FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        
	    
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (state.isOpened()) {
	            // If the session state is open:
	            // Show the authenticated fragment
	            showFragment(TEST, false);
	            this.create= true;
	            this.onCreateOptionsMenu(this.options_menu);
	            // TEST: log the user ID and name
	          
	    	    if (session != null) {

	    	       
	    	           
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
	        } else if (state.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
	        	/*
	        	this.state_changed = true;
	        	 Fragment refresh_fragment = this.fragments[TEST];
	        	    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
	        	    fragTransaction.detach(refresh_fragment);
	        	    fragTransaction.attach(refresh_fragment);
	        	    fragTransaction.commit();
	        	    */
	        	
	            showFragment(LOGIN_SPLASH, false);
	        }
	    }
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	        // if the session is already open,
	        // try to show the selection fragment
	        showFragment(TEST, false);
	    } else {
	        // otherwise present the splash screen
	        // and ask the person to login.
	        showFragment(LOGIN_SPLASH, false);
	    }
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		Session session = Session.getActiveSession();
		if(session != null && (session.isOpened() || session.isClosed()))
		{
			this.onSessionStateChange(session, session.getState(), null);
		}
		is_resumed = true;
		uiHelper.onResume();
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
	
/*
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

*/
	


	
	
	
}

	
	



