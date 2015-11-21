package com.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefUtils implements AppConstant {


    private SharedPreferences shared;

    private Editor edit;

    public SharedPrefUtils(Context context) {

        shared = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        edit = shared.edit();
    }

    /**
     * This method is used to save string in shared preference
     */
    public void saveString(String key, String value) {
        edit.putString(key, value);
        edit.commit();
    }


    /**
     * This method is used to get string value from shared preference. if not exist then it return blank value
     *
     */
    public String getString(String key) {
        return shared.getString(key, "");

    }


    /**
     * This method is used to check that given value is exist in shared preference or not. it return boolean value
     *
     */
    public boolean isExist(String key) {
        return shared.contains(key);

    }


}
