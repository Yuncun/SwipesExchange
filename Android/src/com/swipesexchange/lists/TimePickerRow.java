package com.swipesexchange.lists;

import com.swipesexchange.helpers.ChildRow;

import android.widget.TimePicker;

public class TimePickerRow extends ChildRow {

	private TimePicker tp;
	
	public TimePicker getTimePicker() {
		return this.tp;
	}
	
	public void setTimePicker(TimePicker time_picker) {
		this.tp = time_picker;
	}
}
