package com.example.tabsfinal;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final String[] values;
  //private final String[] values2;
 // private final String[] values3;
  //private final List<String> vals;
  

//The best practice for passing the data should be having all the data be in one object, rather than three arguments of strings. This is for early implementation sake.
  public MyArrayAdapter(Context context, String[] values) {
    super(context, R.layout.list_item, values);
    this.context = context;
    this.values = values;
    //this.values2 = values2;
    //this.values3 = values3;
    
    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.list_item, parent, false);
    
    //Set the values of the strings on the list
    TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
//    TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
//    TextView thirdLine = (TextView) rowView.findViewById(R.id.secondLine);
    
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    firstLine.setText(values[position]);
 //   secondLine.setText(values2[position]);
   
  //  imageView.setImageResource(R.drawable.yes);
 
  
    return rowView;
  }
} 