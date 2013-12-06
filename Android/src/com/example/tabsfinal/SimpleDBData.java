package com.example.tabsfinal;

/*
 * Copyright 2010-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

/**NOTES (ES)**/
/*
 * 1) Data in simpleDB is stored as a key-value pairing (I think) using attributes. Attributes correspond to values. For example NAME_ATTRIBUTE m
 * might correspond to the the string name under listing
 * 
 * 2) Right now this only supports buyLISTINGS
 * 
 * 3) The comments are relics of the sampleHighScoresDB code, so it uses names like 'players' and 'scores. Ive changed these into 'users' and 'venues' respectively
 * changing the type of scores into string from int. 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.Region;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.amazonaws.services.simpledb.util.SimpleDBUtils;

// ========================================================
// This article provides more details on SimpleDB Queries.
// http://aws.amazon.com/articles/1231
// ========================================================


/*
 * This class provides all the functionality for database interaction list.
 *
 * The class uses SimpleDB to store individuals Items in a Domain.  
 * Each Item represents a player and their score.
 */
public class SimpleDBData {

        private static final String SELL_LISTINGS_DOMAIN = "SellListings";
        private static final String BUY_LISTINGS_DOMAIN = "BuyListings";
        
        private static final String USERNAME_ATTRIBUTE = "username";
        private static final String VENUE_ATTRIBUTE = "venue";
        private static final String START_TIME_ATTRIBUTE = "startTime";
        private static final String END_TIME_ATTRIBUTE = "endTime";
        private static final String COUNT_ATTRIBUTE = "count";
        private static final String PRICE_ATTRIBUTE = "price";
        
		private static final String SELL_LISTINGS_QUERY = "select * from 'SellListings' limit 100";
		private static final String BUY_LISTINGS_QUERY = "select * from 'BuyListings' limit 100";
        
        private static final String SELL_COUNT_QUERY = "select count(*) from 'SellListings'";
		private static final String BUY_COUNT_QUERY = "select count(*) from 'BuyListings'";
		
		private static final String LISTINGS_DOMAIN = "Listings";
		private static final String HIGH_SCORES_DOMAIN = "HighScores";
		private static final String PLAYER_ATTRIBUTE = "player";
		private static final String SCORE_ATTRIBUTE = "score";

        private static final String USER_SORT_QUERY = "select player, score from HighScores where player > '' order by player asc";
        private static final String VENUE_SORT_QUERY = "select player, score from HighScores where score >= '0' order by score desc";
        private static final String NO_SORT_QUERY = "select player, score from HighScores";        
        private static final String COUNT_QUERY = "select count(*) from HighScores";

        public static final int USER_SORT	= 1;
        public static final int VENUE_SORT  = 2;
        public static final int NO_SORT     = 0;

        protected AmazonSimpleDBClient sdbClient;

        protected String nextToken;
        protected int sortMethod;

        public SimpleDBData() {
                this( NO_SORT );
        }
        
        public SimpleDBData( int sortMethod ) {
				// Initialize the SimpleDB Client
                AWSCredentials credentials = new BasicAWSCredentials( Constants.ACCESS_KEY_ID, Constants.SECRET_KEY );
				this.sdbClient = new AmazonSimpleDBClient( credentials);
				this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_1));
                this.nextToken = null;
                this.sortMethod = sortMethod;
        }
    /*
     * Method returns the number of items in the SellListings Domain.
     */
        public int getSellCount() {
                SelectRequest selectRequest = new SelectRequest( SELL_COUNT_QUERY ).withConsistentRead( true );
                List<Item> items = this.sdbClient.select( selectRequest ).getItems();        
                        
                Item countItem = (Item) items.get(0);
                Attribute countAttribute = (Attribute)(((com.amazonaws.services.simpledb.model.Item) countItem).getAttributes().get(0));
                return Integer.parseInt( countAttribute.getValue() );
		}
    /*
     * Method returns the number of items in the High Scores Domain.
     */
        public int getBuyCount() {
				SelectRequest selectRequest = new SelectRequest( BUY_COUNT_QUERY ).withConsistentRead( true );
				List<Item> items = this.sdbClient.select( selectRequest ).getItems();        
                        
				Item countItem = (Item)items.get(0);
				Attribute countAttribute = (Attribute)(((com.amazonaws.services.simpledb.model.Item) countItem).getAttributes().get(0));
				return Integer.parseInt( countAttribute.getValue() );
		}

    /*
     * Gets all SellListings in the SellListings Domain
     */
        public synchronized List<SellListing> getSellListings() {

                SelectRequest selectRequest = new SelectRequest( SELL_LISTINGS_QUERY ).withConsistentRead( true );
                selectRequest.setNextToken( this.nextToken );
                
                SelectResult response = this.sdbClient.select( selectRequest );
                this.nextToken = response.getNextToken();
                
                return this.convertItemToSellListingArray( response.getItems() );        
        }

    /*
     * Gets all BuyListings in the BuyListings Domain
     */
        public synchronized List<BuyListing> getBuyListings() {

                SelectRequest selectRequest = new SelectRequest( BUY_LISTINGS_QUERY ).withConsistentRead( true );
                selectRequest.setNextToken( this.nextToken );
                
                SelectResult response = this.sdbClient.select( selectRequest );
                this.nextToken = response.getNextToken();
                
                return this.convertItemToBuyListingArray( response.getItems() );        
        }

    /*
     * Creates a new item and adds it to the SellListings domain.
     */
        public void addSellListing( Listing  newSeller) {

                String username = newSeller.getUser().getName();
				String venue = newSeller.getVenue().getName();
				String startTime = newSeller.getStartTime();
				String endTime = newSeller.getEndTime();
				String count = newSeller.getSwipeCountString();
				String price = newSellter.getPriceString();

                ReplaceableAttribute usernameAttribute = new ReplaceableAttribute( USERNAME_ATTRIBUTE, username, Boolean.TRUE );
                ReplaceableAttribute venueAttribute = new ReplaceableAttribute( VENUE_ATTRIBUTE, venue, Boolean.TRUE );
                ReplaceableAttribute startTimeAttribute = new ReplaceableAttribute( START_TIME_ATTRIBUTE, startTime, Boolean.TRUE );
                ReplaceableAttribute endTimeAttribute = new ReplaceableAttribute( END_TIME_ATTRIBUTE, endTime, Boolean.TRUE );
                ReplaceableAttribute countAttribute = new ReplaceableAttribute( COUNT_ATTRIBUTE, count, Boolean.TRUE );
                ReplaceableAttribute priceAttribute = new ReplaceableAttribute( PRICE_ATTRIBUTE, price, Boolean.TRUE );

                List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(6);

                attrs.add( usernameAttribute );
                attrs.add( venueAttribute );
                attrs.add( startTimeAttribute);
				attrs.add( endTimeAttribute );
                attrs.add( countAttribute );
				attrs.add( priceAttribute );
                
				PutAttributesRequest par = new PutAttributesRequest( SELL_LISTINGS_DOMAIN, username, attrs);                
                try {
                        this.sdbClient.putAttributes( par );
                }
                catch ( Exception exception ) {
                        System.out.println( "EXCEPTION = " + exception );
                }
        }

    /*
     * Creates a new item and adds it to the BuyListings domain.
     */
        public void addBuyListing( Listing newBuyer ) {

                String username = newBuyer.getUser().getName();
				String venue = newBuyer.getVenue().getName();
				String startTime = newBuyer.getStartTime();
				String endTime = newBuyer.getEndTime();
				String count = newBuyer.getSwipeCount();

                ReplaceableAttribute usernameAttribute = new ReplaceableAttribute( USERNAME_ATTRIBUTE, username, Boolean.TRUE );
                ReplaceableAttribute venueAttribute = new ReplaceableAttribute( VENUE_ATTRIBUTE, venue, Boolean.TRUE );
                ReplaceableAttribute startTimeAttribute = new ReplaceableAttribute( START_TIME_ATTRIBUTE, startTime, Boolean.TRUE );
                ReplaceableAttribute endTimeAttribute = new ReplaceableAttribute( END_TIME_ATTRIBUTE, endTime, Boolean.TRUE );
                ReplaceableAttribute countAttribute = new ReplaceableAttribute( COUNT_ATTRIBUTE, count, Boolean.TRUE );
                
                List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(5);

                attrs.add( usernameAttribute );
                attrs.add( venueAttribute );
                attrs.add( startTimeAttribute);
				attrs.add( endTimeAttribute );
                attrs.add( countAttribute );                
                
                PutAttributesRequest par = new PutAttributesRequest( BUY_LISTINGS_DOMAIN, username, attrs);                
                try {
                        this.sdbClient.putAttributes( par );
                }
                catch ( Exception exception ) {
                        System.out.println( "EXCEPTION = " + exception );
                }
        }
        
    /*
     * Converts an array of Items into an array of SellListing objects.
     */
        protected List<SellListing> convertItemToSellListingArray( List<Item> items ) {
                List<Listing> lsts = new ArrayList<Listing>( items.size() ); 
                for ( Item item : items ) {
                        lsts.add( this.convertItemToSellListing( item ) );
                }
                return lsts;
        }

    /*
     * Converts an array of Items into an array of BuyListing objects.
     */
        protected List<BuyListing> convertItemToBuyListingArray( List<Item> items ) {
                List<Listing> lsts = new ArrayList<Listing>( items.size() ); 
                for ( Item item : items ) {
                        lsts.add( this.convertItemToBuyListing( item ) );
                }         
                return lsts;
        }

    /*
     * Converts a single SimpleDB Item into a SellListing object.
     */
        protected SellListing convertItemToSellListing( Item item ) {
                Listing lst = new SellListing();

                User usr = new User(this.getUserForItem(item));
                Venue vne = new Venue(this.getVenueForItem(item));
				String strt = this.getStringValueForAttributeFromList( START_TIME_ATTRIBUTE, item.getAttributes() );
				String end = this.getStringValueForAttributeFromList( END_TIME_ATTRIBUTE, item.getAttributes() );
				int cnt = this.getIntValueForAttributeFromList( START_TIME_ATTRIBUTE, item.getAttributes() );
				double prc = this.getDoubleValueForAttributeFromList( PRICE_ATTRIBUTE, item.getAttributes() );

                lst.setUser(usr);
                lst.setVenue(vne);
				lst.setStartTime(strt);
				lst.setEndtime(end);
				lst.setSwipecount(cnt);
				lst.setPrice(prc);

                return lst;
		}

    /*
     * Converts a single SimpleDB Item into a BuyListing object.
     */
        protected BuyListing convertItemToBuyListing( Item item ) {
                BuyListing lst = new BuyListing();

                User usr = new User(this.getUserForItem(item));
                Venue vne = new Venue(this.getVenueForItem(item));
				String strt = this.getStringValueForAttributeFromList( START_TIME_ATTRIBUTE, item.getAttributes() );
				String end = this.getStringValueForAttributeFromList( END_TIME_ATTRIBUTE, item.getAttributes() );
				int cnt = this.getIntValueForAttributeFromList( START_TIME_ATTRIBUTE, item.getAttributes() );

                lst.setUser(usr);
                lst.setVenue(vne);
				lst.setStartTime(strt);
				lst.setEndtime(end);
				lst.setSwipecount(cnt);

                return lst;
		}
        
	/*
     * Extracts the 'username' attribute from the SimpleDB Item
     */
        protected String getUserForItem( Item item ) {
                return this.getStringValueForAttributeFromList( USERNAME_ATTRIBUTE, item.getAttributes() );
        }

	/*
     * Extracts the 'venue' attribute from the SimpleDB Item
     */
        protected String getVenueForItem( Item item ) {
                return this.getStringValueForAttributeFromList( VENUE_ATTRIBUTE, item.getAttributes() );
        }

    /*
     * Extracts the value for the given attribute from the list of attributes.
     * Extracted value is returned as a String.
     */
        protected String getStringValueForAttributeFromList( String attributeName, List<Attribute> attributes ) {
                for ( Attribute attribute : attributes ) {
                        if ( attribute.getName().equals( attributeName ) ) {
                                return attribute.getValue();
                        }
                }
                
                return "";                
        }
        
    /*
     * Extracts the value for the given attribute from the list of attributes.
     * Extracted value is returned as an int.
     */
        protected int getIntValueForAttributeFromList( String attributeName, List<Attribute> attributes ) {
                for ( Attribute attribute : attributes ) {
                        if ( attribute.getName().equals( attributeName ) ) {
                                return Integer.parseInt( attribute.getValue() );
                        }
                }
                
                return 0;     
        }

    /*
     * Extracts the value for the given attribute from the list of attributes.
     * Extracted value is returned as a double.
     */
        protected int getDoubleValueForAttributeFromList( String attributeName, List<Attribute> attributes ) {
                for ( Attribute attribute : attributes ) {
                        if ( attribute.getName().equals( attributeName ) ) {
                                return Double.parseDouble( attribute.getValue() );
                        }
                }
                
                return 0;     
        }

	/*
	 *  THESE FUNCTIONS ARE UNUSED
	 */

	/*
     * Gets the item from the List domain with the item name equal to 'listing'.
     */
        public Listing getListing( String listing ) {
                GetAttributesRequest gar = new GetAttributesRequest( LISTINGS_DOMAIN, listing );
                GetAttributesResult response = this.sdbClient.getAttributes(gar);
                
                String playerName = this.getStringValueForAttributeFromList( USER_ATTRIBUTE, response.getAttributes() );                                
                int score = this.getIntValueForAttributeFromList( VENUE_ATTRIBUTE, response.getAttributes() );
                
                Listing retrievedList = new BuyListing();//Later write in SellListing too, depending on case (ES)
                
                return retrievedList;                
        }

    /*
     * Removes the item from the HighScores domain.
     * The item removes is the item whose 'player' matches the theHighScore submitted.
     */
        public void removeHighScore( Listing score ) {
                DeleteAttributesRequest dar = new DeleteAttributesRequest( LISTINGS_DOMAIN, score.getUser().getName() );
                this.sdbClient.deleteAttributes( dar );
        }

        
    /*
     * Using the pre-defined query, extracts items from the domain in a determined order using the 'select' operation.
     */
        public synchronized List<Listing> getListings() {
                String query = null;
                switch ( this.sortMethod ) {
                        case USER_SORT: query = USER_SORT_QUERY; break;
                        case VENUE_SORT: query = VENUE_SORT_QUERY; break;
                        default: query = NO_SORT_QUERY; break;
                }
                
                SelectRequest selectRequest = new SelectRequest( query ).withConsistentRead( true );
                selectRequest.setNextToken( this.nextToken );
                
                SelectResult response = this.sdbClient.select( selectRequest );
                this.nextToken = response.getNextToken();
                
                return this.convertItemToListingArray( response.getItems() );        
        }

	/*
     * If a 'nextToken' was returned on the previous query execution, use the next token to get the next batch of items.
     */
        @SuppressWarnings("unchecked")
        public synchronized List<Listing> getNextPageOfScores() {
                if ( this.nextToken == null ) {
                        return Collections.EMPTY_LIST;
                }
                else {
                        return this.getListings();
                }
        }
}
