package com.weather.utils;

/**
 * This class is override android log class. In this,  we can manage debug and oter log message easily.
 * **/
public class Log implements AppConstant {

/**
 * This method is used to display debug log message
 * **/
    public static void d(String message){
        if (IS_DEBUG) {
            android.util.Log.d(LOG_TAG, message);
        }
    }
}
