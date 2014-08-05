package com.swipesexchange;

import java.io.IOException;
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

import sharedObjects.BuyListing;
import sharedObjects.Message;
import sharedObjects.Self;
import sharedObjects.SellListing;
import sharedObjects.User;
import sharedObjects.Venue;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.Time;
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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.RequestTimeoutException;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;



public class ListingsList extends ListFragment
{
		//page_num 0 is Buy listings, 1 is Sell listings
		public int page_num;
		public BackendData data;
		static MainActivity mActivity;
        private List<BuyListing> buyEntries;
        private List<SellListing> sellEntries;
        private boolean first_time = true;
        private boolean buy_entries_init = false;
        private boolean sell_entries_init = false;
        
        private BLConnectGet bc;
        private SLConnectGet sc;
        private Context v;
        private BuyListAdapter b_adapter;
        private SellListAdapter s_adapter;
        Button btnStartProgress;

        
		static ListingsList newInstance(int num, MainActivity my_activity, BackendData bd) {
        	mActivity = my_activity;
        ListingsList l = new ListingsList();
        l.v = my_activity;
        l.buyEntries = new ArrayList<BuyListing>();
        l.sellEntries = new ArrayList<SellListing>();
        l.page_num = num;
   
        Bundle args = new Bundle();
        args.putInt("num", num);
        l.setArguments(args);

        return l;
    }
		
		private Date getTimeDate(String date_str) {
	    	
	    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";

	    	String oldDateString = date_str;
	    	

	    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
	    	Date d = null;
			try {
				d = sdf.parse(oldDateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	    	
	    	return d;
	    }
		
	   @Override
	   public void onListItemClick(ListView l, View v, int position, long id) {
	       super.onListItemClick(l, v, position, id);
	       Log.d("pig", "[onListItemClick] Selected Position "+ position);
	       
	       
	       
	       
	       
	       final Dialog dialog = new Dialog(getActivity());
	       if(this.page_num==0) //Buy Listings Page
	       {
	   
	    	   // if a conversation exists for this listing, then move to that conversation
	    	   final String lid = this.b_adapter.myList.get(position).getListingID();
	    	   String other_person_uid = this.b_adapter.myList.get(position).getUser().getUID();
	    	   
	    	   // if the clicked listing is our own, do nothing 
	    	   // TODO: inflate listing dialog with delete button
	    	   if(other_person_uid.equals(Self.getUser().getUID()))
	    	   {
	    		   // inflate the dialog with delete button
	    		   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	   dialog.setContentView(R.layout.buy_list_dialog_self);
		    	   
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
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.b_adapter.myList.get(position).getEndTime(), this.b_adapter.myList.get(position).getTimeCreated()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.b_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicBuy("10152153150921342"));
		      
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
		           
		           
			        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			        Display display = wm.getDefaultDisplay();
			        Point size = new Point();
			        display.getSize(size);
			        int width = size.x;
			        int height = size.y;
				   
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
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.b_adapter.myList.get(position).getEndTime(), this.b_adapter.myList.get(position).getTimeCreated()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.b_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicBuy("10152153150921342"));
		      
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
		                now.setToNow();
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
			   
		        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		        Display display = wm.getDefaultDisplay();
		        Point size = new Point();
		        display.getSize(size);
		        int width = size.x;
		        int height = size.y;
			   
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
	    	   // TODO: inflate listing dialog with delete button
	    	   if(other_person_uid.equals(Self.getUser().getUID()))
	    	   {
	    		   // inflate the dialog with delete button
	    		   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	   dialog.setContentView(R.layout.sell_list_dialog_self);
		    	   
		    	   TextView description = (TextView) dialog.findViewById(R.id.firstLine_ds_sell);
		           
		           ImageView fb_pic = (ImageView) dialog.findViewById(R.id.fb_pic_ds_sell);
		           TextView exp_time = (TextView) dialog.findViewById(R.id.expiration_time_ds_sell);
		           TextView v1 = (TextView) dialog.findViewById(R.id.box_1_text_ds_sell);
		           TextView v2 = (TextView) dialog.findViewById(R.id.box_2_text_ds_sell);
		           TextView v3 = (TextView) dialog.findViewById(R.id.box_3_text_ds_sell);
		           TextView v4 = (TextView) dialog.findViewById(R.id.box_4_text_ds_sell);
		           TextView time_created = (TextView) dialog.findViewById(R.id.sell_listing_time_created_ds);
		           TextView name = (TextView) dialog.findViewById(R.id.sell_listing_name_ds);
		           TextView price = (TextView) dialog.findViewById(R.id.price_sell_ds);
		           
		           price.setText(Double.toString(this.s_adapter.myList.get(position).getPrice()));
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
						// TODO Auto-generated catch block
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.s_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicBuy("10152153150921342"));
		      
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
		           
		           dialog.show();
		           
			        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
			        Display display = wm.getDefaultDisplay();
			        Point size = new Point();
			        display.getSize(size);
			        int width = size.x;
			        int height = size.y;
				   
				   dialog.getWindow().setLayout((7 * width)/8, LayoutParams.WRAP_CONTENT);
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
	
			       
			       TextView description = (TextView) dialog.findViewById(R.id.firstLine_d_sell);
		           
		           ImageView fb_pic = (ImageView) dialog.findViewById(R.id.fb_pic_d_sell);
		           TextView exp_time = (TextView) dialog.findViewById(R.id.expiration_time_d_sell);
		           TextView v1 = (TextView) dialog.findViewById(R.id.box_1_text_d_sell);
		           TextView v2 = (TextView) dialog.findViewById(R.id.box_2_text_d_sell);
		           TextView v3 = (TextView) dialog.findViewById(R.id.box_3_text_d_sell);
		           TextView v4 = (TextView) dialog.findViewById(R.id.box_4_text_d_sell);
		           TextView time_created = (TextView) dialog.findViewById(R.id.sell_listing_time_created_d);
		           TextView name = (TextView) dialog.findViewById(R.id.sell_listing_name_d);
		           TextView price = (TextView) dialog.findViewById(R.id.price_sell_d);
		           
		           price.setText(Double.toString(this.s_adapter.myList.get(position).getPrice()));
		           
		           description.setText(this.s_adapter.myList.get(position).getMessageBody());
		           try {
						exp_time.setText(StaticHelpers.figureOutExpirationTime(this.s_adapter.myList.get(position).getEndTime(), this.s_adapter.myList.get(position).getTimeCreated()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						exp_time.setText(">1h");
						e.printStackTrace();
					}
		           name.setText(this.s_adapter.myList.get(position).getUser().getName());
		           fb_pic.setImageBitmap(PictureCache.getFBPicBuy("10152153150921342"));
		      
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
		                now.setToNow();
		                String time = now.format2445();
		                Log.d("ListingsList", "s_adapter.myList.get(pos).getListingID() = " + s_adapter.myList.get(pos).getListingID());
		                Message msg = new Message(sender, receiver, b_adapter.myList.get(pos).getListingID(), time, message_contents);
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

		        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		        Display display = wm.getDefaultDisplay();
		        Point size = new Point();
		        display.getSize(size);
		        int width = size.x;
		        int height = size.y;
			   
			   dialog.getWindow().setLayout((7 * width)/8, LayoutParams.WRAP_CONTENT);
		       dialog.show();
		       
	    	   }
	       }
	   
	   }
    
	   private synchronized boolean deleteListingByLid(String lid){
		   /**
		    * Used to delete a listing - Performs action locally, and calls the connectToServlet function to delete from server
		    */
		   for(BuyListing bl : buyEntries){
			   if (bl.getListingID() == lid){
				   ConnectToServlet.deleteListing(bl);
				   buyEntries.remove(bl);
				   this.b_adapter.notifyDataSetChanged();
				   //ListingsUpdateTimer.toggleJustSubmittedListing();
				   return true;
			   }
		   }
		   
		   for (SellListing sl : sellEntries){
			   if (sl.getListingID() == lid){
				   ConnectToServlet.deleteListing(sl);
				   sellEntries.remove(sl);
				   this.s_adapter.notifyDataSetChanged();
				   //ListingsUpdateTimer.toggleJustSubmittedListing();
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
            	if (ListingsUpdateTimer.shouldListBeUpdatedAgain()){
            		
            		if(page_num==0 && !first_time)
                	{
                		
                		Log.d("frag visibility", "Buy frag visible..."); 
                   	 	bc = new BLConnectGet(getActivity(), false);
                 	   	bc.execute();
                 	    setBLAdapter();
                 	   	this.b_adapter.notifyDataSetChanged();
                	}
                	else if(page_num==1 && !first_time)
                	{
                		Log.d("frag visibility", "List frag visible..."); 
                   	 	sc = new SLConnectGet(getActivity(), false);
                 	   	sc.execute();
                 	   	this.s_adapter.notifyDataSetChanged();
                	}
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
        	
        	public PictureTaskBuy(Context context) {
        		this.context = context;
        	}
        	
            @Override
	        protected void onPreExecute() {
	           // super.onPreExecute();
	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Finishing up...", true);
	        }
        	
            @Override
            public Map<String, Bitmap> doInBackground(Void... params) {
                URL url = null;
                Bitmap pic_bitmap = null;
                //BitmapFactory.Options options;
                
                while (Self.getUser().getUID() == null && buy_entries_init == false) {
   	             Log.d("waitForvalues", "Waiting - getUID yields " + Self.getUser().getUID());
   	             		
   	             try {
   	                 Thread.sleep(100);
   	             } catch (InterruptedException e) {
   	                 e.printStackTrace();
   	                 Log.d("waitForvalues", e.toString());
   	             }
   	         
   	  		 }
                Log.d("picture", Integer.toString(buyEntries.size()));
                for(int i=0; i < buyEntries.size(); i++)
                {
	                try {
	                	//options = new BitmapFactory.Options();
	                	//options.inSampleSize = 2;
	                	
	                	// TODO: get the FBUID
	                	url = new URL("https://graph.facebook.com/10152153150921342/picture?type=large");
	                    //Log.d("picture", "URL is: " + url.toString());
	                    pic_bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	                    map.put("10152153150921342", pic_bitmap);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                    Log.d("picture", "Malformed URL!");
	                } catch (IOException e) {
	                    e.printStackTrace();
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
        	
        	public PictureTaskSell(Context context) {
        		this.context = context;
        	}
        	
            @Override
	        protected void onPreExecute() {
	           // super.onPreExecute();
	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Finishing up...", true);
	        }
        	
            @Override
            public Map<String, Bitmap> doInBackground(Void... params) {
                URL url = null;
                Bitmap pic_bitmap = null;
                //BitmapFactory.Options options;
                
                while (Self.getUser().getUID() == null && sell_entries_init == false) {
   	             Log.d("waitForvalues", "Waiting - getUID yields " + Self.getUser().getUID());
   	             		
   	             try {
   	                 Thread.sleep(100);
   	             } catch (InterruptedException e) {
   	                 e.printStackTrace();
   	                 Log.d("waitForvalues", e.toString());
   	             }
   	         
   	  		 }
                Log.d("picture", Integer.toString(sellEntries.size()));
                for(int i=0; i < sellEntries.size(); i++)
                {
	                try {
	                	//options = new BitmapFactory.Options();
	                	//options.inSampleSize = 2;
	                	
	                	// TODO: get the FBUID
	                	url = new URL("https://graph.facebook.com/10152153150921342/picture?type=large");
	                    //Log.d("picture", "URL is: " + url.toString());
	                    pic_bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	                    map.put("10152153150921342", pic_bitmap);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                    Log.d("picture", "Malformed URL!");
	                } catch (IOException e) {
	                    e.printStackTrace();
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
            
   
            
            //ASYNC TASK for BUY LISTINGS
            private class BLConnectGet extends AsyncTask<Void, Void, List<BuyListing>> {
	   
        		   private Context context;
        		   private ProgressDialog progressBar;
        	       int count;
        	       boolean get_pics;
        	  
        	        public BLConnectGet(Context context, boolean get_pics) {
        	        	this.context = context;
        	        	this.get_pics = get_pics;
        	        }
        	        
        	        @Override
        	        protected void onPreExecute() {
        	           // super.onPreExecute();
        	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
        	        }
        	        
        	      @Override
        	        protected void onPostExecute(List<BuyListing> result) {
        	    	  Log.d("test", "PostExecute1");
        	    	  progressBar.dismiss();
        	    	  buyEntries = result;
        	    	  buy_entries_init = true;
        	    	  if(this.get_pics)
        	    		  getPicturesBuy();
        	    	  
        	        }
        	    
        	      	@Override
        	        protected List<BuyListing> doInBackground(Void... params) {
        	        	List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();

        	            try {updatedBuyList = ConnectToServlet.updateBList();}
        	            catch (RequestTimeoutException e)
        	               	{Log.d("LOUD AND CLEAR", "Failure at updatedBlist");}
        	            Log.d("LOUD AND CLEAR", "doInBackground reached. List contains; " + updatedBuyList.size() + " elements");

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
     	       int count;
     	       boolean get_pics;
     	  
     	        public SLConnectGet(Context context, boolean get_pics) {
     	        	this.context = context;
     	        	this.get_pics = get_pics;
     	        }
     	        
     	        @Override
     	        protected void onPreExecute() {
     	           // super.onPreExecute();
     	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
     	        }
     	        
     	      @Override
     	        protected void onPostExecute(List<SellListing> result) {
     	    	  Log.d("test", "PostExecute1");
     	    	  progressBar.dismiss();
     	    	  sellEntries = result;
     	    	  sell_entries_init = true;
     	    	 if(this.get_pics)
     	    		 getPicturesSell();
     	    	  setSLAdapter();
     	        }
     	    
     	      	@Override
     	        protected List<SellListing> doInBackground(Void... params) {
     	        	List<SellListing> updatedSellList = new ArrayList<SellListing>();

     	            try {updatedSellList = ConnectToServlet.updateSList();}
     	            catch (RequestTimeoutException e)
     	               	{Log.d("LOUD AND CLEAR", "Failure at updatedBlist");}
     	            Log.d("LOUD AND CLEAR", "doInBackground reached. List contains; " + updatedSellList.size() + " elements");

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
    	 	
    	 	 public MessageTask(Context context) {
    		        	this.context = context;
    		        }
    	 	

    	 	@Override
    		        protected void onPreExecute() {
    		           // super.onPreExecute();
    		        	progressBar = ProgressDialog.show(context, "Loading...", "Messages are loading...", true);
    		        }
    	 	
    	     @Override
    	     protected List<Message> doInBackground(Void... params) {
    	    	 //Block this until UID is successfully retrieved
    	    	 Log.d("waitForvalues", "Checking that UID is safely retrieved");
    	    	 while (((MainActivity) context).getUID() == null) {
    	             Log.d("waitForvalues", "Waiting - getUID yields " + ((MainActivity) context).getUID());
    	             		
    	             try {
    	                 Thread.sleep(100);
    	             } catch (InterruptedException e) {
    	                 e.printStackTrace();
    	                 Log.d("waitForvalues", e.toString());
    	             }
    	         
    	  		 }
    	    	 
    	    	 
    	     	//Log.d("LOUD AND CLEAR", "Attempting to update messages list");
    	 		List<Message> newConversations = new ArrayList<Message>();
    	 		newConversations = ConnectToServlet.requestAllMsgs(((MainActivity) context).getUID());
    	 		
    	 		//Log.d("LOUD AND CLEAR", "Message list returned from server with size " + newConversations.size());
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