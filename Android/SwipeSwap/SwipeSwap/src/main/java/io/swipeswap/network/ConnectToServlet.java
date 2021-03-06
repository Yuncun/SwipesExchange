package io.swipeswap.network;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swipeswap.helpers.Constants;
import io.swipeswap.messaging.Conversation;
import io.swipeswap.sharedObjects.BuyListing;
import io.swipeswap.sharedObjects.Listing;
import io.swipeswap.sharedObjects.Message;
import io.swipeswap.sharedObjects.MsgStruct;
import io.swipeswap.sharedObjects.Self;
import io.swipeswap.sharedObjects.SellListing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

 
public class ConnectToServlet {

	/**
	 * Handles all communication with Application Server
	 */
	public static String SERVERURL = "http://anotherservlet14-env.elasticbeanstalk.com/HelloWorld";
    String inputValue=null;
    static String doubledValue = "";

    
    public ConnectToServlet()
    {
  
    }

    
    public static void logoutRemoveUIDRegIDPair(final String UID, final String RegID){
    	/**
    	 * 
    	 */
    	
    	 new Thread(new Runnable() {
     		public void run() {
			  try {
				  URL url = new URL(SERVERURL);
				  URLConnection connection = url.openConnection();
	
				  Gson gson = new Gson();
				  String msgpayload = gson.toJson(UID+","+RegID);
				  Log.d("logoutRemoveUIDRegIDPair", "logoutRemoveUIDRegIDPair " + msgpayload);
				  Log.d("logoutRemoveUIDRegIDPair", "ID Pair from SELF: UID = " + Self.getUser().getUID() + " and RegID " + Self.getUser().getRegid());
		          MsgStruct msgReq = new MsgStruct();
		          msgReq.setHeader(Constants.LOGOUT_REMOVE_IDPAIR);
		          msgReq.setPayload(msgpayload);
		          //Create request
	
		          String blstring = gson.toJson(msgReq);
		          connection.setDoOutput(true);
	
		          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
		          objectOut.writeObject(blstring);
	
		          objectOut.flush();
		          objectOut.close();
	
		          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          String returnString="";
		          doubledValue = "";
	
	          	while ((returnString = in.readLine()) != null) 
	          	{
	          		doubledValue= returnString;
	          	}
	          	in.close();
	          	
	          	
	          		Log.d("logoutRemoveUIDRegIDPair", "logoutRemoveUIDRegIDPair responded with " + doubledValue); 
	
			  } catch(Exception e) { Log.d("logoutRemoveUIDRegIDPair", "logoutRemoveUIDRegIDPair failed with exception " + e.toString());}
	
			  return;
     		}
     		}).start();
    	
    }
    
    public static void sendMessage(final Message msg)
    {
    	/*
    	 * Sends the already-composed message to server
    	 * 
    	 * Message target should be account ID, not phone ID (RegID). Server will decide what regIDs are associated with each recipient
    	 * 
    	 * @Eric
    	 */
    	 Log.d("LOUD AND CLEAR", "Attempting to send message ");
    	 new Thread(new Runnable() {
     		public void run() {
			  try {
				  URL url = new URL(SERVERURL);
				  URLConnection connection = url.openConnection();
	
				  Gson gson = new Gson();
				  String msgpayload = gson.toJson(msg);
	          
		          MsgStruct msgReq = new MsgStruct();
		          msgReq.setHeader(Constants.SEND_MSG);
		          msgReq.setPayload(msgpayload);
		          //Create request
	
		          String blstring = gson.toJson(msgReq);
		          
		          Log.d("LOUD AND CLEAR", "Breakpoint " + blstring); 
		          
		          connection.setDoOutput(true);
	
		          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
		          objectOut.writeObject(blstring);
	
		          objectOut.flush();
		          objectOut.close();
	
		          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          String returnString="";
		          doubledValue = "";
	
	          	while ((returnString = in.readLine()) != null) 
	          	{
	          		doubledValue= returnString;
	          	}
	          	in.close();
	          	
	          	
	          		Log.d("LOUD AND CLEAR", "Send Message GCM responded with " + doubledValue); 
	
			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed with exception " + e.toString());}
	
			  return;
     		}
     		}).start();
    }
    
    public static void sendListing(final Object inputListing)
    {
    	/*
    	 * Messages must already have correct Message heading and be GSON'd
    	 */
    	
    	new Thread(new Runnable() {
    		public void run() {
    			  Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect with Listing support");
    			  try{
            	   URL url = new URL(SERVERURL);
                   URLConnection connection = url.openConnection();

                   connection.setDoOutput(true);
                   
                   //Begin to open a new OutputObjectStream
                 // OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                   
                   ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
                   objectOut.writeObject(inputListing);

                  // out.write(inputString);
                  // out.close();
                   objectOut.flush();
                   objectOut.close();
 
                   BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                   String returnString="";
                   doubledValue = "";

                            while ((returnString = in.readLine()) != null) 
                            {
                                doubledValue= returnString;
                             
                            }
                            in.close();
                            Log.d("ConnectToServlet.sendListing()", doubledValue);
                        	    
                            }catch(Exception e)
                            {
                                Log.d("Exception",e.toString());
                            }
                    }
                  }).start();
            }

    public static List<BuyListing> updateBList() {
    	
    			  Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect to pull BUY LISTS");
    			  List<BuyListing> nl = new ArrayList<BuyListing>();
    			  
    			  try {
            	   URL url = new URL(SERVERURL);
                   URLConnection connection = url.openConnection();
    			 
                   
                   
                   MsgStruct buyRequest = new MsgStruct();
                   buyRequest.setHeader(Constants.BL_REQUEST);
                   buyRequest.setPayload("0");
                   //Create request
                   Gson gson = new Gson();
                   String blstring = gson.toJson(buyRequest);
                   
                   connection.setDoOutput(true);
                   
                   //Begin to open a new OutputObjectStream
                   ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
                   objectOut.writeObject(blstring);
                   
                   objectOut.flush();
                   objectOut.close();

                   BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                   String returnString="";
                   doubledValue = "";
 
                   	while ((returnString = in.readLine()) != null) 
                   	{
                   		doubledValue= returnString;
                   	}
                   	in.close();
                   	
                   	MsgStruct dmsg = new MsgStruct();
                   	try{
                   		dmsg = gson.fromJson(doubledValue, MsgStruct.class);
                   	}catch (Exception e) {  
                   		Log.d("LOUD AND CLEAR", "Could not do first level of deserialization into MsgStruct");  }
                   	

	                   	Type listType = new TypeToken<ArrayList<BuyListing>>() {}.getType();
	                    List<BuyListing> rl = new Gson().fromJson(dmsg.getPayload(), listType);
	                    Log.d("LOUD AND CLEAR", "Server bl list deserialized, list has size" + rl.size()); 
	                 //   Log.d("LOUD AND CLEAR", "Clientside bls updated" + rl.size() + rl.get(0).getUser().getName());


	                    return rl;
    			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed at updateBlist " + e.toString());
    			  }
    			  
    			  return nl;
        }

	public static List<SellListing> updateSList() {
		  Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect to pull SELL LISTS");
		  List<SellListing> nl = new ArrayList<SellListing>();

		  try {
    	   URL url = new URL(SERVERURL);
           URLConnection connection = url.openConnection();

           MsgStruct sellRequest = new MsgStruct();
           sellRequest.setHeader(Constants.SL_REQUEST);
           sellRequest.setPayload("0");
           //Create request
           Gson gson = new Gson();
           String blstring = gson.toJson(sellRequest);
           
           connection.setDoOutput(true);
           
           //Begin to open a new OutputObjectStream
           ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
           objectOut.writeObject(blstring);
           
           objectOut.flush();
           objectOut.close();

           BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           String returnString="";
           doubledValue = "";

           	while ((returnString = in.readLine()) != null) 
           	{
           		doubledValue= returnString;
           	}
           	in.close();
           	
           	
           	MsgStruct dmsg = new MsgStruct();
           	try{
           		dmsg = gson.fromJson(doubledValue, MsgStruct.class);
           	}catch (Exception e) {  
           		Log.d("LOUD AND CLEAR", "Could not do first level of deserialization into MsgStruct");  }
           	

               	Type listType = new TypeToken<ArrayList<SellListing>>() {}.getType();
                List<SellListing> rl = new Gson().fromJson(dmsg.getPayload(), listType);
                Log.d("LOUD AND CLEAR", "Server sl list deserialized, list has size" + rl.size()); 
            //    Log.d("LOUD AND CLEAR", "Clientside bls updated" + rl.size() + rl.get(0).getUser().getName());
                
                
           //     Log.d("LOUD AND CLEAR", rl.get(0).getUser().getName() + " " + rl.get(0).getStartTime() + " " + rl.get(0).getEndTime() + " " + 
           // 	            			rl.get(0).getVenue().getName() + " " + rl.get(0).getSwipeCount() + " ");
                return rl;
		  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed at updatesl" + e.toString()); }

		  return nl;

	}
	
	public static void deleteListing(final Listing listing){
		/**
		 * Used to delete a listing owned by the user. This function:
		 * 	1) Deletes Listing from server. The server accepts a Message with deletionrequest in header and a strnig specifying listingID,UserID
		 *  2) Does not delete the listing locally - This must be performed elsewhere. (Most likely on button click in ListingsList)
		 */

		 
		 Log.d("LOUD AND CLEAR", "Attempting to delete listing from server");
    	 new Thread(new Runnable() {
     		public void run() {
			  try {
				  URL url = new URL(SERVERURL);
				  URLConnection connection = url.openConnection();
	
				  Gson gson = new Gson();
				  Log.d("LISTING ID IS", listing.getListingID());
				  String listingID_comma_selfID = listing.getListingID()+","+Self.getUser().getUID();
				  String msgpayload = listingID_comma_selfID;
	          
		          MsgStruct msgReq = new MsgStruct();
		          if (listing instanceof BuyListing){
		        	  msgReq.setHeader(Constants.DELETE_BUYLISTING);
		          }
		          else if (listing instanceof SellListing){
		        	  msgReq.setHeader(Constants.DELETE_SELLLISTING);
		          }
		          else{
		        	  Log.d("INSTANCE OF ", "Listing isn't instanec of anything");
		          }

		          msgReq.setPayload(msgpayload);
		          //Create request
	
		          String delreq = gson.toJson(msgReq);
		          
		          Log.d("LOUD AND CLEAR", "Breakpoint " + delreq); 
		          
		          connection.setDoOutput(true);
	
		          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
		          objectOut.writeObject(delreq);
	
		          objectOut.flush();
		          objectOut.close();
	
		          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          String returnString="";
		          doubledValue = "";
	
	          	while ((returnString = in.readLine()) != null) 
	          	{
	          		doubledValue= returnString;
	          	}
	          	in.close();
	          	
	          	
	          		Log.d("LOUD AND CLEAR", "Delete Listing Request responded with " + doubledValue); 
	
			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed with exception " + e.toString());}
	
			  return;
     		}}).start();
		
    	 //Delete listing from local lists
    	 
	}
	
	public static void deleteConversationLocally(Conversation conversation)
	{
		final List<String> messageID_comma_flagCode = new ArrayList<String>();
		for (int i = 0; i < conversation.getAllMessages().size(); i++)
		{
			String mcf_entry = conversation.getAllMessages().get(i).getMessageID() + ",";
			if(Self.getUser().getUID().equals(conversation.getAllMessages().get(i).getSender().getUID())){
				mcf_entry += Constants.DELETED_BY_SENDER;
			}
			else if (Self.getUser().getUID().equals(conversation.getAllMessages().get(i).getReceiver().getUID())){
				mcf_entry += Constants.DELETED_BY_RECEIVER;
			}
			else {
				Log.d("deleteConvesationLocally", "User does not have permission to delete this message " + conversation.getAllMessages().get(i).getText());
				break;
			}
			messageID_comma_flagCode.add(mcf_entry);
		}
		
		//Send request to server
		 Log.d("LOUD AND CLEAR", "Attempting to send message ");
    	 new Thread(new Runnable() {
     		public void run() {
			  try {
				  URL url = new URL(SERVERURL);
				  URLConnection connection = url.openConnection();
	
				  Gson gson = new Gson();
				  String msgpayload = gson.toJson(messageID_comma_flagCode);
	          
		          MsgStruct msgReq = new MsgStruct();
		          msgReq.setHeader(Constants.DELETE_CONVERSATION);
		          msgReq.setPayload(msgpayload);
		          //Create request
	
		          String blstring = gson.toJson(msgReq);
		          
		          Log.d("LOUD AND CLEAR", "Breakpoint " + blstring); 
		          
		          connection.setDoOutput(true);
	
		          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
		          objectOut.writeObject(blstring);
	
		          objectOut.flush();
		          objectOut.close();
	
		          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          String returnString="";
		          doubledValue = "";
	
	          	while ((returnString = in.readLine()) != null) 
	          	{
	          		doubledValue= returnString;
	          	}
	          	in.close();
	          	
	          	
	          		Log.d("LOUD AND CLEAR", "DeleteConversation GCM responded with " + doubledValue); 
	
			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed with exception " + e.toString());}
	
			  return;
     		}
     		}).start();
	
	}
	
	public static void markMessageRead(final String messageID){
		//Send request to server
		new Thread(new Runnable() {
    		public void run() {
			  try {
				  URL url = new URL(SERVERURL);
				  URLConnection connection = url.openConnection();
	
				  Gson gson = new Gson();
				  String msgpayload = gson.toJson(messageID);
	          
		          MsgStruct msgReq = new MsgStruct();
		          msgReq.setHeader(Constants.MARK_MSG_READ);
		          msgReq.setPayload(msgpayload);
		          //Create request
	
		          String blstring = gson.toJson(msgReq);
		     
		          connection.setDoOutput(true);
	
		          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
		          objectOut.writeObject(blstring);
	
		          objectOut.flush();
		          objectOut.close();
	
		          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          String returnString="";
		          doubledValue = "";
	
	          	while ((returnString = in.readLine()) != null) 
	          	{
	          		doubledValue= returnString;
	          	}
	          	in.close();
	 
	          		Log.d("LOUD AND CLEAR", "Mark_Msg_Read GCM responded with " + doubledValue); 
	
			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed with exception " + e.toString());}
	
			  return;
    		}
    		}).start();
		
	}
	public static List<Message> requestAllMsgs(String myID){
		 Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect to pull ALL MESSAGES");
		  List<Message> nl = new ArrayList<Message>();

		  try {
			  URL url = new URL(SERVERURL);
          URLConnection connection = url.openConnection();

          MsgStruct msgRequest = new MsgStruct();
          msgRequest.setHeader(Constants.GETALL_MSG);
          msgRequest.setPayload(myID);
          //Create request
          Gson gson = new Gson();
          String blstring = gson.toJson(msgRequest);
          
          connection.setDoOutput(true);
          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
          objectOut.writeObject(blstring);
          objectOut.flush();
          objectOut.close();

          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          String returnString="";
          String finalString ="";
          	while ((returnString = in.readLine()) != null) 
          	{
          		finalString = returnString;
          	}
          	in.close();
          	
          	MsgStruct dmsg = new MsgStruct();
          	try{
          		dmsg = gson.fromJson(finalString, MsgStruct.class);
          	}catch (Exception e) {  
          		Log.d("LOUD AND CLEAR", "Could not do first level of deserialization into MsgStruct:   "+finalString + " and request.header is: " + msgRequest.getHeader());  }
          	

               Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
               List<Message> rl = new Gson().fromJson(dmsg.getPayload(), listType);
               Log.d("LOUD AND CLEAR", "Server MESSAGE list deserialized with size" + rl.size()); 

               return rl;
		  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed " + e.toString());
		  }

		  return nl;

	}
	
	public static void deleteMessageLocally(final String msg)
	{
		/**
		 * Makes a serverside flag for this message so that it is not included in future pulls by this user
		 * 
		 */
		Log.d("LOUD AND CLEAR", "Attempting to send message ");
   	 new Thread(new Runnable() {
    		public void run() {
			  try {
				  URL url = new URL(SERVERURL);
				  URLConnection connection = url.openConnection();
	
				  Gson gson = new Gson();
				  String msgpayload = gson.toJson(msg);
	          
		          MsgStruct msgReq = new MsgStruct();
		          msgReq.setHeader(Constants.DELETE_MSG);
		          msgReq.setPayload(msgpayload);
		          //Create request
	
		          String blstring = gson.toJson(msgReq);
		          
		          Log.d("LOUD AND CLEAR", "Breakpoint " + blstring); 
		          
		          connection.setDoOutput(true);
	
		          ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
		          objectOut.writeObject(blstring);
	
		          objectOut.flush();
		          objectOut.close();
	
		          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		          String returnString="";
		          doubledValue = "";
	
	          	while ((returnString = in.readLine()) != null) 
	          	{
	          		doubledValue= returnString;
	          	}
	          	in.close();
	          	
	          	
	          		Log.d("LOUD AND CLEAR", "Delete message responded with" + doubledValue); 
	
			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed with exception " + e.toString());}
	
			  return;
    		}
    		}).start();
		
	}

	public static String sendIDPair(String UID, String RegID) {

		MsgStruct dmsg = new MsgStruct();
		dmsg.setPayload("");
		dmsg.setHeader(Constants.FBID_GET);

		 try {

      	   	 URL url = new URL(SERVERURL);
             URLConnection connection = url.openConnection();

             connection.setDoOutput(true);
			 dmsg.setPayload(UID+","+RegID);

			  Gson gson = new Gson();
			  String gmsg = gson.toJson(dmsg);
          
			  Log.d("handleIDAsync", "SendIDPair reached, sending the string:" + gmsg);
             //Begin to open a new OutputObjectStream
             ObjectOutputStream objectOut = new ObjectOutputStream(connection.getOutputStream());
             objectOut.writeObject(gmsg);
             
             objectOut.flush();
             objectOut.close();

             BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             String returnString="";
             String ret = "";

             	while ((returnString = in.readLine()) != null) 
             	{
             		ret= returnString;
             	}
             	in.close();
             	
             	 dmsg.setPayload(ret);
             	/*
             	try{
             		dmsg = gson.fromJson(doubledValue, MsgStruct.class);
             	//dmsg = gson.fromJson(dmsg.getPayload(), MsgStruct.class);
             		
             	}catch (Exception e) {  
             		Log.d("LOUD AND CLEAR", "No UUID retrieved from server");
             		dmsg.setPayload("No UUID retrieved from server");
             		}
             	*/

			  } catch(Exception e) { 
				  Log.d("handleIDAsync", "url connection failed at getFBID");
				  //dmsg.setPayload("URLConnectionFailedID");
			  }

		 Log.d("handleIDAsync", "dmsg.getPayload() is " + dmsg.getPayload());
			  return dmsg.getPayload();
	}
	
	public static boolean isOnline(Context mcontext) {
	    ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}