package com.example.tabsfinal;

import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BuyerFragment extends Fragment{

	//Button createbuylisting;
	
	
	
	   static BuyerFragment newInstance(int num) {
	       
	        BuyerFragment myfrag = new BuyerFragment();
/*	        Bundle args = new Bundle();
	        args.putInt("num", num);

	        l.setArguments(args);
*/
	        return myfrag;
	    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View view = inflater.inflate(R.layout.addlisting, container, false);
        return view;
    }
	
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		
	    super.onActivityCreated(savedInstanceState);
	    
	    
	    final BuyListing buyList = new BuyListing();
	    User randomUser = new User("Fuck you");
	    buyList.setUser(randomUser);
	    buyList.setEndTime("3:00PM");
	    buyList.setStartTime("2:00PM");
	    buyList.setSwipeCount(5);
	    Venue randomVenue = new Venue("Fuck you again");
	    buyList.setVenue(randomVenue);
	    
	    //Later this should be moved into main activity
	 //   final Communications coms = new Communications();
	    
	    Button button = (Button) getView().findViewById(R.id.buybutton);
	    button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	Communications.addBuyerListing(buyList);
	        	System.out.println("BuyListingUpdated!");
	            // Do something in response to button click
	        }
	    });
	        
	}
	    
	
}

