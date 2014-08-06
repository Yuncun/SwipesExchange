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
	  private Stack<Conversation> my_list;
	  private List<String> sender_names;
	  private List<String> sender_ids;
	  private List<String> message_texts;
	  public List<Boolean> first_time;
	  public boolean deletion_mode = false;

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
            
            this.first_time = new ArrayList<Boolean>(my_list.size());
            for(int i=0; i < my_list.size(); i++)
            	this.first_time.add(true);
           
    }
    
  
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  
    	
        // inflate the layout for each item of listView  
        view = inflater.inflate(R.layout.message_item, null);

        TextView sender_name = (TextView) view.findViewById(R.id.sender_name);
        //TextView sender_id = (TextView) view.findViewById(R.id.sender_id);
        TextView text = (TextView) view.findViewById(R.id.message_text);
        TextView time = (TextView) view.findViewById(R.id.message_time);
        LinearLayout trashButtonButton = createTrashButton(view, position);
        String sender_string = "";
        
        
        if(Self.getUser().getUID().equals(my_list.get(position).getSender().getUID()))
			sender_string = my_list.get(position).getReceiver().getName();
		else 
			sender_string = my_list.get(position).getSender().getName();
        
        if (Self.getUser().getUID().equals(my_list.get(position).getSender().getUID()) && Self.getUser().getUID().equals(my_list.get(position).getReceiver().getUID())){
        	sender_string = "Talking to Yourself";
        }
        sender_name.setText(sender_string);
        Log.d("MessageAdapter says senderName is ", "Sender name : " + sender_string + " and MY NAME IS " + Self.getUser().getName());
        //sender_id.setText(sender_ids.get(position));
        
        String message_string = "";
        message_string = my_list.get(position).getMostRecentMessage().getText();
        
        text.setText(message_string);
        
        time.setText(StaticHelpers.getTimeText(my_list.get(position).getMostRecentMessage().getTime()));
       
        
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

           	final Dialog delete_dialog = new Dialog(v.getContext(), R.style.CustomDialogTheme);
           	delete_dialog.setContentView(R.layout.dialog_submit);
     
			   
			
				Button cancel_button = (Button) delete_dialog.findViewById(R.id.Cancel_Button);
		 		Button yes_button = (Button) delete_dialog.findViewById(R.id.Yes_Button);
				
		 		delete_dialog.setTitle("Delete this Conversation?");
				
				
				cancel_button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                   	//cancel
                   	delete_dialog.dismiss();
                   	
                   }
               });
			
				
				//yes_button.setTag(position); //Genius way of passing an argument into onclick
				yes_button.setOnClickListener(new View.OnClickListener() {
					 @Override
					 public void onClick(View view) {
						 ConnectToServlet.deleteConversationLocally(my_list.get(position));
	                    //Delete the listing
						delete_dialog.dismiss();
						deleteAConversationAndUpdate(my_list.get(position));
						
	               }
		        });
			delete_dialog.show();				
			}  
       }); 
       
       if(deletion_mode) {
	       trashButton.startAnimation(animationSlideInLeft);
	       trashButton.setVisibility(View.VISIBLE);
       }
       else if(this.first_time.get(position))
       {
    	   first_time.set(position, false);
    	   trashButton.setVisibility(View.GONE);
       }
       else 
       {
    	   trashButton.startAnimation(animationSlideOutRight);
       }
       
       return trashButton;
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
	
	public synchronized void deleteAConversationAndUpdate(Conversation convo){
		my_list.removeElement(convo);
		addAndUpdate();
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
