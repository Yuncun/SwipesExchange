package com.swipesexchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sharedObjects.SellListing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class MessagesFragment extends ListFragment {

	
	// member variables
	private final int num_parents = 7;
	//private ArrayList<ParentRow> parents;
    static MainActivity mActivity;
    private final ConversationList c_list;
    private int page_num;
    
    
   static MessagesFragment newInstance(int num, MainActivity my_activity) {
       
        MessagesFragment myFrag = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        myFrag.page_num = 5;
        mActivity = my_activity;
        myFrag.setArguments(args);
        //myFrag.c_list = new ConversationList();
        myFrag.c_list.addFakeMessages();
        
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
