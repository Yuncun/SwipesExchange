package com.swipesexchange;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class TestTask extends AsyncTask<Void, Void, List<BuyListing>> {

	  public static final int USER_SORT = 1;
        
        public static final int VENUE_SORT  = 2;
        public static final int NO_SORT     = 0;
        
        private static final String LISTINGS_DOMAIN = "Listings";
        
        private static final String USER_ATTRIBUTE = "Name"; //Not sure what this might fuck up so Im not gonna change it to "users" as it should be
        private static final String VENUE_ATTRIBUTE = "Venue";
        
        private static final String USER_SORT_QUERY = "select player, score from HighScores where player > '' order by player asc";
        private static final String VENUE_SORT_QUERY = "select player, score from HighScores where score >= '0' order by score desc";
        private static final String NO_SORT_QUERY = "select player, score from HighScores";
        
        private static final String COUNT_QUERY = "select count(*) from HighScores";
                
        protected AmazonSimpleDBClient sdbClient;
        protected String nextToken;
        protected int sortMethod;
        protected int count;
        public String reg_key = "AKIAJWQU5ZV4ZEZHRDWA";
        public String sec_key = "cgwIKqYn1YoYDhnkqt4oPaizIXdWeHtgNlliBaND";
        
        
        public interface OnTaskCompleted{
            List<BuyListing> onTaskCompleted();
        }

        private OnTaskCompleted listener;

        public TestTask(OnTaskCompleted listener){
            this.listener = listener;
        }
protected void onPostExecute(List<BuyListing> mylist){
            // Call the interface method
            if (listener != null)
                listener.onTaskCompleted(mylist);
            
        }
        
        @Override
        protected List<BuyListing> doInBackground(Void... params) {
        	android.os.Debug.waitForDebugger();
        	List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();
            //for (int i = 0; i < 5; i++) {
                try {
                	AWSCredentials credentials = new BasicAWSCredentials( reg_key,sec_key );
                    this.sdbClient = new AmazonSimpleDBClient( credentials); 
                    sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
                    this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
                    String test = "Listings";
                    
                    
                  List<Item> items  = new ArrayList<Item>();
                String query = "select * from `Listings`";
                Log.d("panda", "fuck");
                String nextToken = null;
                int count = 0;
                do{
                    SelectRequest selectRequest = new SelectRequest(query);
                    selectRequest.setConsistentRead(false);
                  Log.d("panda", "fuck");
             
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
                
                Log.d("panda", Integer.toString(items.size()));
                
              Log.d("panda", Integer.toString(count));
              
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
                     **/
                     
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            
            return updatedBuyList;
        }
  }
  