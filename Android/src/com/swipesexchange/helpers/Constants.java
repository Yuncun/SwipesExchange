package com.swipesexchange.helpers;



public final class Constants {

	public final static String API_KEY = "AIzaSyDoRZatk2YxluSIjEOtWUVa6iOTD9bV3Pk";
    
    /*
     * MESSAGE CODES
     */
    public static final int BL_PUSH = 1;
    public static final int SL_PUSH = 2;
    public static final int BL_REQUEST = 3;
    public static final int SL_REQUEST = 4;
    public static final int FBID_GET = 5;
    public static final int FBID_RE = 6;
    public static final int SEND_MSG = 7;
    public static final int GETALL_MSG = 8;
    public static final int PING = 9;
    public static final int PUSH_MSG = 10;
    public static final int CLAIM_MSGS = 11;
    public static final int DELETE_MSG = 12;
    public static final int DELETE_CONVERSATION = 13;
    public static final int SAVE_CONVERSATION = 14;
    public static final int DELETE_BUYLISTING = 15;
    public static final int DELETE_SELLLISTING = 16;
    public static final int LOGOUT_REMOVE_IDPAIR = 17;
    public static final int MARK_MSG_READ = 18;
    
    public static final String NO_FLAG = "0";
    public static final String DELETED_BY_SENDER = "1";
    public static final String DELETED_BY_RECEIVER = "2";
    
    // if deleted by both sender and receiver, message is removed from database

    public static final String SAVED_BY_SENDER = "1";
    public static final String SAVED_BY_RECEIVER = "2";
    public static final String SAVED_BY_BOTH = "3";
    
    public static final int MAXIMUM_LISTINGS_ALLOWED_PER_USER = 2;

}