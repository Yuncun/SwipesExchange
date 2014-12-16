package io.swipeswap.sharedObjects;

public class MsgStruct {
	
	private int header;
	private String payload;
	
	public MsgStruct()
	{
		
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

	/**
	 * @return the header
	 */
	public int getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(int header) {
		this.header = header;
	}

	
}