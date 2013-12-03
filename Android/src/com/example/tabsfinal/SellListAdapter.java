package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SellListAdapter  extends BaseAdapter
{
    
    private Context myContext;
    private List<SellListing> myList;
    List<String> VenueNames;
    List<String> UserNames;
    List<String> DescStartTime;
    List<String> DescEndTime;
    List<String> DescAmount;
    List<String> DescPrice;
    
    public SellListAdapter(Context context,List<SellListing> list) 
    {
            super();
            myContext=context;
            myList = list;
            
            VenueNames = new ArrayList<String>();
            UserNames = new ArrayList<String>();
            DescStartTime = new ArrayList<String>();
            DescEndTime = new ArrayList<String>();
            DescAmount = new ArrayList<String>();
            DescPrice = new ArrayList<String>();
           // List<String> Prices = new ArrayList<String>();
            
            for (int i = 0; i < myList.size(); ++i) {
                    VenueNames.add(((myList.get(i)).getVenue()).getName());
                    
                    UserNames.add(((myList.get(i)).getUser()).getName());
                   // UserNames.add(((sellEntries.get(i)).getUser()).getName());
                    DescStartTime.add((myList.get(i)).getStartTime());
                    DescEndTime.add((myList.get(i)).getEndTime());
                    //DescTime.add((sellEntries.get(i)).getEndTime());
                    DescAmount.add(String.valueOf((myList.get(i)).getSwipeCount()));
                    DescPrice.add(String.valueOf((myList.get(i)).getPrice()));
                    //DescAmount.add(String.valueOf((sellEntries.get(i)).getSwipeCount()));
                    //Prices.add(String.valueOf((sellEntries.get(i)).getPrice()));
            }
           
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
                    // inflate the layout for each item of listView
                    LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.sell_list_item, null);
            
                    TextView name = (TextView) view.findViewById(R.id.firstLine);
                    TextView numRequested = (TextView) view.findViewById(R.id.firstLineRight);
                    TextView time = (TextView) view.findViewById(R.id.secondLine);
                    TextView price = (TextView) view.findViewById(R.id.secondLineRight);
                    
                    name.setText(UserNames.get(position));
                    numRequested.setText(DescAmount.get(position));
                    time.setText(DescStartTime.get(position) + " - " + DescEndTime.get(position));
                    price.setText(DescPrice.get(position));
                   
                    int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
               
                    int my_color_white = Color.WHITE;
                    
                    if (position % 2 == 0)
                    	view.setBackgroundColor(my_color_white);
                    else
                    	view.setBackgroundColor(my_color);
                    	
                    
                    // Set the Sender number and smsBody to respective TextViews 
                    
        
                    return view;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}


	
}
