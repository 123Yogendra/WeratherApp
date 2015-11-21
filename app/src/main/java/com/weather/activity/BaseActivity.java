package com.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.weather.utils.AppConstant;
import com.weather.utils.Log;



/*
  *
  * This class is used as base activity and extend by other activity in this application.In this activity we are define common method and common exceptions handle methode.
  **/

public abstract class BaseActivity extends Activity implements AppConstant {


    private ProgressDialog pDialog;

    /*
    *
    * This method is used to get content view id
    * **/
    public abstract int getLayoutView();


    /*
    *
    * This method is used to initilise other classes, adpters, database and other.
    * **/

    public abstract void initialise();

    /*
    *
    * This method is used to init all widgets which is used in that activity.
    * **/

    public abstract void initViews();


    /*
    *
    * This method is used to set up listener, adapters and others things..
    * **/
    public abstract void setupViews();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        initialise();
        initViews();
        setupViews();

    }



    /*
    *
	* This method is used to show loading dialog
	* **/

    public void showProgress(final String msg, final String title) {


        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.cancel();
                pDialog = null;
            }
        }
        pDialog = ProgressDialog.show(this, title, msg, true);


    }

    /*
    *
    * This method is used to close loading dialog
    * **/
    public void dismissProgress() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog.cancel();
                pDialog = null;
            }
        }
    }

    /*
   *
   * This method is used to handle exceptions in all applications. In this method we can manage exception flow.
   * **/
    public void onException(Exception e) {
        Log.d(e.getMessage());
    }

}
