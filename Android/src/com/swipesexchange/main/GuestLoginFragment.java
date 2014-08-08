package com.swipesexchange.main;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.swipesexchange.R;
import com.swipesexchange.main.MainActivity;

public class GuestLoginFragment extends DialogFragment {
	
	EditText mEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        MainActivity mactivity = (MainActivity) getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //final EditText mEdit = EditText ;
        View myView = inflater.inflate(R.layout.dialog_signin, null);
        final EditText mEdit = (EditText) myView.findViewById(R.id.username);
        mEdit.setText(mactivity.retrieveSavedGuestName());
        
   
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        // Add action buttons
               .setPositiveButton(R.string.dialog_login, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                	   //Dialog f = (Dialog) dialog;
                       //mEdit = (EditText) f.findViewById(R.id.username);

                       // sign in the user ...              	   
                	   String username = mEdit.getText().toString().trim();
                	   MainActivity mactivity = (MainActivity) getActivity();
                	   mactivity.handleNewGuest(username);
                	   mactivity.showFragment(mactivity.MAIN, false);
                   }
               })
               .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   LoginFragment.dialogOpened = false;
                       GuestLoginFragment.this.getDialog().cancel();
                   }
               });      
        return builder.create();
    }
    
  
}
