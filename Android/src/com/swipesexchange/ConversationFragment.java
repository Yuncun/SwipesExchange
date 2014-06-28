package com.swipesexchange;



import java.util.ArrayList;
import sharedObjects.Message;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

		 //getActivity().getActionBar().hide();
         View view = inflater.inflate(R.layout.conversation_fragment_list, container, false);
         getActivity().getActionBar().show();
    
         return view;
     }

     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
         
         ConversationAdapter adapter = new ConversationAdapter(this.getActivity(), this.passed_messages);
         setListAdapter(adapter);

     }
}
