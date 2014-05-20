package sharedObjects;

public class MsgStruct {
	
	private String header;
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
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	
}