package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

class MyList extends ListFragment
{
		//page_num 0 is Buy listings, 1 is Sell listings, 2 is Messages (can add more)
		private int page_num;
        
        static MyList newInstance(int num) {
        MyList l = new MyList();
        
        if (num == 0)
        	l.page_num = 0;
        else if (num == 0)
        	l.page_num = 1;
        else
        	l.page_num = 2;
        
        Bundle args = new Bundle();
        args.putInt("num", num);

        l.setArguments(args);

        return l;
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
                
                BackendData data = new BackendData();
                data.updateListings();
                
                /*Some crazy shit to convert List<Listing> into List<String>*/
                
             //   switch (this.getId())
               // {
                
              //  }
               if(this.page_num==0)
               {
            	   List<BuyListing> buyEntries = data.getBuyListings();
                   
                   
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

                   SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.list_item, new String[] { "Headline", "Subline", "Headline2" }, new int[] { R.id.firstLine, R.id.secondLine , R.id.firstLineRight});
                   setListAdapter(adapter);
               }
               else if(this.page_num==1)
               {
            	   //List<BuyListing> buyEntries = data.getBuyListings();
                List<SellListing> sellEntries = data.getSellListings();
                
                List<String> VenueNames = new ArrayList<String>();
                List<String> UserNames = new ArrayList<String>();
                List<String> DescTime = new ArrayList<String>();
                List<String> DescAmount = new ArrayList<String>();
                List<String> Prices = new ArrayList<String>();
                
                for (int i = 0; i < sellEntries.size(); ++i) {
                       // VenueNames.add(((buyEntries.get(i)).getVenue()).getName());
                        VenueNames.add(((sellEntries.get(i)).getVenue()).getName());
                        //UserNames.add(((buyEntries.get(i)).getUser()).getName());
                        UserNames.add(((sellEntries.get(i)).getUser()).getName());
                        //DescTime.add((buyEntries.get(i)).getEndTime());
                        DescTime.add((sellEntries.get(i)).getEndTime());
                        //DescAmount.add(String.valueOf((buyEntries.get(i)).getSwipeCount()));
                        DescAmount.add(String.valueOf((sellEntries.get(i)).getSwipeCount()));
                        Prices.add(String.valueOf((sellEntries.get(i)).getPrice()));
                }
                
                //SimpleAdapter that should be able to put up multiple List<Strings>
            
                
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                
                int count = sellEntries.size();
                for(int i = 0; i < count; i++) {
                	Map<String, String> map = new HashMap<String, String>();
                    map.put("Headline", UserNames.get(i));
                    map.put("Subline", "Until " +  DescTime.get(i));
                    map.put("Headline2", "" + DescAmount.get(i) + " requested");
                    map.put("Headline3", "Price: " + Prices.get(i));
                    
                    list.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.list_item, new String[] { "Headline", "Subline", "Headline2", "Headline3" }, new int[] { R.id.firstLine, R.id.secondLine , R.id.firstLineRight, R.id.secondLineRight});
                setListAdapter(adapter);
                
                //test cause the debugger is fucked up for me
                System.out.println("Hello I'm at the updateListings thing");
                System.out.println("Data is");
               }
                
            //    setListAdapter(new MyArrayAdapter(getActivity(), data.getListings())); 
                /*
                setListAdapter(new ArrayAdapter<String>(getActivity(),
                       android.R.layout.simple_list_item_1, android.R.id.text1,
                        VenueNames));*/
            
            else
            {
            	List<BuyListing> buyEntries = data.getBuyListings();
                
                
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

                SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.list_item, new String[] { "Headline", "Subline", "Headline2" }, new int[] { R.id.firstLine, R.id.secondLine , R.id.firstLineRight});
                setListAdapter(adapter);
            
            }
            }
}