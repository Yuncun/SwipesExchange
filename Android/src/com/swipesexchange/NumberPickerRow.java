package com.swipesexchange;

import android.widget.NumberPicker;

public class NumberPickerRow extends ChildRow {

	private NumberPicker np;
	
	
	public NumberPicker getNumberPicker() {
		return this.np;
	}
	
	public void setNumberPicker(NumberPicker num_picker) {
		this.np = num_picker;
	}
}
