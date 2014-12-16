package io.swipeswap.helpers;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class FX {

	public static void slide_down(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, io.swipeswap.R.anim.slide_down);
		  if(a != null){
		     a.reset();
		     if(v != null){
		      v.clearAnimation();
		      v.startAnimation(a);
		     }
	}
	}
	public static void slide_up(Context ctx, View v) {
		Animation a = AnimationUtils.loadAnimation(ctx, io.swipeswap.R.anim.slide_up);
		  if(a != null){
		     a.reset();
		     if(v != null){
		      v.clearAnimation();
		      v.startAnimation(a);
		     }
	}
	}
}
