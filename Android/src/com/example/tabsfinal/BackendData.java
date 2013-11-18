package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.List;

//This is the class where we keep all of our backend, and keep functions that pulls data from the backend. 
public class BackendData {
        
        private List<SellListing> mySellListings;
        private List<BuyListing> myBuyListings;
        
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
                
                List<SellListing> updatedSellList = new ArrayList<SellListing>();
                List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();
                
        for (int i = 0; i < random; ++i) {
                
                BuyListing randomBuyEntry = new BuyListing();
                SellListing randomSellEntry = new SellListing();
                
                /*RANDOM ENTRIES*/
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
                
                randomBuyEntry.setEndTime(String.valueOf((int)(Math.random() * 7 + 4)));
                randomSellEntry.setEndTime(String.valueOf((int)(Math.random() * 7 + 4)));
                randomBuyEntry.setStartTime("start time");
                randomSellEntry.setStartTime("start time");
                
                int randomMath = (int )(Math.random() * 15 + 1);
                randomBuyEntry.setSwipeCount(randomMath); 
                randomSellEntry.setSwipeCount(randomMath);
                
                double random_price = 2.02;
                randomSellEntry.setPrice(random_price);
                
         updatedSellList.add(randomSellEntry);
          updatedBuyList.add(randomBuyEntry);
          }
                this.mySellListings = updatedSellList;
                this.myBuyListings = updatedBuyList;
                
        }
        

}