package com.swipesexchange;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import sharedObjects.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import android.util.Log;

 
public class ConnectToServlet {

	/**
	 * Handles all communication with Application Server
	 */
	public static String SERVERURL = "http://anotherservlet14-env.elasticbeanstalk.com/HelloWorld";
    String inputValue=null;
    static String doubledValue = "";
    public static List<BuyListing> bls;
    private List<SellListing> sls;
    
    public ConnectToServlet()
    {
    	this.bls = new ArrayList<BuyListing>();
    	this.sls = new ArrayList<SellListing>();
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
	                    Log.d("LOUD AND CLEAR", "Clientside bls updated" + rl.size() + rl.get(0).getUser().getName());


	                    Log.d("LOUD AND CLEAR", rl.get(0).getUser().getName() + " " + rl.get(0).getStartTime() + " " + rl.get(0).getEndTime() + " " + 
	                	            			rl.get(0).getVenue().getName() + " " + rl.get(0).getSwipeCount() + " ");
	                    return rl;
    			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed");
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
                Log.d("LOUD AND CLEAR", "Clientside bls updated" + rl.size() + rl.get(0).getUser().getName());
                
                
                Log.d("LOUD AND CLEAR", rl.get(0).getUser().getName() + " " + rl.get(0).getStartTime() + " " + rl.get(0).getEndTime() + " " + 
            	            			rl.get(0).getVenue().getName() + " " + rl.get(0).getSwipeCount() + " ");
                return rl;
		  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed"); }

		  return nl;

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
		  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed");
		  }

		  return nl;

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
          
			  Log.d("LOUD AND CLEAR", "SendIDPair reached, sending the string:" + gmsg);
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
				  Log.d("LOUD AND CLEAR", "url connection failed at getFBID");
				  //dmsg.setPayload("URLConnectionFailedID");
			  }

		 Log.d("LOUD AND CLEAR", "dmsg.getPayload() is " + dmsg.getPayload());
			  return dmsg.getPayload();
	}
}