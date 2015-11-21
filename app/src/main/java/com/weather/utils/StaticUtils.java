package com.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weather.activity.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaticUtils implements AppConstant {


    public StaticUtils() {

    }

    /*
       *
       * This method is used to check iternet is working or not
       * **/
    public static String getFormatedDate(Context context, String dateValue) {
        String formatedDate = dateValue;
       try{
        SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat parseFormat = new SimpleDateFormat("E, dd MMM,yy");
        Date date = inputFormate.parse(dateValue);
       formatedDate = parseFormat.format(date);
        Log.d("Formated date: "+formatedDate);

    } catch (ParseException e) {
           ((BaseActivity)context).onException(e);
    }

        return formatedDate;

    }




    /*
    *
    * This method is used to check iternet is working or not
    * **/
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }


}
