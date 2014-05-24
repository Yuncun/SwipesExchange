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
	private ExpandableListView parent_list_view;
	
	final private ArrayList<ParentRow> parents;
	 
	Button yes_button;
	Button cancel_button;
	Dialog submit_dialog;
	
	Button ok_button;
	Dialog time_error_dialog;
	

	public MyExpandableAdapterBuy(ExpandableListView parent, ArrayList<ParentRow> parents) {
		this.parents = parents;
		
		venue = new Venue("Any");
		// TODO: Change default times to current time + ~3 hours
		
		this.minutes_start = 0;
		this.minutes_end = 0;
		this.hours_start = 8;
		this.hours_end = 12;
		this.venue_name = "Any";
		this.num_swipes=1;
		this.parent_list_view = parent;
		
		// Update the parent rows with information about default values to be shown in groupViews
		for(int i=0; i<3; i++)
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
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		//ViewHolder holder = null;
		// TODO: recycle views instead of always inflating
		
		
		convertView = null;
		if(convertView != null)
		{
		
		}
		else
		{
				
			
			/*
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
		*/
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
		else if (groupPosition == 2) //Picking the venue
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
		
		/*
		
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
		}
		*/
		}
	
			
		return convertView;
	}
	


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		// GroupViewHolder used to store individual GroupViews
		GroupViewHolder holder = null;
		
		// if the view has not been inflated
		if (convertView == null) {
			if(groupPosition==0)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex, parent, false);
				holder = new GroupViewHolder();
				holder.left_view = ((CheckedTextView) convertView.findViewById(R.id.group1_text_left));
				holder.right_view = ((TextView) convertView.findViewById(R.id.group1_text_right));
				// set the tag
				convertView.setTag(holder);
			}
			else if(groupPosition==1)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex2, parent, false);
				holder = new GroupViewHolder();
				holder.left_view = ((CheckedTextView) convertView.findViewById(R.id.group2_text_left));
				holder.right_view = ((TextView) convertView.findViewById(R.id.group2_text_right));
				// set the tag
				convertView.setTag(holder);
			}
			else if(groupPosition==2)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex3, parent, false);
				holder = new GroupViewHolder();
				holder.left_view = ((CheckedTextView) convertView.findViewById(R.id.group3_text_left));
				holder.right_view = ((TextView) convertView.findViewById(R.id.group3_text_right));
				// set the tag
				convertView.setTag(holder);
			}
		}
		else // Retrieve the view from the GroupViewHolder specified by its tag
		{
			holder = (GroupViewHolder) convertView.getTag();
		}
	
				
				/*
					convertView = inflater.inflate(R.layout.row_ex, null);
			else if(groupPosition==1)
				convertView = inflater.inflate(R.layout.row_ex2, null);
			else if(groupPosition==2)
				convertView = inflater.inflate(R.layout.row_ex3, null);
			else if(groupPosition==3)
				convertView = inflater.inflate(R.layout.row_ex4, null);
			else if(groupPosition==4)
				convertView = inflater.inflate(R.layout.row_ex5, null);
			else if(groupPosition==5)
				convertView = inflater.inflate(R.layout.row_ex6, null);
		
*/
			
			/*
		
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

		                    	sl.setTime("9:30");
		                    	
		                    	Venue ven = new Venue("De Neve");
		                    	sl.setVenue(ven);
		                    	User usr = new User("David Beckham"); 
		                    	sl.setUser(usr);
		                    	sl.getUser().setIdNumber("123123");
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
		else
		{
			((CheckedTextView) convertView).setText(parent_items.get(groupPosition));
			((CheckedTextView) convertView).setChecked(isExpanded);
			int back_color = Color.WHITE;
			((CheckedTextView) convertView).setBackgroundColor(back_color);
		}
		}
		*/
		
		// TODO: add this back
		/*
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
		                    	sl.getUser().setIdNumber("123123");
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
		 */
		

		holder.left_view.setText(parents.get(groupPosition).getTextLeft());
		holder.right_view.setText(parents.get(groupPosition).getTextRight());
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
	
	static class GroupViewHolder {
		CheckedTextView left_view;
		TextView right_view;
	}
	
	
	
  
    
    

}