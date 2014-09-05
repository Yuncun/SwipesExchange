package com.swipesexchange.helpers;

public class ClosedInfo {

	// ClosedInfo contains a boolean that is true if the tab was minimized via the minimize button
	// and false otherwise. It also contains a boolean that is true if the client received a message
	// during ConversationActivity
	
	private static boolean minimized = true;
	
	public static int num_unread = 0;
	
	public static boolean wasMinimized() {
		return ClosedInfo.minimized;
	}
	
	public static void setMinimized(boolean m) {
		ClosedInfo.minimized = m;
	}
	
}