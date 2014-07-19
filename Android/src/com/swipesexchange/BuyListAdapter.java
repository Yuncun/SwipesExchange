package com.swipesexchange;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;




















import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import sharedObjects.BuyListing;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            this.myContext=context;
            this.myList = list;
            
            VenueNames = new ArrayList<String>();
            UserNames = new ArrayList<String>();
            DescStartTime = new ArrayList<String>();
            DescEndTime = new ArrayList<String>();
            DescAmount = new ArrayList<String>();
            DescVenue = new ArrayList<String>();
            
            String[] divider_names = new String[20];
          
            
            if(myList.size()>0)
            {
            	
            //String venueCheck = myList.get(0).getVenue().getName();
            num_dividers = 0;
            for (int i = 0; i < myList.size(); i++) {
            
            	/*
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
            		*/
            		
             
              
            		
            }
            }
           
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(final int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	/*if (myList.get(position).isSection)
    	{
    		
    		view= inflater.inflate(R.layout.test_text, null);
    		TextView divider_name = (TextView) view.findViewById(R.id.test_inflate);
    		divider_name.setText(myList.get(position).getSection());
    		divider_name.setTypeface(null, Typeface.BOLD);
    		view.setBackgroundColor(Color.BLACK);
    	}
    	else
    	{
    	*/
                    // inflate the layout for each item of listView
    	
    	
   
                 
                view = inflater.inflate(R.layout.buy_list_item, null);
        
                TextView description = (TextView) view.findViewById(R.id.firstLine);
                //TextView numRequested = (TextView) view.findViewById(R.id.firstLineRight);
               // TextView time = (TextView) view.findViewById(R.id.secondLine);
                //TextView desc = (TextView) view.findViewById(R.id.thirdLine);
                ImageView fb_pic = (ImageView) view.findViewById(R.id.fb_pic);
                TextView exp_time = (TextView) view.findViewById(R.id.expiration_time);
                TextView v1 = (TextView) view.findViewById(R.id.box_1_text);
                TextView v2 = (TextView) view.findViewById(R.id.box_2_text);
                TextView v3 = (TextView) view.findViewById(R.id.box_3_text);
                TextView v4 = (TextView) view.findViewById(R.id.box_4_text);
                //Display display = ((Activity) this.myContext).getWindowManager().getDefaultDisplay();
               // FlowTextHelper.tryFlowText(this.myContext, this.myList.get(position).getMessageBody(), exp_time, description, display, 4);
                
                description.setText("\t\t\t  " + this.myList.get(position).getMessageBody());
                
                fb_pic.setImageBitmap(PictureCache.getFBPicBuy("10152153150921342"));
                //description.setText(this.myList.get(position).getMessageBody());
                //numRequested.setText(Integer.toString(this.myList.get(position).getSwipeCount()));
                String s_string = this.getTimeText(this.myList.get(position).getStartTime());
                String e_string = this.getTimeText(this.myList.get(position).getEndTime());
                exp_time.setText(">2h");
                
                // set the venue boxes
                String venue_string = this.myList.get(position).getVenue().getName();
                List<String> items = Arrays.asList(venue_string.split("\\s*,\\s*"));
                
                LinearLayout b1 = (LinearLayout) view.findViewById(R.id.box_1);
                LinearLayout b2 = (LinearLayout) view.findViewById(R.id.box_2);
                LinearLayout b3 = (LinearLayout) view.findViewById(R.id.box_3);
                LinearLayout b4 = (LinearLayout) view.findViewById(R.id.box_4);
                
                for(int i = 0; i<items.size(); i++)
                {
                	if(i==0)
                	{
                		b1.setVisibility(View.VISIBLE);
                		v1.setText(items.get(i));
                	}
                	else if(i==1)
                	{
                		b2.setVisibility(View.VISIBLE);
                		v2.setText(items.get(i));
                	}
                	else if(i==2)
                	{
                		b3.setVisibility(View.VISIBLE);
                		v3.setText(items.get(i));
                	}
                	else if(i==3)
                	{
                		b4.setVisibility(View.VISIBLE);
                		v4.setText(items.get(i));
                	}
                }
                
                //time.setText("Expires: " + e_string);
                //desc.setText(this.myList.get(position).getMessageBody());
               
                int my_color = inflater.getContext().getResources().getColor(R.color.mycolor1);
                
                int my_color_white = Color.WHITE;
                
                Log.d("porcupine", "Listing ID: " + this.myList.get(position).getListingID());
               
                
                if(ConversationList.doesConversationExist(this.myList.get(position).getListingID()))
                	view.setBackgroundColor(my_color);
                else
                	view.setBackgroundColor(my_color_white);
       	
               return view;
    }
    

    public Object getItem(int position) {
        
        return position;
    }

    public long getItemId(int position) {
      
        return position;
    }


	@Override
	public int getCount() {
		
		return myList.size();
	}
	
   private String getTimeText(String date_str) {
    	
    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";
    	final String NEW_FORMAT = "EEE, MMM dd, hh:mm aaa";

    	String oldDateString = date_str;
    	String newDateString;

    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    	Date d = null;
		try {
			d = sdf.parse(oldDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return date_str;
		}
    	sdf.applyPattern(NEW_FORMAT);
    	newDateString = sdf.format(d);
    	
        return newDateString;
    }


	
}
