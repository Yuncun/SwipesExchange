package com.swipesexchange;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import sharedObjects.MsgStruct;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import android.util.Log;

 
public abstract class ConnectToServlet {
 
    String inputValue=null;
    static String doubledValue = "";
    
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
                            
                            Gson gson = new Gson();
                            
                            
                            
                            JsonReader reader = new JsonReader(new StringReader(doubledValue));
                            reader.setLenient(true);
                          
                    		MsgStruct packet = gson.fromJson(doubledValue, MsgStruct.class);
                    		/*
                    		if (packet.getHeader() == "bl")
                    		{
                    				BuyListing bl = gson.fromJson(packet.getPayload(), BuyListing.class);
                    				doubledValue = "User " + bl.getUser().getName() + " and venue " + bl.getVenue().getName();
                    		}
                    		else doubledValue = "no bl";


                            */
                    		
                  		  Log.d("LOUD AND CLEAR", doubledValue);  
 
                            }catch(Exception e)
                            {
                                Log.d("Exception",e.toString());
                            }
                    }
                  }).start();
            }

 
    public static void talk(final String inputMessage) {
    	new Thread(new Runnable() {
    		public void run() {
    			  Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect");
    			  try{
            	   URL url = new URL("http://anotherservlet14env-jxfwis2wdy.elasticbeanstalk.com/HelloWorld");
                   URLConnection connection = url.openConnection();
                   
                   
                   String inputString = inputMessage;
                   connection.setDoOutput(true);
                   
                   //Begin to open a new OutputObjectStream
                   OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                          
                   out.write(inputString);
                   out.close();

                   BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                   String returnString="";
                   doubledValue = "";
 
                            while ((returnString = in.readLine()) != null) 
                            {
                                doubledValue= returnString;
                      		  Log.d("LOUD AND CLEAR", doubledValue);
                                
                            }
                            in.close();
 
                            }catch(Exception e)
                            {
                                Log.d("Exception",e.toString());
                            }
                    }
                  }).start();
            }
        }
 