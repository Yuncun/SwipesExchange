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

public class NewListingFragment extends Fragment{

	// member variables
		private final int num_parents = 5;
		private ArrayList<ParentRow> parents;
        static MainActivity mActivity;
        private ExpandableListView lv;
        private int page_num;
        
        
       static NewListingFragment newInstance(int num, MainActivity my_activity) {
           
            NewListingFragment myFrag = new NewListingFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            myFrag.page_num = 4;
            mActivity = my_activity;
            myFrag.setArguments(args);
            
            
            return myFrag;

       }
           
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        	super.onCreateView(inflater, container, savedInstanceState);
        	
        	// inflate the fragment View with the LinearLayout containing the ELV
	        View view = inflater.inflate(R.layout.expandable_list_view, container, false);

	        return view;
    }

        
    
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                
            super.onActivityCreated(savedInstanceState);
            
            
            // find the ELV
            this.lv = (ExpandableListView) getActivity().findViewById(R.id.exlist);
         
            this.parents = new ArrayList<ParentRow>();
            // fill the ParentRow list
            //if(parents.isEmpty())
            	this.setGroupParents();
            
            // set the custom adapter
            MyExpandableAdapter adapter = new MyExpandableAdapter(this.lv, this.parents);
            adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
            this.lv.setAdapter(adapter);
       
        }
        
        public void setGroupParents() {
        	this.parents.clear();
        	// there are 6 parent rows: StartTime, EndTime, Venue, Number of Swipes, (EMPTY), and Submit
        	for(int i=0; i < this.num_parents; i++)
        	{
        		final ParentRow parent = new ParentRow();
        		// StartTime, i==0
        		if(i==0)
        		{
        			parent.setName("StartTime");
        			parent.setTextLeft("Create a Sell Listing");
        			parent.setTextRight("");
        			parent.setChildren(new ArrayList<ChildRow>());
        			
        			// create the TimePicker for StartTime and add it
        			TimePickerRow child = new TimePickerRow();
        			child.setName("StartTimePicker");
        			parent.getChildren().add(child);
        		}
        		else if(i==1) // EndTime, i==1
        		{
        			parent.setName("EndTime");
        			parent.setTextLeft("End Time:");
        			parent.setTextRight("");
        			parent.setChildren(new ArrayList<ChildRow>());
        			
        			// create the TimePicker for EndTime and add it
        			TimePickerRow child = new TimePickerRow();
        			child.setName("EndTimePicker");
        			parent.getChildren().add(child);
        		}
        		else if(i==2) // Venue, i==2
        		{
        			parent.setName("SetVenue");
        			parent.setTextLeft("Set Venue:");
        			parent.setTextRight("");
        			parent.setChildren(new ArrayList<ChildRow>());
        			
        		    // create the TextRow children and add them
        			// Any
        			TextRow child0 = new TextRow();
        			child0.setName("V0");
        			child0.setText("Any");
        			parent.getChildren().add(child0);
        			// Bruin Cafe
        			TextRow child1 = new TextRow();
        			child1.setName("V1");
        			child1.setText("Bruin Cafe");
        			parent.getChildren().add(child1);
        			// Cafe 1919
        			TextRow child2 = new TextRow();
        			child2.setName("V2");
        			child2.setText("Cafe 1919");
        			parent.getChildren().add(child2);
        			// Covell
        			TextRow child3 = new TextRow();
        			child3.setName("V3");
        			child3.setText("Covell");
        			parent.getChildren().add(child3);
        			// De Neve
        			TextRow child4 = new TextRow();
        			child4.setName("V4");
        			child4.setText("De Neve");
        			parent.getChildren().add(child4);
        			// Feast
        			TextRow child5 = new TextRow();
        			child5.setName("V5");
        			child5.setText("Feast");
        			parent.getChildren().add(child5);
        			// Hedrick
        			TextRow child6 = new TextRow();
        			child6.setName("V6");
        			child6.setText("Hedrick");
        			parent.getChildren().add(child6);
        			// Rendezvous
        			TextRow child7 = new TextRow();
        			child7.setName("V7");
        			child7.setText("Rendezvous");
        			parent.getChildren().add(child7);
        		}
        	    else if(i==3) //price
        		{
        	    	parent.setName("Price");
        			parent.setTextLeft("Set Price:");
        			parent.setTextRight("");
        			parent.setChildren(new ArrayList<ChildRow>());
        			
        			// create the NumberPickerRow and add it
        			NumberPickerRow child = new NumberPickerRow();
        			child.setName("PricePicker");
        			parent.getChildren().add(child);
        		}
        		/*
        	    else if(i==4) // empty
        		{
        	    	parent.setName("Emtpy");
        			parent.setTextLeft("");
        			parent.setTextRight("");
        			parent.setChildren(new ArrayList<ChildRow>());
        			// create the Empty child
        			TextRow child = new TextRow();
        			child.setName("Empty");
        			child.setText("");
        			
        			parent.getChildren().add(child);
        		}*/
        		else if(i==4) //submit
        		{
        			parent.setName("Submit");
        			parent.setTextLeft("Submit");
        			parent.setTextRight("");
        			
        			
        			parent.setChildren(new ArrayList<ChildRow>());
        			// create the Empty child
        			TextRow child = new TextRow();
        			child.setName("Empty_Submit");
        			child.setText("");
        			
        			parent.getChildren().add(child);
        		}
        	

        		this.parents.add(parent);
        	}
        	
        }
        
       
            
        
}

