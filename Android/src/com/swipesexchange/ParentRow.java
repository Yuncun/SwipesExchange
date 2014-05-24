package com.swipesexchange;

import java.util.ArrayList;

public class ParentRow {

	private String name;
	private String text_right;
	private String text_left;
	private String checkedtype;
	private boolean checked;
	
	private ArrayList<ChildRow> children;
	
	public String getName()
    {
        return name;
    }
     
    public void setName(String name)
    {
        this.name = name;
    }
    public String getTextRight()
    {
        return text_right;
    }
     
    public void setTextRight(String text1)
    {
        this.text_right = text1;
    }
     
    public String getTextLeft()
    {
        return text_left;
    }
     
    public void setTextLeft(String text2)
    {
        this.text_left = text2;
    }
    public String getCheckedType()
    {
        return checkedtype;
    }
     
    public void setCheckedType(String checkedtype)
    {
        this.checkedtype = checkedtype;
    }
     
     
    public boolean isChecked()
    {
        return checked;
    }
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }
     
    // ArrayList to store child objects
    public ArrayList<ChildRow> getChildren()
    {
        return children;
    }
     
    public void setChildren(ArrayList<ChildRow> children)
    {
        this.children = children;
    }
	
	
	
}
