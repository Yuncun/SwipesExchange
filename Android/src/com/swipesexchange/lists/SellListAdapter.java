package com.swipesexchange.lists;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import com.swipesexchange.R;
import com.swipesexchange.helpers.StaticHelpers;
import com.swipesexchange.messaging.ConversationList;
import com.swipesexchange.messaging.PictureCache;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.SellListing;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SellListAdapter  extends BaseAdapter
{
    
    private Context myContext;
    public List<SellListing> myList;
    List<String> VenueNames;
    List<String> UserNames;
    List<String> DescStartTime;
    List<String> DescEndTime;
    List<String> DescPrice;
    List<String> DescAmount;
    List<String> DescVenue;
    public int num_dividers;
    
    public SellListAdapter(Context context,List<SellListing> list) 
    {
            super();
            myContext=context;
            myList = list;	
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.sell_list_item, null);

        TextView description = (TextView) view.findViewById(R.id.firstLine_sell);
      
        ImageView fb_pic = (ImageView) view.findViewById(R.id.fb_pic_sell);
        TextView exp_time = (TextView) view.findViewById(R.id.expiration_time_sell);
        TextView v1 = (TextView) view.findViewById(R.id.box_1_text_sell);
        TextView v2 = (TextView) view.findViewById(R.id.box_2_text_sell);
        TextView v3 = (TextView) view.findViewById(R.id.box_3_text_sell);
        TextView v4 = (TextView) view.findViewById(R.id.box_4_text_sell);
        TextView time_created = (TextView) view.findViewById(R.id.sell_listing_time_created);
        TextView name = (TextView) view.findViewById(R.id.sell_listing_name);
        
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
        
        LinearLayout b1 = (LinearLayout) view.findViewById(R.id.box_1_sell);
        LinearLayout b2 = (LinearLayout) view.findViewById(R.id.box_2_sell);
        LinearLayout b3 = (LinearLayout) view.findViewById(R.id.box_3_sell);
        LinearLayout b4 = (LinearLayout) view.findViewById(R.id.box_4_sell);
        
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

       
        //TODO: pick a better color/indicator that the client is involved in a listing
       if(ConversationList.doesConversationExist(this.myList.get(position).getListingID(), this.myList.get(position).getUser().getUID()) || 
    		   this.myList.get(position).getUser().getUID().equals(Self.getUser().getUID()))
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



}