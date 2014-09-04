package com.swipesexchange.messaging;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.swipesexchange.R;
import com.swipesexchange.helpers.ClosedInfo;
import com.swipesexchange.lists.SelectionFragment;
import com.swipesexchange.main.MainActivity;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.User;

public class MessagesFragment extends ListFragment {

	
	//private ArrayList<ParentRow> parents;
    static Context mActivity;
    MessageAdapter adapter;
    private TextView edit_button_text;
    
    
   public static MessagesFragment newInstance(int num, MainActivity my_activity) {
       
	   
        MessagesFragment myFrag = new MessagesFragment(); 
        Bundle args = new Bundle();
        args.putInt("num", num);
        mActivity = my_activity;
        myFrag.setArguments(args);
        //myFrag.c_list = new ConversationList();
        
        //Log.d("LOUD AND CLEAR", "c_list is updated. The first elemnt is from msgID "+ myFrag.c_list.getConversations().get(0).getSender().getUID());
        return myFrag;

   }
   
   public MessagesFragment()
   {
	   new ConversationList();
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
	   this.updateFragmentWithMessage(false);
   }
   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       // refresh
       this.updateFragmentWithMessage(false);
   }
   
   private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	   @Override
	   public void onReceive(Context context, Intent intent) {
	     // Get extra data included in the Intent
	     String message = intent.getStringExtra("message");
	     
	     MainActivity m = (MainActivity) getActivity();
     	((SelectionFragment) m.fragments[1]).num_unread.setVisibility(View.VISIBLE);
     	ClosedInfo.num_unread = ClosedInfo.num_unread + 1;
     	int num = ClosedInfo.num_unread;
     	((SelectionFragment) m.fragments[1]).num_unread.setText(Integer.toString(num));

	     updateFragmentWithMessage(true);
	     
	     MediaPlayer player = new MediaPlayer();
	     AssetFileDescriptor afd = null;
		try {
			afd = context.getAssets().openFd("message.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	     try {
			player.setDataSource(afd.getFileDescriptor());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     try {
			player.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     player.start();
	     Log.d("zebra", "Got message: " + message);
	   }
	 };
   
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {

	   int unread = 0;
	   
	   for(int i = 0; i < ConversationList.getConversations().size(); i++)
	   {
		   if(!ConversationList.getConversations().get(i).getMostRecentMessage().getSender().equals(Self.getUser().getUID()) && 
				   ConversationList.getConversations().get(i).getMostRecentMessage().getHasBeenReadFlag().equals("0"))
		   {
			   unread++;
		   }
	   }
	   
	   ClosedInfo.num_unread = unread;
	  
	   if(unread > 0)
	   {
		   MainActivity m = (MainActivity) getActivity();
	       ((SelectionFragment) m.fragments[1]).num_unread.setVisibility(View.VISIBLE);
	       int num = ClosedInfo.num_unread;
	       ((SelectionFragment) m.fragments[1]).num_unread.setText(Integer.toString(num));
	   }

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
   public void updateFragmentWithMessage(boolean dot) {
	   
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
			 this.adapter.addAndUpdate(dot);
	 }
   
 
   
   @Override
   public void onListItemClick(ListView l, View v, int position, long id) {
       super.onListItemClick(l, v, position, id);
       Log.d("pig", "[onListItemClick] Selected Position "+ position);
      // Fragment conv_list = new MessageListFragment();
       //this.pullAndAddMessages();
       
       // mark all messages in the conversation as read locally
       ConversationList.getConversations().get(position).markAllRead();
       
       MainActivity m = (MainActivity) getActivity();
       if(ClosedInfo.num_unread > 0)
       {
	       ((SelectionFragment) m.fragments[1]).num_unread.setVisibility(View.VISIBLE);
	       int num = ClosedInfo.num_unread;
	       ((SelectionFragment) m.fragments[1]).num_unread.setText(Integer.toString(num));
       }
       else
       {
    	   ((SelectionFragment) m.fragments[1]).num_unread.setVisibility(View.GONE);
       }
       
       // mark all as read and send to database

       Intent nextScreen = new Intent(getActivity(), ConversationActivity.class);
       
       String other_person_uid;
       Message clicked_message = ConversationList.getConversations().get(position).getMostRecentMessage();
       
       if(clicked_message.getSender().getUID().equals(Self.getUser().getUID()))
    	   other_person_uid = clicked_message.getReceiver().getUID();
       else
    	   other_person_uid = clicked_message.getSender().getUID();
       
       String key_str = other_person_uid+clicked_message.getListing_id();
       if(this.adapter.dotted_messages.containsKey(key_str))
    	   adapter.dotted_messages.put(other_person_uid+clicked_message.getListing_id(), false);
       
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
   
	@SuppressLint("SimpleDateFormat")
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
