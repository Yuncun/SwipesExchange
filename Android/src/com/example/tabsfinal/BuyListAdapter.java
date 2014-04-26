package com.example.tabsfinal;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BuyListAdapter  extends BaseAdapter
{
    
    private Context myContext;
    private List<BuyListing> myList;
    List<String> VenueNames;
    List<String> UserNames;
    List<String> DescStartTime;
    List<String> DescEndTime;
    List<String> DescAmount;
    List<String> DescVenue;
    public int num_dividers;
    
    //need to switch to some better method of adding dividers
    //currently check for a change in venue in the sorted list 
   
    
    public BuyListAdapter(Context context,List<BuyListing> list) 
    {
            super();
            myContext=context;
            myList = list;
            
            VenueNames = new ArrayList<String>();
            UserNames = new ArrayList<String>();
            DescStartTime = new ArrayList<String>();
            DescEndTime = new ArrayList<String>();
            DescAmount = new ArrayList<String>();
            DescVenue = new ArrayList<String>();
            
            String[] divider_names = new String[20];
          
            
            if(myList.size()>0)
            {
            	
            String venueCheck = myList.get(0).getVenue().getName();
            num_dividers = 0;
            for (int i = 0; i < myList.size(); i++) {
            
            		if(!(venueCheck.equals(myList.get(i).getVenue().getName())) || i==0)
            		{
            			
                                 BuyListing bl = new BuyListing();
                                 
                                 divider_names[num_dividers] = myList.get(i).getVenue().getName();
                                 bl.isSection = true;
                                 bl.setSection(divider_names[num_dividers]);
                                 myList.add(i, bl);
                             VenueNames.add(null);
                     
                     UserNames.add(null);
                   
                     DescStartTime.add(null);
                     DescEndTime.add(null);
                   
                     DescAmount.add(null);
                     DescVenue.add(null);
                     
                     if(i+1 < myList.size())
                     venueCheck = myList.get(i+1).getVenue().getName();
                     
                     num_dividers++;
            			
                   
            		}
            		else
            		{
            		
                    VenueNames.add(((myList.get(i)).getVenue()).getName());
                    
                    UserNames.add(((myList.get(i)).getUser()).getName());
                  
                    DescStartTime.add((myList.get(i)).getStartTime());
                    DescEndTime.add((myList.get(i)).getEndTime());
                   
                    DescAmount.add(String.valueOf((myList.get(i)).getSwipeCount()));
                    DescVenue.add(myList.get(i).getVenue().getName());
              
            		}
            }
            }
           
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	if (myList.get(position).isSection)
    	{
    		
    		view= inflater.inflate(R.layout.test_text, null);
    		TextView divider_name = (TextView) view.findViewById(R.id.test_inflate);
    		divider_name.setText(myList.get(position).getSection());
    		divider_name.setTypeface(null, Typeface.BOLD);
    		view.setBackgroundColor(Color.BLACK);
    	}
    	else
    	{
                    // inflate the layout for each item of listView
                 
                    view = inflater.inflate(R.layout.buy_list_item, null);
            
                    TextView name = (TextView) view.findViewById(R.id.firstLine);
                    TextView numRequested = (TextView) view.findViewById(R.id.firstLineRight);
                    TextView time = (TextView) view.findViewById(R.id.secondLine);
                  
                    
                    name.setText(UserNames.get(position));
                    numRequested.setText(DescAmount.get(position));
                    time.setText(DescStartTime.get(position) + " - " + DescEndTime.get(position));
                   
                   
                    int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
                    
                    int my_color_white = Color.WHITE;
                    
                    if ((position+1) % 2 == 0)
                    	view.setBackgroundColor(my_color_white);
                    else
                    	view.setBackgroundColor(my_color);
                    	
                    
                    
    	}   
    	
        
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
