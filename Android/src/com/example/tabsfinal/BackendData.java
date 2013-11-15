package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.List;

//This is the class where we keep all of our backend, and keep functions that pulls data from the backend. 
public class BackendData {
        
        private List<Listing> Listings;

        public List<Listing> getListings() {
                return Listings;
        }

        public void Listings(List<Listing> listings) {
                Listings = listings;
        }
        
        public void updateListings()
        {
                
                //Currently hardwired to sample values, but this will later be used to update the listings from backend data.
                int random = (int )(Math.random() * 12 + 1);
                
                List<Listing> updatedList = new ArrayList<Listing>();
                
        for (int i = 0; i < random; ++i) {
                
                Listing randomEntry = new Listing();
                
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
                
                randomEntry.setVenue(randomVenue);
                randomEntry.setUser(randomUser);
                randomEntry.setTime("time left");
                randomEntry.setEndTime(String.valueOf((int)(Math.random() * 7 + 4)));
                randomEntry.setStartTime("start time");
                
                int randomMath = (int )(Math.random() * 15 + 1);
                randomEntry.setSwipeCount(randomMath);                
                
         updatedList.add(randomEntry);
          
          }
                this.Listings = updatedList;
                
        }
        

}