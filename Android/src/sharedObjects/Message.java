package sharedObjects;

import java.io.Serializable;


public class Message implements Serializable {
	
	private String sender_uid;
	private String receiver_uid;
	
	private String sender_regid;
	private String receiver_regid;
	
	private String listing_id;
	
	private String sender_name;
	private String receiver_name;
	private String text;
	
	/** Message constructor for creating a new message to be stored in a Conversation
	 * 
	 * @param sid: the sender id of the message
	 * @param rid: the receiver id of the message
	 * @param sname: the sender name of the message
	 * @param rname: the receiver name of the message
	 * @param lid: the listing id of the listing associated with this message
	 * @param t: the text contents of the message
	 * @param sender_regid: GCM ID (phone id) of sender
	 */
	public Message(User sender, User receiver, String listid, String payload) {
		sender_uid = sender.getIdNumber();
		receiver_uid = receiver.getIdNumber();
		sender_regid = sender.getRegid();
		receiver_regid = receiver.getRegid();
		listing_id = listid;
		sender_name = sender.getName();
		receiver_name = receiver.getName();
		text = payload;
		
	}

	/**
	 * @return the sender_uid
	 */
	public String getSender_uid() {
		return sender_uid;
	}

	/**
	 * @param sender_uid the sender_uid to set
	 */
	public void setSender_uid(String sender_uid) {
		this.sender_uid = sender_uid;
	}

	/**
	 * @return the receiver_uid
	 */
	public String getReceiver_uid() {
		return receiver_uid;
	}

	/**
	 * @param receiver_uid the receiver_uid to set
	 */
	public void setReceiver_uid(String receiver_uid) {
		this.receiver_uid = receiver_uid;
	}

	/**
	 * @return the sender_regid
	 */
	public String getSender_regid() {
		return sender_regid;
	}

	/**
	 * @param sender_regid the sender_regid to set
	 */
	public void setSender_regid(String sender_regid) {
		this.sender_regid = sender_regid;
	}

	/**
	 * @return the receiver_regid
	 */
	public String getReceiver_regid() {
		return receiver_regid;
	}

	/**
	 * @param receiver_regid the receiver_regid to set
	 */
	public void setReceiver_regid(String receiver_regid) {
		this.receiver_regid = receiver_regid;
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

	/**
	 * @return the sender_name
	 */
	public String getSender_name() {
		return sender_name;
	}

	/**
	 * @param sender_name the sender_name to set
	 */
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	/**
	 * @return the receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * @param receiver_name the receiver_name to set
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
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

	
	

}
