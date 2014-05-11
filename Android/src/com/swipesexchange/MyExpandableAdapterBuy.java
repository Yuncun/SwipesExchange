package com.swipesexchange;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sharedObjects.BuyListing;
import sharedObjects.Listing;
import sharedObjects.MsgStruct;
import sharedObjects.User;
import sharedObjects.Venue;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.google.gson.Gson;
/**
 * 
 * MyExpandableAdapterBuy
 * 
 * Adapter for NewListingFragmentBuy.  Feeds data to the
 * expandable list view. The expandable list view contains items
 * that will modified by a user to allow for the submission of a BuyListing
 * 
 * 
 * @author Kyle
 *
 */
public class MyExpandableAdapterBuy extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> child_items;
	private  LayoutInflater inflater;
	private ArrayList<String> parent_items, child;
	private int minutes_start, minutes_end, hours_start, hours_end;
	private TimePicker tp_start, tp_end;
	private int num_swipes;
	private NumberPicker swipes;
	private Venue venue;
	private String venue_name;
	private ExpandableListView parent;
	
	Button yes_button;
	Button cancel_button;
	Dialog submit_dialog;
	
	Button ok_button;
	Dialog time_error_dialog;
	
	private TestConnect tc;
	

	public MyExpandableAdapterBuy(ExpandableListView parent, ArrayList<String> parents, ArrayList<Object> childern) {
		this.parent_items = parents;
		this.child_items = childern;
		
		venue = new Venue("");
		//TODO Change default times to current time + ~3 hours
		minutes_start = 0;
		minutes_end = 0;
		hours_start = 8;
		hours_end = 12;
		venue_name = "De Neve";
		num_swipes=1;
		this.parent = parent;
		
		
		
	
		
	}
	
	 public void setInflater(LayoutInflater inflater, Activity activity) {
		         this.inflater = inflater;
		         this.activity = activity;
		 		
		 		
		        
		
	 }
	 
	 // Data retrieval and data setting methods
	 
	 public int getNumSwipes()
	 {
		 return num_swipes;
	 }
	 
	 public void setNumSwipes(int num)
	 {
		 num_swipes = num;
	 }
	 
	 
	 public ExpandableListView getParent()
	 {
		 return parent;
	 }
	 
	 public void setVenueName (String name) {
		 venue_name = name;
	 }
	 public String getVenueName () {
		 return venue_name;
	 }
	 
	 public int getStartMinutes() {
		return minutes_start;
	 }
	 
	 public void setStartMinutes(int num) {
		 minutes_start = num;
	 }
	 
	 public int getEndMinutes() {
		 return minutes_end;
	 }
	 
	 public void setEndMinutes(int num) {
		 minutes_end = num;
	 }
	 public int getStartHours() {
		 return hours_start;
	 }
	 
	 public void setStartHours(int num)
	 {
		 hours_start = num;
	 }
	 
	 public int getEndHours() {
		 return hours_end;
	 }
	 
	 public void setEndHours(int num) {
		 hours_end = num;
	 }
	 
	 public int fixHours(int hours_24)
	 {
		 if (hours_24 == 0)
			 return 12;
		 else if(hours_24 <= 12)
			 return hours_24;
		 else
		 {
			return hours_24 - 12;
			 
		 }
	 }
	 
	 public boolean isPM(int hours_24)
	 {
		 if (hours_24>=12)
			 return true;
		 
		 return false;
	 }
	 
	 
	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		
			if(groupPosition == 0)
				convertView = inflater.inflate(R.layout.group_ex, null);
			else if(groupPosition == 1)
				convertView = inflater.inflate(R.layout.group_ex1, null);
			else if(groupPosition == 2)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition ==3)
				convertView = inflater.inflate(R.layout.group_ex5, null);
			else if (groupPosition == 4)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition ==5)
				convertView = inflater.inflate(R.layout.group_ex2, null);
		
		if (groupPosition == 0) // Picking the start time
		{
			// Initialize the TimePicker
			this.tp_start = (TimePicker) convertView.findViewById(R.id.timePicker1);
			
			// TODO: Change to current time
			tp_start.setCurrentMinute(minutes_start);
			tp_start.setCurrentHour(hours_start);
		}
		
		
		
		if(groupPosition == 1) //Picking the end time
		{
			// Initialize the TimePicker
			this.tp_end = (TimePicker) convertView.findViewById(R.id.timePicker2);
			
			// TODO: Change to current time + ~3 hours
			tp_end.setCurrentMinute(minutes_end);
			tp_end.setCurrentHour(hours_end);
		}
		
		if (groupPosition == 2) //Picking the venue
		{
		
			// Unchecked cast is necessary
			child = (ArrayList<String>) child_items.get(groupPosition);
			TextView textView = null;
			
			textView = (TextView) convertView.findViewById(R.id.textView1ex);
			textView.setText(child.get(childPosition));
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					setVenueName(child.get(childPosition));
					getParent().collapseGroup(2);
					
				}
			});
			
		}
		
		if(groupPosition == 3) //Picking the # of desired swipes
		{
			this.swipes = (NumberPicker) convertView.findViewById(R.id.numberPickerSwipes);
			swipes.setMinValue(1);
			swipes.setMaxValue(9);
			swipes.setValue(num_swipes);
		}
			
		return convertView;
	}
	
	


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			
					convertView = inflater.inflate(R.layout.row_ex, null);
		}

		
		if(groupPosition == 0)
		{
		String mystring = String.format("%d:%02d", fixHours(getStartHours()), getStartMinutes());
		
			if(isPM(getStartHours()))
			{
				//TODO: Better method of creating a group item string
				String end_str = mystring + "PM";
				String start_str = parent_items.get(groupPosition) + "                                 ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);

				str.setSpan(new ForegroundColorSpan(my_color),start,end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				((CheckedTextView) convertView).setText(str);
				
			}
			else
			{
				String end_str = mystring + "AM";
				String start_str = parent_items.get(groupPosition) + "                                 ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);

				str.setSpan(new ForegroundColorSpan(my_color), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				((CheckedTextView) convertView).setText(str);
				
			}
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition == 2)
		{
			String mystring = getVenueName();
			String end_str = mystring;
			String start_str = parent_items.get(groupPosition) + "                                        ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);

			str.setSpan(new ForegroundColorSpan(my_color), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			
			 end = start_str.length();
			 start = whole.indexOf(start_str);
			 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			((CheckedTextView) convertView).setText(str);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition == 1)
		{
		String mystring = String.format("%d:%02d", fixHours(getEndHours()), getEndMinutes());
		
			if(isPM(getEndHours()))
			{
				String end_str = mystring + "PM";
				String start_str = parent_items.get(groupPosition) + "                                   ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				
				
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);

				str.setSpan(new ForegroundColorSpan(my_color), start, end,  SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				((CheckedTextView) convertView).setText(str);
			}
			else
			{
				String end_str = mystring + "AM";
				String start_str = parent_items.get(groupPosition) + "                                   ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				
				
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);

				str.setSpan(new ForegroundColorSpan(my_color), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				((CheckedTextView) convertView).setText(str);
			}
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition==3)
		{
			String my_swipes = String.valueOf(getNumSwipes());
			String mystring = my_swipes + " swipe(s)";
			String end_str = mystring;
			String start_str = parent_items.get(groupPosition) + "                                    ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			
			
			int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
			
			str.setSpan(new ForegroundColorSpan(my_color), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			 end = start_str.length();
			 start = whole.indexOf(start_str);
			 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			 ((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setText(str);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if (groupPosition==4)
		{
			((CheckedTextView) convertView).setPadding(0, 5, 0, 5);
			((CheckedTextView) convertView).setText(parent_items.get(groupPosition));
			
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition ==5)
		{
			((CheckedTextView) convertView).setText(parent_items.get(groupPosition));
			
			((CheckedTextView) convertView).setPadding(0, 10, 0, 10);
			((CheckedTextView) convertView).setTypeface(null, Typeface.BOLD);
			((CheckedTextView) convertView).setGravity(Gravity.CENTER_HORIZONTAL);
			
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					boolean cancel = false;
					if((getStartHours()>getEndHours()) || (getStartHours()==getEndHours() && getStartMinutes()>getEndMinutes()))
					{
						cancel = true;
						time_error_dialog = new Dialog(inflater.getContext(), R.style.cust_dialog);
						time_error_dialog.setContentView(R.layout.dialog_time_error);
						ok_button = (Button) time_error_dialog.findViewById(R.id.Ok_Button);
						time_error_dialog.setTitle("One does not finish eating until they've begun...");
						
						
						ok_button.setOnClickListener(new View.OnClickListener() {
							 
		                    @Override
		                    public void onClick(View view) {
		                        
		                        time_error_dialog.dismiss();
		 
		                    }
		                });
						
						time_error_dialog.show();
						
						
					}
			
					if(!cancel)
					{
						submit_dialog = new Dialog(inflater.getContext(), R.style.cust_dialog);
						submit_dialog.setContentView(R.layout.dialog_submit);
						cancel_button = (Button) submit_dialog.findViewById(R.id.Cancel_Button);
				 		yes_button = (Button) submit_dialog.findViewById(R.id.Yes_Button);
						
						submit_dialog.setTitle("Submit new listing?");
						
						
						cancel_button.setOnClickListener(new View.OnClickListener() {
							 
		                    @Override
		                    public void onClick(View view) {
		                        
		                        submit_dialog.dismiss();
		 
		                    }
		                });
						
						yes_button.setOnClickListener(new View.OnClickListener() {
							 
		                    @Override
		                    public void onClick(View view) {
		                    	
		                    	tc = new TestConnect();
		                    	BuyListing bl = new BuyListing();
		                    	tc.execute(bl);
		                    	
		                    	//Create a new sellListing for testing
		                    	BuyListing sl = new BuyListing();
		                    	sl.setEndTime("9:23");
		                    	sl.setStartTime("9:20");
		                    	//sl.setPrice(5.00);
		                    	sl.setSwipeCount(3);
		                    	
		                    	Venue ven = new Venue("De Neve");
		                    	sl.setVenue(ven);
		                    	User usr = new User("David Beckham"); 
		                    	sl.setUser(usr);
		                    	
		                    	sl.setSection("Section?");
		                    	
		                    	//
		                    	Gson gson = new Gson();
		                    	
		                    	MsgStruct nMsg = new MsgStruct();
		                    	nMsg.packeType = "bl"; //Identifies message as a sell listing
		                    	nMsg.payload = sl;
		                    	
		                    	
		                    	String json = gson.toJson(nMsg);
		                    	
		                    	
		                    	//
		                    	String slString = "******* Jose Mourinho **********";
		                    	ConnectToServlet.sendListing(json);
		                    	 Log.d("LOUD AND CLEAR", "ConnectToServlet.sendListing is reached in MyExpandableAdapterBuy");
		                    	
		                    	
		                    	//Test after testing sendListing(SellListing sellList);
		                    	//ConnectToServlet.talk("HELLO FROM NEW SWIPES");
		                    	
		                    	
		                        submit_dialog.dismiss();
		 
		                    }
		                });
						
						
						submit_dialog.show();
					}
					
				}
			});
			
		}
		else
		{
			((CheckedTextView) convertView).setText(parent_items.get(groupPosition));
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) child_items.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parent_items.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
		if(groupPosition == 0)
		{
			minutes_start = tp_start.getCurrentMinute();
			hours_start = tp_start.getCurrentHour();
		}
		if (groupPosition == 1)
		{
			minutes_end = tp_end.getCurrentMinute();
			hours_end = tp_end.getCurrentHour();
		}
		if(groupPosition==2)
		{
			venue.setName(venue_name);
		}
		if(groupPosition==3)
		{
			num_swipes = swipes.getValue();
		}
	
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		
		super.onGroupExpanded(groupPosition);
		
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
  

//This onPostExecute is not applicable to our fragment layout. Normally this closes the addListing activity, returning the user to the original screen.
/*
            protected void onPostExecute(Void result) {

                    AddScoreActivity.this.finish();
            }*/
            

    
    private class TestConnect extends AsyncTask<Listing, Void, Void> {

		  
	        
	   
	        
	        private static final String BUY_LISTINGS_DOMAIN = "BuyListings";
	        
	        private static final String USER_ATTRIBUTE = "Name"; 
	        private static final String VENUE_ATTRIBUTE = "Venue";
	        private static final String NUM_ATTRIBUTE = "Num";
	        private static final String ENDTIME_ATTRIBUTE = "EndTime";
	        private static final String STARTTIME_ATTRIBUTE = "StartTime";
	        
	        
	                
	        protected AmazonSimpleDBClient sdbClient;
	        protected String nextToken;
	        protected int sortMethod;
	        protected int count;
	        
	        @Override
	        protected Void doInBackground(Listing... params) {
	        	
	            
	                try {
	                	AWSCredentials credentials = new BasicAWSCredentials(Constants.getRegKey(),Constants.getSecKey());
	                    this.sdbClient = new AmazonSimpleDBClient( credentials); 
	                    sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
	                    this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	              
	                    
	                    Listing score = new BuyListing();
	             	   User my_user = new User("Thomas Muller");
	             	   Venue my_venue = new Venue(getVenueName());
	             	  score.setVenue(my_venue);
	             	   score.setUser(my_user);
	             	  String mystring = String.format("%d:%02d", fixHours(getStartHours()), getStartMinutes());
	             	 String mystring2 = String.format("%d:%02d", fixHours(getEndHours()), getEndMinutes());
	             	   if(getEndHours()>=12)
	             		   score.setEndTime(mystring2 + "PM");
	             	   else
	             		   score.setEndTime(mystring2 + "AM");
	             	   if(getStartHours()>=12)
	             		   score.setStartTime(mystring + "PM");
	             	   else
	             		   score.setStartTime(mystring + "AM");
	            		score.setSwipeCount(getNumSwipes());
	            		
	            	
	                    
	                    ReplaceableAttribute playerAttribute = new ReplaceableAttribute( USER_ATTRIBUTE, score.getUser().getName(), Boolean.TRUE );
	                    ReplaceableAttribute scoreAttribute = new ReplaceableAttribute( VENUE_ATTRIBUTE, score.getVenue().getName(), Boolean.TRUE );
	                    ReplaceableAttribute swipesAttribute = new ReplaceableAttribute( NUM_ATTRIBUTE, Integer.toString(score.getSwipeCount()), Boolean.TRUE );
	                    ReplaceableAttribute endTimeAttribute = new ReplaceableAttribute( ENDTIME_ATTRIBUTE, score.getEndTime(), Boolean.TRUE );
	                    ReplaceableAttribute startTimeAttribute = new ReplaceableAttribute( STARTTIME_ATTRIBUTE, score.getStartTime(), Boolean.TRUE );
	                    
	                    List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(5);
	                    attrs.add( playerAttribute );
	                    attrs.add( scoreAttribute );
	                    attrs.add( swipesAttribute );
	                    attrs.add( endTimeAttribute );
	                    attrs.add( startTimeAttribute );
	                    
	                    Calendar c = Calendar.getInstance(); 
	                    int day = c.get(Calendar.DAY_OF_MONTH);
	                    String dayString = Integer.toString(day);
	                    int month = c.get(Calendar.MONTH);
	                    String monthString = Integer.toString(month);
	                    int year = c.get(Calendar.YEAR);
	                    String yearString = Integer.toString(year);
	                    long milli = System.currentTimeMillis();
	                    String milliString = Long.toString(milli);
	                    
	                    String uID = dayString + monthString + yearString + milliString + score.getUser().getName();
	                    
	                    
	                    
	                    PutAttributesRequest par = new PutAttributesRequest(BUY_LISTINGS_DOMAIN, uID, attrs);                
	                    try {
	                    		
	                    		
	                            this.sdbClient.putAttributes( par );
	                    }
	                    catch ( Exception exception ) {
	                    	
	                            System.out.println( "EXCEPTION = " + exception );
	                            
	                    }

	               
	                } catch (Exception e) {
	                    
	                }
	           
	            return null;
	        }
	  }

}