package com.swipesexchange.lists;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;
import com.swipesexchange.R;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
import com.swipesexchange.R.id;
import com.swipesexchange.R.layout;
=======
>>>>>>> 395bd822774ad3f37b840b647fe71c70d3364de4
>>>>>>> Stashed changes
import com.swipesexchange.helpers.AccurateTimeHandler;
import com.swipesexchange.helpers.ChildRow;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.helpers.Constants;
import com.swipesexchange.helpers.ListingsUpdateTimer;
import com.swipesexchange.helpers.ParentRow;
import com.swipesexchange.helpers.TextRow;
import com.swipesexchange.main.MainActivity;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.BuyListing;
import com.swipesexchange.sharedObjects.MsgStruct;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.User;
import com.swipesexchange.sharedObjects.Venue;


//import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		                    	 nowCal.setTimeInMillis(AccurateTimeHandler.getAccurateTime());
		                    	 
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
     	// there are 3 parent rows: Description, EndTime, Venue
     	for(int i=0; i < this.num_parents; i++)
     	{
     		final ParentRow parent = new ParentRow();
     		// Description, i==0
     		if(i==0)
     		{
     			parent.setName("Description");
     			parent.setTextLeft("Create Your Listing");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());
     			
     			TextRow child0 = new TextRow();
     			child0.setName("V0");
     			child0.setText("Placeholder");
     			parent.getChildren().add(child0);
     		}
     		else if(i==1) // EndTime, i==1
     		{
     			parent.setName("EndTime");
     			parent.setTextLeft("Set Expiration Time:");
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
     			parent.setTextLeft("Set Venues:");
     			parent.setTextRight("");
     			parent.setChildren(new ArrayList<ChildRow>());

     		    // create the TextRow children and add them
     			// Any
     			TextRow child0 = new TextRow();
     			child0.setName("V0");
     			child0.setText("Any");
     			parent.getChildren().add(child0);
     			
     			
     		}

     		this.parents.add(parent);
     	}
     	
     }
     
}
