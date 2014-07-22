package com.swipesexchange;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;








import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sharedObjects.Message;
import sharedObjects.Self;

public class ConversationAdapter extends BaseAdapter {

    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private final ArrayList<Message> chatMessages;
    private Context context;
    

    public ConversationAdapter(Context context, ArrayList<Message> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public Message getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
       
       // always return false, can't click on messages in a conversation
       return false;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        boolean is_outoing;
        Message chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.bubble_message_item, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if(chatMessage.getReceiver().getUID().equals(Self.getUser().getUID()))
        	is_outoing = true;
        else
        	is_outoing = false;
        
        
        
        setAlignment(holder, is_outoing);
        holder.txtMessage.setText(chatMessage.getText());
        if (chatMessage.getSender() != null) {
        	if(!is_outoing)
        		holder.txtInfo.setText(StaticHelpers.getTimeText(chatMessage.getTime()));
        	else
        		holder.txtInfo.setText(chatMessage.getSender().getName() + ": " + StaticHelpers.getTimeText(chatMessage.getTime()));
        } else {
            holder.txtInfo.setText(chatMessage.getTime());
        }

        return convertView;
    }

    public void add(Message message) {
        chatMessages.add(message);
    }

    public void add(ArrayList<Message> messages) {
        chatMessages.addAll(messages);
    }
    
    public void addAndUpdate(Message message, boolean update_locally) {
    	// TODO: remove this??
    
    	ConversationList.addMessage(message);
    	this.notifyDataSetChanged();
    }
    
    public void addAndUpdateNoMsg() {
    	// TODO: remove this??
    	//chatMessages.add(message);
    	this.notifyDataSetChanged();
    }

    private void setAlignment(ViewHolder holder, boolean isIncoming) {
    	
    	
        if (!isIncoming) {
            holder.contentWithBG.setBackgroundResource(R.drawable.incoming_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.outgoing_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }

	 public int fixHours(int hours_24)
	 {
		 if (hours_24 == 0)
			 return 12;
		 else if(hours_24 <= 12)
			 return hours_24;
		 else
		 {
			return hours_24 - 12;
			 
		 }
	 }
	 
	
	 
	 public boolean isPM(int hours_24)
	 {
		 if (hours_24>=12)
			 return true;
		 
		 return false;
	 }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
}
