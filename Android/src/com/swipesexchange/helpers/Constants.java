package com.swipesexchange.helpers;



public final class Constants {

	public final static String API_KEY = "AIzaSyDoRZatk2YxluSIjEOtWUVa6iOTD9bV3Pk";
	public static final String reg_key = "AKIAIBKEOA7FKTHHVG7Q";
    public static final String sec_key = "5kap6qSvIB6VYdxEt+w10rYz8C41UUp2s1f2umd/";
    
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
    
    public static final String NO_FLAG = "0";
    public static final String DELETED_BY_SENDER = "1";
    public static final String DELETED_BY_RECEIVER = "2";
    //If deleted by both sender and receiver, message is removed from database

    public static final String SAVED_BY_SENDER = "1";
    public static final String SAVED_BY_RECEIVER = "2";
    public static final String SAVED_BY_BOTH = "3";

    

    public static final String getRegKey()
    {
    	return reg_key;
    }
    
    public static final String getSecKey()
    {
    	return sec_key;
    }
    

}