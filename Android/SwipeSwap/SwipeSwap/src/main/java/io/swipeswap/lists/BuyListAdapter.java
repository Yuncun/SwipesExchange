package io.swipeswap.lists;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.swipeswap.R;
import io.swipeswap.helpers.StaticHelpers;
import io.swipeswap.messaging.ConversationList;
import io.swipeswap.messaging.PictureCache;
import io.swipeswap.sharedObjects.BuyListing;
import io.swipeswap.sharedObjects.Self;

public class BuyListAdapter  extends BaseAdapter
{
    
    private Context myContext;
    public List<BuyListing> myList;
    List<String> VenueNames;
    List<String> UserNames;
    List<String> DescStartTime;
    List<String> DescEndTime;
    List<String> DescAmount;
    List<String> DescVenue;

    public BuyListAdapter(Context context,List<BuyListing> list) 
    {
            super();
            this.myContext=context;
            this.myList = list;
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(final int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.buy_list_item, null);
        
                TextView description = (TextView) view.findViewById(R.id.firstLine);
              
                ImageView fb_pic = (ImageView) view.findViewById(R.id.fb_pic);
                TextView exp_time = (TextView) view.findViewById(R.id.expiration_time);
                TextView v1 = (TextView) view.findViewById(R.id.box_1_text);
                TextView v2 = (TextView) view.findViewById(R.id.box_2_text);
                TextView v3 = (TextView) view.findViewById(R.id.box_3_text);
                TextView v4 = (TextView) view.findViewById(R.id.box_4_text);
                TextView time_created = (TextView) view.findViewById(R.id.buy_listing_time_created);
                TextView name = (TextView) view.findViewById(R.id.buy_listing_name);
                
                description.setText(this.myList.get(position).getMessageBody());
                time_created.setText(StaticHelpers.getTimeText(this.myList.get(position).getTimeCreated()));
                name.setText(this.myList.get(position).getUser().getName());
                fb_pic.setImageBitmap(PictureCache.getFBPicBuy(this.myList.get(position).getUser().getUID()));

                try {
					exp_time.setText(StaticHelpers.figureOutExpirationTime(this.myList.get(position).getTimeCreated(), this.myList.get(position).getEndTime()));
				} catch (ParseException e) {
			
					exp_time.setText(">1h");
					e.printStackTrace();
				}
        
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

               
                int my_color = inflater.getContext().getResources().getColor(R.color.light_teal);
                int my_color_white = Color.WHITE;
               
               try{
	                //TODO: pick a better color/indicator that the client is involved in a listing
	               if(ConversationList.doesConversationExist(this.myList.get(position).getListingID(), this.myList.get(position).getUser().getUID()) || 
	            		   this.myList.get(position).getUser().getUID().equals(Self.getUser().getUID()))
	                	view.setBackgroundColor(my_color);
	               else
	                	view.setBackgroundColor(my_color_white);
               }catch(Exception e){
            	   view.setBackgroundColor(my_color_white);
            	   Log.d("BuyListadapter", "Colorpicking background did not work - May be a null pointer somewhere" + e.toString());
               }
       	
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

	
}
