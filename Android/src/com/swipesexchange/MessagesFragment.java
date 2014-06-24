package com.swipesexchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sharedObjects.SellListing;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class MessagesFragment extends ListFragment {

	
	// member variables
	private final int num_parents = 7;
	//private ArrayList<ParentRow> parents;
    static Context mActivity;
    private final ConversationList c_list;
    private int page_num;
    
    
   static MessagesFragment newInstance(int num, Context my_activity) {
       
	   
        MessagesFragment myFrag = new MessagesFragment(); //Why? @eric
        Bundle args = new Bundle();
        args.putInt("num", num);
        myFrag.page_num = 5;
        mActivity = my_activity;
        myFrag.setArguments(args);
        //myFrag.c_list = new ConversationList();
        myFrag.c_list.updateEverything();
        Log.d("LOUD AND CLEAR", "c_list is updated. The first elemnt is from msgID "+ myFrag.c_list.getConversations().get(0).getSender().getUID());
        return myFrag;

   }
   
   public MessagesFragment()
   {
	   this.c_list = new ConversationList();
   }
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.mylist, container, false);

       return view;
   }
   
  
   
   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
       

  
    
          MessageAdapter adapter = new MessageAdapter(getActivity(),ConversationList.getConversations());
          setListAdapter(adapter);
     }

   
       
}
