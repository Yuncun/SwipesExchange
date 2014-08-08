package com.swipesexchange.messaging;



import com.swipesexchange.R;
import com.swipesexchange.R.layout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageListFragment extends ListFragment {

	
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {

		 getActivity().getActionBar().hide();
         View view = inflater.inflate(R.layout.message_list, container, false);
         
         return view;
     }

     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
         


     }
}
