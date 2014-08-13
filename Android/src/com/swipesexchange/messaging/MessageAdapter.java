package com.swipesexchange.messaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.amazonaws.services.simpledb.model.RequestTimeoutException;
import com.swipesexchange.R;
import com.swipesexchange.R.anim;
import com.swipesexchange.R.id;
import com.swipesexchange.R.layout;
import com.swipesexchange.helpers.StaticHelpers;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.BuyListing;
import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.Self;

import android.app.Dialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAdapter extends BaseAdapter {

	  private Context myContext;
	  public List<Conversation> my_list;
	  private List<String> sender_names;
	  private List<String> sender_ids;
	  private List<String> message_texts;
	  public List<Boolean> first_time;
	  public List<Boolean> slide_in;
	  public List<Boolean> slide_out;
	  public boolean deletion_mode = false;
	  public Map<String, Boolean> dotted_messages;
	  public Map<String, ViewHolder> v_map;

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
	            		if(Self.getUser().getUID().equals(my_list.get(i).getSender().getUID()))
	            			sender_ids.add(my_list.get(i).getSender().getUID());
	            		else
	            			sender_ids.add(my_list.get(i).getReceiver().getUID());

	            		
	            	}
            }
            
            this.dotted_messages = new HashMap<String, Boolean>();
            
            this.first_time = new ArrayList<Boolean>();
            for(int i=0; i < my_list.size(); i++)
            	this.first_time.add(true);
            
            this.slide_in = new ArrayList<Boolean>();
            for(int i=0; i < my_list.size(); i++)
            	this.slide_in.add(false);
            
            this.slide_out = new ArrayList<Boolean>();
            for(int i=0; i < my_list.size(); i++)
            	this.slide_out.add(false);
            
         
           
    }
    
  
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
    	// out of bounds exception handling
    	if(position >= this.first_time.size())
    	{
    		boolean first_time_val = true;
    		boolean slide_in_val = false;
    		boolean slide_out_val = false;
    		
    		if(this.first_time.size() != 0)
    		{
    			first_time_val = this.first_time.get(0);
	    		slide_in_val = this.slide_in.get(0);
	    		slide_out_val = this.slide_out.get(0);
    		}

    		
    		this.first_time.add(first_time_val);
    		this.slide_in.add(slide_in_val);
    		this.slide_out.add(slide_out_val);
    		
    	}
    	
    	String sender_string = "";
    	String lid_str = "";
    	String sid_str = "";
    	
        if(Self.getUser().getUID().equals(my_list.get(position).getSender().getUID()))
        {
        	sid_str = my_list.get(position).getReceiver().getUID();
			sender_string = my_list.get(position).getReceiver().getName();
        }
		else 
		{
			sid_str = my_list.get(position).getSender().getUID();
			sender_string = my_list.get(position).getSender().getName();
		}
        
        lid_str = my_list.get(position).getLID();
      
        String key_str = sid_str + lid_str;
    	ViewHolder v_holder = null;
    	
    	// search for the key in the hashmap, setting the holder if the key exists
    	//if(v_map.containsKey(key_str))
    		//v_holder = v_map.get(key_str);
    	
    	if(v_holder==null)
    	{
	    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  
	    	v_holder = new ViewHolder();
	        // inflate the layout for each item of listView  
	        view = inflater.inflate(R.layout.message_item, null);
	
	        v_holder.s_name = (TextView) view.findViewById(R.id.sender_name);
	        v_holder.blue_dot = (ImageView) view.findViewById(R.id.blue_dot);
	        v_holder.msg_txt = (TextView) view.findViewById(R.id.message_text);
	        v_holder.msg_time = (TextView) view.findViewById(R.id.message_time);

	        v_holder.t_button = createTrashButton(view, position);
	        
	        view.setTag(v_holder);
    	}

    

        // set the data
        v_holder.s_name.setText(sender_string);
        Log.d("MessageAdapter says senderName is ", "Sender name : " + sender_string + " and MY NAME IS " + Self.getUser().getName());
        
        String message_string = "";
        message_string = my_list.get(position).getMostRecentMessage().getText();
        v_holder.msg_txt.setText(message_string);
        v_holder.msg_time.setText(StaticHelpers.getTimeText(my_list.get(position).getMostRecentMessage().getTime()));
        
        if(this.dotted_messages != null && this.dotted_messages.containsKey(key_str) && this.dotted_messages.get(key_str) && 
        		!this.my_list.get(position).getMostRecentMessage().getSender().getUID().equals(Self.getUser().getUID()))
        	v_holder.blue_dot.setVisibility(View.VISIBLE);
        else
        	v_holder.blue_dot.setVisibility(View.GONE);

        return view;
    }
    
    public void switchToConversationFrag() {
    	  
       
    }
    
    public void showDeletionTags() {
    
    }
    
    
    private LinearLayout createTrashButton(final View v, final int position){
   	 //@ES - Deletion
       final LinearLayout trashButton = (LinearLayout) v.findViewById(R.id.trashButton_layout);
       
      	final Animation animationSlideInLeft = AnimationUtils.loadAnimation(v.getContext(),
		         R.anim.slide_in_from_right);
		animationSlideInLeft.setDuration(500);
		
		final Animation animationSlideOutRight = AnimationUtils.loadAnimation(v.getContext(),
		         R.anim.slide_out_to_right);
		animationSlideOutRight.setDuration(500);
		
		animationSlideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 trashButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
		
       trashButton.setOnClickListener(new View.OnClickListener() {
			 
           @Override
           public void onClick(View view) {

				ConnectToServlet.deleteConversationLocally(my_list.get(position));
				first_time.remove(position);
				slide_in.remove(position);
				slide_out.remove(position);
				deleteAConversationAndUpdate(my_list.get(position));
					
               }
	
       }); 
       
       if(deletion_mode && !this.first_time.get(position) && this.slide_in.get(position)) {
	       trashButton.startAnimation(animationSlideInLeft);
	       trashButton.setVisibility(View.VISIBLE);
	       this.first_time.set(position, false);
	       this.slide_in.set(position, false);
       }
       else if(deletion_mode && !(this.first_time.get(position)) && !this.slide_in.get(position))
       {
    	   trashButton.setVisibility(View.VISIBLE);
       }
       else if(this.first_time.get(position) && deletion_mode)
       {
    	   trashButton.setVisibility(View.VISIBLE);
    	   this.first_time.set(position, false);
       }
       else if(this.first_time.get(position) && !(deletion_mode))
       {
    	   trashButton.setVisibility(View.GONE);
    	   this.first_time.set(position, false);
       }
       else if(this.slide_out.get(position))
       {
    	   trashButton.startAnimation(animationSlideOutRight);
    	   this.slide_out.set(position, false);
       }
       else
       {
    	   trashButton.setVisibility(View.GONE);
       }
       
       return trashButton;
   }
    
    public void addAndUpdate(boolean dot) {
    	this.my_list = ConversationList.getConversations();
    	for(int i=0; i < ConversationList.getConversations().size(); i++)
    	{
    		Log.d("zebra", "Conversation " + i);
    	}
    	
    	String key_str;
     	String lid_str = "";
    	String sid_str = "";
    	
    	if(my_list != null && my_list.size() > 0)
    	{
	        if(Self.getUser().getUID().equals(my_list.get(0).getSender().getUID()))
	        {
	        	sid_str = my_list.get(0).getReceiver().getUID();
	        }
			else 
			{
				sid_str = my_list.get(0).getSender().getUID();
			}
	        
	        lid_str = my_list.get(0).getLID();
	        
	        key_str = sid_str + lid_str;
	        
	    	if(dot)
	    		this.dotted_messages.put(key_str, true);
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
	
	public synchronized void deleteAConversationAndUpdate(Conversation convo){
		my_list.remove(convo);
		ConversationList.getConversations().remove(convo);
		addAndUpdate(false);
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
    
    public class ViewHolder {
    	TextView s_name;
    	TextView msg_txt;
    	TextView msg_time;
    	LinearLayout t_button;
    	ImageView blue_dot;
    }
    
    
    

}
