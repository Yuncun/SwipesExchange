package com.example.tabsfinal;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
//import com.amazonaws.demo.highscores.AddScoreActivity.AddHighScoreTask;

public class MyExpandableAdapterBuy extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childtems;
	private  LayoutInflater inflater;
	private ArrayList<String> parentItems, child;
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
	
	MainActivity mActivity;
	private TestConnect tc;
	

	public MyExpandableAdapterBuy(ExpandableListView parent, ArrayList<String> parents, ArrayList<Object> childern, MainActivity my_activity) {
		this.parentItems = parents;
		this.childtems = childern;
		
		venue = new Venue("");
		minutes_start = 0;
		minutes_end = 0;
		hours_start = 8;
		hours_end = 12;
		venue_name = "De Neve";
		num_swipes=1;
		mActivity = my_activity;
		this.parent = parent;
		
		
		
	
		
	}
	
	 public void setInflater(LayoutInflater inflater, Activity activity) {
		         this.inflater = inflater;
		         this.activity = activity;
		 		
		 		
		        
		
	 }
	 
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
	 //need to catch error if time hasn't been initialized/time
	 //fields haven't been expanded and they try and submit
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
		 else if(hours_24 < 12)
			 return hours_24;
		 else
		 {
			 switch (hours_24)
			 {
			 case 13:
				 return 1;
			 case 14:
				 return 2;
			 case 15:
				 return 3;
			 case 16:
				 return 4;
			 case 17:
				 return 5;
			 case 18:
				 return 6;
			 case 19:
				 return 7;
			 case 20:
				 return 8;
			 case 21:
				 return 9;
			 case 22:
				 return 10;
			 case 23:
				 return 11;
			 case 24:
				 return 12;
			default:
				break;
					 
			 }
			 
		 }
		 return hours_24;
	 }
	 
	 public boolean isPM(int hours_24)
	 {
		 if (hours_24>=12)
			 return true;
		 
		 return false;
	 }
	 


	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		//if (convertView == null) {
			if(groupPosition == 0)
				convertView = inflater.inflate(R.layout.group_ex, null);
			//else if(groupPosition == 1)
				//convertView = inflater.inflate(R.layout.group_ex2, null);
			else if(groupPosition == 1)
				convertView = inflater.inflate(R.layout.group_ex1, null);
			//else if(groupPosition == 3)
				//convertView = inflater.inflate(R.layout.group_ex2, null);
			else if(groupPosition == 2)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			//else if (groupPosition == 5)
				//convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition ==3)
				convertView = inflater.inflate(R.layout.group_ex5, null);
			//else if (groupPosition==7)
				//convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition == 4)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition ==5)
				convertView = inflater.inflate(R.layout.group_ex2, null);
		//}
		if (groupPosition == 0)
		{
			this.tp_start = (TimePicker) convertView.findViewById(R.id.timePicker1);
			
			
			//tp_start.setSaveEnabled(true);
		}
		
		
		if (groupPosition == 1)
		{
			this.tp_end = (TimePicker) convertView.findViewById(R.id.timePicker2);
			
			//tp_start.setSaveEnabled(true);
		}
		
		if(groupPosition == 0)
		{
			tp_start.setCurrentMinute(minutes_start);
			
			tp_start.setCurrentHour(hours_start);
		}
		if(groupPosition == 1)
		{
			tp_end.setCurrentMinute(minutes_end);
			tp_end.setCurrentHour(hours_end);
		}
		
		if (groupPosition == 2)
		{
			child = (ArrayList<String>) childtems.get(groupPosition);
			TextView textView = null;
			//Log.d("Panda", Integer.toString(getStartMinutes()));
			textView = (TextView) convertView.findViewById(R.id.textView1ex);
			textView.setText(child.get(childPosition));
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					//Toast.makeText(activity, child.get(childPosition),
							//Toast.LENGTH_SHORT).show();
					setVenueName(child.get(childPosition));
					getParent().collapseGroup(2);
					
				}
			});
			
		}
		
		if(groupPosition == 3)
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
			
				
				//if(groupPosition==4)
					//convertView = inflater.inflate(R.layout.row_ex3, null);
				//else
			//if (groupPosition==9)
				//convertView = inflater.inflate(R.layout.row_ex2, null);
			//else
					convertView = inflater.inflate(R.layout.row_ex, null);
		}

		
		
		 ////if (getChildrenCount(groupPosition)== 0) {
	           // convertView.setEnabled(false); //Greys out group name
	        //} 
		//((CheckedTextView) convertView.findViewById(R.id.textView1Checked)).setText(parentItems.get(groupPosition));
		//((CheckedTextView) convertView.findViewById(R.id.textView1Checked)).setChecked(isExpanded);
		
		//checkedView.setText(parentItems.get(groupPosition));
		//checkedView.setChecked(isExpanded);
		//Time stime = null;
		//stime.hour = getStartHours();
		//stime.minute = getStartMinutes();
		//SimpleDateFormat df = new SimpleDateFormat("mm:ss");
		
		
		if(groupPosition == 0)
		{
		String mystring = String.format("%d:%02d", fixHours(getStartHours()), getStartMinutes());
		//df.format()
			if(isPM(getStartHours()))
			{
				String end_str = mystring + "PM";
				String start_str = parentItems.get(groupPosition) + "                                 ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				//int my_color = 5882874;
				
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
				
				//SpannableStringBuilder mystringbuilder = new SpannableStringBuilder(whole);
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//((CheckedTextView) convertView).setText(mystring);
				//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
				((CheckedTextView) convertView).setText(str);
				//((TextView) convertView).setOnClickListener(null);
				//((TextView) convertView).setClickable(false);
				
			}
			else
			{
				String end_str = mystring + "AM";
				String start_str = parentItems.get(groupPosition) + "                                 ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				//int my_color = 5882874;
				
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
				
				//SpannableStringBuilder mystringbuilder = new SpannableStringBuilder(whole);
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
				((CheckedTextView) convertView).setText(str);
				
			}
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		
		//((TextView) convertView).setChecked(isExpanded);
		}
		else if(groupPosition == 2)
		{
			String mystring = getVenueName();
			String end_str = mystring;
			String start_str = parentItems.get(groupPosition) + "                                        ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			//int my_color = 5882874;
			
			int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
					//getResources().
			//my_color.

			str.setSpan(

			    new ForegroundColorSpan(my_color), 

			    start, 

			    end, 

			    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			//SpannableStringBuilder mystringbuilder = new SpannableStringBuilder(whole);
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
		//df.format()
			if(isPM(getEndHours()))
			{
				String end_str = mystring + "PM";
				String start_str = parentItems.get(groupPosition) + "                                   ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				//int my_color = 5882874;
				
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end,  

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
				
				//SpannableStringBuilder mystringbuilder = new SpannableStringBuilder(whole);
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
				((CheckedTextView) convertView).setText(str);
			}
			else
			{
				String end_str = mystring + "AM";
				String start_str = parentItems.get(groupPosition) + "                                   ";
				String whole = start_str + end_str;
				int start = whole.indexOf(end_str);
				int end = whole.length();
				SpannableStringBuilder str = new SpannableStringBuilder(whole);
				//int my_color = 5882874;
				
				int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
				
				//SpannableStringBuilder mystringbuilder = new SpannableStringBuilder(whole);
				 end = start_str.length();
				 start = whole.indexOf(start_str);
				str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
				((CheckedTextView) convertView).setText(str);
			}
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
	
		//((TextView) convertView).setChecked(isExpanded);
		}
		else if(groupPosition==3)
		{
			String my_swipes = String.valueOf(getNumSwipes());
			String mystring = my_swipes + " swipe(s)";
			String end_str = mystring;
			String start_str = parentItems.get(groupPosition) + "                                    ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			//int my_color = 5882874;
			
			int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
					//getResources().
			//my_color.

			str.setSpan(

			    new ForegroundColorSpan(my_color), 

			    start, 

			    end, 

			    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

			);
			
			//SpannableStringBuilder mystringbuilder = new SpannableStringBuilder(whole);
			 end = start_str.length();
			 start = whole.indexOf(start_str);
			 str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
			 ((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setText(str);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if (groupPosition==4)
		{
			((CheckedTextView) convertView).setPadding(0, 5, 0, 5);
			((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			//((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition ==5)
		{
			((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
			//((CheckedTextView) convertView).setChecked(isExpanded);
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
						//inflate error dialog
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
						//TextView submit_title = (TextView) view.findViewById(R.id.dialogTitle1);
						
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
		                    	
		                    	
		                    	
		                        submit_dialog.dismiss();
		                        //TODO: direct data to database
		 
		                    }
		                });
						
						
						submit_dialog.show();
					}
					
					
					
				}
			});
			
		}
		else
		{
			((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
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
		return ((ArrayList<String>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
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
    /**ADD LISTING**/
    private class AddHighScoreTask extends AsyncTask<Listing, Void, Void> {

            protected Void doInBackground(Listing... highScores) {

                    SimpleDBData hs = new SimpleDBData();
                    hs.addHighScore(highScores[0]);

                    return null;
            }

//This onPostExecute is not applicable to our fragment layout. Normally this closes the addListing activity, returning the user to the original screen.
/*
            protected void onPostExecute(Void result) {

                    AddScoreActivity.this.finish();
            }*/
            

    }
    private class TestConnect extends AsyncTask<Listing, Void, Void> {

		  public static final int USER_SORT = 1;
	        
	        public static final int VENUE_SORT  = 2;
	        public static final int NO_SORT     = 0;
	        
	        private static final String BUY_LISTINGS_DOMAIN = "BuyListings";
	        
	        private static final String USER_ATTRIBUTE = "Name"; //Not sure what this might fuck up so Im not gonna change it to "users" as it should be
	        private static final String VENUE_ATTRIBUTE = "Venue";
	        private static final String NUM_ATTRIBUTE = "Num";
	        private static final String ENDTIME_ATTRIBUTE = "EndTime";
	        private static final String STARTTIME_ATTRIBUTE = "StartTime";
	        
	        
	        private static final String USER_SORT_QUERY = "select player, score from HighScores where player > '' order by player asc";
	        private static final String VENUE_SORT_QUERY = "select player, score from HighScores where score >= '0' order by score desc";
	        private static final String NO_SORT_QUERY = "select player, score from HighScores";
	        
	        private static final String COUNT_QUERY = "select count(*) from HighScores";
	                
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
	                    //String test = "Listings";
	                    //sdbClient.
	                   // ListDomainsResult result = sdbClient.listDomains();
	                    //int x = result.getDomainNames().size();
	                   // Boolean found = false;
	                    //while(x>0)
	                    //{
	                      //  if(result.getDomainNames().get(x) == test) 
	                        	//found = true;
	                    //} 
	                    
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
	            		
	            		//score.isSection = false;
	            		//dblisting.setTime(null);
	                       //hs = new SimpleDBData();
	                      // hs.addHighScore(dblisting);
	                    
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

}