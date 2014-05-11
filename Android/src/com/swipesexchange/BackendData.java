package com.swipesexchange;


//WILL BE OBSOLETE ONCE BACKEND IS IMPLEMENTED FOR SELL LIST
import java.util.ArrayList;
import java.util.List;

import sharedObjects.BuyListing;
import sharedObjects.SellListing;
import sharedObjects.User;
import sharedObjects.Venue;
import android.util.Log;
//import com.swipesexchange.TestTask.OnTaskCompleted;
//import com.swipesexchange.MainActivity.TestConnect;




public class BackendData {
	
   
        private List<SellListing> mySellListings;
        private List<BuyListing> myBuyListings;
        private Boolean isFinished;
        
        //private MainActivity mActivity;
        
        
        
        
        public BackendData()
        {
        	//mActivity = my_activity;
        	
        	
        }
        
        private void updateBuyListings(List<BuyListing> mylist)
        {
        	myBuyListings = mylist;
        }
        
       /* private OnTaskCompleted listener = new OnTaskCompleted() {
            public List<BuyListing> onTaskCompleted() {
               // status = true;
                Toast.makeText(mActivity, "Status", Toast.LENGTH_SHORT).show();
                return List<BuyLi>
            }
        };*/
            
        
        public List<SellListing> getSellListings() {
        	return mySellListings;
        }

        public List<BuyListing> getBuyListings() {
                return myBuyListings;
        }

        public void setBuyListings(List<BuyListing> buy_listings) {
                myBuyListings = buy_listings;
        }
        
        public void setSellListings(List<SellListing> sell_listings) {
        	mySellListings = sell_listings;
        }
        
    
        public void updateListings()
        {
                
                //Currently hardwired to sample values, but this will later be used to update the listings from backend data.
                int random = (int )(Math.random() * 12 + 1);
                
              //  new TestTask(listener).execute();
                //if(!(myBuyListings.size()>0))
                
                //TestConnectGet tc = new TestConnectGet(this);
               // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                   // tc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
                //else
                	//tc.execute();
                	//while(tc.in_use==true)
                	//{
                	//	
                	//}
                	
                	//while(!isFinished)
                	//{
                		
                		//Log.d("pie", "hi");
                	//}
               
                List<SellListing> updatedSellList = new ArrayList<SellListing>();
                List<BuyListing> updatedBuyList2 = new ArrayList<BuyListing>();
                
                //---RETRIEVE FROM DB the LISTING NAMED "ERIC SHEN"---//
            	
//            	hs.clearHighScores();
            	//hs.createHighScoresDomain();
            
                //TestConnect tc = mActivity.getTC();
        //DATABASE ITEMS
        
        for (int i = 0; i < random; i++) {
                
                BuyListing randomBuyEntry = new BuyListing();
                SellListing randomSellEntry = new SellListing();
                
                //RANDOM ENTRIES
                String[] venueNames = new String[] {"Covel", "De Neve", "Bruin Cafe", "Feast", "RendezVous", "Cafe 1919"};
                Venue randomVenue = new Venue(venueNames[(int )(Math.random() * 5 + 1)]);
                
                String[] randomNames = new String[] {
                                "Peter Cech",
                                "Branislav Ivanovic",
                                "Ashley Cole",
                                "David Luiz",
                                "Michael Essien",
                                "Ramires",
                                "Frank Lampard",
                                "Fernando Torres",
                                "Juan Mata",
                                "Oscar",
                                "John Mikel Obi",
                                "Andre Schurrle",
                                "Kevin De Bruyne",
                                "Marco van Ginkel",
                                "John Terry"};
                User randomUser = new User(randomNames[(int )(Math.random() * 13 + 1)]);
                
                randomBuyEntry.setVenue(randomVenue);
                randomSellEntry.setVenue(randomVenue);
                
                randomBuyEntry.setUser(randomUser);
                randomSellEntry.setUser(randomUser);
                
                randomBuyEntry.setTime("time left");
                randomSellEntry.setTime("time left");
                
                randomBuyEntry.setEndTime(String.valueOf((int)(Math.random() * 7 + 4)) + ":00");
                randomSellEntry.setEndTime(String.valueOf((int)(Math.random() * 7 + 4)) + ":00");
                randomBuyEntry.setStartTime("2:00");
                randomSellEntry.setStartTime("8:00");
                
                int randomMath = (int )(Math.random() * 15 + 1);
                randomBuyEntry.setSwipeCount(randomMath); 
                randomSellEntry.setSwipeCount(randomMath);
                
                randomBuyEntry.isSection = false;
                randomSellEntry.isSection = false;
                double random_price = 2.35;
                randomSellEntry.setPrice(random_price);
                
         updatedSellList.add(randomSellEntry);
         // updatedBuyList2.add(randomBuyEntry);
          
          
          }

	Log.d("panda", "before updating");
        
                this.mySellListings = updatedSellList;
                /*try {
					this.myBuyListings = tc.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
               
                
               // this.myBuyListings = updatedBuyList2;
        }
        
      /*  private class TestConnectGet extends AsyncTask<Void, Void, List<BuyListing>> {

  		  public static final int USER_SORT = 1;
  		  
  		  private BackendData data_parent;
  	        private final ReentrantLock lock = new ReentrantLock();
  	        public static final int VENUE_SORT  = 2;
  	        public static final int NO_SORT     = 0;
  	        
  	        private static final String LISTINGS_DOMAIN = "Listings";
  	        
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
  	        public String reg_key = "AKIAJWQU5ZV4ZEZHRDWA";
  	        public String sec_key = "cgwIKqYn1YoYDhnkqt4oPaizIXdWeHtgNlliBaND";
  	        
  	        public TestConnectGet(BackendData parent)
  	        {
  	        	data_parent = parent;
  	        }
  	     public List<BuyListing> updatedBuyList;
  	      
  	     public List<BuyListing> getBuyListing()
  	     {
  	    	 return updatedBuyList;
  	     }
  	      @Override
  	    protected void onPostExecute(List<BuyListing> result) {
  	        //do stuff
  	    	  super.onPostExecute(result);
  	    	  Log.d("owl", "cupcake");
  	    	  
  	    	  data_parent.updateBuyListings(result);
  	    	  data_parent.isFinished=true;
  	                
  	        }
  	    
        
       /* private List<BuyListing> myMethod(List<BuyListing> myList){
            //handle value 
        	myList = getBuyListing();
        	Log.d("owl", Integer.toString(myList.size()));
              return myList; 
            }*/
  	      /*
  	    @Override
  	    protected void onPreExecute() {
  	    	super.onPreExecute();
  	       data_parent.isFinished = false;
  	    }
  	      
  	        @Override
  	        protected List<BuyListing> doInBackground(Void... params) {
  	        	//android.os.Debug.waitForDebugger();
  	        
  	        	in_use = true;
  	            //for (int i = 0; i < 5; i++) {
  	                try {
  	                	 updatedBuyList=  new ArrayList<BuyListing>();
  	                	AWSCredentials credentials = new BasicAWSCredentials( reg_key,sec_key );
  	                	try{
  	                	  this.sdbClient = new AmazonSimpleDBClient( credentials); 
  	                	}
  	                	catch (RequestTimeoutException e)
  	                	{
  	                		Log.d("panda", "timedout");
  	                	}
  	                    sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
  	                    this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
  	                    String test = "Listings";
  	                    
  	                    
  	                  List<Item> items  = new ArrayList<Item>();
  	                String query = "select * from `Listings`";
  	                Log.d("panda", "22");
  	                String nextToken = null;
  	                int count = 0;
  	                do{
  	                    SelectRequest selectRequest = new SelectRequest(query);
  	                    
  	                    selectRequest.setConsistentRead(false);
  	                  Log.d("panda", "33");
  	             
  	                    if(nextToken != null){
  	                    	
  	                        selectRequest.setNextToken(nextToken);
  	                    }
  	             
  	                    SelectResult result = sdbClient.select(selectRequest); 
  	                    
  	               //String name = result.getItems().get(count).getAttributes().get(count).getValue();
  	                  items.addAll(result.getItems());
  	                    String name = "bullshit";
  	                    //items.add(name);
  	                    nextToken = result.getNextToken();
  	                    count++;
  	                    
  	             
  	                }while(nextToken != null);
  	                
  	                //Log.d("44", Integer.toString(items.size()));
  	                
  	              //Log.d("panda", Integer.toString(count));
  	              
  	              for(int i=0; i<items.size(); i++)
  	              {
  	            	  BuyListing bl = new BuyListing();
  	            	  User u = new User(items.get(i).getAttributes().get(0).getValue());
  	            	  Log.d("panda", items.get(i).getAttributes().get(0).getValue());
  	            	  bl.setUser(u);
  	            	  Venue v = new Venue(items.get(i).getAttributes().get(1).getValue());
  	            	  bl.setVenue(v);
  	            	  bl.setSwipeCount(Integer.parseInt(items.get(i).getAttributes().get(2).getValue()));
  	            	  bl.setStartTime(items.get(i).getAttributes().get(3).getValue());
  	            	  bl.isSection = false;
  	            	 bl.setEndTime(items.get(i).getAttributes().get(4).getValue());
  	            	  updatedBuyList.add(bl);
  	            	  Log.d("panda", Integer.toString(i));
  	            	  
  	              }
  	                
  	                //int i=0;
  	                //List<String> names = new ArrayList<String>();
  	              //List<String> venues = new ArrayList<String>();
  	                
  	                //for(i=0; i < items.size(); i++)
  	                //{
  	                	//String mystring = items.get(i).getC
  	                //}
  	                 
  	                //logger.debug("Found matching items: " + items.size());
  	                //return items;
  	                   /* 
  	                  GetAttributesRequest gar = new GetAttributesRequest( LISTINGS_DOMAIN, listing );
  	                GetAttributesResult response = this.sdbClient.getAttributes(gar);
  	                
  	                String playerName = this.getStringValueForAttributeFromList( USER_ATTRIBUTE, response.getAttributes() );                                
  	                int score = this.getIntValueForAttributeFromList( VENUE_ATTRIBUTE, response.getAttributes() );
  	                
  	                Listing retrievedList = new BuyListing();
  	                String queryString = "SELECT * FROM `cars`";
  	                  int itemCount = 0;
  	                  String nextToken = null;
  	                  do {
  	                     QueryWithAttributesResult queryResults =
  	                        domain.selectItems(queryString, nextToken);
  	                     Map<String, List<ItemAttribute>> items =
  	                        queryResults.getItems();
  	                     for (String id : items.keySet()) {
  	                         System.out.println("Item : " + id);
  	                         for (ItemAttribute attr : items.get(id)) {
  	                             System.out.println(attr.getName() + " = "
  	                             + attr.getValue());
  	                         }
  	                         itemCount++;
  	                     }
  	                     nextToken = queryResults.getNextToken();
  	                   
  	               */
  	                
  	                    /*Listing score = new BuyListing();
  	             	   User my_user = new User("Mike");
  	             	   Venue my_venue = new Venue("Hell");
  	             	  score.setVenue(my_venue);
  	             	   score.setUser(my_user);
  	            		score.setEndTime("END OF THE WORLD");
  	            		score.setStartTime("THE BEGINNING");
  	            		score.setSwipeCount(99);
  	            		//dblisting.setTime(null);
  	                       //hs = new SimpleDBData();
  	                      // hs.addHighScore(dblisting);
  	                    
  	                    ReplaceableAttribute playerAttribute = new ReplaceableAttribute( USER_ATTRIBUTE, score.getUser().getName(), Boolean.TRUE );
  	                    ReplaceableAttribute scoreAttribute = new ReplaceableAttribute( VENUE_ATTRIBUTE, score.getVenue().getName(), Boolean.TRUE );
  	                    
  	                    List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(2);
  	                    attrs.add( playerAttribute );
  	                    attrs.add( scoreAttribute );
  	                    
  	                    PutAttributesRequest par = new PutAttributesRequest(LISTINGS_DOMAIN, score.getUser().getName(), attrs);                
  	                    try {
  	                    		//my_count++;
  	                    		
  	                            this.sdbClient.putAttributes( par );
  	                    }
  	                    catch ( Exception exception ) {
  	                    	
  	                            System.out.println( "EXCEPTION = " + exception );
  	                    }

  	                    //if(!found)
  	                    //{
  	                        //this.createHighScoresDomain(test);
  	                          
  	                    //}
  	                    //Thread.sleep(1000);
  	                     
  	                     
  	                } catch (Exception e) {
  	                    //e.printStackTrace();
  	                }
  	            in_use = false;
  	            
  	           
  	            return updatedBuyList;
  	        }
  	        

  	  
  	      
  	  
  	  }
        */
        

}

