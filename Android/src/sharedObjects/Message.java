package sharedObjects;

import java.io.Serializable;


public class Message implements Serializable {
	
	private long sender_id;
	private long receiver_id;
	private long listing_id;
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
	 */
	public Message(long sid, long rid, long lid, String sname, String rname, String t) {
		this.sender_id = sid;
		this.receiver_id = rid;
		this.listing_id = lid;
		this.sender_name = sname;
		this.receiver_name = rname;
		this.text = t;
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
	
	public String getRName() {
		return this.receiver_name;
	}
	
	public long getLID() {
		return this.listing_id;
	}
	
	public String getText() {
		return this.text;
	}
	
	

}
