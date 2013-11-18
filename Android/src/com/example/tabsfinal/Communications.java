package com.example.tabsfinal;


import com.google.cloud.backend.android.CloudBackend;
import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;
import java.io.IOException;
import java.util.List;

public class Communications extends CloudBackendActivity {
	
	
	
	public static void addBuyerListing(BuyListing bList) {
		CloudBackend cb = getCloudBackend();
	    CloudEntity newEntity = new CloudEntity("Buyer Listing");
	    newEntity.put("UserName", bList.getUser());
	  //  newEntity.put("Price", bList.getPrice());
	    newEntity.put("Venue", bList.getVenue());
	    newEntity.put("Time", bList.getEndTime());
	    try {
			cb.insert(newEntity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //Currently only using EndTime
	}

	public void getBuyerListing() {
		
	    CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
	      @Override
	      public void onComplete(List<CloudEntity> results) {
	        //Insert Code to Update UI here
	      }

	      @Override
	      public void onError(IOException exception) {
	        //For reference, not sure when you would use this event
	      }
	    };


	    //listByKind(KindName, PropertyName, Order, Number of, TimeScope, Handler)
	    getCloudBackend().listByKind("Buyer Listing", CloudEntity.PROP_CREATED_AT, Order.DESC, 50, Scope.FUTURE_AND_PAST, handler);
	}

}
