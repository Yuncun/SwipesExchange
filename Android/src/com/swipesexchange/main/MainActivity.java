package com.swipesexchange.main;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.swipesexchange.R;
import com.swipesexchange.helpers.AccurateTimeHandler;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.helpers.Constants;
import com.swipesexchange.helpers.DisplayExceptionAlertDialog;
import com.swipesexchange.helpers.FontsOverride;
import com.swipesexchange.lists.NewListingFragment;
import com.swipesexchange.lists.NewListingFragmentBuy;
import com.swipesexchange.lists.SelectionFragment;
import com.swipesexchange.lists.SelectionFragment.SectionsPagerAdapter;
import com.swipesexchange.main.MyApplication.TrackerName;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.User;


public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */

	//Constants
	public static final String PROPERTY_UID = "user_id";
	public static final String PROPERTY_GUESTNAME = "guest_name";
	public static final int LOGIN_SPLASH = 0;
	public static final int MAIN = 1;
	public static final int SETTINGS = 2;
	public static final int GUESTLOGOUT = 3;
	public static final int NL_REQUESTCODE = 121;
	public static final int SL_REQUESTCODE = 122;
	//GCM Constants
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    public static AccurateTimeHandler accurateTimeHandler;
	
	//GCM Variables
    String SENDER_ID = "59031275379";
    static final String TAG = "GCMDemo @eric";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();

    private boolean create;
	private boolean is_resumed;
	public boolean loggedInAsGuest = false;
	public boolean has_successfully_logged_in_sometime = false;
    //Member Instances
    protected Context context;
	SectionsPagerAdapter mSectionsPagerAdapter;
	private MenuItem settings;
	private Menu options_menu;
	private Session session;
	public Fragment[] fragments = new Fragment[4];
	ViewPager mViewPager;
	private UiLifecycleHelper uiHelper;
	
	//Instances for error dialog
	Button ok_button;
	Dialog time_error_dialog;
	TextView text_field;
	
	//Test variables
	public boolean isInFront;
	
	
	/**
	 * onCreate function for FragmentActivity
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Arimo-Regular.ttf");
	    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
	
		
		
		//ActionBar actionBar = getActionBar();
		//actionBar.hide();

		//FontsOverride.setDefaultFont(this, "DEFAULT", "MyFontAsset.ttf");
		
		 
		
		//FontsOverride.setDefaultFont(this, "SANS_SERIF", "MyFontAsset3.ttf");
		
		//Used when we need to close all activities. Since MainActivity is our first activity, in order to close all activities we need to
		//restart MainActivity, and then finish it. Therefore, sometimes this activity will be recreated when we want to close everything.
		if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();
		}
		
		if (!isOnline()){
			Log.d("MainActivity", "onCreate detected no network; stopping application");
			DisplayExceptionAlertDialog error = new DisplayExceptionAlertDialog();
			error.showAlert(this, "No Network Detected", true);
			
		}
		
		ClosedInfo.num_unread = 0;

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		
		Self.setUser(new User("Guest"));
		session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
		
		ClosedInfo.setMinimized(true);
		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		
		FragmentManager fragment_manager = this.getSupportFragmentManager();
		fragments[LOGIN_SPLASH] = fragment_manager.findFragmentById(R.id.splash_fragment);
		fragments[MAIN] = fragment_manager.findFragmentById(R.id.selection_fragment);
		fragments[SETTINGS] = fragment_manager.findFragmentById(R.id.userSettingsFragment);
		fragments[GUESTLOGOUT] = fragment_manager.findFragmentById(R.id.guestlogoutfragment);
		
		accurateTimeHandler = new AccurateTimeHandler();

		context = getApplicationContext();
		
	
		//TEMPORARY TO FIND DEVELOPER KEYHASH
		try{
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.swipesexchange", PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));       
//
	        }
	    } catch (Exception e) {
	    	Log.d("KeyHash:", "Exception" + e.toString());
	    } 
		//*************************

		/**
		 * Google Analytics intialization
		 */
		//Get a Tracker (should auto-report)
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
		
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);

		
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		for(int i=0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		
		transaction.commit();
	        
		this.create = false;

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

 	  	            	  
 	  	                Self.getUser().setUID(user.getId());
 	  	                Self.getUser().setName(user.getName());
 	  	                
 	  	             if (checkPlayServices()) {
 	  	                gcm = GoogleCloudMessaging.getInstance(context);
 	  	                Self.getUser().setRegid(getRegistrationId(getApplicationContext()));
 	  	                if (getRegistrationId(getApplicationContext()).isEmpty()) {
 	  	                    registerInBackground();
 	  	                }   
 	  	             handleIDsAsync(Self.getUser().getUID(), Self.getUser().getRegid());
 	  	            } else {
 	  	                Log.i("LOUD AND CLEAR", "No valid Google Play Services APK found.");
 	  	            }

 	  	                
 	  	              }
 	  	            }
 	  	          }).executeAsync();
			 	 
			 	has_successfully_logged_in_sometime = true;

			 	 //Todo: Try to extract from sharedpreferences
			}
			else if (session.isClosed()){
				 Log.d("Guest Logout ", "session.isClosed()");
				  if (fragments[LOGIN_SPLASH].isVisible()){
				    	Log.d("OnResume", "Hiding actionbar");
				    	getActionBar().hide();
				    }
				  
				if (has_successfully_logged_in_sometime){
            	Log.d("logged out" , "Finishing all activities");
            	//activity.finish();
            	ConnectToServlet.logoutRemoveUIDRegIDPair(Self.getUser().getUID(), Self.getUser().getRegid());
            	
            	
            	Intent intent = new Intent(context, MainActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	intent.putExtra("EXIT", true);
            	startActivity(intent);
				}
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
		Log.d("Actionbar", "Showing actionbar");
	    // only add the menu when the selection fragment is showing
		this.options_menu = menu;
		super.onCreateOptionsMenu(this.options_menu);
	    if (fragments[MAIN].isVisible() || create) {
	        if (this.options_menu.size() == 0) {
	            settings = this.options_menu.add(R.string.logout);
	            getMenuInflater().inflate(R.menu.main, menu);
	    		RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.layout_item).getActionView();
	    		View inflatedView = getLayoutInflater().inflate(R.layout.actionbar_top, null);
	    		
	    		inflatedView.findViewById(R.id.new_button).setOnClickListener(new OnClickListener() {
	     
	    			@Override
	    			public void onClick(View v) {
	    				
	    				if (Self.getUser().getUID()!=null){
	    				 if (((SelectionFragment) fragments[MAIN]).countBuyListingsFromUserID(Self.getUser().getUID())>=Constants.MAXIMUM_LISTINGS_ALLOWED_PER_USER){
	    	
	    				
	    					time_error_dialog = new Dialog(v.getContext());
	 						time_error_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 						time_error_dialog.setContentView(R.layout.dialog_time_error);
	 						
	 						ok_button = (Button) time_error_dialog.findViewById(R.id.Ok_Button);
	 						text_field = (TextView) time_error_dialog.findViewById(R.id.dialog_text_view);
	 						
	 						//Return the first error found
	 						text_field.setText("You cannot have more than " + Constants.MAXIMUM_LISTINGS_ALLOWED_PER_USER + " listings per section. Please delete one before making another");
	 						
	 						ok_button.setOnClickListener(new View.OnClickListener() {
	 							 
	 		                    @Override
	 		                    public void onClick(View view) {       
	 		                        time_error_dialog.dismiss();
	 		                    }
	 		                });
	 						
	 						time_error_dialog.show();	
	    				 }
	    				 else{
	    				 Intent nextScreen = new Intent(v.getContext(), NewListingFragmentBuy.class);
	    				 nextScreen.putExtra("new_listing_type", "buy");
	    				 Log.d("MainActivity Onclick for NL", "startActivityForResult(nextScreen, NL_REQUESTCODE);" );
	    				 startActivityForResult(nextScreen, NL_REQUESTCODE);
	    				 }
	    			 }
	    			}
	    		});
	    		
	    		inflatedView.findViewById(R.id.new_button2).setOnClickListener(new OnClickListener() {
	    			 
	    			@Override
	    			public void onClick(View v) {
	    				
	    				if (Self.getUser().getUID()!=null){
	    				 if (((SelectionFragment) fragments[MAIN]).countSellListingsFromUserID(Self.getUser().getUID())>=Constants.MAXIMUM_LISTINGS_ALLOWED_PER_USER){
	    					time_error_dialog = new Dialog(v.getContext());
	 						time_error_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 						time_error_dialog.setContentView(R.layout.dialog_time_error);
	 						
	 						ok_button = (Button) time_error_dialog.findViewById(R.id.Ok_Button);
	 						text_field = (TextView) time_error_dialog.findViewById(R.id.dialog_text_view);
	 						
	 						//Return the first error found
	 						text_field.setText("You cannot have more than " + Constants.MAXIMUM_LISTINGS_ALLOWED_PER_USER + " listings per section. Please delete one before making another");
	 						
	 						ok_button.setOnClickListener(new View.OnClickListener() {
	 							 
	 		                    @Override
	 		                    public void onClick(View view) {
	 		                        
	 		                        time_error_dialog.dismiss();
	 		 
	 		                    }
	 		                });
	 						
	 						time_error_dialog.show();	
	    				 }
	    				 else{
	    				 Intent nextScreen = new Intent(v.getContext(), NewListingFragment.class);
	    				 nextScreen.putExtra("new_listing_type", "sell");
	    				 Log.d("MainActivity Onclick for SL", "startActivityForResult(nextScreen, SL_REQUESTCODE);" );
	    				 startActivityForResult(nextScreen, SL_REQUESTCODE);
	    				 } 
	    			 }
	    			}
	    		});
	    		
	    	
	    		relativeLayout.addView(inflatedView);
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
	    	if (Session.getActiveSession().isOpened() )
		        showFragment(SETTINGS, true);
	    	else
	    		showFragment(GUESTLOGOUT, true);
		        return true;
	    }
	    return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  uiHelper.onActivityResult(requestCode, resultCode, data);
	  
	  //From ListingsList 
	  //Used to take some data from NLBuy or SLBuy to determine whether UI should be reloaded right awya
	  Log.d("onActivityResult", "****onActivityResult IN LISTINGS LIST****" + Integer.toString(resultCode));
	     
	     switch(requestCode) { 
	       case (MainActivity.NL_REQUESTCODE) : { 
	         if (resultCode == 1) { 
	         String newText = Boolean.toString(data.getBooleanExtra("submitted_new_BL", false));
	         Log.d("onActivityResult", "(data.getBooleanExtra('submitted_new_BL', false) : " + newText);
	     
	 	     SelectionFragment.refreshBL();
	 	     
	         } 
	         break; 
	       } 
	       case (MainActivity.SL_REQUESTCODE) : { 
	         if (resultCode == 1) { 
	         String newText = Boolean.toString(data.getBooleanExtra("submitted_new_SL", false));
	         Log.d("onActivityResult", "(data.getBooleanExtra('submitted_new_SL', false) : " + newText);

	 	     SelectionFragment.refreshSL();
	 	     
	         } 
	         break; 
	       } 
	     } 

	}

	@Override
    protected void onStart() {
        super.onStart();
         
        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("STATEINFO", MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.commit();
    }
     
    @Override
    protected void onStop() {
        super.onStop();
         
        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("STATEINFO", MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();
    }
	    
	@Override
	public void onPause() {
		super.onPause();
		this.isInFront = false;
		is_resumed = false;
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Stop the analytics tracking
		DisplayExceptionAlertDialog.killDialog();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
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
	            //Remember to test it
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
	                Self.getUser().setRegid(gcm.register(SENDER_ID));
	                msg = "Device registered, registration ID=" + Self.getUser().getRegid();

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, Self.getUser().getRegid());
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
	           if (msg!=null){
	           handleIDsAsync(Self.getUser().getUID(), msg);
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
    	Self.getUser().setUID(registrationId);

    	if (username.equals("Name") || (username.trim().length() == 0) ){
    		username = "";
    	}
    
    	String processedUsername = "Guest-" + registrationId.substring(registrationId.length()-6, registrationId.length()-1) + " (" + username + ")";
    	
    	Self.getUser().setName(processedUsername);
		Log.d("LOUD AND CLEAR", "Accepting guest login with username: " + processedUsername);

        if (checkPlayServices()) {
   			Log.d("LOUD AND CLEAR", "Creating GCM");
               gcm = GoogleCloudMessaging.getInstance(context);
               Self.getUser().setRegid(getRegistrationId(getApplicationContext()));
               if (Self.getUser().getRegid().isEmpty()) {
                   registerInBackground();
               }   
	           handleIDsAsync(Self.getUser().getUID(), Self.getUser().getRegid());
	            } else {
	                Log.i("LOUD AND CLEAR", "No valid Google Play Services APK found.");
	            }
     
		loggedInAsGuest = true;
		storeUID(context, Self.getUser().getUID());
		Log.d("LOUD AND CLEAR", "Guest Login accepted and storing name " + Self.getUser().getName());
		storeGuestName(context, username);
		//Store UID info
    	
	}
	
	
	private void storeUID(Context context, String regID){
		final SharedPreferences prefs = getGCMPreferences(context);
		    int appVersion = getAppVersion(context);
		    Log.i(TAG, "Saving uidId on app version " + appVersion);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(PROPERTY_UID, Self.getUser().getUID());
		    editor.commit();
	}
	
	private void storeGuestName(Context context, String guestName){
		final SharedPreferences prefs = getGCMPreferences(context);
		    int appVersion = getAppVersion(context);
		    Log.i(TAG, "Saving guestName on app version " + appVersion);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(PROPERTY_GUESTNAME, guestName);
		    editor.commit();
	}
		
	public String retrieveSavedGuestName()
	{
		Log.d("retrieveSavedGuestName()", "Attempting to retrieve saved guest name");
		String guestName = null;
		final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationGuest = prefs.getString(PROPERTY_GUESTNAME, "");
		if (registrationGuest.isEmpty()) {
		    	Log.i("LOUD AND CLEAR", "No previous guest name registered.");
		    	guestName = "Name";
		    	Log.d("retrieveSavedGuestName()", "guest name registratedGuest is empty guestname is now" + guestName);
			}
		else{ 
			guestName = registrationGuest;
		Log.d("retrieveSavedGuestName()", "guestname is found, guestname is " + guestName);
		}

		return guestName;
	}
		
	
	  @Override
      public void onBackPressed() {
               
               Log.d("pig", "back pressed!");
               int frag_num = this.getVisibleFragment();
               if(frag_num == SETTINGS || frag_num == GUESTLOGOUT)
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
	        		Log.d("onPrepareMenusOption", "getActionBar().show()");
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
	 * Performs logout if session state changes (ie logged out of Facebook)
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
	        	if(this.getVisibleFragment() != MAIN)
	        	{
		            showFragment(MAIN, false);
		            this.create= true;
		            this.onCreateOptionsMenu(this.options_menu);
		            Log.d("onSessionStateChange", "onSessionStateChange shows state.isOpened() - Showing MAINFragment");
	        	}
	        } else {
	            // If the session state is closed:
	            // Show the login fragment
	        	 Log.d("onSessionStateChange", "onSessionStateChange shows state is not opened - Showing loginsplash fragment");
        //	if(this.getVisibleFragment() != LOGIN_SPLASH)
        			showFragment(LOGIN_SPLASH, false);
        			getActionBar().hide();
	        	 
	        }
	    }
	    	
	    
	}
	
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Log.d("OnResumeFragments", "OnResumeFragments called");
	    checkPlayServices();
	    session = Session.getActiveSession();

		if (!isOnline()){
			Log.d("MainActivity", "onCreate detected no network; stopping application");
			DisplayExceptionAlertDialog error = new DisplayExceptionAlertDialog();
			error.showAlert(this, "No Network Detected", true);
		}
		
	    		
	    if ((session != null && session.isOpened()) || Self.getUser().getUID()!=null) {
	        // if the session is already open,
	        // try to show the selection fragment
	        showFragment(MAIN, false);
	        Log.d("OnResumeFragments", "OnResumeFragments - showFragment(MAIN)" );
	    } else {
	        // otherwise present the splash screen
	        // and ask the person to login.
	    	Log.d("ONResumeFragments", "OnResumeFragments - showfragment.login_Splash");
	    	getActionBar().hide();
	    	//if(this.getVisibleFragment() != LOGIN_SPLASH)
	    		showFragment(LOGIN_SPLASH, false);
	    	
	    	
	        
	    }
	}

	

	@Override
	public void onUserLeaveHint() {

	    super.onUserLeaveHint();
	     
	}
	
	public void setLoggedIn (int l) {
		session = Session.getActiveSession();
		session.getState();
		if(l==2)
		{
			 FragmentManager manager = getSupportFragmentManager();
        // Get the number of entries in the back stack
        int backStackSize = manager.getBackStackEntryCount();
        // Clear the back stack
        
        for (int i = 0; i < backStackSize; i++) {
            manager.popBackStack();
        }
        this.showFragment(LOGIN_SPLASH, false);
		}
		else if(l==1)
		{
			FragmentManager manager = getSupportFragmentManager();
	        // Get the number of entries in the back stack
	        int backStackSize = manager.getBackStackEntryCount();
	        // Clear the back stack
	        
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        this.showFragment(MAIN, false);
		}
	}
	

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	
	@Override
	public void onResume() {
		super.onResume();
		 Log.d("OnResume", "OnResume in MainActivity called");
		 
		 this.isInFront = true;
		 
		session = Session.getActiveSession();
		
		
	    if (fragments[LOGIN_SPLASH].isVisible()){
	    	Log.d("OnResume", "Hiding actionbar");
	    	getActionBar().hide();
	    }
	    
		if(session != null && (session.isOpened() || session.isClosed()))
		{
			this.onSessionStateChange(session, session.getState(), null);
		}
		is_resumed = true;
		uiHelper.onResume();
	}
	
	
	public static User getSelf(){
		return Self.getUser();
	}
	
	public String getUID()
	{
		return Self.getUser().getUID();
	}
	
    private void handleIDsAsync(String UID, String RegID) {
	    new AsyncTask<String, Void, String>() {
	        @Override
	        protected String doInBackground(String... params) {
	            String msg = "";
	            try {
	            	
	            	Log.d("LOUD AND CLEAR", "**** HandleIDASync doinbackground is reached and our IDs are" + params[0] + "*** and *****" + params[1]);
	            	ConnectToServlet.sendIDPair(params[0],  params[1]);
	            } catch (Exception ex) {
	            	
	                msg = "Error :" + ex.getMessage();
	                Log.d("LOUD AND CLEAR", "**** handleIDAsync error **** " + msg);
	                Log.d("Timeout in MessageTask", "Timeout in MessageTask ");
	           // 	 DisplayExceptionAlertDialog errorDialog = new DisplayExceptionAlertDialog();
  	           //      errorDialog.showAlert(((MainActivity) context), "Timeout Exception - Could not resolve identity of client, possibly because of connection failure", true);
	              
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	           Log.d("LOUD AND CLEAR", "GCM register in background msg: " + msg);
	        }
	    }.execute(UID, RegID, null);
	    
	}
    
    public boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
    


}
