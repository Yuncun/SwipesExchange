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
 
    String inputValue=null;
    static String doubledValue = "";
    public static List<BuyListing> bls;
    private List<SellListing> sls;
    
    public ConnectToServlet()
    {
    	this.bls = new ArrayList<BuyListing>();
    	this.sls = new ArrayList<SellListing>();
    }

    public static void sendListing(final Object inputListing)
    {
    	new Thread(new Runnable() {
    		public void run() {
    			  Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect with Listing support");
    			  try{
            	   URL url = new URL("http://anotherservlet14env-jxfwis2wdy.elasticbeanstalk.com/HelloWorld");
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
                            /*
                            Gson gson = new Gson();
                            
                            
                            
                            JsonReader reader = new JsonReader(new StringReader(doubledValue));
                            reader.setLenient(true);
                          
                    		MsgStruct packet = gson.fromJson(doubledValue, MsgStruct.class);
                    		
                    		String doubledvalue2 = "no bl";
                    		if (packet.getHeader() == "bl")
                    		{
                    				BuyListing bl = gson.fromJson(packet.getPayload(), BuyListing.class);
                    				doubledvalue2 = "User " + bl.getUser().getName() + " and venue " + bl.getVenue().getName();
                    		}
                    		*/
                    		
                  		//  Log.d("LOUD AND CLEAR", doubledValue);  
                  		//  Log.d("LOUD AND CLEAR", doubledvalue2);
 
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
            	   URL url = new URL("http://anotherservlet14env-jxfwis2wdy.elasticbeanstalk.com/HelloWorld");
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
    			  } catch(Exception e) { Log.d("LOUD AND CLEAR", "url connection failed"); }
    			  
    			  return nl;
    			  
    			  
    			

    	
        }
    }