package com.swipesexchange.helpers;

public class ClosedInfo {

	// ClosedInfo contains a boolean that is true if the tab was minimized via the minimize button
	// and false otherwise. It also contains a boolean that is true if the client received a message
	// during ConversationActivity
	
	private static boolean minimized = true;
	
	public static int num_unread = 0;
	
	private static boolean received_message = false;
	
	public static boolean wasMinimized() {
		return ClosedInfo.minimized;
	}
	
	public static void setMinimized(boolean m) {
		ClosedInfo.minimized = m;
	}
	
	public static boolean receivedMessage() {
		return ClosedInfo.received_message;
	}
	
	public static void setReceivedMessage(boolean r) {
		ClosedInfo.received_message = r;
	}
	
}