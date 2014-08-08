package com.swipesexchange.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

public class PictureCache {

	// maps FBUIDs to their small profile pictures
	private static Map<String, Bitmap> pic_map_buy = new HashMap<String, Bitmap>();
	private static Map<String, Bitmap> pic_map_sell = new HashMap<String, Bitmap>();
	
	//  constructor
	public PictureCache() {
		
	}
	
	public static Bitmap getFBPicBuy(String FBuid) {
		return pic_map_buy.get(FBuid);
	}
	
	public static void setPicMappingBuy(String FBuid, Bitmap pic) {
		pic_map_buy.put(FBuid, pic);
	}
	
	public static void cachePicMapBuy(Map<String, Bitmap> src_pic_map) {
		pic_map_buy.putAll(src_pic_map);
	}
	
	public static Bitmap getFBPicSell(String FBuid) {
		return pic_map_sell.get(FBuid);
	}
	
	public static void setPicMappingSell(String FBuid, Bitmap pic) {
		pic_map_sell.put(FBuid, pic);
	}
	
	public static void cachePicMapSell(Map<String, Bitmap> src_pic_map) {
		pic_map_sell.putAll(src_pic_map);
	}
	
}
