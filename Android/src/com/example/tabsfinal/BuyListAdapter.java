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
    //List<Boolean> HeaderPositions; //really janky way of doing this, there are some really nice classes out there that do stick/scrollable headers
    //good enough for now
    
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
            
            String[] divider_names = new String[10];
            //HeaderPositions = new ArrayList<Boolean>();
           // List<String> Prices = new ArrayList<String>();
            
            
            String venueCheck = myList.get(0).getVenue().getName();
            num_dividers = 0;
            for (int i = 0; i < myList.size(); ++i) {
            	//if(myList.get(i).getVenue().getName() != venueCheck)
            	//{
            	//	HeaderPositions
            	//}
            		if(venueCheck!=myList.get(i).getVenue().getName() || i==0)
            		{
            			BuyListing bl = new BuyListing();
            			
            			divider_names[num_dividers] = myList.get(i).getVenue().getName();
            			bl.isSection = true;
            			bl.setSection(divider_names[num_dividers]);
            			myList.add(i, bl);
            		    VenueNames.add(null);
                        
                        UserNames.add(null);
                       // UserNames.add(((sellEntries.get(i)).getUser()).getName());
                        DescStartTime.add(null);
                        DescEndTime.add(null);
                        //DescTime.add((sellEntries.get(i)).getEndTime());
                        DescAmount.add(null);
                        DescVenue.add(null);
                        
                        venueCheck = myList.get(i+1).getVenue().getName();
                        
                        num_dividers++;
                        //add the real one
                        /*
                        VenueNames.add(((myList.get(i+1)).getVenue()).getName());
                        
                        UserNames.add(((myList.get(i+1)).getUser()).getName());
                       // UserNames.add(((sellEntries.get(i)).getUser()).getName());
                        DescStartTime.add((myList.get(i+1)).getStartTime());
                        DescEndTime.add((myList.get(i+1)).getEndTime());
                        //DescTime.add((sellEntries.get(i)).getEndTime());
                        DescAmount.add(String.valueOf((myList.get(i+1)).getSwipeCount()));
                        DescVenue.add(myList.get(i+1).getVenue().getName());
                        venueCheck = myList.get(i+1).getVenue().getName();
                        */
            		}
            		else
            		{
            		
                    VenueNames.add(((myList.get(i)).getVenue()).getName());
                    
                    UserNames.add(((myList.get(i)).getUser()).getName());
                   // UserNames.add(((sellEntries.get(i)).getUser()).getName());
                    DescStartTime.add((myList.get(i)).getStartTime());
                    DescEndTime.add((myList.get(i)).getEndTime());
                    //DescTime.add((sellEntries.get(i)).getEndTime());
                    DescAmount.add(String.valueOf((myList.get(i)).getSwipeCount()));
                    DescVenue.add(myList.get(i).getVenue().getName());
                    //DescAmount.add(String.valueOf((sellEntries.get(i)).getSwipeCount()));
                    //Prices.add(String.valueOf((sellEntries.get(i)).getPrice()));
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
                    //LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.buy_list_item, null);
            
                    TextView name = (TextView) view.findViewById(R.id.firstLine);
                    TextView numRequested = (TextView) view.findViewById(R.id.firstLineRight);
                    TextView time = (TextView) view.findViewById(R.id.secondLine);
                   // TextView venue = (TextView) view.findViewById(R.id.thirdLine);
                    
                    name.setText(UserNames.get(position));
                    numRequested.setText(DescAmount.get(position));
                    time.setText(DescStartTime.get(position) + " - " + DescEndTime.get(position));
                    //venue.setText(DescVenue.get(position));
                   
                    int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
                    
                    int my_color_white = Color.WHITE;
                    
                    if ((position+1) % 2 == 0)
                    	view.setBackgroundColor(my_color_white);
                    else
                    	view.setBackgroundColor(my_color);
                    	
                    
                    // Set the Sender number and smsBody to respective TextViews 
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
