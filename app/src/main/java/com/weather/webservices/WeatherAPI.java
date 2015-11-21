package com.weather.webservices;


import android.content.Context;

import com.weather.activity.BaseActivity;

import java.net.URLEncoder;

/**
 * This class is used to create weather url. In this class we can use set all parameter regarding weather url.
 */
public class WeatherAPI implements WebserviceUrl {

    private Context context;
    private String q;
    private String key;
    private String format;
    private int num_of_days = 0;


    public WeatherAPI(Context context) {
        this.context = context;
    }


    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getNum_of_days() {
        return num_of_days;
    }

    public void setNum_of_days(int num_of_days) {
        this.num_of_days = num_of_days;
    }


    /**
     * This method is used to create url for weather api
     */

    public String createWeatherUrl() {


        return BASE_URL

                + getStringValue("key", getKey())
                + getStringValue("q", getQ())
                + getStringValue("format", getFormat())
                + getStringValue("num_of_days", String.valueOf(getNum_of_days()));



    }


    /**
     * This method is used to check value if exist then add otherwise return blank value
     */
    private String getStringValue(String key, String value) {
        try {
            if (value != null && value.trim().length() > 0) {
                return "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }
        } catch (Exception e) {

            ((BaseActivity) context).onException(e);
        }
        return "";
    }

}
