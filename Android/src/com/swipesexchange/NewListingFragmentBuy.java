package com.swipesexchange;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

public class NewListingFragmentBuy extends Fragment{


	
		Button time1;
		
		private ArrayList<String> parentItems = new ArrayList<String>();
		private ArrayList<Object> childItems = new ArrayList<Object>();
        static MainActivity mActivity;
        private int page_num;
        
      
        
           static NewListingFragmentBuy newInstance(int num, MainActivity my_activity) {
               
                NewListingFragmentBuy myFrag = new NewListingFragmentBuy();
                Bundle args = new Bundle();
                args.putInt("num", num);
                myFrag.page_num = 3;
                mActivity = my_activity;
                myFrag.setArguments(args);
                
                return myFrag;

            }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        	
        View view = inflater.inflate(R.layout.expandable_list_view_buy, container, false);
       
        return view;
    }

    
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                
            super.onActivityCreated(savedInstanceState);
            
            
            ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.exlistbuy);
            if(parentItems.isEmpty())
            	setGroupParents();
            if(childItems.isEmpty())
            	setChildData();
            
         
            MyExpandableAdapterBuy adapter = new MyExpandableAdapterBuy(lv, parentItems, childItems);
            adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
            lv.setAdapter(adapter);
            
            
          
        }
        
        public void setGroupParents() {
        	parentItems.add("Set Start Time");
        	parentItems.add("Set End Time");
        	parentItems.add("Set Venue");
        	parentItems.add("Set Amount");
        	parentItems.add("");
        	parentItems.add("Submit");
        	
        }
        
        public void setChildData() {
        	ArrayList<String> child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	childItems.add(child);
        	

       
        	
        	child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	childItems.add(child);
        	
        	
        	
        	
        	child = new ArrayList<String>();
        	child.add("Any");
        	child.add("Bruin Cafe");
        	child.add("Cafe 1919");
        	child.add("Covell");
        	child.add("De Neve");
        	child.add("Feast");
        	child.add("Hedrick");
        	child.add("RendeZvouz");
        	childItems.add(child);
        	
        	
        	
        	child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	childItems.add(child);
        	
        
        	
        	child = new ArrayList<String>();
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	childItems.add(child);
        	
        }
            
        
}

