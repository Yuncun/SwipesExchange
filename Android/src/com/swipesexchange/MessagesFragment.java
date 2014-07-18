package com.swipesexchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;






import sharedObjects.Message;
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
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class MessagesFragment extends ListFragment {

	
	// member variables
	private final int num_parents = 7;
	//private ArrayList<ParentRow> parents;
    static Context mActivity;
    private ConversationList c_list;
    MessageAdapter adapter;
    private int page_num;
    
    
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
       ArrayList<Message> clicked_messages = (ArrayList<Message>) ConversationList.getConversations().get(position).getAllMessages();
       Log.d("pig", Integer.toString(clicked_messages.size()));
       User myUser = ((MainActivity) mActivity).getSelf();
       nextScreen.putExtra("is_new", false);
       nextScreen.putExtra("listing_id", clicked_messages.get(0).getListing_id());
       nextScreen.putExtra("myUser", myUser);

       startActivity(nextScreen);
       getActivity().overridePendingTransition(R.anim.slide_in_from_right,
               R.anim.slide_out_to_left);
       
       
   }
   
   public void setMessageAdapter() {
	  
	   
	   adapter = new MessageAdapter(getActivity(),ConversationList.getConversations());
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
	        	progressBar = ProgressDialog.show(context, "Loading...", "Finishing up...", true);
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
   
   

   
       
}
