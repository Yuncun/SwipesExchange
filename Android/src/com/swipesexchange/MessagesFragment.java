package com.swipesexchange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;














import java.util.Map;
import java.util.Map.Entry;

import com.swipesexchange.MessageAdapter.ViewHolder;

import sharedObjects.BuyListing;
import sharedObjects.Message;
import sharedObjects.Self;
import sharedObjects.SellListing;
import sharedObjects.User;


import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesFragment extends ListFragment {

	
	// member variables
	private final int num_parents = 7;
	//private ArrayList<ParentRow> parents;
    static Context mActivity;
    private ConversationList c_list;
    MessageAdapter adapter;
    private int page_num;
    private TextView edit_button_text;
    
    
   static MessagesFragment newInstance(int num, MainActivity my_activity) {
       
	   
        MessagesFragment myFrag = new MessagesFragment(); 
        Bundle args = new Bundle();
        args.putInt("num", num);
        myFrag.page_num = 5;
        mActivity = my_activity;
        myFrag.setArguments(args);
        //myFrag.c_list = new ConversationList();
        
        //Log.d("LOUD AND CLEAR", "c_list is updated. The first elemnt is from msgID "+ myFrag.c_list.getConversations().get(0).getSender().getUID());
        return myFrag;

   }
   
   public MessagesFragment()
   {
	   this.c_list = new ConversationList();
   }
   
   @Override
   public void onResume() {
	   super.onResume();
	   if(adapter!=null)
	   {
		   for(int i=0; i<adapter.first_time.size(); i++)
		   {
			   adapter.first_time.set(i, true);
		   }
	   		adapter.deletion_mode = false;
	   		edit_button_text.setText("Edit");
	   }
	   this.updateFragmentWithMessage();
   }
   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       // refresh
       this.updateFragmentWithMessage();
   }
   
   private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	   @Override
	   public void onReceive(Context context, Intent intent) {
	     // Get extra data included in the Intent
	     String message = intent.getStringExtra("message");
	     updateFragmentWithMessage();
	     Log.d("zebra", "Got message: " + message);
	   }
	 };
   
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.message_overview, container, false);
       
       RelativeLayout edit_button = (RelativeLayout) view.findViewById(R.id.edit_messages_button);
       edit_button_text = (TextView) view.findViewById(R.id.edit_messages_text);
       edit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				final Animation animationFadeOut = AnimationUtils.loadAnimation(arg0.getContext(),
				         android.R.anim.fade_out);
				final Animation animationFadeIn = AnimationUtils.loadAnimation(arg0.getContext(),
				         android.R.anim.fade_in);
				
				// deletion mode must be swapped
				if(adapter.deletion_mode==true)
				{
					adapter.deletion_mode = false;
					edit_button_text.startAnimation(animationFadeOut);
					edit_button_text.setText("Edit");
					edit_button_text.startAnimation(animationFadeIn);
				}
				else
				{
					adapter.deletion_mode = true;
					edit_button_text.startAnimation(animationFadeOut);
					edit_button_text.setText("Done");
					edit_button_text.startAnimation(animationFadeIn);
				}
				
				// deletion mode is now stable
				if(adapter.deletion_mode)
				{
					for(int i = 0; i < adapter.slide_in.size(); i++)
						adapter.slide_in.set(i, true);
					for(int i = 0; i < adapter.slide_out.size(); i++)
						adapter.slide_out.set(i, false);
				}
				else
				{
					for(int i = 0; i < adapter.slide_in.size(); i++)
						adapter.slide_in.set(i, false);
					for(int i = 0; i < adapter.slide_out.size(); i++)
						adapter.slide_out.set(i, true);
				}

				adapter.v_map.clear();
				adapter.notifyDataSetChanged();
			}
    	   
       });
       
       LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
    		      new IntentFilter("message_received"));
       return view;
   }
   
   /*
   public void pullAndAddMessages() {
	   
		  MessageTask m_task = new MessageTask(getActivity());
		  m_task.execute();
		  
		    
	}
   */
   public void updateFragmentWithMessage() {
	   
		if(adapter != null && adapter.my_list.size() > 1) 
    	{
     	   Collections.sort(adapter.my_list, new Comparator<Conversation>(){
     		   public int compare(Conversation emp1, Conversation emp2) 
     		   {
     			   Long l1 = getTimeDate(emp1.getMostRecentMessage().getTime()).getTime();
     			   Long l2 = getTimeDate(emp2.getMostRecentMessage().getTime()).getTime();
     			   return l2.compareTo(l1); 		     
     		   }	   
     	   });
     	   
     	  setListAdapter(adapter);
     	}

		 if(this.adapter != null)
			 this.adapter.addAndUpdate();
	 }
   
 
   
   @Override
   public void onListItemClick(ListView l, View v, int position, long id) {
       super.onListItemClick(l, v, position, id);
       Log.d("pig", "[onListItemClick] Selected Position "+ position);
      // Fragment conv_list = new MessageListFragment();
       //this.pullAndAddMessages();
       Intent nextScreen = new Intent(getActivity(), ConversationActivity.class);
       
       String other_person_uid;
       Message clicked_message = ConversationList.getConversations().get(position).getMostRecentMessage();
       
       if(clicked_message.getSender().getUID().equals(Self.getUser().getUID()))
    	   other_person_uid = clicked_message.getReceiver().getUID();
       else
    	   other_person_uid = clicked_message.getSender().getUID();
       
       
       User myUser = Self.getUser();
       nextScreen.putExtra("listing_id", clicked_message.getListing_id());
       nextScreen.putExtra("other_uid", other_person_uid);
       nextScreen.putExtra("myUser", myUser);

       startActivity(nextScreen);
       getActivity().overridePendingTransition(R.anim.slide_in_from_right,
               R.anim.slide_out_to_left);
       
       
   }
   
   public void setMessageAdapter() {
	  
	   
	   adapter = new MessageAdapter(getActivity(),ConversationList.getConversations());
		if(adapter.my_list.size()>1) 
    	{
     	   Collections.sort(adapter.my_list, new Comparator<Conversation>(){
     		   public int compare(Conversation emp1, Conversation emp2) 
     		   {
     			   Long l1 = getTimeDate(emp1.getMostRecentMessage().getTime()).getTime();
     			   Long l2 = getTimeDate(emp2.getMostRecentMessage().getTime()).getTime();
     			   return l2.compareTo(l1); 		     
     		   }	   
     	   });
     	}
	   setListAdapter(adapter);
   }
   
   
   public void waitForValues() {
 	   
		  WaitTask m_task = new WaitTask(getActivity());
		  m_task.execute();
	}
	
	

	private class WaitTask extends AsyncTask<Void, Void, List<Message>> {
 	
 	private ProgressDialog progressBar;
 	private Context context;
 	
 	 public WaitTask(Context context) {
	        	this.context = context;
	        }
 	

 	@Override
	        protected void onPreExecute() {
	           // super.onPreExecute();
	        	progressBar = ProgressDialog.show(context, "Loading...", "Doing some work...", true);
	        }
 	
     @Override
     protected List<Message> doInBackground(Void... params) {
    	 //Block this until UID is successfully retrieved
    	 Log.d("waitForvalues", "Checking that UID is safely retrieved");
    	 while (((MainActivity) context).getUID() == null) {
             Log.d("porcupine", "Waiting - getUID yields " + ((MainActivity) context).getUID());
             		
             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
                 Log.d("waitForvalues", e.toString());
             }
         
  		 }
    	 
    	 while (ConversationList.is_set == false) {
             Log.d("porcupine", "Waiting for conversations to be loaded!!");
             		
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
     	
     	Log.d("porcupine", "Dandelions");
     	progressBar.dismiss();
     	setMessageAdapter();
     	
     	
     }
	}
  
   
   
   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       
       this.waitForValues();
     
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
   
   

   
       
}
