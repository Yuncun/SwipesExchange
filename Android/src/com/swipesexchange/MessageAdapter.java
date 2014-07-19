package com.swipesexchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.amazonaws.services.simpledb.model.RequestTimeoutException;

import sharedObjects.BuyListing;
import sharedObjects.Message;
import sharedObjects.Self;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAdapter extends BaseAdapter {

	  private Context myContext;
	  private Stack<Conversation> my_list;
	  private List<String> sender_names;
	  private List<String> sender_ids;
	  private List<String> message_texts;

    public MessageAdapter(Context context, Stack<Conversation> list) 
    {
            super();
            myContext=context;
            my_list = list;

            sender_ids = new ArrayList<String>();

            if(my_list.size()>0)
            {
            	    for (int i = 0; i < my_list.size(); i++) 
      	            {
            	   
	            		
	            		//Log.d("pig", Self.getUID());
	            		if(Self.getUser().getUID() == my_list.get(i).getSender().getUID())
	            			sender_ids.add(my_list.get(i).getSender().getUID());
	            		else
	            			sender_ids.add(my_list.get(i).getReceiver().getUID());
	            		
	            		
	            			
	            		
	            	}
            }
           
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  
    	
        // inflate the layout for each item of listView  
        view = inflater.inflate(R.layout.message_item, null);
        
        /*
        view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String test_msg = "Clicked on Message!";
				Toast.makeText(v.getContext(), test_msg, Toast.LENGTH_SHORT).show();
				
			}
        });
        */

        TextView sender_name = (TextView) view.findViewById(R.id.sender_name);
        //TextView sender_id = (TextView) view.findViewById(R.id.sender_id);
        TextView text = (TextView) view.findViewById(R.id.message_text);
        TextView time = (TextView) view.findViewById(R.id.message_time);
      
        String sender_string = "";
        if(Self.getUser().getUID() == my_list.get(position).getSender().getUID())
			sender_string = my_list.get(position).getSender().getName();
		else
			sender_string = my_list.get(position).getReceiver().getName();
        sender_name.setText(sender_string);
        //sender_id.setText(sender_ids.get(position));
        
        String message_string = "";
        message_string = my_list.get(position).getMostRecentMessage().getText();
        
        text.setText(message_string);
        
        time.setText(this.getTimeText(my_list.get(position).getMostRecentMessage().getTime()));
       
        
        return view;
    }
    
    public void switchToConversationFrag() {
    	  
       
    }
    
    public void addAndUpdate() {
    	//this.my_list.clear();
    	this.my_list = ConversationList.getConversations();
    	for(int i=0; i < ConversationList.getConversations().size(); i++)
    	{
    		Log.d("zebra", "Conversation " + i);
    	}
    	this.notifyDataSetChanged();
    }

    public Object getItem(int position) {
        
        return position;
    }

    public long getItemId(int position) {
      
        return position;
    }


	@Override
	public int getCount() {
		
		return my_list.size();
	}

   public static String getTimeText(String date_str) {
    	
    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";
    	final String NEW_FORMAT = "EEE, MMM dd, hh:mm aaa";

    	String oldDateString = date_str;
    	String newDateString;

    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    	Date d = null;
		try {
			d = sdf.parse(oldDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return date_str;
		}
    	sdf.applyPattern(NEW_FORMAT);
    	newDateString = sdf.format(d);
    	
        return newDateString;
    }
	
	//This class will allow us to safely "block" until all necessary values (like UID) have been accounted for by the initialization code
	//DEPRECATED @Eric 7/5/14
    private class WaitForKeyValues extends AsyncTask<Void, Void, String> {

		   private Context context;
		   private ProgressDialog progressBar;
		   boolean status = false;
	  
	        public WaitForKeyValues(Context context) {
	        	this.context = context;
	        }
	        
	        @Override
	        protected void onPreExecute() {
	           // super.onPreExecute();
	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Messages are loading...", true);
	        }
	        
	      @Override
	        protected void onPostExecute(String input) {
	    	  Log.d("waitForvalues", "PostExecute " + input);
	    	  progressBar.dismiss();

	        }
	      
	      	public void UIDisRetrieved()
	      	{
	      		status = true;
	      	}
	    
	      	@Override
	        protected String doInBackground(Void... params) {
	        	
	      		 while (!status) {
	                 Log.d("waitForvalues", "Waiting");
	                 		
	                 try {
	                     Thread.sleep(100);
	                 } catch (InterruptedException e) {
	                     e.printStackTrace();
	                     Log.d("waitForvalues", e.toString());
	                 }
	             
	      		 }
				return "success";
				
	        } 
	      
	  }         
    
    

}
