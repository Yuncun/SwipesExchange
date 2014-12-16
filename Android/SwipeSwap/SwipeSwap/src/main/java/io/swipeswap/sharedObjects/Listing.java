package io.swipeswap.sharedObjects;
import java.io.Serializable;

public abstract class Listing implements Serializable{ 
        
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//the following variables need to be set when we create a new listing from the backend
        private int swipeCount; 
        private User user;
        private Venue venue; //might need to change this to just a string
        private String time;
        private String startTime;
        private String endTime;
        private String time_created;
        public String section;
        public Boolean isSection;
        private String messageBody;
        private String id;
        
        
        
        public String getSection()
        {
        	return section;
        }
        
        public void setSection(String section)
        {
        	this.section = section;
        }
        
        public int getSwipeCount() {
                return swipeCount;
        }
        public void setSwipeCount(int swipeCount) {
                this.swipeCount = swipeCount;
        }
        
        public User getUser() {
                return user;
        }
        public void setUser(User user) {
                this.user = user;
        }
        
        public Venue getVenue() {
                return venue;
        }
        public void setVenue(Venue venue) {
                this.venue = venue;
        }
        
        public String getTime() {
                return time;
        }
        public void setTime(String time) {
                this.time = time;
        }
        
        public String getStartTime() {
                return startTime;
        }
        public void setStartTime(String startTime) {
                this.startTime = startTime;
        }
        
        public String getEndTime() {
                return endTime;
        }
        public void setEndTime(String endTime) {
                this.endTime = endTime;
        }
        
        public String getListingID() {
        	return this.id;
        }
        
        public void setListingID(String l_id) {
        	this.id = l_id;
        }
        
        public String getTimeCreated() {
        	return this.time_created;
        }
        
        public void setTimeCreated(String time) {
        	this.time_created = time;
        }

		/**
		 * @return the messageBody
		 */
		public String getMessageBody() {
			return messageBody;
		}

		/**
		 * @param messageBody the messageBody to set
		 */
		public void setMessageBody(String messageBody) {
			this.messageBody = messageBody;
		}
        
        
        
}