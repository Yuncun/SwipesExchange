package com.swipesexchange;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sharedObjects.BuyListing;
import sharedObjects.Message;
import sharedObjects.SellListing;
import sharedObjects.User;
import sharedObjects.Venue;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.RequestTimeoutException;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;



public class ListingsList extends ListFragment
{
		//page_num 0 is Buy listings, 1 is Sell listings
		public int page_num;
		public BackendData data;
		static MainActivity mActivity;
        public List<BuyListing> buyEntries;
        public List<SellListing> sellEntries;
        private boolean first_time = true;
        private boolean buy_entries_init = false;
        
        private BLConnectGet bc;
        private SLConnectGet sc;
        private Context v;
        private BuyListAdapter b_adapter;
        private SellListAdapter s_adapter;
        Button btnStartProgress;

        
		static ListingsList newInstance(int num, MainActivity my_activity, BackendData bd) {
        	mActivity = my_activity;
        ListingsList l = new ListingsList();
        l.v = my_activity;
        l.buyEntries = new ArrayList<BuyListing>();
        l.sellEntries = new ArrayList<SellListing>();
        l.page_num = num;
   
        Bundle args = new Bundle();
        args.putInt("num", num);
        l.setArguments(args);

        return l;
    }
		
		private Date getTimeText(String date_str) {
	    	
	    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";

	    	String oldDateString = date_str;
	    	

	    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
	    	Date d = null;
			try {
				d = sdf.parse(oldDateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	    	
	    	return d;
	    }
		
	   @Override
	   public void onListItemClick(ListView l, View v, int position, long id) {
	       super.onListItemClick(l, v, position, id);
	       Log.d("pig", "[onListItemClick] Selected Position "+ position);
	       
	       String current_lid;
	       if(this.page_num == 0)
	    	    current_lid = this.buyEntries.get(position).getListingID();
	       else
	    	    current_lid = this.sellEntries.get(position).getListingID();
	    		   
	       
	       Intent nextScreen = new Intent(getActivity(), ConversationActivity.class);
	       
	       
	       User myUser = MainActivity.getSelf();
	     
	       if(ConversationList.doesConversationExist(current_lid))
	       {
	    	   nextScreen.putExtra("is_new", false);
	    	   //nextScreen.putExtra("clicked_messages_lid", current_lid);
	       }
	       else
	       {
	    	   nextScreen.putExtra("is_new", true);
	       }
	       
	       
	       nextScreen.putExtra("listing_id", current_lid);
	       
	       nextScreen.putExtra("myUser", myUser);

	       startActivity(nextScreen);
	       getActivity().overridePendingTransition(R.anim.slide_in_from_right,
	               R.anim.slide_out_to_left);
	       
	       
	   }
	   

    
        
        public void setBLAdapter()
        {	
        	if(buyEntries.size()>1) 
        	{
         	   Collections.sort(buyEntries, new Comparator<BuyListing>(){
         		   public int compare(BuyListing emp1, BuyListing emp2) 
         		   {
         			   Long l1 = getTimeText(emp1.getTimeCreated()).getTime();
         			   Long l2 = getTimeText(emp2.getTimeCreated()).getTime();
         			   return l1.compareTo(l2); 		     
         		   }	   
         	   });
         	}
    
         	   b_adapter= new BuyListAdapter(getActivity(), buyEntries);
                setListAdapter(b_adapter);
        }
        
        public void setSLAdapter()
        {
     	   	if(sellEntries.size()>1)
     	   	{
     	   		Collections.sort(sellEntries, new Comparator<SellListing>(){
         		    public int compare(SellListing emp1, SellListing emp2) 
         	   		{
         		    	 Long l1 = getTimeText(emp1.getTimeCreated()).getTime();
           			     Long l2 = getTimeText(emp2.getTimeCreated()).getTime();
           			     return l1.compareTo(l2); 	
         	   		}
     		   	});
     	   	}
     	   s_adapter= new SellListAdapter(getActivity(), sellEntries);
           setListAdapter(s_adapter);
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
                
                this.first_time = false;
                
      
               if(this.page_num==0) //Buy Listings page
               {   
            	   this.pullAndAddMessages();
            	   bc = new BLConnectGet(getActivity(), true);
            	   bc.execute();
               }
               else if(this.page_num==1) //Sell Listings page
               {
            	 sc = new SLConnectGet(getActivity());
            	 sc.execute();
               }
               
               
       
            }
            

        
        public void pullAndAddMessages() {
      	   
  		  MessageTask m_task = new MessageTask(getActivity());
  		  m_task.execute();
  		  
  		    
        }
        
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) 
            { 
            	if(page_num==0 && !first_time)
            	{
            		Log.d("frag visibility", "Buy frag visible..."); 
               	 	bc = new BLConnectGet(getActivity(), false);
             	   	bc.execute();
             	    setBLAdapter();
             	   	this.b_adapter.notifyDataSetChanged();
            	}
            	else if(page_num==1 && !first_time)
            	{
            		Log.d("frag visibility", "List frag visible..."); 
               	 	sc = new SLConnectGet(getActivity());
             	   	sc.execute();
             	   	this.s_adapter.notifyDataSetChanged();
            	}
            	
            }
            else {}
        }
        
        public void getPictures() {
        	PictureTaskBuy pb_task = new PictureTaskBuy(getActivity());
      	   	pb_task.execute();
        }
        
        private class PictureTaskBuy extends AsyncTask<Void, Void, Map<String, Bitmap>> {

        	Map<String, Bitmap> map = new HashMap<String, Bitmap>();
        	private ProgressDialog progressBar;
        	private Context context;
        	
        	public PictureTaskBuy(Context context) {
        		this.context = context;
        	}
        	
            @Override
	        protected void onPreExecute() {
	           // super.onPreExecute();
	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Finishing up...", true);
	        }
        	
            @Override
            public Map<String, Bitmap> doInBackground(Void... params) {
                URL url = null;
                Bitmap pic_bitmap = null;
                //BitmapFactory.Options options;
                
                while (((MainActivity) context).getUID() == null && buy_entries_init == false) {
   	             Log.d("waitForvalues", "Waiting - getUID yields " + ((MainActivity) context).getUID());
   	             		
   	             try {
   	                 Thread.sleep(100);
   	             } catch (InterruptedException e) {
   	                 e.printStackTrace();
   	                 Log.d("waitForvalues", e.toString());
   	             }
   	         
   	  		 }
                Log.d("picture", Integer.toString(buyEntries.size()));
                for(int i=0; i < buyEntries.size(); i++)
                {
	                try {
	                	//options = new BitmapFactory.Options();
	                	//options.inSampleSize = 2;
	                	
	                	// TODO: get the FBUID
	                	url = new URL("https://graph.facebook.com/10152153150921342/picture?type=large");
	                    //Log.d("picture", "URL is: " + url.toString());
	                    pic_bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	                    map.put("10152153150921342", pic_bitmap);
	                } catch (MalformedURLException e) {
	                    e.printStackTrace();
	                    Log.d("picture", "Malformed URL!");
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    Log.d("picture", "IO Exception!");
	                }
                }
                
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Bitmap> map) {
            	progressBar.dismiss();
                PictureCache.cachePicMapBuy(map);
                Log.d("picture", "Map size is: " + Integer.toString(map.size()));
                setBLAdapter();
            }

        }
            
   
            
            //ASYNC TASK for BUY LISTINGS
            private class BLConnectGet extends AsyncTask<Void, Void, List<BuyListing>> {
	   
        		   private Context context;
        		   private ProgressDialog progressBar;
        	       int count;
        	       boolean get_pics;
        	  
        	        public BLConnectGet(Context context, boolean get_pics) {
        	        	this.context = context;
        	        	this.get_pics = get_pics;
        	        }
        	        
        	        @Override
        	        protected void onPreExecute() {
        	           // super.onPreExecute();
        	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
        	        }
        	        
        	      @Override
        	        protected void onPostExecute(List<BuyListing> result) {
        	    	  Log.d("test", "PostExecute1");
        	    	  progressBar.dismiss();
        	    	  buyEntries = result;
        	    	  buy_entries_init = true;
        	    	  if(this.get_pics)
        	    		  getPictures();
        	    	  
        	        }
        	    
        	      	@Override
        	        protected List<BuyListing> doInBackground(Void... params) {
        	        	List<BuyListing> updatedBuyList = new ArrayList<BuyListing>();

        	            try {updatedBuyList = ConnectToServlet.updateBList();}
        	            catch (RequestTimeoutException e)
        	               	{Log.d("LOUD AND CLEAR", "Failure at updatedBlist");}
        	            Log.d("LOUD AND CLEAR", "doInBackground reached. List contains; " + updatedBuyList.size() + " elements");

        	            for (int i = 0; i < updatedBuyList.size(); i ++)
        	            {
        	            	updatedBuyList.get(i).isSection = false;
        	            }
        	            return updatedBuyList;
        	        }     
        	  }           
            
            
            //Async Task for pulling SELL LIST data
            private class SLConnectGet extends AsyncTask<Void, Void, List<SellListing>> {
         	   
     		   private Context context;
     		   private ProgressDialog progressBar;
     	       int count;
     	  
     	        public SLConnectGet(Context context) {
     	        	this.context = context;
     	        }
     	        
     	        @Override
     	        protected void onPreExecute() {
     	           // super.onPreExecute();
     	        	progressBar = ProgressDialog.show(this.context, "Loading...", "Listings are loading...", true);
     	        }
     	        
     	      @Override
     	        protected void onPostExecute(List<SellListing> result) {
     	    	  Log.d("test", "PostExecute1");
     	    	  progressBar.dismiss();
     	    	  sellEntries = result;
     	    	  setSLAdapter();
     	        }
     	    
     	      	@Override
     	        protected List<SellListing> doInBackground(Void... params) {
     	        	List<SellListing> updatedSellList = new ArrayList<SellListing>();

     	            try {updatedSellList = ConnectToServlet.updateSList();}
     	            catch (RequestTimeoutException e)
     	               	{Log.d("LOUD AND CLEAR", "Failure at updatedBlist");}
     	            Log.d("LOUD AND CLEAR", "doInBackground reached. List contains; " + updatedSellList.size() + " elements");

     	            //Temporary code for fixing the "isSecton" missing field
     	            //TODO: Eliminate isSection
     	            for (int i = 0; i < updatedSellList.size(); i ++)
     	            {
     	            	updatedSellList.get(i).isSection = false;
     	            }
     	            return updatedSellList;
     	        }     
     	  } 
            
    	private class MessageTask extends AsyncTask<Void, Void, List<Message>> {
    	 	
    	 	private ProgressDialog progressBar;
    	 	private Context context;
    	 	
    	 	 public MessageTask(Context context) {
    		        	this.context = context;
    		        }
    	 	

    	 	@Override
    		        protected void onPreExecute() {
    		           // super.onPreExecute();
    		        	progressBar = ProgressDialog.show(context, "Loading...", "Messages are loading...", true);
    		        }
    	 	
    	     @Override
    	     protected List<Message> doInBackground(Void... params) {
    	    	 //Block this until UID is successfully retrieved
    	    	 Log.d("waitForvalues", "Checking that UID is safely retrieved");
    	    	 while (((MainActivity) context).getUID() == null) {
    	             Log.d("waitForvalues", "Waiting - getUID yields " + ((MainActivity) context).getUID());
    	             		
    	             try {
    	                 Thread.sleep(100);
    	             } catch (InterruptedException e) {
    	                 e.printStackTrace();
    	                 Log.d("waitForvalues", e.toString());
    	             }
    	         
    	  		 }
    	    	 
    	    	 
    	     	//Log.d("LOUD AND CLEAR", "Attempting to update messages list");
    	 		List<Message> newConversations = new ArrayList<Message>();
    	 		newConversations = ConnectToServlet.requestAllMsgs(((MainActivity) context).getUID());
    	 		
    	 		//Log.d("LOUD AND CLEAR", "Message list returned from server with size " + newConversations.size());
    	 		return newConversations;
    	     }

    	     @Override
    	     protected void onPostExecute(List<Message> msgs) {
    	     	
    	     	Log.d("LOUD AND CLEAR", "Adding messages...");
    	     	ConversationList.addMessageList(msgs);
    	     	ConversationList.is_set = true;
    	     	progressBar.dismiss();
    	     	
    	     	
    	     }
    		}
    
            
}