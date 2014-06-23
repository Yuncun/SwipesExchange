package sharedObjects;

import java.io.Serializable;


public class Message implements Serializable {
	
	private User Sender;
	private User Receiver;
	
	private String listing_id;
	private String text;
	
	/** Message constructor for creating a new message to be stored in a Conversation
	 * 
	 *@param: Sender: User sending the message
	 *@param: Receiver: User Recipient of message
	 *@param: listing_id: ID of listing
	 *@param: text: Body of message
	 */
	public Message(User Sender_, User Receiver_, String listid, String payload) {
		setSender(Sender_);
		setReceiver(Receiver_);
		setListing_id(listid);
		text = payload;
		
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

	
	

}
