package com.swipesexchange.lists;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.services.simpledb.model.RequestTimeoutException;
import com.swipesexchange.R;
import com.swipesexchange.helpers.AccurateTimeHandler;
import com.swipesexchange.helpers.DisplayExceptionAlertDialog;
import com.swipesexchange.helpers.ListingsUpdateTimer;
import com.swipesexchange.helpers.StaticHelpers;
import com.swipesexchange.main.MainActivity;
import com.swipesexchange.messaging.ConversationActivity;
import com.swipesexchange.messaging.ConversationList;
import com.swipesexchange.messaging.PictureCache;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.BuyListing;
import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.SellListing;
import com.swipesexchange.sharedObjects.User;



public class ListingsList extends ListFragment
{
		//page_num 0 is Buy listings, 1 is Sell listings
		public int page_num;
		
		//static MainActivity mActivity;
        private List<BuyListing> buyEntries;
        private List<SellListing> sellEntries;
        private boolean first_time = true;
        private boolean buy_entries_init = false;
        private boolean sell_entries_init = false;
        
        private BLConnectGet bc;
        private SLConnectGet sc;
        private BuyListAdapter b_adapter;
        private SellListAdapter s_adapter;
        Button btnStartProgress;


		static ListingsList newInstance(int num, MainActivity my_activity) {
    //    	mActivity = my_activity;
        ListingsList l = new ListingsList();
        l.buyEntries = new ArrayList<BuyListing>();
        l.sellEntries = new ArrayList<SellListing>();
        l.page_num = num;
   
        Bundle args = new Bundle();
        args.putInt("num", num);
        l.setArguments(args);

        return l;
    }
		
		@SuppressLint("SimpleDateFormat")
		private Date getTimeDate(String date_str) {
	    	
	    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";

	    	String oldDateString = date_str;
	    	

	    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
	    	Date d = null;
			try {
				d = sdf.parse(oldDateString);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
	    	
	    	return d;
	    }
		
	
	   @Override
	   public void onListItemClick(ListView l, View v, int position, long id) {
	       super.onListItemClick(l, v, position, id);

	       final Dialog dialog = new Dialog(getActivity());
	       if(this.page_num==0) //Buy Listings Page
	       {
	   
	    	   // if a conversation exists for this listing, then move to that conversation
	    	   final String lid = this.b_adapter.myList.get(position).getListingID();
	    	   String other_person_uid = this.b_adapter.myList.get(position).getUser().getUID();
	    	   
	    	   // if the clicked listing is our own, do nothing
	    	   if(other_person_uid.equals(Self.getUser().getUID()))
	    	   {
	    		   // inflate the dialog with delete button
	    		   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	   dialog.setContentView(R.layout.buy_list_dialog_self);
		    	   
		    	   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xE6FFFFFF));
		    	   
		    	   TextView description = (TextView) dialog.findViewById(R.id.firstLine_ds);
		           
		           ImageView fb_pic = (ImageView) dialog.findViewById(R.id.fb_pic_ds);
		           TextView exp_time = (TextView) dialog.findViewById(R.id.expiration_time_ds);
		           TextView v1 = (TextView) dialog.findViewById(R.id.box_1_text_ds);
		           TextView v2 = (TextView) dialog.findViewById(R.id.box_2_text_ds);
		           TextView v3 = (TextView) dialog.findViewById(R.id.box_3_text_ds);
		           TextView v4 = (TextView) dialog.findViewById(R.id.box_4_text_ds);
		           TextView time_created = (TextView) dialog.findViewById(R.id.buy_listing_time_created_ds);
		           TextView name = (TextView) dialog.findViewById(R.id.buy_listing_name_ds);
		           
		           //Set button for deleting your own listnig
		           Button deleteListingButton = (Button) dialog.findViewById(R.id.delete_listing_button);
		           deleteListingButton.setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View view) {
				            if(deleteListingByLid(lid)){
				            	Log.d("Successful Deletion", "Listing " + lid + " should be successfully deleted");
				            }
				            dialog.dismiss();
			            }
			        });
		           
		           description.setText(this.b_adapter.myList.get(position).getMessageBody());
		           try {
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.b_adapter.myList.get(position).getTimeCreated(), this.b_adapter.myList.get(position).getEndTime()));
					} catch (ParseException e) {
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.b_adapter.myList.get(position).getUser().getName());
		           
		           fb_pic.setImageBitmap(PictureCache.getFBPicBuy(this.b_adapter.myList.get(position).getUser().getUID()));
		      
		           time_created.setText(StaticHelpers.getTimeText(this.b_adapter.myList.get(position).getTimeCreated()));
		           
		           // set the venue boxes
		           String venue_string = this.b_adapter.myList.get(position).getVenue().getName();
		           List<String> items = Arrays.asList(venue_string.split("\\s*,\\s*"));
		           
		           LinearLayout b1 = (LinearLayout) dialog.findViewById(R.id.box_1_ds);
		           LinearLayout b2 = (LinearLayout) dialog.findViewById(R.id.box_2_ds);
		           LinearLayout b3 = (LinearLayout) dialog.findViewById(R.id.box_3_ds);
		           LinearLayout b4 = (LinearLayout) dialog.findViewById(R.id.box_4_ds);
		           
		           for(int i = 0; i<items.size(); i++)
		           {
		           	if(i==0)
		           	{
		           		b1.setVisibility(View.VISIBLE);
		           		v1.setText(items.get(i));
		           	}
		           	else if(i==1)
		           	{
		           		b2.setVisibility(View.VISIBLE);
		           		v2.setText(items.get(i));
		           	}
		           	else if(i==2)
		           	{
		           		b3.setVisibility(View.VISIBLE);
		           		v3.setText(items.get(i));
		           	}
		           	else if(i==3)
		           	{
		           		b4.setVisibility(View.VISIBLE);
		           		v4.setText(items.get(i));
		           	}
		           }

		           DisplayMetrics displaymetrics = new DisplayMetrics();
				   getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				   int width = displaymetrics.widthPixels;
				   
				   dialog.getWindow().setLayout((7 * width)/8, LayoutParams.WRAP_CONTENT);
				   
		           dialog.show();
		           return;
		       
	    	   }
	    	   
	    	   int conv_pos = ConversationList.findConversationIndexByListingID(lid, other_person_uid);
	    	   
	    	   // if the conversation exists
	    	   if(conv_pos != -1)
	    	   {
	    		   Intent nextScreen = new Intent(getActivity(), ConversationActivity.class);
	    	       
	    	       Message clicked_message = ConversationList.getConversations().get(conv_pos).getMostRecentMessage();

	    	       User myUser = Self.getUser();
	    	       nextScreen.putExtra("listing_id", clicked_message.getListing_id());
	    	       nextScreen.putExtra("other_uid", other_person_uid);
	    	       nextScreen.putExtra("myUser", myUser);

	    	       startActivity(nextScreen);
	    	       getActivity().overridePendingTransition(R.anim.slide_in_from_right,
	    	               R.anim.slide_out_to_left);
	    	   }
	    	   else
	    	   {
	
	    		   // if the conversation DOESN'T exist, we inflate a "first message" listing dialog

		    	   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	   dialog.setContentView(R.layout.buy_list_dialog);
	
		    	   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xE6FFFFFF));
			       TextView description = (TextView) dialog.findViewById(R.id.firstLine_d);
		           
		           ImageView fb_pic = (ImageView) dialog.findViewById(R.id.fb_pic_d);
		           TextView exp_time = (TextView) dialog.findViewById(R.id.expiration_time_d);
		           TextView v1 = (TextView) dialog.findViewById(R.id.box_1_text_d);
		           TextView v2 = (TextView) dialog.findViewById(R.id.box_2_text_d);
		           TextView v3 = (TextView) dialog.findViewById(R.id.box_3_text_d);
		           TextView v4 = (TextView) dialog.findViewById(R.id.box_4_text_d);
		           TextView time_created = (TextView) dialog.findViewById(R.id.buy_listing_time_created_d);
		           TextView name = (TextView) dialog.findViewById(R.id.buy_listing_name_d);
		           
		           description.setText(this.b_adapter.myList.get(position).getMessageBody());
		           try {
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.b_adapter.myList.get(position).getTimeCreated(), this.b_adapter.myList.get(position).getEndTime()));
					} catch (ParseException e) {
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.b_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicBuy(this.b_adapter.myList.get(position).getUser().getUID()));
		      
		           time_created.setText(StaticHelpers.getTimeText(this.b_adapter.myList.get(position).getTimeCreated()));
		           // set the venue boxes
		           String venue_string = this.b_adapter.myList.get(position).getVenue().getName();
		           List<String> items = Arrays.asList(venue_string.split("\\s*,\\s*"));
		           
		           LinearLayout b1 = (LinearLayout) dialog.findViewById(R.id.box_1_d);
		           LinearLayout b2 = (LinearLayout) dialog.findViewById(R.id.box_2_d);
		           LinearLayout b3 = (LinearLayout) dialog.findViewById(R.id.box_3_d);
		           LinearLayout b4 = (LinearLayout) dialog.findViewById(R.id.box_4_d);
		           
		           for(int i = 0; i<items.size(); i++)
		           {
		           	if(i==0)
		           	{
		           		b1.setVisibility(View.VISIBLE);
		           		v1.setText(items.get(i));
		           	}
		           	else if(i==1)
		           	{
		           		b2.setVisibility(View.VISIBLE);
		           		v2.setText(items.get(i));
		           	}
		           	else if(i==2)
		           	{
		           		b3.setVisibility(View.VISIBLE);
		           		v3.setText(items.get(i));
		           	}
		           	else if(i==3)
		           	{
		           		b4.setVisibility(View.VISIBLE);
		           		v4.setText(items.get(i));
		           	}
		           }
		       
		       
		       // get the submit button and the edittext
		       Button submit_message = (Button) dialog.findViewById(R.id.chatSendButton_d);
		       final EditText message_content_holder = (EditText) dialog.findViewById(R.id.messageEdit_d);
		       final int pos = position;
		       
		       // override the onClick method
			   submit_message.setOnClickListener(new View.OnClickListener() {
					 
		            @Override
		            public void onClick(View view) {
		            	
		            	// set the users
		            	User sender = Self.getUser();
		            	User receiver = b_adapter.myList.get(pos).getUser();
	
		            	// get the contents of the EditText field holding the message string to be sent
		                String message_contents = message_content_holder.getText().toString();
		                if(message_contents == null || (message_contents.length() == 0))
		                	return;
		          
		                Log.d("OTHERGUY ISSUE", "Sender is " + Self.getUser().getUID() + " and otherguy is " + receiver.getUID());
		                
		                // get current time
		                Time now = new Time();
		                now.set(AccurateTimeHandler.getAccurateTime());
		                String time = now.format2445();
		                Log.d("ListingsList", "b_adapter.myList.get(pos).getListingID() = " + b_adapter.myList.get(pos).getListingID());
		                Message msg = new Message(sender, receiver, b_adapter.myList.get(pos).getListingID(), time, message_contents);
		                Log.d("**RECEIVER**", "Receiver UID is " + receiver.getUID() + " because pos is " + Integer.toString(pos) + " and ListingID is " + b_adapter.myList.get(pos).getListingID());
		                
		                ConversationList.addMessage(msg);
		                ConnectToServlet.sendMessage(msg);
		                // clear the edittext
		                message_content_holder.getText().clear();
		                
		                // hide the keyboard
		                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
		                	      Context.INPUT_METHOD_SERVICE);
		                imm.hideSoftInputFromWindow(message_content_holder.getWindowToken(), 0);
		                
		              
			            SelectionFragment.refreshConversationFragment();
			            
			            dialog.dismiss();
	
		            }
		        });

		       // show the dialog
			   dialog.setCanceledOnTouchOutside(true); 
			   
			   // get the screen size of the device
			   
			   DisplayMetrics displaymetrics = new DisplayMetrics();
			   getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			   int width = displaymetrics.widthPixels;
			   
			   dialog.getWindow().setLayout((7 * width)/8, LayoutParams.WRAP_CONTENT);
			   
		       dialog.show();
		       
	    	   }
	       
	       }
	       else if(this.page_num==1) // Sell Listings page
	       {
	    	   // if a conversation exists for this listing, then move to that conversation
	    	   final String lid = this.s_adapter.myList.get(position).getListingID();
	    	   String other_person_uid = this.s_adapter.myList.get(position).getUser().getUID();
	    	   
	    	   // if the clicked listing is our own, do nothing
	    	   if(other_person_uid.equals(Self.getUser().getUID()))
	    	   {
	    		   // inflate the dialog with delete button
	    		   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	   dialog.setContentView(R.layout.sell_list_dialog_self);
		    	   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xE6FFFFFF));
		    	   TextView description = (TextView) dialog.findViewById(R.id.firstLine_ds_sell);
		           
		           ImageView fb_pic = (ImageView) dialog.findViewById(R.id.fb_pic_ds_sell);
		           TextView exp_time = (TextView) dialog.findViewById(R.id.expiration_time_ds_sell);
		           TextView v1 = (TextView) dialog.findViewById(R.id.box_1_text_ds_sell);
		           TextView v2 = (TextView) dialog.findViewById(R.id.box_2_text_ds_sell);
		           TextView v3 = (TextView) dialog.findViewById(R.id.box_3_text_ds_sell);
		           TextView v4 = (TextView) dialog.findViewById(R.id.box_4_text_ds_sell);
		           TextView time_created = (TextView) dialog.findViewById(R.id.sell_listing_time_created_ds);
		           TextView name = (TextView) dialog.findViewById(R.id.sell_listing_name_ds);
		           
		           
		           
		           //Set button for deleting your own listnig
		           Button deleteListingButton = (Button) dialog.findViewById(R.id.delete_listing_button_sell);
		           deleteListingButton.setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View view) {
				            if(deleteListingByLid(lid)){
				            	Log.d("Successful Deletion", "Listing " + lid + " should be successfully deleted");
				            }
				            dialog.dismiss();
			            }
			        });
		           
		           description.setText(this.s_adapter.myList.get(position).getMessageBody());
		           try {
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.s_adapter.myList.get(position).getEndTime(), this.s_adapter.myList.get(position).getTimeCreated()));
					} catch (ParseException e) {
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.s_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicSell(this.s_adapter.myList.get(position).getUser().getUID()));
		      
		           time_created.setText(StaticHelpers.getTimeText(this.s_adapter.myList.get(position).getTimeCreated()));
		           
		           // set the venue boxes
		           String venue_string = this.s_adapter.myList.get(position).getVenue().getName();
		           List<String> items = Arrays.asList(venue_string.split("\\s*,\\s*"));
		           
		           LinearLayout b1 = (LinearLayout) dialog.findViewById(R.id.box_1_ds_sell);
		           LinearLayout b2 = (LinearLayout) dialog.findViewById(R.id.box_2_ds_sell);
		           LinearLayout b3 = (LinearLayout) dialog.findViewById(R.id.box_3_ds_sell);
		           LinearLayout b4 = (LinearLayout) dialog.findViewById(R.id.box_4_ds_sell);
		           
		           for(int i = 0; i<items.size(); i++)
		           {
		           	if(i==0)
		           	{
		           		b1.setVisibility(View.VISIBLE);
		           		v1.setText(items.get(i));
		           	}
		           	else if(i==1)
		           	{
		           		b2.setVisibility(View.VISIBLE);
		           		v2.setText(items.get(i));
		           	}
		           	else if(i==2)
		           	{
		           		b3.setVisibility(View.VISIBLE);
		           		v3.setText(items.get(i));
		           	}
		           	else if(i==3)
		           	{
		           		b4.setVisibility(View.VISIBLE);
		           		v4.setText(items.get(i));
		           	}
		           }
		           
		           
		           DisplayMetrics displaymetrics = new DisplayMetrics();
				   getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				   int width = displaymetrics.widthPixels;
				   
				   dialog.getWindow().setLayout((7 * width)/8, LayoutParams.WRAP_CONTENT);
				   
				   dialog.show();
		           return;
		       
	    	   }
	    	   
	    	   int conv_pos = ConversationList.findConversationIndexByListingID(lid, other_person_uid);
	    	   
	    	   // if the conversation exists
	    	   if(conv_pos != -1)
	    	   {
	    		   Intent nextScreen = new Intent(getActivity(), ConversationActivity.class);
	    	       
	    	       Message clicked_message = ConversationList.getConversations().get(conv_pos).getMostRecentMessage();

	    	       User myUser = Self.getUser();
	    	       nextScreen.putExtra("listing_id", clicked_message.getListing_id());
	    	       nextScreen.putExtra("other_uid", other_person_uid);
	    	       nextScreen.putExtra("myUser", myUser);

	    	       startActivity(nextScreen);
	    	       getActivity().overridePendingTransition(R.anim.slide_in_from_right,
	    	               R.anim.slide_out_to_left);
	    	   }
	    	   else
	    	   {
	
	    		   // if the conversation DOESN'T exist, we inflate a "first message" listing dialog

		    	   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	   dialog.setContentView(R.layout.sell_list_dialog);
	
		    	   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xE6FFFFFF));
			       TextView description = (TextView) dialog.findViewById(R.id.firstLine_d_sell);
		           
		           ImageView fb_pic = (ImageView) dialog.findViewById(R.id.fb_pic_d_sell);
		           TextView exp_time = (TextView) dialog.findViewById(R.id.expiration_time_d_sell);
		           TextView v1 = (TextView) dialog.findViewById(R.id.box_1_text_d_sell);
		           TextView v2 = (TextView) dialog.findViewById(R.id.box_2_text_d_sell);
		           TextView v3 = (TextView) dialog.findViewById(R.id.box_3_text_d_sell);
		           TextView v4 = (TextView) dialog.findViewById(R.id.box_4_text_d_sell);
		           TextView time_created = (TextView) dialog.findViewById(R.id.sell_listing_time_created_d);
		           TextView name = (TextView) dialog.findViewById(R.id.sell_listing_name_d);

		           description.setText(this.s_adapter.myList.get(position).getMessageBody());
		           try {
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.s_adapter.myList.get(position).getEndTime(), this.s_adapter.myList.get(position).getTimeCreated()));
					} catch (ParseException e) {
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.s_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicSell(this.s_adapter.myList.get(position).getUser().getUID()));
		      
		           time_created.setText(StaticHelpers.getTimeText(this.s_adapter.myList.get(position).getTimeCreated()));
		           // set the venue boxes
		           String venue_string = this.s_adapter.myList.get(position).getVenue().getName();
		           List<String> items = Arrays.asList(venue_string.split("\\s*,\\s*"));
		           
		           LinearLayout b1 = (LinearLayout) dialog.findViewById(R.id.box_1_d_sell);
		           LinearLayout b2 = (LinearLayout) dialog.findViewById(R.id.box_2_d_sell);
		           LinearLayout b3 = (LinearLayout) dialog.findViewById(R.id.box_3_d_sell);
		           LinearLayout b4 = (LinearLayout) dialog.findViewById(R.id.box_4_d_sell);
		           
		           for(int i = 0; i<items.size(); i++)
		           {
		           	if(i==0)
		           	{
		           		b1.setVisibility(View.VISIBLE);
		           		v1.setText(items.get(i));
		           	}
		           	else if(i==1)
		           	{
		           		b2.setVisibility(View.VISIBLE);
		           		v2.setText(items.get(i));
		           	}
		           	else if(i==2)
		           	{
		           		b3.setVisibility(View.VISIBLE);
		           		v3.setText(items.get(i));
		           	}
		           	else if(i==3)
		           	{
		           		b4.setVisibility(View.VISIBLE);
		           		v4.setText(items.get(i));
		           	}
		           }
		       
		       
		       // get the submit button and the edittext
		       Button submit_message = (Button) dialog.findViewById(R.id.chatSendButton_d_sell);
		       final EditText message_content_holder = (EditText) dialog.findViewById(R.id.messageEdit_d_sell);
		       final int pos = position;
		       
		       // override the onClick method
			   submit_message.setOnClickListener(new View.OnClickListener() {
					 
		            @Override
		            public void onClick(View view) {
		            	
		            	// set the users
		            	User sender = Self.getUser();
		            	User receiver = s_adapter.myList.get(pos).getUser();
	
		            	// get the contents of the EditText field holding the message string to be sent
		                String message_contents = message_content_holder.getText().toString();
		                if(message_contents == null || (message_contents.length() == 0))
		                	return;
		          
		                Log.d("OTHERGUY ISSUE", "Sender is " + Self.getUser().getUID() + " and otherguy is " + receiver.getUID());
		                
		                // get current time
		                Time now = new Time();
		                now.set(AccurateTimeHandler.getAccurateTime());
		                String time = now.format2445();
		                Log.d("ListingsList", "s_adapter.myList.get(pos).getListingID() = " + s_adapter.myList.get(pos).getListingID());
		                Message msg = new Message(sender, receiver, s_adapter.myList.get(pos).getListingID(), time, message_contents);
		                Log.d("**RECEIVER**", "Receiver UID is " + receiver.getUID() + " because pos is " + Integer.toString(pos) + " and ListingID is " + s_adapter.myList.get(pos).getListingID());
		                
		                ConversationList.addMessage(msg);
		                ConnectToServlet.sendMessage(msg);
		                // clear the edittext
		                message_content_holder.getText().clear();
		                
		                // hide the keyboard
		                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
		                	      Context.INPUT_METHOD_SERVICE);
		                imm.hideSoftInputFromWindow(message_content_holder.getWindowToken(), 0);
		                
		              
			            SelectionFragment.refreshConversationFragment();
			            
			            dialog.dismiss();
	
		            }
		        });

		       // show the dialog
			   dialog.setCanceledOnTouchOutside(true); 
			   
			   // get the screen size of the device

			   DisplayMetrics displaymetrics = new DisplayMetrics();
			   getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			   int width = displaymetrics.widthPixels;
			   
			   dialog.getWindow().setLayout((7 * width)/8, LayoutParams.WRAP_CONTENT);
		       dialog.show();
		       
	    	   }
	       }
	   
	   }

	   private boolean deleteListingByLid(String lid){
		   /**
		    * Used to delete a listing - Performs action locally, and calls the connectToServlet function to delete from server
		    */
		   Log.d("DELETE LISTING", "Reached deleteListingByLID in listings list, LID IS, " + lid);
		   Log.d("DELETE LISTING", "Buy Entries contains " + buyEntries.size() + "elements");
		   String aggregateBLIDs = "";
		   for(BuyListing bl : buyEntries){
			   aggregateBLIDs += bl.getListingID() + " ";
			   if (bl.getListingID().equals(lid)){
				   ConnectToServlet.deleteListing(bl);
				   buyEntries.remove(bl);
				   this.b_adapter.notifyDataSetChanged();
				  
				   return true;
			   }
		   }
		   Log.d("DELETE LISTING", "Buy Entries IDs..." + aggregateBLIDs);
		   for (SellListing sl : sellEntries){
			   if (sl.getListingID().equals(lid)){
				   ConnectToServlet.deleteListing(sl);
				   sellEntries.remove(sl);
				   this.s_adapter.notifyDataSetChanged();
				  
				   return true;
			   }
		   }
		   return false;
	   }
        
        public void setBLAdapter()
        {	
        	if(buyEntries.size()>1) 
        	{
         	   Collections.sort(buyEntries, new Comparator<BuyListing>(){
         		   public int compare(BuyListing emp1, BuyListing emp2) 
         		   {
         			   Long l1 = getTimeDate(emp1.getTimeCreated()).getTime();
         			   Long l2 = getTimeDate(emp2.getTimeCreated()).getTime();
         			   return l2.compareTo(l1); 		     
         		   }	   
         	   });
         	}
    
     	   // reveal the hidden message?
     	   if(buyEntries != null && buyEntries.size() == 0)
     		   getView().findViewById(R.id.hidden_message).setVisibility(View.VISIBLE);
     	   else
     		  getView().findViewById(R.id.hidden_message).setVisibility(View.GONE);
     	   
     	   b_adapter= new BuyListAdapter(getActivity(), buyEntries);
           setListAdapter(b_adapter);
        }
        
        public void setSLAdapter()
        {
     	   	if(sellEntries.size()>1)
     	   	{
     	   		Collections.sort(sellEntries, new Comparator<SellListing>(){
         		    public int compare(SellListing emp1, SellListing emp2) 
         	   		{
         		    	 Long l1 = getTimeDate(emp1.getTimeCreated()).getTime();
           			     Long l2 = getTimeDate(emp2.getTimeCreated()).getTime();
           			     return l2.compareTo(l1); 	
         	   		}
     		   	});
     	   	}
     	   	
     	// reveal the hidden message?
       	   if(sellEntries != null && sellEntries.size() == 0)
       		   getView().findViewById(R.id.hidden_message).setVisibility(View.VISIBLE);
       	   else
       		   getView().findViewById(R.id.hidden_message).setVisibility(View.GONE);
       	   
     	   s_adapter= new SellListAdapter(getActivity(), sellEntries);
           setListAdapter(s_adapter);
        }
        
          @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {

                View view = inflater.inflate(R.layout.mylist, container, false);

                return view;
            }

            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                
                this.first_time = false;
                
               if(this.page_num==0) //Buy Listings page
               {   
            	   Log.d("First time", "**First Time Pulling Messages - notifydataset not hupdated");
            	   this.pullAndAddMessages();
            	   bc = new BLConnectGet(getActivity(), true);
            	   bc.execute();
               }
               else if(this.page_num==1) //Sell Listings page
               {
            	 sc = new SLConnectGet(getActivity(), true);
            	 sc.execute();
               }

            }
            

        
        public void pullAndAddMessages() {
      	   
  		  MessageTask m_task = new MessageTask(getActivity());
  		  m_task.execute();
  		  
  		    
        }
        
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) 
            { 
            	
            		
        		if(page_num==0 && !first_time && ListingsUpdateTimer.shouldListBeUpdatedAgain(0))
            	{
            		
            		Log.d("frag visibility", "Buy frag visible..."); 
               	 	bc = new BLConnectGet(getActivity(), true);
             	   	bc.execute();
             	    setBLAdapter();
             	   	this.b_adapter.notifyDataSetChanged();
            	}
            	else if(page_num==1 && !first_time  && ListingsUpdateTimer.shouldListBeUpdatedAgain(1))
            	{
            		Log.d("frag visibility", "List frag visible..."); 
               	 	sc = new SLConnectGet(getActivity(), true);
             	   	sc.execute();
             	   	setSLAdapter();
             	   	this.s_adapter.notifyDataSetChanged();
            	}
            	
                     	
            }
            else {}
        }
        
        public void getPicturesBuy() {
        	PictureTaskBuy pb_task = new PictureTaskBuy(getActivity());
      	   	pb_task.execute();
        }
        
        public void getPicturesSell() {
        	PictureTaskSell pl_task = new PictureTaskSell(getActivity());
      	   	pl_task.execute();
        }
        
        private class PictureTaskBuy extends AsyncTask<Void, Void, Map<String, Bitmap>> {

        	Map<String, Bitmap> map = new HashMap<String, Bitmap>();
        	private ProgressDialog progressBar;
        	private Context context;
        	private int timeoutCounter; 
        	
        	//Currently, loop sleeps in intervals of 500ms, so a timeoutLimit of X will represent a time of X*1/2 seconds.
        	private static final int timeoutLimit = 40; //20 seconds
        	
        	
        	public PictureTaskBuy(Context context) {
        		this.context = context;
        		this.timeoutCounter = 0;
        		
        	}
        	
            @Override
	        protected void onPreExecute() {
	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Finishing up...", true);
	        }
        	
            @Override
            public Map<String, Bitmap> doInBackground(Void... params) {
                URL url = null;
                Bitmap pic_bitmap = null;
                
                while (Self.getUser().getUID() == null && buy_entries_init == false) {         	
                	try {
	                	if (timeoutCounter > timeoutLimit){
	                		throw new TimeoutException();
	                	}
	                	else{
		   	             Log.d("waitForvalues", "Waiting - getUID yields " + Self.getUser().getUID());
		   	             		
		   	            
		   	                 Thread.sleep(500);
		   	                 timeoutCounter++;
	                	}
	   	             } catch (Exception e) {
	   	                 e.printStackTrace();
	   	                 Log.d("waitForvalues", e.toString());
	   	                 DisplayExceptionAlertDialog errorDialog = new DisplayExceptionAlertDialog();
	   	                 errorDialog.showAlert(((MainActivity) context), "Could not resolve identity of client, possibly because of connection failure", true);
	   	             }
            	
   	         
   	  		 }
                Log.d("picture", Integer.toString(buyEntries.size()));
                
                Bitmap guest_icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.bear_greyscale);
                String uid = "";
                for(int i=0; i < buyEntries.size(); i++)
                {
	                try {
	              
	                	uid = buyEntries.get(i).getUser().getUID();
	                	url = new URL("https://graph.facebook.com/" + uid + "/picture?type=large");
	                	InputStream in = (InputStream) url.getContent();
	                    Log.d("picture", "URL is: " + url.toString());
	                    pic_bitmap = BitmapFactory.decodeStream(in);
	                    map.put(uid, pic_bitmap);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                    map.put(uid, guest_icon);
	                    Log.d("picture", "Malformed URL!");
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    map.put(uid, guest_icon);
	                    Log.d("picture", "IO Exception!");
	                }
                }
                
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Bitmap> map) {
            	progressBar.dismiss();
                PictureCache.cachePicMapBuy(map);
                Log.d("picture", "Map size is: " + Integer.toString(map.size()));
                setBLAdapter();
            }

        
        }
        
 	 
        
        private class PictureTaskSell extends AsyncTask<Void, Void, Map<String, Bitmap>> {

        	Map<String, Bitmap> map = new HashMap<String, Bitmap>();
        	private ProgressDialog progressBar;
        	private Context context;
        	private int timeoutCounter; 
        	
        	//Currently, loop sleeps in intervals of 500ms, so a timeoutLimit of X will represent a time of X*1/2 seconds.
        	private static final int timeoutLimit = 40; //20 seconds
        	
        	public PictureTaskSell(Context context) {
        		this.context = context;
        		timeoutCounter = 0;
        	}
        	
            @Override
	        protected void onPreExecute() {
	      
	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Finishing up...", true);
	        }
        	
            @Override
            public Map<String, Bitmap> doInBackground(Void... params) {
                URL url = null;
                Bitmap pic_bitmap = null;
                
                
                while (Self.getUser().getUID() == null && sell_entries_init == false) {
   	             Log.d("waitForvalues", "Waiting - getUID yields " + Self.getUser().getUID());
   	             		
   	          try {
              	if (timeoutCounter > timeoutLimit){
              		throw new TimeoutException();
              	}
              	else{
	   	             Log.d("waitForvalues", "Waiting - getUID yields " + Self.getUser().getUID());
	   	                 Thread.sleep(500);
	   	                 timeoutCounter++;
              		}
 	            }catch (InterruptedException e) {

   	              e.printStackTrace();
	                 Log.d("PictureTaskSell", e.toString());
 	            }
   	             catch (Exception e){
	                 DisplayExceptionAlertDialog errorDialog = new DisplayExceptionAlertDialog();
	                 errorDialog.showAlert(((MainActivity) context), "Could not resolve identity of client, possibly because of connection failure", true);
   	             }
   	         
   	  		 }
                Log.d("picture", Integer.toString(sellEntries.size()));
                String uid = "";
                Bitmap guest_icon = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.bear_greyscale);
                for(int i=0; i < sellEntries.size(); i++)
                {
	                try {
	                
	                	
	                	uid = sellEntries.get(i).getUser().getUID();
	                	url = new URL("https://graph.facebook.com/" + uid + "/picture?type=large");
	                	InputStream in = (InputStream) url.getContent();
	                    Log.d("picture", "URL is: " + url.toString());
	                    pic_bitmap = BitmapFactory.decodeStream(in);
	                    map.put(uid, pic_bitmap);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                    map.put(uid, guest_icon);
	                    Log.d("picture", "Malformed URL!");
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    map.put(uid, guest_icon);
	                    Log.d("picture", "IO Exception!");
	                }
                }
                
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Bitmap> map) {
            	progressBar.dismiss();
                PictureCache.cachePicMapSell(map);
                Log.d("picture", "Map size is: " + Integer.toString(map.size()));
                setSLAdapter();
            }

        }
        
        public void forceRefreshBL(){
        	Log.d("ListingsList", "Forcing a BL Refresh"); 
       	 	bc = new BLConnectGet(getActivity(), true);
     	   	bc.execute();
     	  //  setBLAdapter();
     	   	this.b_adapter.notifyDataSetChanged();
        }
        
        public int countBLsByUserID(String userID){
        	
        	Log.d("ListingsList", "Counting BLs for userID " + userID + "with the initial buy entries size: " + Integer.toString(buyEntries.size())  );
        //	Log.d("ListingsList", " and the size of b_adapater.myList is " + b_adapter.getCount());
        	int count = 0;
        	for (BuyListing bl : buyEntries){
        		if (bl.getUser().getUID().equals(userID)){
        			count++;
        		}
        	}
        	return count;
        }
        
        
        public void forceRefreshSL(){
        	Log.d("ListingsList", "Forcing a SL Refresh"); 
       	 	sc = new SLConnectGet(getActivity(), true);
     	   	sc.execute();
     	  //  setBLAdapter();
     	   	this.s_adapter.notifyDataSetChanged();
        }
       
        public int countSLsbyUserID(String userID){
        	int count = 0;
        	for (SellListing sl : sellEntries){
        		if (sl.getUser().getUID().equals(userID)){
        			count++;
        		}
        	}
        	return count;
        }
        
   
            
            //ASYNC TASK for BUY LISTINGS
            private class BLConnectGet extends AsyncTask<Void, Void, List<BuyListing>> {
	   
        		   private Context context;
        		   private ProgressDialog progressBar;
        	      
        	       boolean get_pics;
        	  
        	        public BLConnectGet(Context context, boolean get_pics) {
        	        	this.context = context;
        	        	this.get_pics = get_pics;
        	        	
        	        }
        
        	        
        	        protected boolean seeIfThereIsANewListingFromUs(List<BuyListing> updatedList, List<String> ledger){
        	        	for (BuyListing bl : updatedList){
        	        		if (bl.getUser().getUID().equals(Self.getUser().getUID())){
        	        			if (!ledger.contains(bl.getListingID())){
        	        				Log.d("NewListingsAwait", "Checked if new listings was ready - returned true");
        	        				return true;
        	        				//We looked through the list of listingIDs owned by this user in our clientside buy_entries and did not find this buylisting
        	        				//This means we have been updated
        	        			}
        	        		}
        	        	}
        	        	Log.d("NewListingsAwait", "Checked if new listings was ready - returned false");
        	        	return false;
        	        }
        	        
        	        @Override
        	        protected void onPreExecute() {
        	           // super.onPreExecute();
        	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
        	        }
        	        
        	      @Override
        	        protected void onPostExecute(List<BuyListing> result) {
        	    	  
        	    	  
        	    	  progressBar.dismiss();
        	    	  buyEntries = result;
        	    	  Log.d("BLExecute", "BuyEntries contains " + Integer.toString(buyEntries.size()));
        	    	  buy_entries_init = true;
        	    	  ListingsUpdateTimer.resetUpdateCountdown(0);
        	    	  if(this.get_pics)
        	    		  getPicturesBuy();
        	    	  
        	        }
        	    
        	      	@Override
        	        protected List<BuyListing> doInBackground(Void... params) {
        	        	List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();

        	        	
        	        	
        	        	//@Eric - The following bit is invoked when a new listing has just been submitted. In this case, we want the user to see his or her
        	        	//new listing when it comes up. Therefore, we first count the number of listinsg this user has in  buy_entries, then keep keep the user waiting
        	        	//until we finally see a new listing owned by the creator 
        	        	if (ListingsUpdateTimer.isForceRepeatedUpdates_BL() && Self.getUser().getUID()!=null){
        	        		//Turn off the bool
        	        		ListingsUpdateTimer.setForceRepeatedUpdates_BL(false);
        	        		
        	        		
	        	        	List<String> listingsOwnedByUser = new ArrayList<String>();
	        	        	//Count the number of entries owned by user right now
	        	        	for (BuyListing bl : buyEntries){
	        	        		if (bl.getUser().getUID().equals(Self.getUser().getUID())){
	        	        			listingsOwnedByUser.add(bl.getListingID());
	        	        		}
	        	        	}
	        	        	
	        	        	Log.d("Special cycle starting ", "Special Cycle Starting listingsOwnedByUser (Ledger) contains " + Integer.toString(listingsOwnedByUser.size()) + " elements and BuyEntries contains " + Integer.toString(buyEntries.size()) + " entries");
	        	        	int count = 0;
	        	        	while(!seeIfThereIsANewListingFromUs(updatedBuyList, listingsOwnedByUser) && count < 10){
	        	        		if (count < 1){
	        	        			try {
	        	        				Log.d("GetALLBL", "Sleeping for 250ms");
	        	        		        Thread.sleep(250);         
	        	        		    } catch (InterruptedException e) {
	        	        		       e.printStackTrace();
	        	        		    }
	        	        		}else{
	        	        			try {
	        	        		        Thread.sleep(80);    
	        	        		        Log.d("GetALLBL", "Sleeping for 80ms");
	        	        		    } catch (InterruptedException e) {
	        	        		       e.printStackTrace();
	        	        		    }
	        	        		}
	        	        		
	        	        		
		        	            try {updatedBuyList = ConnectToServlet.updateBList();}
		        	            catch (RequestTimeoutException e)
		        	               	{Log.d("LOUD AND CLEAR", "Failure at updatedBlist special edition" + e.toString());}
		        	            Log.d("LOUD AND CLEAR", "Special GetALLBL in anticipation of new listing - doInBackground reached. List contains; " + updatedBuyList.size() + " elements");
		        	            count++;
		        	            Log.d("LOUD AND CLEAR", "Special GetALLBL - number of times we've pulled in this refresh: " + Integer.toString(count));  
	        	        	}
        	        	}else{
        	        		try {updatedBuyList = ConnectToServlet.updateBList();}
	        	            catch (RequestTimeoutException e)
	        	               	{Log.d("LOUD AND CLEAR", "Failure at updatedBlist" + e.toString());}
	        	            Log.d("LOUD AND CLEAR", "Normal GetALLBL - doInBackground reached. List contains; " + updatedBuyList.size() + " elements");
	        	           
        	        	}
        	        	
        	        
	        	        	
        	        	
        	            for (int i = 0; i < updatedBuyList.size(); i ++)
        	            {
        	            	updatedBuyList.get(i).isSection = false;
        	            }
        	            return updatedBuyList;
        	        }     
        	  }           
            
            
            //Async Task for pulling SELL LIST data
            private class SLConnectGet extends AsyncTask<Void, Void, List<SellListing>> {
         	   
     		   private Context context;
     		   private ProgressDialog progressBar;
     	       boolean get_pics;
     	  
     	        public SLConnectGet(Context context, boolean get_pics) {
     	        	this.context = context;
     	        	this.get_pics = get_pics;
     	        }
     	        
     	        @Override
     	        protected void onPreExecute() {
     	           
     	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
     	        }
     	        
     	      @Override
     	        protected void onPostExecute(List<SellListing> result) {
     	    	  Log.d("test", "PostExecute1");
     	    	 ListingsUpdateTimer.resetUpdateCountdown(1);
     	    	  progressBar.dismiss();
     	    	  sellEntries = result;
     	    	  sell_entries_init = true;
     	    	 if(this.get_pics)
     	    		 getPicturesSell();
     	    	  setSLAdapter();
     	        }
     	      
     	       
  	        protected boolean seeIfThereIsANewListingFromUs(List<SellListing> updatedList, List<String> ledger){
  	        	for (SellListing sl : updatedList){
  	        		if (sl.getUser().getUID().equals(Self.getUser().getUID())){
  	        			if (!ledger.contains(sl.getListingID())){
  	        				Log.d("NewListingsAwait", "SL - Checked if new listings was ready - returned true");
  	        				return true;
  	        				//We looked through the list of listingIDs owned by this user in our clientside buy_entries and did not find this buylisting
  	        				//This means we have been updated
  	        			}
  	        		}
  	        	}
  	        	Log.d("NewListingsAwait", "SL Checked if new listings was ready - returned false");
  	        	return false;
  	        }
  	        
     	    
     	      	@Override
     	        protected List<SellListing> doInBackground(Void... params) {
     	        	List<SellListing> updatedSellList = new ArrayList<SellListing>();
     	        	
     	        	if (ListingsUpdateTimer.isForceRepeatedUpdates_SL()  && Self.getUser().getUID()!=null){
    	        		//Turn off the bool
    	        		ListingsUpdateTimer.setForceRepeatedUpdates_SL(false);
    	        		
    	        		
        	        	List<String> listingsOwnedByUser = new ArrayList<String>();
        	        	//Count the number of entries owned by user right now
        	        	for (BuyListing bl : buyEntries){
        	        		if (bl.getUser().getUID().equals(Self.getUser().getUID())){
        	        			listingsOwnedByUser.add(bl.getListingID());
        	        		}
        	        	}
        	        	
        	        	Log.d("Special cycle starting ", "Special Cycle Starting listingsOwnedByUser (Ledger) contains " + Integer.toString(listingsOwnedByUser.size()) + " elements and BuyEntries contains " + Integer.toString(buyEntries.size()) + " entries");
        	        	int count = 0;
        	        	while(!seeIfThereIsANewListingFromUs(updatedSellList, listingsOwnedByUser) && count < 10){
        	        		if (count < 1){
        	        			try {
        	        				Log.d("GetALLBL", "Sleeping for 250ms");
        	        		        Thread.sleep(250);         
        	        		    } catch (InterruptedException e) {
        	        		       e.printStackTrace();
        	        		    }
        	        		}else{
        	        			try {
        	        		        Thread.sleep(80);    
        	        		        Log.d("GetALLSL", "Sleeping for 800ms");
        	        		    } catch (InterruptedException e) {
        	        		       e.printStackTrace();
        	        		    }
        	        		}
        	        		
        	        		
	        	            try {updatedSellList = ConnectToServlet.updateSList();}
	        	            catch (RequestTimeoutException e)
	        	               	{Log.d("LOUD AND CLEAR", "Failure at updatedSlist special edition" + e.toString());}
	        	            Log.d("LOUD AND CLEAR", "Special GetALLSL in anticipation of new listing - doInBackground reached. List contains; " + updatedSellList.size() + " elements");
	        	            count++;
	        	            Log.d("LOUD AND CLEAR", "Special GetALLSL - number of times we've pulled in this refresh: " + Integer.toString(count));  
        	        	}
    	        	}else{

	     	            try {updatedSellList = ConnectToServlet.updateSList();}
	     	            catch (RequestTimeoutException e)
	     	               	{Log.d("LOUD AND CLEAR", "Failure at updatedSlist");}
	     	            Log.d("LOUD AND CLEAR", "doInBackground reached. List contains; " + updatedSellList.size() + " elements");
    	        	}
     	        	
     	            //Temporary code for fixing the "isSecton" missing field
     	            //TODO: Eliminate isSection
     	            for (int i = 0; i < updatedSellList.size(); i ++)
     	            {
     	            	updatedSellList.get(i).isSection = false;
     	            }
     	            return updatedSellList;
     	        }     
     	  } 
            
    	private class MessageTask extends AsyncTask<Void, Void, List<Message>> {
    	 	
    	 	private ProgressDialog progressBar;
    	 	private Context context;
    	 	private int timeoutCounter = 0;
    	 	private final int timeoutLimit = 60; //currently in halfseconds
    	 	
    	 	 public MessageTask(Context context) {
    		        	this.context = context;
    		        }
    	 	

    	 	@Override
    		        protected void onPreExecute() {
    		           
    		        	progressBar = ProgressDialog.show(context, "Loading...", "Messages are loading...", true);
    		        }
    	 	
    	     @Override
    	     protected List<Message> doInBackground(Void... params) {
    	    	 //Block this until UID is successfully retrieved
    	    	 Log.d("waitForvalues", "Checking that UID is safely retrieved");
    	    	 while (Self.getUser().getUID() == null) {
    	             Log.d("waitForvalues", "Waiting - getUID yields " + ((MainActivity) context).getUID());
    	             		
    	             try {
    	            	 
    	            	 if (timeoutCounter >= timeoutLimit){
    	            		 throw new TimeoutException();
    	            	 }
    	                 Thread.sleep(500);
    	                 timeoutCounter++;
    	             } catch (InterruptedException e) {
    	                 e.printStackTrace();    	                 
    	                 Log.d("waitForvalues", e.toString());

    	             }
    	             catch (TimeoutException e){
    	            	 Log.d("Timeout in MessageTask", "Timeout in MessageTask ");
    	            	 DisplayExceptionAlertDialog errorDialog = new DisplayExceptionAlertDialog();
	   	                 errorDialog.showAlert(((MainActivity) context), "Timeout Exception - Could not resolve identity of client, possibly because of connection failure", true);
    	             }
    	         
    	  		 }

    	 		List<Message> newConversations = new ArrayList<Message>();
    	 		newConversations = ConnectToServlet.requestAllMsgs(((MainActivity) context).getUID());

    	 		return newConversations;
    	     }

    	     @Override
    	     protected void onPostExecute(List<Message> msgs) {
    	     	
    	     	Log.d("LOUD AND CLEAR", "Adding messages...");
    	     	ConversationList.addMessageList(msgs);
    	     	ConversationList.is_set = true;
    	     	progressBar.dismiss();
    	     	
    	     	
    	     }
    		}
    
            
}