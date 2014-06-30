package com.swipesexchange;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import sharedObjects.MsgStruct;
import sharedObjects.User;
import android.support.v4.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.model.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.swipesexchange.SelectionFragment.SectionsPagerAdapter;

import android.location.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private String fbid;
	private String myID;
    private String regid;
    private User self;
	public static final String PROPERTY_UID = "user_id";
    public boolean loggedInAsGuest = false;
    private boolean minimized;
    private int logged_in;
	
	protected Context context;
	
	//private GetFbidAsync fg;
	
	//***************************************************
	//GCM Variables
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "59031275379";
    static final String TAG = "GCMDemo @eric";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();

   //****************************************************
    
	private boolean state_changed;
	private boolean create;
	private LoginFragment login_fragment;
	SectionsPagerAdapter mSectionsPagerAdapter;
	public BackendData l;
	private final String login_tag = "login_fragment";
	private static final int LOGIN_SPLASH = 0;
	static final int MAIN = 1;
	private static final int SETTINGS = 2;
	private MenuItem settings;
	private Menu options_menu;
	private boolean first_login;
	private final Handler handler = new Handler();
	private Runnable run_pager;
	
	private boolean is_resumed;
	private Session session;
	
	private Fragment[] fragments = new Fragment[3];

	ViewPager mViewPager;
	
	private UiLifecycleHelper uiHelper;
	
	/**
	 * onCreate function for FragmentActivity
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		minimized = false;
		
		this.logged_in = 0;
		
		session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
		
		ClosedInfo.setMinimized(true);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		
		FragmentManager fragment_manager = this.getSupportFragmentManager();
		fragments[LOGIN_SPLASH] = fragment_manager.findFragmentById(R.id.splash_fragment);
		fragments[MAIN] = fragment_manager.findFragmentById(R.id.selection_fragment);
		fragments[SETTINGS] = fragment_manager.findFragmentById(R.id.userSettingsFragment);
		
		
		//Create self
		self = new User(null);
		context = getApplicationContext();
		
		/*
		//@Eric -  shared preferences on machine are initialized here.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		self.setSharedPref(sp);
		String prefTest = self.getSharedPref().getString("myID", null);
		Log.d("LOUD AND CLEAR", "Shared preference on initialization is" + prefTest);
		*/
		//fg = new GetFbidAsync();
		
		//Initializing GCM Connection
		//@Eric 
		/*
		if (checkPlayServices()) {
			Log.d("LOUD AND CLEAR", "Creating GCM");
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (regid.isEmpty()) {
                registerInBackground();
                
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
		//End GCM Connetion inti
		*/
		
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		for(int i=0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		
		transaction.commit();
	        
		this.state_changed = false;
		this.create = false;
		//this.first_login = true;
	    
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
	
	/**
	 * Facebook Login State Change Callback
	 * 
	 * This function is called when facebook session status is changed - in other words, when the user logs in or out.
	 * A user will "log in" (thereby activating this callback) on creation of the app, even if his info was previously saved
	 * Therefore, we can use this callback as part of the oncreate process and use this function to prepare the GCM when
	 * FBID login information can be retrieved.
	 * 
	 * 
	 * 
	 */
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
			if (session.isOpened())
			{
				//Check for UUID
			 	 Request.newMeRequest(session, new Request.GraphUserCallback() {
			 		
 	  	            @Override
 	  	            public void onCompleted(GraphUser user, Response response) {
 	  	              if (user != null) {

 	  	            	  
 	  	                fbid = user.getId();
 	  	                myID = fbid;
 	  	                self.setUID(fbid);
 	  	                Log.d("LOUD AND CLEAR", "****** SESSION STATE CHANGE ****** fbname: " + user.getName());
 	  	                Log.d("LOUD AND CLEAR", "fbid " + user.getId());
 	  	                
 	  	             if (checkPlayServices()) {
 	  	    			Log.d("LOUD AND CLEAR", "Creating GCM");
 	  	                gcm = GoogleCloudMessaging.getInstance(context);
 	  	                self.setRegid(getRegistrationId(getApplicationContext()));
 	  	                regid = getRegistrationId(getApplicationContext());
 	  	                if (regid.isEmpty()) {
 	  	                    registerInBackground();
 	  	                }   
 	  	             handleIDsAsync(self.getUID(), self.getRegid());
 	  	            } else {
 	  	                Log.i("LOUD AND CLEAR", "No valid Google Play Services APK found.");
 	  	            }
 	  	                
 	  	               
 	  	             
 	  	               // fg.execute(fbid);
 	  	                
 	  	              }
 	  	            }
 	  	          }).executeAsync();
			 	 //Todo: Try to extract from sharedpreferences
			}
		}
	};
	
	public int getVisibleFragment(){
	    FragmentManager fragmentManager = this.getSupportFragmentManager();
	    List<Fragment> fragments = fragmentManager.getFragments();
	    for(int i = 0; i < fragments.size(); i++){
	        if(fragments.get(i) != null && fragments.get(i).isVisible())
	            return i;
	    }
	    return -1;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    // only add the menu when the selection fragment is showing
		this.options_menu = menu;
		super.onCreateOptionsMenu(this.options_menu);
	    if (fragments[MAIN].isVisible() || create) {
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
	
	//GCM Functions
	//***************************************************************
	private boolean checkPlayServices() {
		
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            Log.d("Loud and Clear", "checkPlayServices() device not supported");
	            finish();
	            //@Eric - Should be careful with this shit
	            //REmember to test it
	        }
	        return false;
	    }
	    return true;
	}
	
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	    	Log.i("LOUD AND CLEAR", "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	    	Log.i("LOUD AND CLEAR", "App version changed.");
	        return "";
	    }
	    return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
	    new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = null;
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, regid);
	            } catch (IOException ex) {
	                msg = null;
	                Log.d("LOUD AND CLEAR", "Error in registerinbackround() IO exception");
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	        	  Log.d("LOUD AND CLEAR", "GCM register in background msg: (Nothing is good)" + msg);
	           Log.d("LOUD AND CLEAR", "After register in background, REGID is set to be " + regid);
	           if (msg!=null){
	           handleIDsAsync(self.getUID(), msg);
	           }
	        }
	    }.execute(null, null, null);
	    
	}
	
	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
	    // Your implementation here.
		Log.d("LOUD AND CLEAR", "Sending registration ID to backend! ");
		
	}
	
	
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	//End GCM Functions
	//***************************************************************
	
	
	public void handleNewGuest(String username)
	{
		final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(PROPERTY_UID, "");
		if (registrationId.isEmpty()) {
		    	Log.i("LOUD AND CLEAR", "Registration not found.");
		    	registrationId = UUID.randomUUID().toString();
			}
    	self.setUID(registrationId);
    	self.setName(username);
		Log.d("LOUD AND CLEAR", "Accepting guest login with username: " + username);

        if (checkPlayServices()) {
   			Log.d("LOUD AND CLEAR", "Creating GCM");
               gcm = GoogleCloudMessaging.getInstance(context);
               regid = getRegistrationId(getApplicationContext());
               self.setRegid(regid);
               if (self.getRegid().isEmpty()) {
                   registerInBackground();
               }   
	           handleIDsAsync(self.getUID(), self.getRegid());
	            } else {
	                Log.i("LOUD AND CLEAR", "No valid Google Play Services APK found.");
	            }
        Log.d("LOUD AND CLEAR", "Guest Login accepted with stats: " + self.getUID() + " and " + self.getRegid());
		loggedInAsGuest = true;
		storeUID(context, self.getUID());
		//Store UID info
    	
	}
	
		private void storeUID(Context context, String regID){
		 final SharedPreferences prefs = getGCMPreferences(context);
		    int appVersion = getAppVersion(context);
		    Log.i(TAG, "Saving uidId on app version " + appVersion);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(PROPERTY_UID, self.getUID());
		    editor.commit();
	}
		
	  @Override
      public void onBackPressed() {
               
               Log.d("pig", "back pressed!");
               int frag_num = this.getVisibleFragment();
               if(frag_num == SETTINGS)
               {
            	   this.showFragment(MAIN, false);
               }
               else
            	   super.onBackPressed();

               
            	   
               
          }
	
	public void showFragment(int fragmentIndex, boolean addToBackStack) {
	    boolean update_menu = false;
		FragmentManager fm = getSupportFragmentManager();
	    FragmentTransaction transaction = fm.beginTransaction();
	    for (int i = 0; i < fragments.length; i++) {
	        if (i == fragmentIndex) {
	        	if(i==0)
	        	{
	        		ClosedInfo.setMinimized(false);
	        		getActionBar().hide();
	        	}
	        	else if(i==1)
	        	{
	        		ClosedInfo.setMinimized(true);
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
		
	    if(is_resumed) {
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
	            showFragment(MAIN, false);
	            this.create= true;
	            this.onCreateOptionsMenu(this.options_menu);
	            // MAIN: log the user ID and name
	    	    if (session != null) {
	    	    	 Request.newMeRequest(session, new Request.GraphUserCallback() {
	    	  	            // callback after Graph API response with user object
	    	  	            @Override
	    	  	            public void onCompleted(GraphUser user, Response response) {
	    	  	              if (user != null) {
	    	  	                Log.d("facebook", user.getName() + "normal");
	    	  	              }
	    	  	            }
	    	  	          }).executeAsync();
	    	    }
	        } else {
	            // If the session state is closed:
	            // Show the login fragment
	        	/*
	        	this.state_changed = true;
	        	 Fragment refresh_fragment = this.fragments[MAIN];
	        	    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
	        	    fragTransaction.detach(refresh_fragment);
	        	    fragTransaction.attach(refresh_fragment);
	        	    fragTransaction.commit();
	        	    */
	        	
	        	//this.first_login = false;
	            showFragment(LOGIN_SPLASH, false);
	        }
	    }
	    	
	    
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    
	    checkPlayServices();
	    session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	        // if the session is already open,
	        // try to show the selection fragment
	        showFragment(MAIN, false);
	    } else {
	        // otherwise present the splash screen
	        // and ask the person to login.
	    	//this.first_login = false;
	        showFragment(LOGIN_SPLASH, false);
	        
	    }
	}
	

	
	
        
         /*
        
              Log.d("pig","BACK FROM BACKGROUND");
              Session session = Session.getActiveSession();
              
              /*Request.newMeRequest(session, new Request.GraphUserCallback() {

	  	            // callback after Graph API response with user object
	  	            @Override
	  	            public void onCompleted(GraphUser user, Response response) {
	  	              if (user != null) {
	  	                
	  	                Log.d("pig", user.getName());
	  	              }
	  	            }
	  	          }).executeAsync();
              
              if (session != null && session.isOpened()) {
      	        // if the session is already open,
      	        // try to show the selection fragment
      	        showFragment(MAIN, false);
      	    } else {
      	        // otherwise present the splash screen
      	        // and ask the person to login.
      	    	//this.first_login = false;
      	        showFragment(LOGIN_SPLASH, false);
      	    }
*/
      	 
	@Override
	public void onUserLeaveHint() {

	    super.onUserLeaveHint();
	    this.minimized = true;
	     
	}
	
	public void setLoggedIn (int l) {
		this.logged_in = l;
		session = Session.getActiveSession();
		SessionState state = session.getState();
		Exception e = null;
		if(l==2)
		{
			 FragmentManager manager = getSupportFragmentManager();
        // Get the number of entries in the back stack
        int backStackSize = manager.getBackStackEntryCount();
        // Clear the back stack
        
        for (int i = 0; i < backStackSize; i++) {
            manager.popBackStack();
        }
        this.logged_in=0;
        this.showFragment(LOGIN_SPLASH, false);
		}
		else if(l==1)
		{
			this.logged_in=0;
			FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        this.logged_in=0;
	        this.showFragment(MAIN, false);
		}
	}
	
	
   /*
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if(ClosedInfo.wasMinimized())
		{
			
			
			
			
			SessionState state = session.getState();
			
			
		
			
			
			
			Request.GraphUserCallback callback = new Request.GraphUserCallback() {
				
			
					  // callback after Graph API response with user object
	  	            @Override
	  	            public void onCompleted(GraphUser user, Response response) {
	  	              if (response.getError() == null) {
	  	                
	  	                Log.d("facebook", user.getName() + "abnormal");
	  	                setLoggedIn(1);
	  	                
	  	                
	  	              }
	  	              else
		  	          {
	                        // Handle error
		  	        	  setLoggedIn(2);
		  	        	  Log.d("facebook", "bullshit");
	                  }
	  	            }
	  	            

	              
					
				
			};
			
			
			Exception e = null;
			
			 Request.newMeRequest(session, callback).executeAsync();
			 
			
		
			 
			 
		
	
		
			
		}
		
	}
	*/

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	
	@Override
	public void onResume() {
		super.onResume();
		
		session = Session.getActiveSession();
		
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
	  	                
	  	                Log.d("facebook", user.getName() + "Normal");
	  	              }
	  	            }
	  	          }).executeAsync();
	  	        }
	  	      }
	  	    });

	    }
	}
	
    private void handleIDsAsync(String UID, String RegID) {
	    new AsyncTask<String, Void, String>() {
	        @Override
	        protected String doInBackground(String... params) {
	            String msg = "";
	            try {
	            	
	            	int me = 1+1;
	            	Log.d("LOUD AND CLEAR", "**** HandleIDASync doinbackground is reached and our IDs are" + params[0] + "*** and *****" + params[1]);
	            	ConnectToServlet.sendIDPair(params[0],  params[1]);
	            } catch (Exception ex) {
	                msg = "Error :" + ex.getMessage();
	                Log.d("LOUD AND CLEAR", "**** handleIDAsync error **** " + msg);
	              
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	           Log.d("LOUD AND CLEAR", "GCM register in background msg: " + msg);
	        }
	    }.execute(UID, RegID, null);
	    
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
	
	private class GetFbidAsync extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
        	
        	Gson gson = new Gson();
        	
        	String fbid2 = params[0];
        	
        	MsgStruct nMsg = new MsgStruct();
        	nMsg.setHeader(Constants.FBID_GET); //Identifies message as a sell listing
        	nMsg.setPayload(fbid2);

        	String j1 = gson.toJson(nMsg);
        	Log.d("LOUD AND CLEAR", "fbid is" + fbid2);
        	Log.d("LOUD AND CLEAR", "Sending FBID to server" + j1);
        	String ourUUID = ConnectToServlet.sendIDPair(j1, "fs");
           return ourUUID;
        }

        protected void onPostExecute(String result) {
        	
        	if (checkPlayServices()) {
    			Log.d("LOUD AND CLEAR", "Creating GCM");
                gcm = GoogleCloudMessaging.getInstance(context);
                regid = getRegistrationId(getApplicationContext());

                if (regid.isEmpty()) {
                    registerInBackground();
                }
                
            } else {
                Log.i(TAG, "No valid Google Play Services APK found.");
            }
        	
        	Self.setUID(result);
        	myID = result; //deprecated
		 	Log.d("LOUD AND CLEAR", "FB LOGIN COMPLETED: UUID IS NOW" + myID);
		 	SharedPreferences.Editor editor = Self.getSharedPref().edit();
		 	editor.putString("uid", result); // value to store
		 	editor.commit();
            Log.d("LOUD AND CLEAR", "FB LOGIN COMPLETED: SHARED PREF IS NOW " + Self.getSharedPref().getString("uid", null));
               
        }

	

		}
}


	
	
	


	
	



