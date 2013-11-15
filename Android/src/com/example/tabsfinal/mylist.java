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
		String[] values = new String[] {"1", "2", "3"};
	
	static MyList newInstance(int num) {
        MyList l = new MyList();

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
	        List<Listing> entries = data.getListings();
	        
	        List<String> VenueNames = new ArrayList<String>();
	        List<String> UserNames = new ArrayList<String>();
	        List<String> DescTime = new ArrayList<String>();
	        List<String> DescAmount = new ArrayList<String>();
	        
	        for (int i = 0; i < entries.size(); ++i) {
	        	VenueNames.add(((entries.get(i)).getVenue()).getName());
	        	UserNames.add(((entries.get(i)).getUser()).getName());
	        	DescTime.add((entries.get(i)).getEndTime());
	        	DescAmount.add(String.valueOf((entries.get(i)).getSwipeCount()));
	        	
	        }
	        
	        //SimpleAdapter that should be able to put up multiple List<Strings>
	        
	        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	        Map<String, String> map;
	        int count = entries.size();
	        for(int i = 0; i < count; i++) {
	            map = new HashMap<String, String>();
	            map.put("Headline", UserNames.get(i)+" @ "+VenueNames.get(i));
	            map.put("Subline", "Selling "+ DescAmount.get(i) + " swipes at $5 until " +  DescTime.get(i));
	            
	            list.add(map);
	        }

	        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.list_item, new String[] { "Headline", "Subline" }, new int[] { R.id.firstLine, R.id.secondLine });
	        setListAdapter(adapter);
	        
	        //test cause the debugger is fucked up for me
	        System.out.println("Hello I'm at the updateListings thing");
	        System.out.println("Data is");
	        
	    //    setListAdapter(new MyArrayAdapter(getActivity(), data.getListings())); 
	        /*
	        setListAdapter(new ArrayAdapter<String>(getActivity(),
	               android.R.layout.simple_list_item_1, android.R.id.text1,
	                VenueNames));*/
	    }
	
}
