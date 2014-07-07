package com.swipesexchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.amazonaws.services.simpledb.model.RequestTimeoutException;

import sharedObjects.BuyListing;
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
            
            sender_names = new ArrayList<String>();
            sender_ids = new ArrayList<String>();
            message_texts = new ArrayList<String>();
            
    
            if(my_list.size()>0)
            {
            	    for (int i = 0; i < my_list.size(); i++) 
      	            {
            	    	if(Self.getUID() == my_list.get(i).getSender().getUID())
	            			sender_names.add(my_list.get(i).getSender().getName());
	            		else
	            			sender_names.add(my_list.get(i).getReceiver().getName());
	            		
	            		//Log.d("pig", Self.getUID());
	            		if(Self.getUID() == my_list.get(i).getSender().getUID())
	            			sender_ids.add(my_list.get(i).getSender().getUID());
	            		else
	            			sender_ids.add(my_list.get(i).getReceiver().getUID());
	            		
	            		
	            			
	            		//sender_ids.add(my_list.get(i).getSender().getUID());
	                    message_texts.add(my_list.get(i).getMostRecentMessage().getText());
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
      
        
        sender_name.setText(sender_names.get(position));
        //sender_id.setText(sender_ids.get(position));
        text.setText(message_texts.get(position));
       
        
        return view;
    }
    
    public void switchToConversationFrag() {
    	  
       
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
