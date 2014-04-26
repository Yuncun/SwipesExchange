package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.RequestTimeoutException;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
//import com.example.tabsfinal.MyExpandableAdapterBuy.TestConnect;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
        	    	
        	    	 
        	    	  //Log.d("owl", "cupcake");
        	    	  Log.d("test", "PostExecute");
        	    	  //d
        	    	
        	        }
        	    
    
        	      
        	        @Override
        	        protected List<BuyListing> doInBackground(Void... params) {
        	        	//android.os.Debug.waitForDebugger();
        	        	
        	        	 List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();
        	        	 //isFinished = false;
        	        	//in_use = true;
        	            //for (int i = 0; i < 5; i++) {
        	                try {
        	                	 //updatedBuyList=  new ArrayList<BuyListing>();
        	                	AWSCredentials credentials = new BasicAWSCredentials(Constants.getRegKey(),Constants.getSecKey() );
        	                	try{
        	                	  this.sdbClient = new AmazonSimpleDBClient( credentials); 
        	                	  Log.d("owl", "success creating DBClient");
        	                	}
        	                	catch (RequestTimeoutException e)
        	                	{
        	                		Log.d("panda", "timedout");
        	                	}
        	                   // sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
        	                    this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        	                    String test = "Listings";
        	                    
        	                    Log.d("test", "Beginning requests...");
        	                    
        	                  List<Item> items  = new ArrayList<Item>();
        	                String query = "select * from `BuyListings`";
        	                //Log.d("panda", "22");
        	                String nextToken = null;
        	                //int count = 1;
        	                do{
        	                    SelectRequest selectRequest = new SelectRequest(query);
        	                    
        	                    selectRequest.setConsistentRead(false);
        	                  //Log.d("panda", "33");
        	             
        	                    if(nextToken != null){
        	                    	
        	                        selectRequest.setNextToken(nextToken);
        	                    }
        	             
        	                    SelectResult result = sdbClient.select(selectRequest); 
        	                    
        	               //String name = result.getItems().get(count).getAttributes().get(count).getValue();
        	                  items.addAll(result.getItems());
        	                    String name = "bullshit";
        	                    //items.add(name);
        	                    nextToken = result.getNextToken();
        	                    //progressBar.setProgress(count*15);
        	                   count++;
        	                    
        	             
        	                }while(nextToken != null);
        	                
        	                Log.d("owl", "We passed the get loop");
        	                //Log.d("44", Integer.toString(items.size()));
        	                
        	              //Log.d("panda", Integer.toString(count));
        	              
        	              for(int i=0; i<items.size(); i++)
        	              {
        	            	  
        	            	  BuyListing bl = new BuyListing();
        	            	  int num_attributes = items.get(i).getAttributes().size();
        	            	  bl.isSection = false;
        	            	 
        	            	  if(num_attributes>0)
        	            	  {
        	            		  User u = new User(items.get(i).getAttributes().get(0).getValue());
        	            		  Log.d("owl", items.get(i).getAttributes().get(0).getValue());
        	            		  Log.d("owl", items.get(i).getAttributes().get(1).getValue());
        	            		  Log.d("owl", items.get(i).getAttributes().get(2).getValue());
        	            		  Log.d("owl", items.get(i).getAttributes().get(3).getValue());
        	            		  Log.d("owl", items.get(i).getAttributes().get(4).getValue());
        	            		  bl.setUser(u);
        	            		  if(num_attributes>1)
        	            		  {
        	            			  Venue v = new Venue(items.get(i).getAttributes().get(1).getValue());
                	            	  bl.setVenue(v);
	        	            		  if(num_attributes>2)
	        	            		  {
	        	            			  bl.setSwipeCount(Integer.parseInt(items.get(i).getAttributes().get(2).getValue()));
		        	            		  if(num_attributes>3)
		        	            		  {
		        	            			  bl.setEndTime(items.get(i).getAttributes().get(3).getValue());
			        	            		  if(num_attributes>4)
			        	            			  bl.setStartTime(items.get(i).getAttributes().get(4).getValue());
		        	            		  }
	        	            		  }
        	            		  }
        	            		  
        	            		  
        	            			  
        	            	  }
        	            	//progressBar.setProgress(i*15);
        	            	  updatedBuyList.add(bl);
        	            	
        	            
        	            	  Log.d("owl", "We finished the for set loop again");
        	            	  //Log.d("panda", Integer.toString(i));
        	            	  
        	              }
        	                
        	                
        	                     
        	                } catch (Exception e) {
        	                	Log.d("owl", "failure in setting our list");
        	                	Log.d("owl", e.getMessage());
        	                    //e.printStackTrace();
        	                }
        	            //in_use = false;
        	                //progressBar.dismiss();
        	           //cancel(true);
        	            return updatedBuyList;
        	        }
        	        

        	  
        	      
        	  
        	  }
              
              
}