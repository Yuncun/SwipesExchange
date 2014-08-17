package com.swipesexchange.messaging;

import java.util.List;
import java.util.Stack;

import android.util.Log;

import com.swipesexchange.sharedObjects.Message;
import com.swipesexchange.sharedObjects.Self;

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
		//Log.d ("ConversationList.findConversationIndex...", "THE ASSOCIATED LID IS " + lid);
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
			ConversationList.needs_update = true;
		}
	}

}
