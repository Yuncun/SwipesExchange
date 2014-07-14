package com.swipesexchange;

import java.lang.reflect.Field;

import sharedObjects.Message;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectionFragment extends Fragment implements ActionBar.TabListener {

	private static final int NEW_LISTING_FRAGMENT = 0;
	private static final int NEW_LISTING_BUY_FRAGMENT = 1;

	private LoginFragment loginFragment;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ActionBar actionBar;
	private static MessagesFragment m_frag;
	public BackendData l;
	
	private static final int NUM_FRAGMENTS = 5;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private Fragment[] fragments = new Fragment[NUM_FRAGMENTS];
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.nested_fragment, container, false);
	    
	    
	  
	    
	    /*LinearLayout ll = new LinearLayout(getActivity());
	    
	    
	    
	    Fragment new_listing_fragment = new NewListingFragment();
	    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
	    transaction.add(R.id.new_listing_fragment, new_listing_fragment);*/
	    
	    /*Fragment new_listing_fragment_buy = new NewListingFragmentBuy();
	   
	   
	    transaction.add(R.id.new_listing_fragment_buy, new_listing_fragment_buy);
	    
	    */
		
	    
		
		/*
		fragments[] = fragment_manager.findFragmentById(R.id.splash_fragment);
		fragments[TEST] = fragment_manager.findFragmentById(R.id.selection_fragment);
		fragments[SETTINGS] = fragment_manager.findFragmentById(R.id.userSettingsFragment);
		
		FragmentTransaction transaction = fragment_manager.beginTransaction();
		for(int i=0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		*/
		//transaction.commit();
	    
actionBar = getActivity().getActionBar();
		 

		// Set up the action bar.
		
		//actionBar.setIcon(R.drawable.yes);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.show();
		//tp = (TimePicker) findViewById(R.id.timePicker1);
		
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_grey)));
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), (MainActivity) getActivity());;
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) view.findViewById(R.id.v_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(5);
		
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
			View tabView = getActivity().getLayoutInflater().inflate(R.layout.tab_layout, null);
			
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
			
			int imageResource = R.drawable.actionbar_tab_indicator;
			Drawable image = getResources().getDrawable(imageResource);
			//actionBar.setBackgroundDrawable(image);
			//actionBar.setSplitBackgroundDrawable(image);
			actionBar.setStackedBackgroundDrawable(image);
			
			
			
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
		
	    
		return view;
	}
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		*/
		
	
		
		/*
		if(Session.getActiveSession().isOpened())
		{
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		transaction.hide(loginFragment);
		transaction.commit();
		}
		*/
		
		
		
	//}
	
	
	/*
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * onSessionStateChange
	 * Performs logout if sesion state changes (ie logged out of Facebook)
	 */

	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.layout_item).getActionView();
		View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.actionbar_top, null);
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
		
		 super.onCreateOptionsMenu(menu,inflater);
		
		
		
	}
	
	   public void refreshConversationFragment() {
		   Fragment fragment = m_frag;
		   if(fragment != null)
		   {
			   MessagesFragment m_frag = (MessagesFragment) fragment;
			   m_frag.updateFragmentWithMessage();
			  
		   }
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
		public void destroyItem(ViewGroup container, int position, Object object) {
		    Log.d("LOUD AND CLEAR", "destroy!");
		    
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page
		
			switch(position)
			{
			case 0:
				return ListingsList.newInstance(position, mActivity, l);
			case 1:
				return ListingsList.newInstance(position, mActivity, l);
			case 2:
				return NewListingFragmentBuy.newInstance(position, mActivity);
			case 3:
				return NewListingFragment.newInstance(position, mActivity);
			case 4:
				m_frag = MessagesFragment.newInstance(position, mActivity);
				return m_frag;
		
			
			}
				return null;
			}

		@Override
		public int getCount() {
			// Return the number of fragment pages in the application
			
			// TODO: Change back
			return NUM_FRAGMENTS;
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
			case 4:
				return getString(R.string.title_section6);
			
			}
			return null;
		}
	}



	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// When the given tab is selected, switch to the corresponding page in
				// the ViewPager.
				mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	 @Override
	    public void onDetach() {
	        super.onDetach();
	        try {
	            Field childFragmentManager = Fragment.class
	                    .getDeclaredField("mChildFragmentManager");
	            childFragmentManager.setAccessible(true);
	            childFragmentManager.set(this, null);
	        } catch (NoSuchFieldException e) {
	            throw new RuntimeException(e);
	        } catch (IllegalAccessException e) {
	            throw new RuntimeException(e);
	        }
	    }
	
	


}
