package com.swipesexchange;


import java.util.ArrayList;
import android.text.format.Time;
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
import android.widget.RelativeLayout;
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
public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private  LayoutInflater inflater;
	private int minutes_start, minutes_end, hours_start, hours_end;
	private TimePicker tp_start, tp_end;
	private int num_swipes;
	private NumberPicker swipes, dollars, cents;
	private Venue venue;
	private String venue_name;
	private ExpandableListView parent_list_view;
	private int num_dollars, num_cents;
	
	
	// ParentRows passed in from NewListingFragmentBuy
	final private ArrayList<ParentRow> parents;
	 
	Button yes_button;
	Button cancel_button;
	Dialog submit_dialog;
	
	Button ok_button;
	Dialog time_error_dialog;
	

	public MyExpandableAdapter(ExpandableListView parent, ArrayList<ParentRow> parents) {
		this.parents = parents;
		this.venue = new Venue("Any");
		// set the default times in the TimePickers based off of current time
		Time now = new Time();
		now.setToNow();
		
		this.minutes_start = now.minute;
		this.minutes_end = now.minute;
		this.hours_start = now.hour;
		this.hours_end = now.hour + 3;
		this.venue_name = "Any";
		this.num_swipes=1;
		this.num_cents = 0;
		this.num_dollars = 0;
		this.parent_list_view = parent;
		
		// Update the parent rows with information about default values to be shown in groupViews
		for(int i=0; i<5; i++)
			this.updateParentsRightViews(i);
	}
	
	 public void setInflater(LayoutInflater inflater, Activity activity) {
		         this.inflater = inflater;
		         this.activity = activity;  
	 }
	 
	 // Data retrieval and data setting methods
	 
	 public int getNumSwipes()
	 {
		 return this.num_swipes;
	 }
	 
	 public void setNumSwipes(int num)
	 {
		 this.num_swipes = num;
	 }
	 
	 
	 public ExpandableListView getParent()
	 {
		 return this.parent_list_view;
	 }
	 
	 public void setVenueName (String name) {
		 this.venue_name = name;
	 }
	 public String getVenueName () {
		 return this.venue_name;
	 }
	 
	 public int getStartMinutes() {
		return this.minutes_start;
	 }
	 
	 public void setStartMinutes(int num) {
		 this.minutes_start = num;
	 }
	 
	 public int getEndMinutes() {
		 return this.minutes_end;
	 }
	 
	 public void setEndMinutes(int num) {
		 this.minutes_end = num;
	 }
	 public int getStartHours() {
		 return this.hours_start;
	 }
	 
	 public void setStartHours(int num)
	 {
		 this.hours_start = num;
	 }
	 
	 public int getEndHours() {
		 return this.hours_end;
	 }
	 
	 public void setEndHours(int num) {
		 this.hours_end = num;
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
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		
	
		// TODO: recycle views instead of always inflating
		
		
		convertView = null;
		if(convertView==null)
		{
			if (groupPosition == 0) // Picking the start time
			{
				// Inflate the view
				convertView = inflater.inflate(R.layout.group_ex, null);
				// Initialize the TimePicker
				this.tp_start = (TimePicker) convertView.findViewById(R.id.timePicker1);
				
				// TODO: Change to current time
				tp_start.setCurrentMinute(minutes_start);
				tp_start.setCurrentHour(hours_start);
			}
			else if (groupPosition == 1) // Picking the end time
			{
				convertView = inflater.inflate(R.layout.group_ex1, null);
				// Initialize the TimePicker
				this.tp_end = (TimePicker) convertView.findViewById(R.id.timePicker2);
				
				// TODO: Change to current time
				tp_end.setCurrentMinute(minutes_end);
				tp_end.setCurrentHour(hours_end);
			}
			else if (groupPosition == 2) // Picking the venue
			{
				convertView = inflater.inflate(R.layout.group_ex2, null);
				// Unchecked cast is necessary
				TextRow child = (TextRow) parents.get(groupPosition).getChildren().get(childPosition);
				
				
				((TextView) convertView.findViewById(R.id.textView1ex)).setText(child.getText());
				
				final String child_text = child.getText();
				// when user selects a child Venue, modify the groupView and collapse the children
				convertView.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View view) {
						setVenueName(child_text);
						getParent().collapseGroup(2);
					}
				});
				
				
			}
			else if(groupPosition == 3) // Picking the number of swipes
			{
				convertView = inflater.inflate(R.layout.group_ex3, null);
				// Initialize the NumberPicker
				this.swipes = (NumberPicker) convertView.findViewById(R.id.numberPickerSwipes);
				this.swipes.setMinValue(1);
				this.swipes.setMaxValue(9);
				this.swipes.setValue(num_swipes);
			}
			else if(groupPosition == 4)
			{
				convertView = inflater.inflate(R.layout.group_ex6, null);
				// Initialize the NumberPickers
				this.cents = (NumberPicker) convertView.findViewById(R.id.numberPicker2);
				this.dollars = (NumberPicker) convertView.findViewById(R.id.numberPicker1);
				this.cents.setMinValue(0);
				this.cents.setMaxValue(59);
				this.dollars.setMinValue(0);
				this.dollars.setMaxValue(20);
				this.cents.setValue(this.num_cents);
				this.dollars.setValue(this.num_dollars);
				
			}
			else if(groupPosition == 5)
			{
				convertView = inflater.inflate(R.layout.group_ex4, null);
				
			}
			else if(groupPosition == 6)
			{
				convertView = inflater.inflate(R.layout.group_ex5, null);
				
			}
			
		
		
		}
	
		return convertView;
	}
	


	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		
		convertView = null;
		if (convertView == null) {
			
			if(groupPosition==0)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex, parent, false);
			
				((CheckedTextView) convertView.findViewById(R.id.group1_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group1_text_right)).setText(parents.get(groupPosition).getTextRight());
				
			
				
			}
			else if(groupPosition==1)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex2, parent, false);
			
				
				((CheckedTextView) convertView.findViewById(R.id.group2_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group2_text_right)).setText(parents.get(groupPosition).getTextRight());
			
				
			}
			else if(groupPosition==2)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex3, parent, false);
			
				((CheckedTextView) convertView.findViewById(R.id.group3_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group3_text_right)).setText(parents.get(groupPosition).getTextRight());
			
				
			}
			else if(groupPosition==3)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex4, parent, false);
			
				((CheckedTextView) convertView.findViewById(R.id.group4_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group4_text_right)).setText(parents.get(groupPosition).getTextRight());
			
				
			}
			else if(groupPosition==4)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex7, parent, false);
			
				((CheckedTextView) convertView.findViewById(R.id.group7_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group7_text_right)).setText(parents.get(groupPosition).getTextRight());
			
				
			}
			else if(groupPosition==5)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex5, parent, false);
				
				((CheckedTextView) convertView.findViewById(R.id.group5_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group5_text_right)).setText(parents.get(groupPosition).getTextRight());
				
			
				
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						
						getParent().collapseGroup(5);
					}
				});
		
				
			}
			else if(groupPosition==6)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex6, parent, false);
			
				
				((CheckedTextView) convertView.findViewById(R.id.group6_text_left)).setText(parents.get(groupPosition).getTextLeft());
				((TextView) convertView.findViewById(R.id.group6_text_right)).setText(parents.get(groupPosition).getTextRight());
				
				
				
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
			                    	//TODO: modify to submit sell listing
			                    	//Create a new sellListing for testing
			                    	BuyListing sl = new BuyListing();
			                    	sl.setEndTime("9:23");
			                    	sl.setStartTime("9:20");
			                    	//sl.setPrice(5.00);
			                    	sl.setSwipeCount(3);

			                    	sl.setTime("9:30");

			                    	Venue ven = new Venue("De Neve");
			                    	sl.setVenue(ven);
			                    	User usr = new User("David Beckham"); 
			                    	sl.setUser(usr);
			                    	sl.getUser().setUID("123123");
			                    	sl.getUser().setRating("Shit");
			                    	sl.getUser().setConnections("Connections");
			                    	sl.isSection = false;

			                    	sl.setSection("random");

			                    	//
			                    	Gson gson = new Gson();
			                    	String j0 = gson.toJson(sl);

			                    	MsgStruct nMsg = new MsgStruct();
			                    	nMsg.setHeader(1); //Identifies message as a sell listing
			                    	nMsg.setPayload(j0);

			                    	String j1 = gson.toJson(nMsg);

			                    	MsgStruct j2 = gson.fromJson(j1, MsgStruct.class);
			                    	//BuyListing j2 = gson.fromJson(j1, BuyListing.class);

			                    	String j3 = j2.getPayload();

			                    	BuyListing j4 = gson.fromJson(j3, BuyListing.class);



			                    	String j5 = j4.getUser().getName();

			                    	ConnectToServlet.sendListing(j1);

			                    	//ConnectToServlet.updateBList();



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
	
		}
		
		return convertView;
	}

	public void updateParentsRightViews(int groupPosition) {
		if(groupPosition==0)
		{
			String start_time = String.format("%d:%02d", fixHours(this.hours_start), this.minutes_start);
			// add PM/AM
			if(this.isPM(this.hours_start))
				start_time = start_time + "PM";
			else
				start_time = start_time + "AM";
			// set the text field
			this.parents.get(groupPosition).setTextRight(start_time);
		}
		else if(groupPosition==1)
		{
			String end_time = String.format("%d:%02d", fixHours(this.hours_end), this.minutes_end);
			// add PM/AM
			if(this.isPM(this.hours_end))
				end_time = end_time + "PM";
			else
				end_time = end_time + "AM";
			// set the text field
			this.parents.get(groupPosition).setTextRight(end_time);
		}
		else if(groupPosition==2)
		{
			String venue = this.venue.getName();
			this.parents.get(groupPosition).setTextRight(venue);
		}
		else if(groupPosition==3)
		{
			String swipes_req = Integer.toString(this.getNumSwipes());
			this.parents.get(groupPosition).setTextRight(swipes_req);
		}
		else if(groupPosition==4)
		{
			String price_str = "$" + String.format("%d.%02d", this.num_dollars, this.num_cents);
			this.parents.get(groupPosition).setTextRight(price_str);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return parents.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getChildrenCount(int groupPosition) {
		return parents.get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.parents.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parents.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
		if(groupPosition == 0)
		{
			this.minutes_start = tp_start.getCurrentMinute();
			this.hours_start = tp_start.getCurrentHour();
			this.updateParentsRightViews(groupPosition);
		}
		if (groupPosition == 1)
		{
			this.minutes_end = tp_end.getCurrentMinute();
			this.hours_end = tp_end.getCurrentHour();
			this.updateParentsRightViews(groupPosition);
		}
		if(groupPosition==2)
		{
			this.venue.setName(this.venue_name);
			this.updateParentsRightViews(groupPosition);
		}
		if(groupPosition==3)
		{
			this.num_swipes = swipes.getValue();
			this.updateParentsRightViews(groupPosition);
		}
		if(groupPosition==4)
		{
			this.num_dollars = this.dollars.getValue();
			this.num_cents = this.cents.getValue();
			this.updateParentsRightViews(groupPosition);
		}
		
	
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		
		super.onGroupExpanded(groupPosition);
		
		
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	


	@Override
	public boolean hasStableIds() {
		return true;
	}

	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	

	
	
	
  
    
    

}









/*
package com.swipesexchange;


import java.util.ArrayList;

import com.google.gson.Gson;
import android.text.format.Time;
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


public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childtems;
	private  LayoutInflater inflater;
	private ArrayList<String> parentItems, child;
	private int minutes_start, minutes_end, hours_start, hours_end;
	private TimePicker tp_start, tp_end;
	private NumberPicker dollars, cents, swipes;
	
	private Venue venue;
	private String venue_name;
	private ExpandableListView parent;
	private int my_dollars, my_cents;
	private int num_swipes;
	Button yes_button;
	Button cancel_button;
	Dialog submit_dialog;
	

	public MyExpandableAdapter(ExpandableListView parent, ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
		
		venue = new Venue("");
		minutes_start = 0;
		minutes_end = 0;
		hours_start = 12;
		hours_end = 12;
		venue_name = "De Neve";
		my_dollars = 0;
		my_cents = 0;
		num_swipes = 1;
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
	 
	 public int getCents()
	 {
		 return my_cents;
	 }
	 
	 public void setCents(int in_cents) {
		 my_cents = in_cents;
	 }
	 
	 public int getDollars()
	 {
		 return my_dollars;
	 }
	 
	 public void setDollars(int in_dollars) {
		 my_dollars = in_dollars;
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
			else if(groupPosition == 1)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if(groupPosition == 2)
				convertView = inflater.inflate(R.layout.group_ex1, null);
			else if(groupPosition == 3)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if(groupPosition == 4)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition == 5)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition == 6)
				convertView = inflater.inflate(R.layout.group_ex4,  null);
			else if (groupPosition == 7)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition == 8)
				convertView = inflater.inflate(R.layout.group_ex3, null);
			else if (groupPosition == 9)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition == 10)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition ==11)
				convertView = inflater.inflate(R.layout.group_ex2, null);
		//}
		if (groupPosition == 0)
		{
			this.tp_start = (TimePicker) convertView.findViewById(R.id.timePicker1);
			
			
			//tp_start.setSaveEnabled(true);
		}
		
		
		if (groupPosition == 2)
		{
			this.tp_end = (TimePicker) convertView.findViewById(R.id.timePicker2);
			
			//tp_start.setSaveEnabled(true);
		}
		
		if(groupPosition == 0)
		{
			tp_start.setCurrentMinute(minutes_start);
			tp_start.setCurrentHour(hours_start);
		}
		if(groupPosition == 2)
		{
			tp_end.setCurrentMinute(minutes_end);
			tp_end.setCurrentHour(hours_end);
		}
		
		if (groupPosition == 4)
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
					getParent().collapseGroup(4);
					
				}
			});
			
		}
		
		if(groupPosition == 6)
		{
			//this.cents = (NumberPicker) convertView.findViewById(R.id.numberPicker2);
			//this.dollars = (NumberPicker) convertView.findViewById(R.id.numberPicker1);
			cents.setMinValue(0);
			cents.setMaxValue(59);
			dollars.setMinValue(0);
			dollars.setMaxValue(20);
			cents.setValue(my_cents);
			dollars.setValue(my_dollars);
		}
			
		if(groupPosition == 8)
		{
			this.swipes = (NumberPicker) convertView.findViewById(R.id.numberPickerSwipes);
			swipes.setMinValue(1);
			swipes.setMaxValue(20);
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
		
		
		if(groupPosition == 1)
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
				
				int my_color = Color.WHITE;
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
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
				
				int my_color = Color.WHITE;
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
				//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
				((CheckedTextView) convertView).setText(str);
				
			}
			int back_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		
		//((TextView) convertView).setChecked(isExpanded);
		}
		else if(groupPosition == 5)
		{
			String mystring = getVenueName();
			String end_str = mystring;
			String start_str = parentItems.get(groupPosition) + "                                        ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			//int my_color = 5882874;
			
			int my_color = Color.WHITE;
					//getResources().
			//my_color.

			str.setSpan(

			    new ForegroundColorSpan(my_color), 

			    start, 

			    end, 

			    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			((CheckedTextView) convertView).setText(str);
			int back_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition == 3)
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
				
				int my_color = Color.WHITE;
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
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
				
				int my_color = Color.WHITE;
						//getResources().
				//my_color.

				str.setSpan(

				    new ForegroundColorSpan(my_color), 

				    start, 

				    end, 

				    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

				);
				//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
				((CheckedTextView) convertView).setText(str);
			}
			int back_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
	
		//((TextView) convertView).setChecked(isExpanded);
		}
		else if(groupPosition==7)
		{
			String my_money_str = String.format("%d.%02d", getDollars(), getCents());
			String mystring = "$" + my_money_str;
			String end_str = mystring;
			String start_str = parentItems.get(groupPosition) + "                                          ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			//int my_color = 5882874;
			
			int my_color = Color.WHITE;
					//getResources().
			//my_color.

			str.setSpan(

			    new ForegroundColorSpan(my_color), 

			    start, 

			    end, 

			    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

			);
			//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
			((CheckedTextView) convertView).setText(str);
			int back_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition==9)
		{
			String my_swipes = String.valueOf(getNumSwipes());
			String mystring = my_swipes + " swipe(s)";
			String end_str = mystring;
			String start_str = parentItems.get(groupPosition) + "                         ";
			String whole = start_str + end_str;
			int start = whole.indexOf(end_str);
			int end = whole.length();
			SpannableStringBuilder str = new SpannableStringBuilder(whole);
			//int my_color = 5882874;
			
			int my_color = Color.WHITE;
					//getResources().
			//my_color.

			str.setSpan(

			    new ForegroundColorSpan(my_color), 

			    start, 

			    end, 

			    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE

			);
			//((CheckedTextView) convertView).setText(Html.fromHtml(parentItems.get(groupPosition) + "           " + whole));
			((CheckedTextView) convertView).setText(str);
			int back_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if (groupPosition==0)
		{
			//int start = 15;
			 //int end;
			
			String start_str = "Set Start Time                           ";
			String end_str = "Press to set";
			String str = start_str + end_str;
			 SpannableStringBuilder mystring = new SpannableStringBuilder(str);
			 int end = start_str.length();
			 int start = str.indexOf(start_str);
			 mystring.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			((CheckedTextView) convertView).setText(mystring);
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if (groupPosition==2)
		{
			String start_str = "Set End Time                             ";
			String end_str = "Press to set";
			String str = start_str + end_str;
			 SpannableStringBuilder mystring = new SpannableStringBuilder(str);
			 int end = start_str.length();
			 int start = str.indexOf(start_str);
			 mystring.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			((CheckedTextView) convertView).setText(mystring);
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if (groupPosition==4)
		{
			String start_str = "Set Venue                                   ";
			String end_str = "Press to set";
			String str = start_str + end_str;
			 SpannableStringBuilder mystring = new SpannableStringBuilder(str);
			 int end = start_str.length();
			 int start = str.indexOf(start_str);
			 mystring.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			((CheckedTextView) convertView).setText(mystring);
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
			
		}
		else if (groupPosition==6)
		{
			String start_str = "Set Price                                     ";
			String end_str = "Press to set";
			String str = start_str + end_str;
			 SpannableStringBuilder mystring = new SpannableStringBuilder(str);
			 int end = start_str.length();
			 int start = str.indexOf(start_str);
			 mystring.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			((CheckedTextView) convertView).setText(mystring);
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if (groupPosition==8)
		{
			String start_str = "Set Amount                                ";
			String end_str = "Press to set";
			String str = start_str + end_str;
			 SpannableStringBuilder mystring = new SpannableStringBuilder(str);
			 int end = start_str.length();
			 int start = str.indexOf(start_str);
			 mystring.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			((CheckedTextView) convertView).setText(mystring);
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		else if(groupPosition ==11)
		{
			((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
			((CheckedTextView) convertView).setChecked(isExpanded);
			((CheckedTextView) convertView).setPadding(0, 10, 0, 10);
			((CheckedTextView) convertView).setTypeface(null, Typeface.BOLD);
			((CheckedTextView) convertView).setGravity(Gravity.CENTER_HORIZONTAL);
			
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
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
	                    	
	                    	//RANDOM, for testing
	                    	BuyListing newPost = new BuyListing();
	                    	User newUser = new User("Eric Shen");
	                    	Venue newVenue = new Venue("Covel");
	                    	newPost.setUser(newUser);
	                    	newPost.setVenue(newVenue);
	                    	
	                    	new PopulateList().execute(newPost);
	                        
	                        //TODO: direct data to database
	 
	                    }
	                });
					
					
					submit_dialog.show();
					
					
					
					
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
		if (groupPosition == 2)
		{
			minutes_end = tp_end.getCurrentMinute();
			hours_end = tp_end.getCurrentHour();
		}
		if(groupPosition==4)
		{
			venue.setName(venue_name);
		}
		if (groupPosition==6)
		{
			my_dollars = dollars.getValue();
			my_cents = cents.getValue();
		}
		if(groupPosition==8)
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
    /**ADD LISTING*
    private class PopulateList extends AsyncTask<Listing, Void, Void> {

            protected Void doInBackground(Listing... ls) {

            	Gson gson = new Gson();
            	String j0 = gson.toJson(ls[0]);
            	
            	MsgStruct nMsg = new MsgStruct();
            	nMsg.setHeader(Constants.SL_PUSH); //Identifies message as a sell listing
            	nMsg.setPayload(j0);

            	String j1 = gson.toJson(nMsg);
            	Log.d("Panda", "Sending sell LISTING from button" + j1);
            	ConnectToServlet.sendListing(j1);
            	
                    
                    //TODO: Yeah

                    return null;
            }

//This onPostExecute is not applicable to our fragment layout. Normally this closes the addListing activity, returning the user to the original screen.
/*
            protected void onPostExecute(Void result) {

                    AddScoreActivity.this.finish();
            }
    }

}
*/