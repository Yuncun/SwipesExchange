package com.example.tabsfinal;

import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewListingFragment extends Fragment{

        //Button createbuylisting;
        
        private int page_num;
        
           static NewListingFragment newInstance(int num) {
               
                NewListingFragment myFrag = new NewListingFragment();
                Bundle args = new Bundle();
                args.putInt("num", num);
                myFrag.page_num = num;

                myFrag.setArguments(args);

                return myFrag;

            }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
                
        View view = inflater.inflate(R.layout.addlisting, container, false);
        return view;
    }
        
        
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                
            super.onActivityCreated(savedInstanceState);
            
            
            
            //Later this should be moved into main activity
         //   final Communications coms = new Communications();
            
            //Button button = (Button) getView().findViewById(R.id.buybutton);
           // button.setOnClickListener(new View.OnClickListener() {
              //  public void onClick(View v) {
                        
                        //Communications.addBuyerListing(buyList);
                   //     System.out.println("BuyListingUpdated!");
                    // Do something in response to button click
               // }
           // });
                
        }
            
        
}
