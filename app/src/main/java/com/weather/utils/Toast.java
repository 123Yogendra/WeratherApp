package com.weather.utils;

import android.content.Context;

/**
 * This class is override android log class. In this,  we can manage debug and oter log message easily.
 * **/
public class Toast implements AppConstant {

/**
 * This method is used to display debug log message
 * **/
    public static void showMsg(Context context, String message){

        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();

    }
}
