package com.swipesexchange.sharedObjects;

import java.io.Serializable;
import java.util.UUID;

import com.swipesexchange.helpers.Constants;


public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MessageID;
	private User Sender;
	private User Receiver;
	
	private String listing_id;
	private String text;
	private String time;
	
	private String savedFlag;
	private String deletedFlag;
	
	/** Message constructor for creating a new message to be stored in a Conversation
	 * 
	 *@param: Sender: User sending the message
	 *@param: Receiver: User Recipient of message
	 *@param: listing_id: ID of listing
	 *@param: text: Body of message
	 */
	public Message(User Sender_, User Receiver_, String listid, String time, String payload) {
		setSender(Sender_);
		setReceiver(Receiver_);
		setListing_id(listid);
		text = payload;
		this.time = time;
		setSavedFlag(Constants.NO_FLAG);
		setDeletedFlag(Constants.NO_FLAG);
		setMessageID(null);
		MessageID = UUID.randomUUID().toString();
		
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the sender
	 */
	public User getSender() {
		return Sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(User sender) {
		Sender = sender;
	}

	/**
	 * @return the receiver
	 */
	public User getReceiver() {
		return Receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(User receiver) {
		Receiver = receiver;
	}

	/**
	 * @return the listing_id
	 */
	public String getListing_id() {
		return listing_id;
	}

	/**
	 * @param listing_id the listing_id to set
	 */
	public void setListing_id(String listing_id) {
		this.listing_id = listing_id;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public void setTime(String t) {
		this.time = t;
	}

	/**
	 * @return the savedFlag
	 */
	public String getSavedFlag() {
		return savedFlag;
	}

	/**
	 * @param savedFlag the savedFlag to set
	 */
	public void setSavedFlag(String savedFlag) {
		this.savedFlag = savedFlag;
	}

	/**
	 * @return the deletedFlag
	 */
	public String getDeletedFlag() {
		return deletedFlag;
	}

	/**
	 * @param deletedFlag the deletedFlag to set
	 */
	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	/**
	 * @return the messageID
	 */
	public String getMessageID() {
		return MessageID;
	}

	/**
	 * @param messageID the messageID to set
	 */
	public void setMessageID(String messageID) {
		MessageID = messageID;
	}

	
	

}
