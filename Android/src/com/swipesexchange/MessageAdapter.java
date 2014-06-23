package com.swipesexchange;

import java.util.ArrayList;
import java.util.List;

import sharedObjects.BuyListing;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	  private Context myContext;
	  private ArrayList<Conversation> my_list;
	  private List<String> sender_names;
	  private List<String> sender_ids;
	  private List<String> message_texts;

    public MessageAdapter(Context context, ArrayList<Conversation> list) 
    {
            super();
            myContext=context;
            my_list = list;
            
            sender_names = new ArrayList<String>();
            sender_ids = new ArrayList<String>();
            message_texts = new ArrayList<String>();
            
    
            if(my_list.size()>0)
            {
            	    for (int i = 0; i < my_list.size(); i++) 
      	            {
	            		sender_names.add(my_list.get(i).getSName());
	            		sender_ids.add(my_list.get(i).getSID());
	                    message_texts.add(my_list.get(i).getNextMessage().getText());
	            	}
            }
           
    }
       
   
    // getView method is called for each item of ListView
    @Override
    public View getView(int position,  View view, ViewGroup parent) 
    {
    	LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  
    	
        // inflate the layout for each item of listView  
        view = inflater.inflate(R.layout.message_item, null);

        TextView sender_name = (TextView) view.findViewById(R.id.sender_name);
        TextView sender_id = (TextView) view.findViewById(R.id.sender_id);
        TextView text = (TextView) view.findViewById(R.id.message_text);
      
        
        sender_name.setText(sender_names.get(position));
        sender_id.setText(sender_ids.get(position));
        text.setText(message_texts.get(position));
       
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
		
		return my_list.size();
	}

}
