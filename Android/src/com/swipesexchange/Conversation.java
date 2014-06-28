package com.swipesexchange;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sharedObjects.Message;
import sharedObjects.User;

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
	}
	
	public Message getMessage(int index) {
		return this.message_list.get(index);
	}
	
	public Message getMostRecentMessage() {
		return this.message_list.get(this.message_list.size() - 1);
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
	
}
