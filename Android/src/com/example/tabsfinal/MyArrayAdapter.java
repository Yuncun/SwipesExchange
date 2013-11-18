package com.example.tabsfinal;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/************THIS CLASS IS OBSOLETE*************/



public class MyArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
//  private String[] values;
  private List<Listing> entries;
  //private final String[] values2;
 // private final String[] values3;
  //private final List<String> vals;
  

//The best practice for passing the data should be having all the data be in one object, rather than three arguments of strings. This is for early implementation sake.
  public MyArrayAdapter(Context context, List<Listing> values) {
    super(context, R.layout.sell_list_item);
    this.context = context;
    this.entries = values;
    //this.values2 = values2;
    //this.values3 = values3;
    
    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
          //test
          System.out.println("getView is called");
          
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View rowView = inflater.inflate(R.layout.sell_list_item, parent, false);
    
    //Set the values of the strings on the list
    TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
    TextView firstLineRight = (TextView) rowView.findViewById(R.id.firstLineRight);
    TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
    TextView secondLineRight = (TextView) rowView.findViewById(R.id.secondLineRight);
//    TextView thirdLine = (TextView) rowView.findViewById(R.id.secondLine);
 //   ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    //test outputs
    String firstlinetext = ((entries.get(position)).getVenue()).getName();
    String secondlinetext = ((entries.get(position)).getUser()).getName();
    String firstlinerighttext = String.valueOf(((entries.get(position)).getSwipeCount()));
    //String secondlinerighttext = String.valueOf((entries.get(position).getPrice))
    System.out.println("first line is" + firstlinetext);
    System.out.println("second line is" + secondlinetext);
    System.out.println("first line right is" + firstlinerighttext);
    //test outputs
  
    firstLine.setText(((entries.get(position)).getVenue()).getName()); //get the name of the user from the entry in question
    firstLineRight.setText(String.valueOf(((entries.get(position)).getSwipeCount())));
    secondLine.setText(((entries.get(position)).getUser()).getName());
   
//   imageView.setImageResource(R.drawable.yes);
 
  
    return rowView;
  }
} 