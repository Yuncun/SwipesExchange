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


import android.os.AsyncTask;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.Region;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsync;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ListDomainsRequest;
import com.amazonaws.services.simpledb.model.ListDomainsResult;
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
 * This class provides all the functionality for the High Scores list.
 *
 * The class uses SimpleDB to store individuals Items in a Domain.  
 * Each Item represents a player and their score.
 */
public class SimpleDBData {        
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
                
        protected AmazonSimpleDBAsyncClient sdbClient;
        protected String nextToken;
        protected int sortMethod;
        protected int count;
        public String reg_key = "AKIAJWQU5ZV4ZEZHRDWA";
        public String sec_key = "cgwIKqYn1YoYDhnkqt4oPaizIXdWeHtgNlliBaND";
        
        public int my_count;
        
        public SimpleDBData() {
                this( NO_SORT );
        }
                
        public SimpleDBData( int sortMethod ) {
        // Initial the SimpleDB Client.
                AWSCredentials credentials = new BasicAWSCredentials( reg_key,sec_key );
        this.sdbClient = new AmazonSimpleDBAsyncClient( credentials); 
        sdbClient.setEndpoint("sdb.us-west-2.amazonaws.com");
        this.sdbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        String test = "Listings";
        //sdbClient.
        ListDomainsResult result = sdbClient.listDomains();
        int x = result.getDomainNames().size();
        Boolean found = false;
        while(x>0)
        {
            if(result.getDomainNames().get(x) == test) 
            	found = true;
        } 

        if(!found)
        {
            this.createHighScoresDomain(test);
              
        }
        //CreateDomainRequest cdr = new CreateDomainRequest(LISTINGS_DOMAIN);
        //sdbClient.createDomain( cdr );
                //this.nextToken = null;
                this.sortMethod = sortMethod;
                this.nextToken = "";
                boolean bDomainExist = false;
                /*do {
                    ListDomainsRequest listDomainsRequest = new ListDomainsRequest();
                    listDomainsRequest.setMaxNumberOfDomains(100);
                    listDomainsRequest.setNextToken(nextToken);
                    ListDomainsResult listDomainsResult = sdbClient.listDomains(listDomainsRequest);
                    nextToken = listDomainsResult.getNextToken();
                    //List<String> list = listDomainsResult.getDomainNames();
                
                } while (nextToken != null && bDomainExist == false);
                */
                this.count = -1;
                this.my_count = 0;
        }
        
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
     * Method returns the number of items in the High Scores Domain.
     */
        public int getCount() {
                if ( count < 0 ) {
                        SelectRequest selectRequest = new SelectRequest( COUNT_QUERY ).withConsistentRead( true );
                        List<Item> items = this.sdbClient.select( selectRequest ).getItems();        
                        
                        Item countItem = (Item)items.get(0);
                        Attribute countAttribute = (Attribute)(((com.amazonaws.services.simpledb.model.Item) countItem).getAttributes().get(0));
                        this.count = Integer.parseInt( countAttribute.getValue() );
                }

                return this.count;
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
        
    /*
     * Creates a new item and adds it to the HighScores domain.
     */
        public void addHighScore( Listing score ) {
               // String paddedScore = SimpleDBUtils.encodeZeroPadding( score.getUser().getName(), 10 );
               // String paddedScore = score.getUser().getName();
                ReplaceableAttribute playerAttribute = new ReplaceableAttribute( USER_ATTRIBUTE, score.getUser().getName(), Boolean.TRUE );
                ReplaceableAttribute scoreAttribute = new ReplaceableAttribute( VENUE_ATTRIBUTE, score.getVenue().getName(), Boolean.TRUE );
                
                List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(2);
                attrs.add( playerAttribute );
                attrs.add( scoreAttribute );
                
                PutAttributesRequest par = new PutAttributesRequest(LISTINGS_DOMAIN, score.getUser().getName(), attrs);                
                try {
                		my_count++;
                		
                        this.sdbClient.putAttributes( par );
                }
                catch ( Exception exception ) {
                	
                        System.out.println( "EXCEPTION = " + exception );
                }
        }
        
    /*
     * Removes the item from the HighScores domain.
     * The item removes is the item whose 'player' matches the theHighScore submitted.
     */
        public void removeHighScore( Listing score ) {
                DeleteAttributesRequest dar = new DeleteAttributesRequest( LISTINGS_DOMAIN, score.getUser().getName() );
                this.sdbClient.deleteAttributes( dar );
                this.count = -1; // To force count refresh.                
        }
        
    /*
     * Creates the HighScore domain.
     */
        public void createHighScoresDomain(String domain) {
                CreateDomainRequest cdr = new CreateDomainRequest(domain);
                this.sdbClient.createDomain( cdr );
        }
                
    /*
     * Deletes the HighScore domain.
     */
        public void clearHighScores() {
                DeleteDomainRequest ddr = new DeleteDomainRequest( LISTINGS_DOMAIN );
                this.sdbClient.deleteDomain(ddr);
        }
                
    /*
     * Converts an array of Items into an array of HighScore objects.
     */
        protected List<Listing> convertItemToListingArray( List<Item> items ) {
                List<Listing> lsts = new ArrayList<Listing>( items.size() ); 
                for ( Item item : items ) {
                        lsts.add( this.convertItemToListing( item ) );
                }
                
                return lsts;
        }
        
    /*
     * Converts a single SimpleDB Item into a HighScore object.
     */
        protected Listing convertItemToListing( Item item ) {
                Listing lst = new BuyListing();
                User usr = new User(this.getPlayerForItem(item));
                Venue vne = new Venue(this.getScoreForItem(item));
                lst.setUser(usr);
                lst.setVenue(vne);
                return lst;
        //        return new Listing( this.getPlayerForItem( item ), this.getScoreForItem( item ) ); //(ES) Repair 
        }        
        
    /*
     * Extracts the 'player' attribute from the SimpleDB Item.
     */
        protected String getPlayerForItem( Item item ) {
                return this.getStringValueForAttributeFromList( USER_ATTRIBUTE, item.getAttributes() );
        }

    /*
     * Extracts the 'score' attribute from the SimpleDB Item.
     */
        protected String getScoreForItem( Item item ) {
                return this.getStringValueForAttributeFromList( VENUE_ATTRIBUTE, item.getAttributes() );
        } //This happens to be the same thing as getting player name, because they are both scores. 
        
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
        
     
}
        