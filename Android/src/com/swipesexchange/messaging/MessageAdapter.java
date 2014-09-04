package com.swipesexchange.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swipesexchange.R;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.helpers.StaticHelpers;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.Self;

public class MessageAdapter extends BaseAdapter {

	  private Context myContext;
	  
	  // list of Conversations that will be used in this adapter
	  public List<Conversation> my_list;
	  // list of ids, only kept for ease of debugging
	  private List<String> sender_ids;
	  
	  // first_time.get(i) is the boolean that says if it is the first time that this view has been inflated
	  // this check is performed later to ensure we don't slide out the first time we see the fragment
	  public List<Boolean> first_time;
	  
	  // should we slide_in the delete button? should we slide_out the delete button?
	  public List<Boolean> slide_in;
	  public List<Boolean> slide_out;
	  
	  // are we in deletion_mode (set in MessagesFragment by the "Edit" button
	  public boolean deletion_mode = false;
	  
	  // what Conversations contain new, unseen messages that we haven't read?
	  public Map<String, Boolean> dotted_messages;
	  
	  // map that contains our ViewHolders (for view preservation/prevention of view recycling)
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

	            		if(Self.getUser().getUID().equals(my_list.get(i).getSender().getUID()))
	            			sender_ids.add(my_list.get(i).getSender().getUID());
	            		else
	            			sender_ids.add(my_list.get(i).getReceiver().getUID());

	            	}
            }
            
            this.dotted_messages = new HashMap<String, Boolean>();
            
            
            // by default, we don't want to slide_out or slide_in 
            
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
    	// out of bounds exception handling, in the case that for some reason, the deletion button checking arrays
    	// are not large enough for the size of the list
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
    	
    	// UID retrieval
    	
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
      
        // the key string for the dotted_messages map, using listing id concatenated with sender id
        String key_str = sid_str + lid_str;
        
        // if we are creating the view for the first Conversation but we already saw the new message that caused this conversation
        // to be dotted (ie because we saw it in ConversationFragment), then un-dot the list item
    	if(position == 0 && ClosedInfo.receivedMessage() && this.dotted_messages.containsKey(key_str))
    	{
    		this.dotted_messages.put(key_str, false);
    		ClosedInfo.setReceivedMessage(false);
    	}
        
    	ViewHolder v_holder = null;

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
        
        //if(this.dotted_messages != null && this.dotted_messages.containsKey(key_str) && this.dotted_messages.get(key_str) && 
        		//!this.my_list.get(position).getMostRecentMessage().getSender().getUID().equals(Self.getUser().getUID()))
        if(!this.my_list.get(position).getMostRecentMessage().getSender().getUID().equals(Self.getUser().getUID()) && this.my_list.get(position).getMostRecentMessage().getHasBeenReadFlag().equals("0"))
        	v_holder.blue_dot.setVisibility(View.VISIBLE);
        else
        	v_holder.blue_dot.setVisibility(View.GONE);

        return view;
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
       
       // deletion button behavior 
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
	

    // for holding views, recycling prevention
    public class ViewHolder {
    	TextView s_name;
    	TextView msg_txt;
    	TextView msg_time;
    	LinearLayout t_button;
    	ImageView blue_dot;
    }
    
    
    

}
