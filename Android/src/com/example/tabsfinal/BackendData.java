package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

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

				SimpleDBData db = new SimpleDBData();

				int SellListingCount = db.GetSellCount();
				int BuyListingCount = db.GetBuyCount();

				this.mySellListings = db.GetSellListings();
				this.myBuyListings = db.GetBuyListings();		
		}
}

