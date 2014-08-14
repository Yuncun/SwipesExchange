package com.swipesexchange.lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.google.gson.Gson;
import com.swipesexchange.R;
import com.swipesexchange.R.id;
import com.swipesexchange.R.layout;
import com.swipesexchange.helpers.ChildRow;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.helpers.Constants;
import com.swipesexchange.helpers.ListingsUpdateTimer;
import com.swipesexchange.helpers.ParentRow;
import com.swipesexchange.helpers.TextRow;
import com.swipesexchange.main.MainActivity;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.BuyListing;
import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.MsgStruct;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.User;
import com.swipesexchange.sharedObjects.Venue;

//import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class NLBuy extends Fragment {

	
	// member variables
	private final int num_parents = 3;
	private ArrayList<ParentRow> parents;
    static MainActivity mActivity;
    private ExpandableListView lv;
    public MyExpandableAdapterBuy adapter;
 
    private String messageFromEditText = "";
    private Fragment fragment = this;

    
	Button yes_button;
	Button cancel_button;
	Dialog submit_dialog;
	TextView text_field;
	TextView text_field_submit;
	
	Button ok_button;
	Dialog time_error_dialog;
    
	public NLBuy() {
	
	}
	
	 public void setMessageFromEditText(String text)
	 {
		 messageFromEditText = text;
	 }

	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.expandable_list_view_buy, container, false);
         
         Button submit_button = (Button) view.findViewById(R.id.new_buy_listing_submit);
	        
	        submit_button.setOnClickListener(new View.OnClickListener() {
			
	        	@Override
				public void onClick(View v) {
	        		
	        		boolean cancel = false;
				
	        		if (adapter.enterMessage.getText().toString() == null || adapter.enterMessage.getText().toString().isEmpty() )
					{
						cancel = true;
						time_error_dialog = new Dialog(v.getContext());
						time_error_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						time_error_dialog.setContentView(R.layout.dialog_time_error);
						
						ok_button = (Button) time_error_dialog.findViewById(R.id.Ok_Button);
						text_field = (TextView) time_error_dialog.findViewById(R.id.dialog_text_view);
						
						text_field.setText("Please enter something for the message body");
						
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
						submit_dialog = new Dialog(v.getContext());
						submit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						submit_dialog.setContentView(R.layout.dialog_submit);
						cancel_button = (Button) submit_dialog.findViewById(R.id.Cancel_Button);
				 		yes_button = (Button) submit_dialog.findViewById(R.id.Yes_Button);
						
				 		text_field_submit = (TextView) submit_dialog.findViewById(R.id.dialog_text_view);
				 		
						text_field_submit.setText("Submit listing?");
						
						
						cancel_button.setOnClickListener(new View.OnClickListener() {
							 
		                    @Override
		                    public void onClick(View view) {
		                        
		                        submit_dialog.dismiss();
		 
		                    }
		                });
						
						yes_button.setOnClickListener(new View.OnClickListener() {
							 
		                    @Override
		                    public void onClick(View view) {
		                    	
		                    	String receivedString = adapter.enterMessage.getText().toString();	               
				                messageFromEditText = receivedString;
				                Log.v("myExpandableAdapterBuy", messageFromEditText);
				                
		                    	//Create a new sellListing for testing
		                    	BuyListing sl = new BuyListing();
		                    	sl.setMessageBody(messageFromEditText);
		                    	
		                    	sl.setStartTime("Deprecated");
		                    	sl.setTime("Deprecated");
		                    	//Set and calculate endtime

		                    	 
		                    	 //Convert the timePicker values into a format that is readable by us
		                    	 Time endTimeFormatter = new Time();
		                    	
		                    	 
		                    	 Calendar nowCal = Calendar.getInstance();
		                    	 nowCal.setTimeInMillis(MainActivity.accurateTimeHandler.getAccurateTime());
		                    	 
		                    	 Calendar myEndTimeCal = Calendar.getInstance();
		                    	 myEndTimeCal.set(Calendar.HOUR_OF_DAY, adapter.getEndHours());
		                    	 myEndTimeCal.set(Calendar.MINUTE, adapter.getEndMinutes());
		                    	 
		                    	 //If the time selected is "before" the current time (which can be determined as PST + 7)
		                    	 		                    	 //TODO: Change hardcodes
		                    	 		                    	 
		                    	Calendar myEndTimeCal_TEMP = Calendar.getInstance();
 		                    	 myEndTimeCal_TEMP.setTimeInMillis(myEndTimeCal.getTimeInMillis());
 		                    	 myEndTimeCal_TEMP.add(Calendar.HOUR, 7);
 		                    	// myEndTimeCal.add(Calendar.HOUR, 7);
 		                    	if (nowCal.after(myEndTimeCal_TEMP)){
 		                    		 myEndTimeCal.add(Calendar.DATE, 1);
 		                    	 }
		                    	 endTimeFormatter.set(myEndTimeCal.getTimeInMillis());
		                    	 //Set endTime, correctly formatted
		                    	 String endTimeFormatted = endTimeFormatter.format2445();
		                    	 sl.setEndTime(endTimeFormatted);
		                    	 
		                    	 
		                    	 Time now = new Time();
		                    	 now.set(nowCal.getTimeInMillis());
		                    	
		                    	 String time = now.format2445();
		                    	 sl.setTimeCreated(time);

		                    	//sl.setPrice(5.00);
		                    	sl.setSwipeCount(3);
		                    	
		                    	
		                    	//Set listing with all selected inputs, passed as a string seperated by commas 
		                    	Venue ven = new Venue("");
		                    	String commaSeperatedVenueList = "";
		                    	for (int i = 0; i < adapter.selectedVenues.size(); i++){
		                    		commaSeperatedVenueList += adapter.selectedVenues.get(i) + ",";
		                    	}
		                    	if (commaSeperatedVenueList.equals(""))
		                    	{
		                    		commaSeperatedVenueList = "Any";
		                    	}
		                    	else if (Character.toString((commaSeperatedVenueList.charAt(commaSeperatedVenueList.length() - 1))) == ","){
		                    		commaSeperatedVenueList.substring(0, commaSeperatedVenueList.length()-1); //remove trailing comma
		                    		}
		                    	ven.setName(commaSeperatedVenueList);
		                    	
		                    	sl.setVenue(ven);
		                    	User usr = new User(Self.getUser().getName()); 
		                    	sl.setUser(usr);
		                    	sl.getUser().setUID(Self.getUser().getUID());
		                    	sl.getUser().setRating("No Rating");
		                    	sl.getUser().setConnections("Connections");
		                    	sl.isSection = false;
		                    	sl.setSection("random");

		                    	//
		                    	Gson gson = new Gson();
		                    	String j0 = gson.toJson(sl);

		                    	MsgStruct nMsg = new MsgStruct();
		                    	nMsg.setHeader(Constants.BL_PUSH); //Identifies message as a sell listing
		                    	nMsg.setPayload(j0);
		                    	String j1 = gson.toJson(nMsg);
		                    	ConnectToServlet.sendListing(j1);
		                    	
		                    	ListingsUpdateTimer.toggleJustSubmittedListing();
		                    	ListingsUpdateTimer.setForceRepeatedUpdates_BL(true);
		                        submit_dialog.dismiss();
		                        
		                        // finish the new listing activity
		                    	ClosedInfo.setMinimized(false);
		                    	
		                    	Intent resultIntent = new Intent();
		                    	resultIntent.putExtra("submitted_new_BL", true);
		                    	getActivity().setResult(1, resultIntent);

		                    	
		    					getActivity().finish();
		                    }
		                });
						
						submit_dialog.show();
					}
						
					}

				});

         return view;
     }
	 

	 

     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
             
         super.onActivityCreated(savedInstanceState);
         
         // find the ELV
         this.lv = (ExpandableListView) getActivity().findViewById(R.id.exlistbuy);
      
         this.parents = new ArrayList<ParentRow>();
         // fill the ParentRow list
         //if(parents.isEmpty())
         	this.setGroupParents();
         
         // set the custom adapter
         adapter = new MyExpandableAdapterBuy(this.lv, this.parents);
         adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
         this.lv.setAdapter(adapter);
    
     }
     
     public void setGroupParents() {
     	this.parents.clear();
     	// there are 6 parent rows: StartTime, EndTime, Venue, Number of Swipes, (EMPTY), and Submit
     	for(int i=0; i < this.num_parents; i++)
     	{
     		final ParentRow parent = new ParentRow();
     		// StartTime, i==0
     		if(i==0)
     		{
     			parent.setName("StartTime");
     			parent.setTextLeft("Create Your Listing");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());
     			
     			// create the TimePicker for StartTime and add it
     			TimePickerRow child = new TimePickerRow();
     			child.setName("StartTimePicker");
     			parent.getChildren().add(child);
     		}
     		else if(i==1) // EndTime, i==1
     		{
     			parent.setName("EndTime");
     			parent.setTextLeft("End Time:");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());
     			
     			// create the TimePicker for EndTime and add it
     			TimePickerRow child = new TimePickerRow();
     			child.setName("EndTimePicker");
     			parent.getChildren().add(child);
     		}
     		else if(i==2) // Venue, i==2
     		{
     			parent.setName("SetVenue");
     			parent.setTextLeft("Set Venue:");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());
     			
     			
     			
     		    // create the TextRow children and add them
     			// Any
     			TextRow child0 = new TextRow();
     			child0.setName("V0");
     			child0.setText("Any");
     			parent.getChildren().add(child0);
     			
     			/*
     			// Bruin Cafe
     			TextRow child1 = new TextRow();
     			child1.setName("V1");
     			child1.setText("Bruin Cafe");
     			parent.getChildren().add(child1);
     			// Cafe 1919
     			TextRow child2 = new TextRow();
     			child2.setName("V2");
     			child2.setText("Cafe 1919");
     			parent.getChildren().add(child2);
     			// Covell
     			TextRow child3 = new TextRow();
     			child3.setName("V3");
     			child3.setText("Covell");
     			parent.getChildren().add(child3);
     			// De Neve
     			TextRow child4 = new TextRow();
     			child4.setName("V4");
     			child4.setText("De Neve");
     			parent.getChildren().add(child4);
     			// Feast
     			TextRow child5 = new TextRow();
     			child5.setName("V5");
     			child5.setText("Feast");
     			parent.getChildren().add(child5);
     			// Hedrick
     			TextRow child6 = new TextRow();
     			child6.setName("V6");
     			child6.setText("Hedrick");
     			parent.getChildren().add(child6);
     			// Rendezvous
     			TextRow child7 = new TextRow();
     			child7.setName("V7");
     			child7.setText("Rendezvous");
     			parent.getChildren().add(child7);
     			*/
     		}
     		
     		/*
     	    else if(i==3) // Number of Swipes
     		{
     			parent.setName("NumSwipes");
     			parent.setTextLeft("Number of Swipes:");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());
     			
     			// create the NumberPickerRow and add it
     			NumberPickerRow child = new NumberPickerRow();
     			child.setName("NumSwipesPicker");
     			parent.getChildren().add(child);
     		}*//*
     		else if(i==3) // Empty
     		{
     			parent.setName("Emtpy");
     			parent.setTextLeft("");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());
     			// create the Empty child
     			TextRow child = new TextRow();
     			child.setName("Empty");
     			child.setText("");
     			
     			parent.getChildren().add(child);
     		}*/
     		/*else if(i==3) // Submit
     		{
     			parent.setName("Submit");
     			parent.setTextLeft("Submit");
     			parent.setTextRight("");
     			
     			
     			parent.setChildren(new ArrayList<ChildRow>());
     			// create the Empty child
     			TextRow child = new TextRow();
     			child.setName("Empty_Submit");
     			child.setText("");
     			
     			parent.getChildren().add(child);
     		}
     		*/
     		
     		
     		
     		this.parents.add(parent);
     	}
     	
     }
     
}
