package com.swipesexchange.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;





import com.swipesexchange.main.MainActivity;
import com.swipesexchange.network.ConnectToServlet;
import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.Self;
import com.swipesexchange.sharedObjects.User;

public class ConversationList {
	
	private static Stack<Conversation> conversation_list = new Stack<Conversation>();
	
	// boolean to check if the Messages tab needs an update
	private static boolean needs_update;
	public static boolean is_set = false;
	
	public ConversationList() {
		
		needs_update = false;
	}
	

	// accessing the conversation list in fragments
	public static Stack<Conversation> getConversations() {
		return conversation_list;
	}
	
 	public static void addMessageList(List<Message> msgs) {
		Log.d("porcupine", "Adding message list with size " + msgs.size());
		conversation_list.clear();
		for(int i=0; i<msgs.size(); i++)
			addMessage(msgs.get(i));
	
}
	
	
	// for adding conversations to the conversation list
	public static void addConversation(Conversation c) {
		ConversationList.conversation_list.push(c);
		if(!needs_update)
		{
			needs_update = true;
		}
	}
	
	public static void addMessage(Message msg) {
		String lid = msg.getListing_id();
		
		String uid;
		if(Self.getUser().getUID().equals(msg.getSender().getUID()))
			uid = msg.getReceiver().getUID();
		else
			uid = msg.getSender().getUID();
		
		int conversation_index = findConversationIndexByListingID(lid, uid);
		if(conversation_index == -1)
		{
			Conversation c = new Conversation(msg.getSender(), msg.getReceiver(), msg.getListing_id());
			c.addMessageToConversation(msg);
			addConversation(c);
		}
		else
		{
			ConversationList.conversation_list.get(conversation_index).addMessageToConversation(msg);
			if(!needs_update)
			{
				needs_update = true;
			}
		}
		
	}
	
	public static int findConversationIndexByListingID(String lid, String other_person_uid) {
		Log.d ("ConversationList.findConversationIndex...", "THE ASSOCIATED LID IS " + lid);
		Stack<Conversation> l = ConversationList.getConversations();
		for(int i=0; i<l.size(); i++)
		{
			String id;
			if(Self.getUser().getUID().equals(l.get(i).getSender().getUID()))
				id = l.get(i).getReceiver().getUID();
			else
				id = l.get(i).getSender().getUID();
			if(l.get(i).getLID().equals(lid) && other_person_uid.equals(id))
			{
				return i;
			}
		}
		
		return -1;
			
	}
	
	public static boolean doesConversationExist(String lid, String other_person_uid) {
		if(findConversationIndexByListingID(lid, other_person_uid) == -1)
			return false;
		else
			return true;
	}
	
	// for removing conversations from the conversation list, e.g. b/c of a button press on the view
	public void removeConversation(int index) {
		ConversationList.getConversations().remove(index);
		if(!needs_update)
		{
			this.needs_update = true;
		}
	}
	
	// TO BE PHASED OUT: a sample message creator. This is NOT how messages/conversations should be added
	public void addFakeMessages() {
		

		// Conversation 1
		String m1_SID = "100";
		String m1_RID= "200";
		String m1_LID = "300";
		String m1_rname = "Legolas";
		String m1_sname = "Strider";
		String m1_text = "Hello, I saw your listing and" +
				"I want to buy your swipes. Where can you meet?";
		User tu = new User("Legolas");
		tu.setUID(m1_SID);
		tu.setRegid("bullshit");
		tu.setConnections("1");
		tu.setRating("none");
		
		User tv = new User("Strider");
		tv.setUID(m1_SID);
		tv.setRegid("bullshit");
		tv.setConnections("1");
		tv.setRating("none");
		
		String time = "1:01 PM";
		
		Message m1 = new Message(tu, tv, m1_LID, time, m1_text);
		String m2_SID = "101";
		String m2_RID= "201";
		String m2_LID = "303";
		String m2_rname = "Gimli";
		String m2_sname = "Galadriel";
		String m2_text = "Sup, looking to buy your swipes -" +
				" meet up in Rivendell?";
		Message m2 = new Message(tu, tv, m2_LID, time, m2_text);
		Conversation c1 = new Conversation(m1.getSender(), m1.getReceiver(), m1.getListing_id());
		c1.addMessageToConversation(m1);
		c1.addMessageToConversation(m2);
		
		// Conversation 2
		long m3_SID = 102;
		long m3_RID= 202;
		String m3_LID = "302";
		String m3_rname = "Witch King";
		String m3_sname = "Saruman";
		String m3_text = "Give me your swipes for free wizard.";
		Message m3 = new Message(tu, tv, m3_LID, time, m3_text);
		Conversation c2 = new Conversation(m3.getSender(), m3.getReceiver(), m3.getListing_id());
		c2.addMessageToConversation(m3);
		
		this.addConversation(c1);
		this.addConversation(c2);

	}
	
	
}
