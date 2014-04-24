package com.example.tabsfinal;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

 
public abstract class ConnectToServlet {
 
    String inputValue=null;
    static String doubledValue = "";
    
 
    public static void talk(final String inputMessage) {
    	new Thread(new Runnable() {
    		public void run() {
    			  Log.d("LOUD AND CLEAR", "Starting new thread for client/server connect");
    			  try{
            	   URL url = new URL("http://anotherservlet14env-jxfwis2wdy.elasticbeanstalk.com/HelloWorld");
                   URLConnection connection = url.openConnection();
                   
                   
                   String inputString = inputMessage;
                   connection.setDoOutput(true);
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
 