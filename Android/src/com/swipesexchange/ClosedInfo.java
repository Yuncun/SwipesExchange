package com.swipesexchange;

public class ClosedInfo {

	private static boolean minimized = true;
	
	public static boolean wasMinimized() {
		return ClosedInfo.minimized;
	}
	
	public static void setMinimized(boolean m) {
		ClosedInfo.minimized = m;
	}
	
}
