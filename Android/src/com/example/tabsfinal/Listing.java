package com.example.tabsfinal;

import java.io.Serializable;

public abstract class Listing implements Serializable{ //I have no idea if implementing Serializable with this abstract  class will cause problems later
        

        private int swipeCount;
        private User user;
        private Venue venue;
        private String time;
        private String startTime;
        private String endTime;
        public String section;
        public Boolean isSection;
        
        
        
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
        
        
        
}