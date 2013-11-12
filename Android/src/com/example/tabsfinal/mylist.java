package com.example.tabsfinal;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

class MyList extends ListFragment
{
	String[] values = new String[] {"1", "2", "3"};
	static MyList newInstance(int num) {
        MyList l = new MyList();

        Bundle args = new Bundle();
        args.putInt("num", num);

        l.setArguments(args);

        return l;
    }
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {

	        View view = inflater.inflate(R.layout.mylist, container, false);

	        return view;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        setListAdapter(new ArrayAdapter<String>(getActivity(),
	                android.R.layout.simple_list_item_1, android.R.id.text1,
	                values));
	    }
	
}
