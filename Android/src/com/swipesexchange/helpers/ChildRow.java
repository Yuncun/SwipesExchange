package com.swipesexchange.helpers;

public abstract class ChildRow {

	private String name;
	private int backgroundcolor;
	
	
	 public String getName()
	 {
        return name;
    }
	     
    public void setName(String name)
    {
        this.name = name;
    }
    

	/**
	 * @return the backgroundcolor
	 */
	public int getBackgroundcolor() {
		return backgroundcolor;
	}

	/**
	 * @param backgroundcolor the backgroundcolor to set
	 */
	public void setBackgroundcolor(int backgroundcolor) {
		this.backgroundcolor = backgroundcolor;
	}
	
}
