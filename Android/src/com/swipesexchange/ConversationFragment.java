package com.swipesexchange;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import sharedObjects.BuyListing;
import sharedObjects.Message;
import sharedObjects.User;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("ValidFragment")
public class ConversationFragment extends ListFragment {

	private ArrayList<Message> passed_messages;
	
	public ConversationFragment() {
		this.passed_messages = null;
	}
	
	public ConversationFragment(ArrayList<Message> l) {
		this.passed_messages = l;
	
	}
	

	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.conversation_fragment_list, container, false);
         getActivity().getActionBar().show();

         return view;
     }
	 
	 public void sortConversationByDate() {
	
	     	if(this.passed_messages != null && this.passed_messages.size()>1) 
	    	{
	     	   Collections.sort(this.passed_messages, new Comparator<Message>(){
	     		   public int compare(Message emp1, Message emp2) 
	     		   {
	     			 SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
	     			 Date date1 = null;
					try {
						date1 = df.parse(emp1.getTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	     			 Date date2 = null;
					try {
						date2 = df.parse(emp2.getTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	     		     return date1.compareTo(date2); 		     
	     		   }	   
	     	   });
	     	}
	 }
	 

     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
   
         this.sortConversationByDate();
         ConversationAdapter adapter = new ConversationAdapter(this.getActivity(), this.passed_messages);
         setListAdapter(adapter);

     }
}
