package com.swipesexchange;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sharedObjects.Message;

public class Conversation {
	
	// list of Messages
	private LinkedList<Message> message_list;
	private long sender_id;
	private long receiver_id;
	private long listing_id;
	private String sender_name;
	private String receiver_name;
	
	/** Conversation constructor
	 * 
	 * @param sid: the sender id of the conversation
	 * @param rid: the receiver id of the conversation
	 * @param sname: the sender name of the conversation
	 * @param rname: the receiver name of the conversation
	 * @param lid: the listing id of the listing associated with this conversation
	 */
	public Conversation(long sid, long rid, long lid, String sname, String rname) {
		this.sender_id = sid;
		this.receiver_id = rid;
		this.listing_id = lid;
		this.sender_name = sname;
		this.receiver_name = rname;
		
		this.message_list = new LinkedList<Message>();
	}
	
	public long getSID() {
		return this.sender_id;
	}
	
	public long getRID() {
		return this.receiver_id;
	}
	
	public String getSName() {
		return this.sender_name;
	}
	
	public String getRNFame() {
		return this.receiver_name;
	}
	
	public long getLID() {
		return this.listing_id;
	}
	
	public int getNumMessages() {
		return this.message_list.size();
	}
	
	public Message getNextMessage() {
		if(this.message_list.listIterator().hasNext())
			return this.message_list.listIterator().next();
		return null;
	}
	
	public void addMessageToConversation(Message msg) {
		this.message_list.add(msg);
	}
	
}
