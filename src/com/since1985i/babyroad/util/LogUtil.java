package com.since1985i.babyroad.util;


import android.util.Log;

public class LogUtil {

	//If the log can be print
	public static boolean LOGD = true;
	public static String LOG_TAG = "BabyRoad";
	
	public static void log(String msg) {
		if(LOGD) {
			Log.d(LOG_TAG, msg);
		}
	}
	
	public static void log(String tag, String msg) {
		if(LOGD) {
			Log.d(LOG_TAG + "_" + tag, msg);
		}
	}
	public static void log(String tag, String msg, Throwable throwable) {
		if(LOGD) {
			Log.d(LOG_TAG + "_" + tag, msg, throwable);
		}
	}
	
	public static void logW(String tag, String msg) {
		if(LOGD) {
			Log.w(LOG_TAG + "_" + tag, msg);
		}
	}
	
	public static void logE(String tag, String msg) {
		if(LOGD) {
			Log.e(LOG_TAG + "_" + tag, msg);
		}
	}
	
	public static void logE(String msg, Throwable throwable) {
		if(LOGD) {
			Log.e(LOG_TAG, msg, throwable);
		}
	}
	
	public static void logE(String tag, String msg, Throwable throwable) {
		if(LOGD) {
			Log.e(LOG_TAG + "_" + tag, msg, throwable);
		}
	}
	
}
