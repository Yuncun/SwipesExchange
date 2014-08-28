package com.swipesexchange.helpers;

import com.swipesexchange.R;
import com.swipesexchange.main.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DisplayExceptionAlertDialog {
	
	protected Activity m_activity;
	
	public DisplayExceptionAlertDialog(){
		//
	}
	
	
	public void showAlert(Activity activity, String message, final boolean fatal) {

		this.m_activity = activity;
        TextView title = new TextView(activity);
        title.setText("Error");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // builder.setTitle("Title");
        builder.setCustomTitle(title);
        // builder.setIcon(R.drawable.alert_36);

        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                if(fatal){
                	
                	Log.d("DisplayExceptionAlertDialog", "Fatal error, finishg activity");
                	//activity.finish();
                	
                	Intent intent = new Intent(m_activity, MainActivity.class);
                	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	intent.putExtra("EXIT", true);
                	m_activity.startActivity(intent);
                }
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}


