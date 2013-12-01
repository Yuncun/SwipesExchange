package com.example.tabsfinal;



import java.text.SimpleDateFormat;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childtems;
	private  LayoutInflater inflater;
	private ArrayList<String> parentItems, child;
	private int minutes_start, minutes_end, hours_start, hours_end;
	private TimePicker tp_start, tp_end;
	

	public MyExpandableAdapter(ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
		minutes_start = 0;
		minutes_end = 0;
		hours_start = 12;
		hours_end = 12;
		
	
		
	}
	
	 public void setInflater(LayoutInflater inflater, Activity activity) {
		         this.inflater = inflater;
		         this.activity = activity;
		        
		
	 }
	 //need to catch error if time hasn't been initialized/time
	 //fields haven't been expanded and they try and submit
	 public int getStartMinutes() {
		return minutes_start;
	 }
	 
	 public void setStartMinutes(int num) {
		 minutes_start = num;
	 }
	 
	 public int getEndMinutes() {
		 return minutes_end;
	 }
	 
	 public void setEndMinutes(int num) {
		 minutes_end = num;
	 }
	 public int getStartHours() {
		 return hours_start;
	 }
	 
	 public void setStartHours(int num)
	 {
		 hours_start = num;
	 }
	 
	 public int getEndHours() {
		 return hours_end;
	 }
	 
	 public void setEndHours(int num) {
		 hours_end = num;
	 }
	 
	 


	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		//if (convertView == null) {
			if(groupPosition == 0)
				convertView = inflater.inflate(R.layout.group_ex, null);
			else if(groupPosition == 1)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if(groupPosition == 2)
				convertView = inflater.inflate(R.layout.group_ex1, null);
			else if(groupPosition == 3)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if(groupPosition == 4)
				convertView = inflater.inflate(R.layout.group_ex2, null);
			else if (groupPosition == 5)
				convertView = inflater.inflate(R.layout.group_ex2, null);
		//}
		if (groupPosition == 0)
		{
			this.tp_start = (TimePicker) convertView.findViewById(R.id.timePicker1);
			//tp_start.setSaveEnabled(true);
		}
		
		
		if (groupPosition == 2)
		{
			this.tp_end = (TimePicker) convertView.findViewById(R.id.timePicker2);
			//tp_start.setSaveEnabled(true);
		}
		
		if(groupPosition == 0)
		{
			tp_start.setCurrentMinute(minutes_start);
			tp_start.setCurrentHour(hours_start);
		}
		if(groupPosition == 2)
		{
			tp_end.setCurrentMinute(minutes_end);
			tp_end.setCurrentHour(hours_end);
		}
		
		if (groupPosition == 4 || groupPosition == 5)
		{
			child = (ArrayList<String>) childtems.get(groupPosition);
			TextView textView = null;
			//Log.d("Panda", Integer.toString(getStartMinutes()));
			textView = (TextView) convertView.findViewById(R.id.textView1ex);
			textView.setText(child.get(childPosition));
			
		}
		

		

	
/*
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Toast.makeText(activity, child.get(childPosition),
						Toast.LENGTH_SHORT).show();
			}
		});
*/
		return convertView;
	}
	
	


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			
			
			
				convertView = inflater.inflate(R.layout.row_ex, null);
		}
		
		 
		 ////if (getChildrenCount(groupPosition)== 0) {
	           // convertView.setEnabled(false); //Greys out group name
	        //} 
		//((CheckedTextView) convertView.findViewById(R.id.textView1Checked)).setText(parentItems.get(groupPosition));
		//((CheckedTextView) convertView.findViewById(R.id.textView1Checked)).setChecked(isExpanded);
		
		//checkedView.setText(parentItems.get(groupPosition));
		//checkedView.setChecked(isExpanded);
		//Time stime = null;
		//stime.hour = getStartHours();
		//stime.minute = getStartMinutes();
		//SimpleDateFormat df = new SimpleDateFormat("mm:ss");
		if(groupPosition == 1)
		{
		String mystring = String.format("%d:%02d", getStartHours(), getStartMinutes());
		//df.format()
		((TextView) convertView).setText(parentItems.get(groupPosition) + "           " + mystring);
		((TextView) convertView).setOnClickListener(null);
		((TextView) convertView).setClickable(false);
		//((TextView) convertView).setChecked(isExpanded);
		}
		else if(groupPosition == 3)
		{
		String mystring = String.format("%d:%02d", getEndHours(), getEndMinutes());
		//df.format()
		((TextView) convertView).setText(parentItems.get(groupPosition) + "           " + mystring);
		((TextView) convertView).setOnClickListener(null);
		((TextView) convertView).setClickable(false);
		//((TextView) convertView).setChecked(isExpanded);
		}
		else
		{
			((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
			
			//((CheckedTextView) convertView).setCheckMarkDrawable(R.drawable.shopping_basket_checkmark);
			((CheckedTextView) convertView).setChecked(isExpanded);
		}

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
		if(groupPosition == 0)
		{
			minutes_start = tp_start.getCurrentMinute();
			hours_start = tp_start.getCurrentHour();
		}
		if (groupPosition == 2)
		{
			minutes_end = tp_end.getCurrentMinute();
			hours_end = tp_end.getCurrentHour();
		}
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		
		super.onGroupExpanded(groupPosition);
		
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	/**ADD LISTING**/
	private class AddHighScoreTask extends AsyncTask<Listing, Void, Void> {

		protected Void doInBackground(Listing... highScores) {

			SimpleDBData hs = new SimpleDBData();
			hs.addHighScore(highScores[0]);

			return null;
		}

//This onPostExecute is not applicable to our fragment layout. Normally this closes the addListing activity, returning the user to the original screen.
/*
		protected void onPostExecute(Void result) {

			AddScoreActivity.this.finish();
		}*/
	}
	

}