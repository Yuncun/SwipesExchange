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
		//private Boolean isFinished;
        public List<BuyListing> buyEntries;
        
        private TestConnectGet tc;
        private Context v;
        
        Button btnStartProgress;
       // ProgressDialog progressBar;
        private int progressBarStatus = 0;
        private Handler progressBarHandler = new Handler();
        
        
        
        //public boolean isFinished;
        static MyList newInstance(int num, MainActivity my_activity, BackendData bd) {
        	mActivity = my_activity;
        MyList l = new MyList();
        l.v = my_activity;
       //l.data = bd;
        l.buyEntries = new ArrayList<BuyListing>();
        
        if (num == 0)
        {
        	
        	
        	l.page_num = 0;
        }
        else if (num == 1)
        	l.page_num = 1;
        else if (num == 2)
        	l.page_num = 2;
        
        
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
                
                //data = new BackendData();
                //data.updateListings();
                
                Log.d("panda", "even farther");
                
                /*Some crazy shit to convert List<Listing> into List<String>*/
                
             //   switch (this.getId())
               // {-
                
              //  }
               if(this.page_num==0)
               {
            	   
            	   tc = new TestConnectGet(getActivity());
            	   //buyEntries = tc.execute().get();
            	   //isFinished=false;
            	   /*
				   try {
					buyEntries = tc.execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		*/
            	   
            	   //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            		   // tc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
            		//else
            	   //ProgressDialog dialog;
            	   
            		    tc.execute();
            	   
            	   
            	   /*
            	   if(buyEntries.size()>1) {
            	   Collections.sort(buyEntries, new Comparator<BuyListing>(){
            		   public int compare(BuyListing emp1, BuyListing emp2) {
            		     return emp1.getVenue().getName().compareToIgnoreCase(emp2.getVenue().getName());
            		     
            		     
            		   }
            	   
            		 });
            	   }
                   
         
                   

                  
            	   BuyListAdapter adapter= new BuyListAdapter(getActivity(), buyEntries);
                   setListAdapter(adapter);
                   */
                   
               }
               else if(this.page_num==1)
               {
            	   //List<BuyListing> buyEntries = data.getBuyListings();
            	   BackendData bd = new BackendData();
            	   bd.updateListings();
  List<SellListing> sellEntries = bd.getSellListings();
  if(sellEntries.size()>1){
  Collections.sort(sellEntries, new Comparator<SellListing>(){
	   public int compare(SellListing emp1, SellListing emp2) {
	     return emp1.getVenue().getName().compareToIgnoreCase(emp2.getVenue().getName());
	   }
	 });
  }
                   /*
            	  // Log.d("test", "Fuck");
                   //System.out.println("tag", "FUCK11111111111111111111111111");
                   List<String> VenueNames = new ArrayList<String>();
                   List<String> UserNames = new ArrayList<String>();
                   List<String> DescTime = new ArrayList<String>();
                   List<String> DescAmount = new ArrayList<String>();
                   List<String> Prices = new ArrayList<String>();
                   
                   for (int i = 0; i < sellEntries.size(); ++i) {
                           VenueNames.add(((sellEntries.get(i)).getVenue()).getName());
                           
                           UserNames.add(((sellEntries.get(i)).getUser()).getName());
                          // UserNames.add(((sellEntries.get(i)).getUser()).getName());
                           DescTime.add((sellEntries.get(i)).getEndTime());
                           //DescTime.add((sellEntries.get(i)).getEndTime());
                           DescAmount.add(String.valueOf((sellEntries.get(i)).getSwipeCount()));
                           //DescAmount.add(String.valueOf((sellEntries.get(i)).getSwipeCount()));
                           Prices.add(String.valueOf((sellEntries.get(i)).getPrice()));
                   }
                   
                   //SimpleAdapter that should be able to put up multiple List<Strings>
               
                   
                   List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                   
                   int count = sellEntries.size();
                   for(int i = 0; i < count; i++) {
                   	Map<String, String> map = new HashMap<String, String>();
                       map.put("Headline", UserNames.get(i));
                       map.put("Subline", "Until " +  DescTime.get(i));
                       map.put("Headline2", "" + DescAmount.get(i));
                       map.put("Headline3", "Price: " + Prices.get(i));
                       
                       list.add(map);
                   }
	*/
                   SellListAdapter adapter = new SellListAdapter(getActivity(), sellEntries);
                   setListAdapter(adapter);
          
               }
            //else if (this.page_num==2)
            //{
            	//int x = 2;
            	//x++;
            	/*List<BuyListing> buyEntries = data.getBuyListings();
                Log.d("test", "sdf");
                
                List<String> VenueNames = new ArrayList<String>();
                List<String> UserNames = new ArrayList<String>();
                List<String> DescTime = new ArrayList<String>();
                List<String> DescAmount = new ArrayList<String>();
               // List<String> Prices = new ArrayList<String>();
                
                for (int i = 0; i < buyEntries.size(); ++i) {
                        VenueNames.add(((buyEntries.get(i)).getVenue()).getName());
                        
                        UserNames.add(((buyEntries.get(i)).getUser()).getName());
                       // UserNames.add(((sellEntries.get(i)).getUser()).getName());
                        DescTime.add((buyEntries.get(i)).getEndTime());
                        //DescTime.add((sellEntries.get(i)).getEndTime());
                        DescAmount.add(String.valueOf((buyEntries.get(i)).getSwipeCount()));
                        //DescAmount.add(String.valueOf((sellEntries.get(i)).getSwipeCount()));
                        //Prices.add(String.valueOf((sellEntries.get(i)).getPrice()));
                }
                
                //SimpleAdapter that should be able to put up multiple List<Strings>
            
                
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                
                int count = buyEntries.size();
                for(int i = 0; i < count; i++) {
                	Map<String, String> map = new HashMap<String, String>();
                    map.put("Headline", UserNames.get(i));
                    map.put("Subline", "Until " +  DescTime.get(i));
                    map.put("Headline2", "" + DescAmount.get(i) + " requested");
                    
                    list.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.sell_list_item, new String[] { "Headline", "Subline", "Headline2" }, new int[] { R.id.firstLine, R.id.secondLine , R.id.firstLineRight});
                setListAdapter(adapter);
                */
            
            //}
            }
            
            private class TestConnectGet extends AsyncTask<Void, Void, List<BuyListing>> {

        		  public static final int USER_SORT = 1;
        		   
        		   private Context context;
        		   
        		   private ProgressDialog progressBar;
        		  
        		  private BackendData data_parent;
        	        private final ReentrantLock lock = new ReentrantLock();
        	        public static final int VENUE_SORT  = 2;
        	        public static final int NO_SORT     = 0;
        	        
        	        private static final String BUY_LISTINGS_DOMAIN = "BuyListings";
        	        
        	        private static final String USER_ATTRIBUTE = "Name"; //Not sure what this might fuck up so Im not gonna change it to "users" as it should be
        	        private static final String VENUE_ATTRIBUTE = "Venue";
        	        
        	        private static final String USER_SORT_QUERY = "select player, score from HighScores where player > '' order by player asc";
        	        private static final String VENUE_SORT_QUERY = "select player, score from HighScores where score >= '0' order by score desc";
        	        private static final String NO_SORT_QUERY = "select player, score from HighScores";
        	        
        	        private static final String COUNT_QUERY = "select count(*) from HighScores";
        	        public Boolean in_use;
        	        protected AmazonSimpleDBClient sdbClient;
        	        protected String nextToken;
        	        protected int sortMethod;
        	        protected int count;
        	        // OLD KEY public String reg_key = "AKIAJWQU5ZV4ZEZHRDWA";
        	        // OLD KEY public String sec_key = "cgwIKqYn1YoYDhnkqt4oPaizIXdWeHtgNlliBaND";
        	        
        	        public String reg_key = "AKIAIBKEOA7FKTHHVG7Q";
        	        public String sec_key = "5kap6qSvIB6VYdxEt+w10rYz8C41UUp2s1f2umd/";
        	        
        	        
        	        
        	        //private ProgressDialog dialog;
        	    
        	     //public List<BuyListing> updatedBuyList;
        	      
        	     //public List<BuyListing> getBuyListing()
        	     
        	    	 //return updatedBuyList;
        	     //}
        	       /* @Override
        	        protected void onPreExecute() {
        	        	dialog = new ProgressDialog();
        	            this.dialog.setMessage("Progress start");
        	            this.dialog.show();
        	            
        	            
        	        }*/

        	        public TestConnectGet(Context context) {
        	        	this.context = context;
        	        }
        	        
        	        @Override
        	        protected void onPreExecute() {
        	           // super.onPreExecute();
        	            Log.d("test", "PreExecute1");
        	            //progressBar = new ProgressDialog(v);
        	            //progressBar.setCanceledOnTouchOutside(false);
        	           
        	            //progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        	           // progressBar.setProgress(0);
        	            //progressBar.setMax(100);
        	        	//Toast.makeText(this.context, "Loading listings...", Toast.LENGTH_SHORT).show();
        	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
        	            //progressBar.show();
        	            
        	        	Log.d("test", "PreExecute2");
        	            
        	            //progressBarStatus = 0;
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
        	    
              
             /* private List<BuyListing> myMethod(List<BuyListing> myList){
                  //handle value 
              	myList = getBuyListing();
              	Log.d("owl", Integer.toString(myList.size()));
                    return myList; 
                  }*/
        	      
        	   // @Override
        	    //protected void onPreExecute() {
        	    //	super.onPreExecute();
        	      // data_parent.isFinished = false;
        	   // }
        	      
        	        @Override
        	        protected List<BuyListing> doInBackground(Void... params) {
        	        	//android.os.Debug.waitForDebugger();
        	        	
        	        	 List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();
        	        	 //isFinished = false;
        	        	//in_use = true;
        	            //for (int i = 0; i < 5; i++) {
        	                try {
        	                	 //updatedBuyList=  new ArrayList<BuyListing>();
        	                	AWSCredentials credentials = new BasicAWSCredentials( reg_key,sec_key );
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