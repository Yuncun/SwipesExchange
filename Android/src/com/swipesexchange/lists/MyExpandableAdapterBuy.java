package com.swipesexchange.lists;


import java.util.ArrayList;

import android.text.format.Time;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
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
import com.swipesexchange.R;
import com.swipesexchange.R.drawable;
import com.swipesexchange.R.id;
import com.swipesexchange.R.layout;
import com.swipesexchange.helpers.ParentRow;
import com.swipesexchange.sharedObjects.BuyListing;
import com.swipesexchange.sharedObjects.Listing;
import com.swipesexchange.sharedObjects.MsgStruct;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.User;
import com.swipesexchange.sharedObjects.Venue;
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
	private LayoutInflater inflater;
	private int minutes_start, minutes_end, hours_start, hours_end;
	private TimePicker tp_start, tp_end;
	private int num_swipes;
	private NumberPicker swipes;
	private Venue venue;
	private String venue_name;
	private ExpandableListView parent_list_view;
	public EditText enterMessage;
	private String messageFromEditText = "";
	public List<String> selectedVenues;
	private ArrayList<Boolean> is_expanded;
	private String message_str;
	private TextView m_text;
	
	private boolean any_white;
	
	// view int types
	private final int EDIT_TEXT_TYPE = 0;
	private final int TIME_PICKER_TYPE = 1;
	private final int TEXT_TYPE = 2;
	
	
	
	
	
	// ParentRows passed in from NewListingFragmentBuy
	final private ArrayList<ParentRow> parents;
	 
	Button yes_button;
	Button cancel_button;
	Dialog submit_dialog;
	
	Button ok_button;
	Dialog time_error_dialog;
	

	public MyExpandableAdapterBuy(ExpandableListView parent, ArrayList<ParentRow> parents) {
		this.parents = parents;
		this.venue = new Venue("Any");
		// set the default times in the TimePickers based off of current time
		Time now = new Time();
		now.setToNow();
		selectedVenues = new ArrayList<String>();
		this.minutes_start = now.minute;
		this.minutes_end = now.minute;
		this.hours_start = now.hour;
		this.hours_end = now.hour + 3;
		this.venue_name = "Any";
		this.num_swipes=1;
		this.parent_list_view = parent;
		this.message_str = "";
		this.any_white = true;
		this.is_expanded = new ArrayList<Boolean>(4);
		for(int i=0; i<3; i++)
			this.is_expanded.add(i, false);
		
		
		// Update the parent rows with information about default values to be shown in groupViews
		for(int i=0; i<3; i++)
			this.updateParentsRightViews(i);
	}
	
	 public void setInflater(LayoutInflater inflater, Activity activity) {
		         this.inflater = inflater;
		         this.activity = activity;  
	 }
	 


	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
		// TODO: recycle views instead of always inflating

		int v_type = this.getChildType(groupPosition, childPosition);
		
		// view holders
		EditTextViewHolder v_holder;
		TimePickerViewHolder tp_holder;
		TextViewHolder tv_holder;
		
		if(convertView==null)
		{
			if (v_type == 0) // Picking the start time
			{
				// Inflate the view
				convertView = inflater.inflate(R.layout.group_ex, null);
				v_holder = new EditTextViewHolder();
				enterMessage = (EditText) convertView.findViewById(R.id.messageEdit_nlbuy);

				enterMessage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					InputMethodManager mgr = (InputMethodManager) enterMessage.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.showSoftInput(enterMessage, InputMethodManager.SHOW_IMPLICIT);
					enterMessage.requestFocusFromTouch();
					enterMessage.requestFocus();
				}
			});
		
				v_holder.t = enterMessage;
				
				convertView.setTag(v_holder);

			}
			else if (v_type == 1) // Picking the end time
			{
				convertView = inflater.inflate(R.layout.group_ex1, null);
				tp_holder = new TimePickerViewHolder();
				// Initialize the TimePicker
				this.tp_end = (TimePicker) convertView.findViewById(R.id.timePicker2);
				
				// TODO: Change to current time
				tp_end.setCurrentMinute(minutes_end);
				tp_end.setCurrentHour(hours_end);
				tp_holder.tp = tp_end;
				
				convertView.setTag(tp_holder);
			}
			else if (v_type == 2) // Picking the venue
			{
				convertView = inflater.inflate(R.layout.group_ex3, null);
				tv_holder = new TextViewHolder();
				
				final LinearLayout l_any = (LinearLayout) convertView.findViewById(R.id.box_1);
				final LinearLayout l_b_cafe = (LinearLayout) convertView.findViewById(R.id.box_2);
				final LinearLayout l_cafe = (LinearLayout) convertView.findViewById(R.id.box_3);
				final LinearLayout l_covell = (LinearLayout) convertView.findViewById(R.id.box_4);
				final LinearLayout l_de_neve = (LinearLayout) convertView.findViewById(R.id.box_5);
				final LinearLayout l_feast = (LinearLayout) convertView.findViewById(R.id.box_6);
				final LinearLayout l_hedrick = (LinearLayout) convertView.findViewById(R.id.box_7);
				final LinearLayout l_rendezvous = (LinearLayout) convertView.findViewById(R.id.box_8);
				
				// venue strings
				final String child_text_any = "Any";
				final String child_text_bruin_cafe = "Bruin Cafe";
				final String child_text_covell = "Covell";
				final String child_text_de_neve = "De Neve";
				final String child_text_feast = "Feast";
				final String child_text_hedrick = "Hedrick";
				final String child_text_rendezvous = "Rendezvous";
				final String child_text_cafe_1919 = "Cafe 1919";
				
				
				
				l_any.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_any))
						{
							l_any.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_any);	
						}
						else if (!selectedVenues.contains(child_text_any)) {
							l_any.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_any);
							
							// set all the other venues to blue
							l_b_cafe.setBackgroundResource(R.drawable.rectangle);
							l_b_cafe.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_bruin_cafe);
							
							l_cafe.setBackgroundResource(R.drawable.rectangle);
							l_cafe.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_cafe_1919);
							
							l_covell.setBackgroundResource(R.drawable.rectangle);
							l_covell.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_covell);
							
							l_de_neve.setBackgroundResource(R.drawable.rectangle);
							l_de_neve.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_de_neve);
							
							l_feast.setBackgroundResource(R.drawable.rectangle);
							l_feast.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_feast);
							
							l_hedrick.setBackgroundResource(R.drawable.rectangle);
							l_hedrick.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_hedrick);
							
							l_rendezvous.setBackgroundResource(R.drawable.rectangle);
							l_rendezvous.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_rendezvous);
						}
					}
							
				});
				
				l_b_cafe.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_bruin_cafe))
						{
							l_b_cafe.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_b_cafe.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_bruin_cafe);
						}
						else if (!selectedVenues.contains(child_text_bruin_cafe)) {
							l_b_cafe.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_b_cafe.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_bruin_cafe);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				l_cafe.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_cafe_1919))
						{
							l_cafe.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_cafe.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_cafe_1919);
						}
						else if (!selectedVenues.contains(child_text_cafe_1919)) {
							l_cafe.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_cafe.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_cafe_1919);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				l_covell.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_covell))
						{
							l_covell.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_covell.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_covell);
						}
						else if (!selectedVenues.contains(child_text_covell)) {
							l_covell.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_covell.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_covell);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				l_de_neve.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_de_neve))
						{
							l_de_neve.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_de_neve.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_de_neve);
						}
						else if (!selectedVenues.contains(child_text_de_neve)) {
							l_de_neve.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_de_neve.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_de_neve);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				l_feast.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_feast))
						{
							l_feast.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_feast.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_feast);
						}
						else if (!selectedVenues.contains(child_text_feast)) {
							l_feast.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_feast.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_feast);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				l_hedrick.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_hedrick))
						{
							l_hedrick.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_hedrick.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_hedrick);
						}
						else if (!selectedVenues.contains(child_text_hedrick)) {
							l_hedrick.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_hedrick.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_hedrick);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				l_rendezvous.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text_rendezvous))
						{
							l_rendezvous.setBackgroundResource(R.drawable.rectangle);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_rendezvous.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.remove(child_text_rendezvous);
						}
						else if (!selectedVenues.contains(child_text_rendezvous)) {
							l_rendezvous.setBackgroundResource(R.drawable.rectangle_red);
							
							int padding_in_dp = 2;
						    final float scale = view.getContext().getApplicationContext().getResources().getDisplayMetrics().density;
						    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
							l_rendezvous.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
							selectedVenues.add(child_text_rendezvous);
							
							// remove any if it is in selectedVenues
							if(selectedVenues.contains("Any"))
							{
								l_any.setBackgroundResource(R.drawable.rectangle);
								l_any.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
								selectedVenues.remove(child_text_any);
							}
						}
					}
							
				});
				
				
				
				
				// Unchecked cast is necessary
				/*
				final TextRow child = (TextRow) parents.get(groupPosition).getChildren().get(childPosition);
	
				m_text = (TextView) convertView.findViewById(R.id.textView1ex);
				m_text.setText(child.getText());
				convertView.setBackgroundColor(child.getBackgroundcolor());
				final String child_text = child.getText();
				// when user selects a child Venue, modify the groupView and collapse the children
				convertView.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View view) {
						if (selectedVenues.contains(child_text))
						{
							if(child_text.equals("Any"))
								any_white = true;
							parents.get(groupPosition).getChildren().get(childPosition).setBackgroundcolor(Color.WHITE);
							selectedVenues.remove(child_text);
							view.setBackgroundColor(Color.WHITE);
							
							Log.d("Color", "Venue toggled, color should change off");
							
						}
						else if (!selectedVenues.contains(child_text)){
							selectedVenues.add(child_text);
							if(child_text.equals("Any"))
								any_white = false;
							if(!child_text.equals("Any"))
							{
								if(selectedVenues.contains("Any"))
								{
									selectedVenues.remove("Any");
									parents.get(groupPosition).getChildren().get(0).setBackgroundcolor(Color.WHITE);
									any_white = true;
									parent.invalidate();
					
								}
							}
							
						int my_color = inflater.getContext().getResources().getColor(R.color.light_teal);
						parents.get(groupPosition).getChildren().get(childPosition).setBackgroundcolor(my_color);
						if(child_text.equals("Any"))
						{
							if(any_white)
							{
								view.setBackgroundColor(Color.WHITE);
							}
							else
								view.setBackgroundColor(my_color);
						}
						
						view.setBackgroundColor(my_color);
						Log.d("Color", "Venue toggled, color should change on");
						}
					}
				});
				
				tv_holder.tv = m_text;
				*/
				convertView.setTag(tv_holder);
				
				
			}

		
		}
		else // the convertView isn't null and we are at groupPostion 0, the description EditText child
		{
			if(convertView.getTag() instanceof EditTextViewHolder)
			{
				Log.d("Holder class", "Class is: " + convertView.getTag().getClass().toString());
				v_holder = (EditTextViewHolder) convertView.getTag();
			}
			else if(convertView.getTag() instanceof TimePickerViewHolder)
			{
				Log.d("Holder class", "Class is: " + convertView.getTag().getClass().toString());
				tp_holder = (TimePickerViewHolder) convertView.getTag();
			}
			else if(convertView.getTag() instanceof TextViewHolder)
			{
				Log.d("Holder class", "Class is: " + convertView.getTag().getClass().toString());
				tv_holder = (TextViewHolder) convertView.getTag();
			}
		}
	
		return convertView;
	}
	



	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		
		convertView =null;
		if (convertView == null) {
			
			if(groupPosition==0)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex_center, parent, false);
				((TextView) convertView.findViewById(R.id.group1_text_right)).setText(parents.get(groupPosition).getTextLeft());
			}
			else if(groupPosition==1)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex2, parent, false);
				((CheckedTextView) convertView.findViewById(R.id.group2_text_left)).setText(parents.get(groupPosition).getTextLeft());
				
				ImageView flipping_arrow = (ImageView) convertView.findViewById(R.id.expand_arrow_buy);
				
				if(this.is_expanded.get(groupPosition))
					flipping_arrow.setRotation(270);
				else
					flipping_arrow.setRotation(90);
				
				
				((TextView) convertView.findViewById(R.id.group2_text_right)).setText(parents.get(groupPosition).getTextRight());
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						//enterMessage.setFocusable(true);
					    // hide the keyboard
			               InputMethodManager imm = (InputMethodManager) enterMessage.getContext().getSystemService(
			                	      Context.INPUT_METHOD_SERVICE);
			                imm.hideSoftInputFromWindow(enterMessage.getWindowToken(), 0);
			                view.requestFocus();
			                ImageView flipping_arrow = (ImageView) view.findViewById(R.id.expand_arrow_buy);
			              
			                if(is_expanded.get(groupPosition))
			                {
			                	flipping_arrow.setRotation(90);
			                	getParent().collapseGroup(groupPosition);
			                	
			                }
			                else
			                {
			                	flipping_arrow.setRotation(270);
			                	getParent().expandGroup(groupPosition);
			                	
			                }
					}
				});

			}
			else if(groupPosition==2)
			{
				// inflate the view
				convertView = inflater.inflate(R.layout.row_ex3, parent, false);
			
				((CheckedTextView) convertView.findViewById(R.id.group3_text_left)).setText(parents.get(groupPosition).getTextLeft());
				ImageView flipping_arrow = (ImageView) convertView.findViewById(R.id.expand_arrow_buy_2);
				
				if(this.is_expanded.get(groupPosition))
					flipping_arrow.setRotation(270);
				else
					flipping_arrow.setRotation(90);
				((TextView) convertView.findViewById(R.id.group3_text_right)).setText(parents.get(groupPosition).getTextRight());
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						//enterMessage.setFocusable(true);
					    // hide the keyboard
			               InputMethodManager imm = (InputMethodManager) enterMessage.getContext().getSystemService(
			                	      Context.INPUT_METHOD_SERVICE);
			                imm.hideSoftInputFromWindow(enterMessage.getWindowToken(), 0);
			                view.requestFocus();
			              //  view.requestFocusFromTouch();
			                if(is_expanded.get(groupPosition))
			                	getParent().collapseGroup(groupPosition);
			                else
			                	getParent().expandGroup(groupPosition);
					}
				});
			
				
			}

		}
		if (groupPosition == 0){
			getParent().expandGroup(groupPosition);}
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
	public int getChildType(int groupPosition, int childPosition) {
		if(groupPosition==0)
			return EDIT_TEXT_TYPE;
		if(groupPosition==1)
			return TIME_PICKER_TYPE;
		if(groupPosition==2)
			return TEXT_TYPE;
		
		return -1;
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

		this.is_expanded.set(groupPosition, false);
		
	
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		
		super.onGroupExpanded(groupPosition);
		this.is_expanded.set(groupPosition, true);
		
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	 // Data retrieval and data setting methods
	 
	 public int getNumSwipes()
	 {
		 return this.num_swipes;
	 }
	 
	 public void setMessageFromEditText(String text)
	 {
		 messageFromEditText = text;
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
	

	 @Override
	 public int getChildTypeCount() {
		 return 3;
	 }
	 
	 // view holder static classes
	 
	 
	 
	static class EditTextViewHolder {
		EditText t;
	}
	
	static class TimePickerViewHolder {
		TimePicker tp;
	}
	
	static class TextViewHolder {
		TextView tv;
	}



}