package com.swipesexchange.messaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.User;

public class Conversation {
	
	// list of Messages
	private ArrayList<Message> message_list;
	private User sender;
	private User receiver;
	private String listing_id;

	
	/** Conversation constructor
	 * 
	 * @param sid: the sender id of the conversation
	 * @param rid: the receiver id of the conversation
	 * @param sname: the sender name of the conversation
	 * @param rname: the receiver name of the conversation
	 * @param lid: the listing id of the listing associated with this conversation
	 */
	public Conversation(User conversationStarter, User secondUser, String lid) {
		setSender(conversationStarter);
		setReceiver(secondUser);
		this.listing_id = lid;

		
		this.message_list = new ArrayList<Message>();
	}
	
	
	public String getLID() {
		return this.listing_id;
	}
	
	public int getNumMessages() {
		return this.message_list.size();
	}
	
	public List<Message> getAllMessages() {
		return this.message_list;
	}
	
	public Message getNextMessage() {
		if(this.message_list.listIterator().hasNext())
			return this.message_list.listIterator().next();
		return null;
	}
	
	public void addMessageToConversation(Message msg) {
		this.message_list.add(msg);
		this.sortConversationByDate();
	}
	
	public Message getMessage(int index) {
		return this.message_list.get(index);
	}
	
	public Message getMostRecentMessage() {
		return this.message_list.get(this.message_list.size() - 1);
	}
	
	 public void sortConversationByDate() {
			
	     	if(message_list != null && message_list.size() > 1) 
	    	{
	     	   Collections.sort(this.message_list, new Comparator<Message>(){
	     		   public int compare(Message emp1, Message emp2) 
	     		   {
	     			 Long l1 = getTimeDate(emp1.getTime()).getTime();
     			 Long l2 = getTimeDate(emp2.getTime()).getTime();
	     		     return l1.compareTo(l2);		     
	     		   }
	     	
	     	   });
	     	}
	 }


	/**
	 * @return the sender
	 */
	public User getSender() {
		return sender;
	}


	/**
	 * @param sender the sender to set
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}


	/**
	 * @return the receiver
	 */
	public User getReceiver() {
		return receiver;
	}


	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	private Date getTimeDate(String date_str) {
    	
    	final String OLD_FORMAT = "yyyyMMdd'T'HHmmss";

    	String oldDateString = date_str;
    	

    	SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    	Date d = null;
		try {
			d = sdf.parse(oldDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    	return d;
    }
	
}
