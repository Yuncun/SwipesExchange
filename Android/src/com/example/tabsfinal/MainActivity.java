package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.app.ListFragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;


public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	//Button myButton;
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	ActionBar actionBar;
	//public SimpleDBData my_data;
	public BackendData l;
	public String test = "fuck";
	//public AddHighScoreTask hst;
	//private TestConnect tc;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	TimePicker tp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		//tc = new TestConnect();
		//l = new BackendData(this);
		//tc.doInBackground();
		//tc.execute("");
		actionBar = getActionBar();
		 
			        // display error
			    
		//my_data = new SimpleDBData();
		//hst = new AddHighScoreTask();
		//hst.doInBackground();
		
		// Set up the action bar.
		
		//actionBar.setIcon(R.drawable.yes);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		tp = (TimePicker) findViewById(R.id.timePicker1);
		
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);;

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
	

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		
		//TextView tabText = (TextView) tabView.findViewById()
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			View tabView = this.getLayoutInflater().inflate(R.layout.tab_layout, null);
			//tabView.setBackgroundColor(Color.BLACK);
			TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
			tabText.setText(mSectionsPagerAdapter.getPageTitle(i));
			
		
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
			actionBar.addTab(actionBar.newTab()
					
					
					
					.setTabListener(this).setCustomView(tabView));
		}
		
		
		
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
		relativeLayout.addView(inflatedView);
		
		return (super.onCreateOptionsMenu(menu));
		
		//RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.section_label).getActionView();
		//View inflatedView = getLayoutInflater().inflate(R.layout.actionbar_top, null);
		//relativeLayout.addView(inflatedView);
		
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
	
   /* private class PopulateHighScoresTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... voids) {

                SimpleDBData list = new SimpleDBData();
    //list.createHighScoresDomain();
    /*
                for (int i = 1; i <= 10; i++) {
        String playerName = Constants.getRandomPlayerName();
        int score = Constants.getRandomScore();
                        HighScore hs = new HighScore( playerName, score );
                        
                        list.addHighScore(hs);
                }

                return null;
        }

        protected void onPostExecute(Void result) {

        }
}
*/

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
			// TODO Auto-generated method stub
			
			return 4;
		}
		public String getPageTitle(int position) {
			//Locale l = Locale.getDefault();
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

	  /* private class AddHighScoreTask extends AsyncTask<Listing, Void, Void> {

		   public SimpleDBData hs;
           protected Void doInBackground(Listing... highScores) {
        	   Listing dblisting = new BuyListing();
        	   User my_user = new User("Eric");
        	   Venue my_venue = new Venue("Hell");
        	   dblisting.setVenue(my_venue);
        	   dblisting.setUser(my_user);
       		dblisting.setEndTime("END OF THE WORLD");
       		dblisting.setStartTime("THE BEGINNING");
       		dblisting.setSwipeCount(99);
       		//dblisting.setTime(null);
                  hs = new SimpleDBData();
                  hs.addHighScore(dblisting);
                   

                   return null;
           }
       }
	  /* 
	   private class TestConnect extends AsyncTask<Listing, Void, Void> {

			  public static final int USER_SORT = 1;
		        
		        public static final int VENUE_SORT  = 2;
		        public static final int NO_SORT     = 0;
		        
		        private static final String LISTINGS_DOMAIN = "Listings";
		        
		        private static final String USER_ATTRIBUTE = "Name"; //Not sure what this might fuck up so Im not gonna change it to "users" as it should be
		        private static final String VENUE_ATTRIBUTE = "Venue";
		        
		        private static final String USER_SORT_QUERY = "select player, score from HighScores where player > '' order by player asc";
		        private static final String VENUE_SORT_QUERY = "select player, score from HighScores where score >= '0' order by score desc";
		        private static final String NO_SORT_QUERY = "select player, score from HighScores";
		        
		        private static final String COUNT_QUERY = "select count(*) from HighScores";
		                
		        protected AmazonSimpleDBClient sdbClient;
		        protected String nextToken;
		        protected int sortMethod;
		        protected int count;
		        public String reg_key = "AKIAJWQU5ZV4ZEZHRDWA";
		        public String sec_key = "cgwIKqYn1YoYDhnkqt4oPaizIXdWeHtgNlliBaND";
		        @Override
		        protected Void doInBackground(Listing... params) {
		        	android.os.Debug.waitForDebugger();
		            //for (int i = 0; i < 5; i++) {
		                try {
		                	AWSCredentials credentials = new BasicAWSCredentials( reg_key,sec_key );
		                    this.sdbClient = new AmazonSimpleDBClient( credentials); 
		                    sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
		                    this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
		                    String test = "Listings";
		                    //sdbClient.
		                    ListDomainsResult result = sdbClient.listDomains();
		                    int x = result.getDomainNames().size();
		                    Boolean found = false;
		                    //while(x>0)
		                    //{
		                      //  if(result.getDomainNames().get(x) == test) 
		                        	//found = true;
		                    //} 
		                    
		                    Listing score = new BuyListing();
		             	   User my_user = new User("Eric");
		             	   Venue my_venue = new Venue("Hell");
		             	  score.setVenue(my_venue);
		             	   score.setUser(my_user);
		            		score.setEndTime("END OF THE WORLD");
		            		score.setStartTime("THE BEGINNING");
		            		score.setSwipeCount(99);
		            		//dblisting.setTime(null);
		                       //hs = new SimpleDBData();
		                      // hs.addHighScore(dblisting);
		                    
		                    ReplaceableAttribute playerAttribute = new ReplaceableAttribute( USER_ATTRIBUTE, score.getUser().getName(), Boolean.TRUE );
		                    ReplaceableAttribute scoreAttribute = new ReplaceableAttribute( VENUE_ATTRIBUTE, score.getVenue().getName(), Boolean.TRUE );
		                    
		                    List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(2);
		                    attrs.add( playerAttribute );
		                    attrs.add( scoreAttribute );
		                    
		                    PutAttributesRequest par = new PutAttributesRequest(LISTINGS_DOMAIN, score.getUser().getName(), attrs);                
		                    try {
		                    		//my_count++;
		                    		
		                            this.sdbClient.putAttributes( par );
		                    }
		                    catch ( Exception exception ) {
		                    	
		                            System.out.println( "EXCEPTION = " + exception );
		                    }

		                    //if(!found)
		                    //{
		                        //this.createHighScoresDomain(test);
		                          
		                    //}
		                    //Thread.sleep(1000);
		                } catch (Exception e) {
		                    //e.printStackTrace();
		                }
		            //}
		            return null;
		        }
		  }



	
	
	*/
	
	
}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	



