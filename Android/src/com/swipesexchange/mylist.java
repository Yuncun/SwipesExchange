package com.swipesexchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sharedObjects.BuyListing;
import sharedObjects.SellListing;
import sharedObjects.User;
import sharedObjects.Venue;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.RequestTimeoutException;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
//import com.swipesexchange.MyExpandableAdapterBuy.TestConnect;

class MyList extends ListFragment
{
		//page_num 0 is Buy listings, 1 is Sell listings, 2 is Messages (can add more)
		public int page_num;
		public BackendData data;
		static MainActivity mActivity;
        public List<BuyListing> buyEntries;
        
        private TestConnectGet tc;
        private Context v;
        
        Button btnStartProgress;

        static MyList newInstance(int num, MainActivity my_activity, BackendData bd) {
        	mActivity = my_activity;
        MyList l = new MyList();
        l.v = my_activity;
        l.buyEntries = new ArrayList<BuyListing>();
        l.page_num = num;
   
        Bundle args = new Bundle();
        args.putInt("num", num);
       
   	
       l.setArguments(args);

        return l;
    }
        
        
        public void doMore(){
        	
        	if(buyEntries.size()>1) {
         	   Collections.sort(buyEntries, new Comparator<BuyListing>(){
         		   public int compare(BuyListing emp1, BuyListing emp2) {
         		     return emp1.getVenue().getName().compareToIgnoreCase(emp2.getVenue().getName());
         		     
         		     
         		   }
         	   
         		 });
         	   }
                
        	
               
         	   BuyListAdapter adapter= new BuyListAdapter(getActivity(), buyEntries);
                setListAdapter(adapter);
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
                
      
               if(this.page_num==0)
               {
            	   
            	   tc = new TestConnectGet(getActivity());
       
            	   
            	   
            		   tc.execute();
            	   
            	   
          
                   
               }
               else if(this.page_num==1)
               {
            	  
            	   BackendData bd = new BackendData();
            	   bd.updateListings();
            	   List<SellListing> sellEntries = bd.getSellListings();
            	   if(sellEntries.size()>1)
            	   {
            		   	Collections.sort(sellEntries, new Comparator<SellListing>()
            		   	{
	            		    public int compare(SellListing emp1, SellListing emp2) 
	            	   		{
	            		    	return emp1.getVenue().getName().compareToIgnoreCase(emp2.getVenue().getName());
	            	   		}
            		   	});
            	  }
             
                   SellListAdapter adapter = new SellListAdapter(getActivity(), sellEntries);
                   setListAdapter(adapter);
          
               }
       
            }
            
            private class TestConnectGet extends AsyncTask<Void, Void, List<BuyListing>> {

        		
        		   
        		   private Context context;
        		   private ProgressDialog progressBar;
        	       protected AmazonSimpleDBClient sdbClient;
        	       int count;
        	  
 

        	        public TestConnectGet(Context context) {
        	        	this.context = context;
        	        }
        	        
        	        @Override
        	        protected void onPreExecute() {
        	           // super.onPreExecute();
        	            Log.d("test", "PreExecute1");
        	            
        	
        	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
        	            
        	            
        	        	Log.d("test", "PreExecute2");
        	            
        	           
        	        }
        	        
        	      @Override
        	    protected void onPostExecute(List<BuyListing> result) {
        	    	//android.os.Debug.waitForDebugger();
        	    	  Log.d("test", "PostExecute1");
        	        //do stuff
        	    	 //super.onPostExecute(result);
        	    	  progressBar.dismiss();
        	    	  buyEntries = result;
        	    	  doMore();

        	    	  Log.d("test", "PostExecute");
        	    	  //d
        	    	
        	        }
        	    
    
        	      
        	        @Override
        	        protected List<BuyListing> doInBackground(Void... params) {
        	        	List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();

        	                try {
        	                		updatedBuyList = ConnectToServlet.updateBList();
        	                	}
        	                	catch (RequestTimeoutException e)
        	                	{
        	                		Log.d("LOUD AND CLEAR", "Failure at updatedBlist");
        	                		
        	                	}
        	                   // sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
            	        	
        	            Log.d("LOUD AND CLEAR", "doInBackground reached. List contains; " + updatedBuyList.size() + " elements");

        	            for (int i = 0; i < updatedBuyList.size(); i ++)
        	            {
        	            	updatedBuyList.get(i).isSection = false;
        	            }
        	        //    
  
        	            return updatedBuyList;
        	        }
        	        
        	  }
              
              
}