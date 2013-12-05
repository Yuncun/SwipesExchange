package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class NewListingFragment extends Fragment{

        //Button createbuylisting;
	
		Button time1;
		
		private ArrayList<String> parentItems = new ArrayList<String>();
		private ArrayList<Object> childItems = new ArrayList<Object>();
        
        private int page_num;
        
      
        
           static NewListingFragment newInstance(int num) {
               
                NewListingFragment myFrag = new NewListingFragment();
                Bundle args = new Bundle();
                args.putInt("num", num);
                myFrag.page_num = 4;
                
                myFrag.setArguments(args);
                
                

                return myFrag;

            }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        	
        	//setContentView(R.layout.expandable_list_view);
                
        View view = inflater.inflate(R.layout.expandable_list_view, container, false);
       
        
        //final TimePicker tp = (TimePicker) view.findViewById(R.id.timePicker1);
        //tp.setVisibility(View.INVISIBLE);
       // tp.setVisibility(View.VISIBLE);
  
        //TimePickerFragment tpf = TimePickerFragment.newInstance(2);
       // FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
       
       // transaction.add(tpf, "embeddedTime").commit();
        
        
        
        /*view.findViewById(R.id.Text1).setOnClickListener(new OnClickListener() {
        	 
			@Override
			public void onClick(View v) {
					Log.d("button", "Qutie far");
					
					Log.d("button", "END");
				
					toggle_contents(tp, v);
				
			}
		});
        */
        return view;
    }
        /*
        public void toggle_contents(TimePicker tp, View v) {
        	if(tp.isShown())
			{
        		FX.slide_up(getActivity().getApplicationContext(), tp);
				tp.setVisibility(View.INVISIBLE);
			}
			else
			{
				FX.slide_down(getActivity().getApplicationContext(), tp);
				tp.setVisibility(View.VISIBLE);
			}
        	
        }
*/

    
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                
            super.onActivityCreated(savedInstanceState);
            
            
            ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.exlist);
            if(parentItems.isEmpty())
            	setGroupParents();
            if(childItems.isEmpty())
            	setChildData();
            
           
            //lv.setGroupIndicator(null);
         
            MyExpandableAdapter adapter = new MyExpandableAdapter(lv, parentItems, childItems);
            adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
            
           /* lv.setOnGroupClickListener(new OnGroupClickListener() {
           	 public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id){
                    // TODO Auto-generated method stub.
                    if(adapter.getChildrenCount(groupPosition) == 0){
                        return false;
                    }else{
                        return true;                   
                    }
                 
           	
           }});*/
            /*
            lv.setOnChildClickListener(new OnChildClickListener() {



                @Override

                public boolean onChildClick(ExpandableListView parent, View v,

                        int groupPosition, int childPosition, long id) {

                    // TODO Auto-generated method stub

                    if (groupPosition==4)
                    {
                    	parent.collapseGroup(groupPosition);
                    }

                    return true;

                }

            });*/
            
           // lv.setGroupIndicator(null);
            
           //lv.setClickable(true);
            lv.setAdapter(adapter);
            
            
           
           // lv.setOnChildClickListener(new OnClickListener();
            
            
            
            
            
            //Later this should be moved into main activity
         //   final Communications coms = new Communications();
           
              //  public void onClick(View v) {
                        
                        //Communications.addBuyerListing(buyList);
                   //     System.out.println("BuyListingUpdated!");
                    // Do something in response to button click
               // }
           // });
                
        }
        
        public void setGroupParents() {
        	parentItems.add("Set Start Time                 SET");
        	parentItems.add("Start Time:");
        	parentItems.add("Set End Time                           PRESS");
        	parentItems.add("End Time:");
        	parentItems.add("Set Venue");
        	parentItems.add("Venue:");
        	parentItems.add("Set Price");
        	parentItems.add("Price:");
        	parentItems.add("Set amount:");
        	parentItems.add("Swipes offered:");
        	parentItems.add("");
        	parentItems.add("Submit");
        	
        }
        
        public void setChildData() {
        	ArrayList<String> child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	//child.add("Games");
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	//child.add("PlaceHolder");
        	childItems.add(child);
       
        	
        	child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	childItems.add(child);
        	
        	
        	child = new ArrayList<String>();
        	//child.add("PlaceHolder");
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
        	//child.add("Applet");
        	//child.add("AspectJ");
        	//child.add("Beans");
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	//child.add("Some text");
        	childItems.add(child);
        	
        	
        	
        	child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	child.add("PlaceHolder");
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	childItems.add(child);
        	
        	child = new ArrayList<String>();
        	childItems.add(child);
        	
        }
            
        
}
