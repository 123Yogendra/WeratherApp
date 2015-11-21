package com.weather.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.weather.utils.Log;
import com.weather.utils.SharedPrefUtils;
import com.weather.utils.StaticUtils;
import com.weather.utils.Toast;

import java.util.Arrays;

/**
 * This class is extend BaseActivity which override some intial method. In this activity user can select city for weather.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private SharedPrefUtils sharedPrefUtils;
    private Spinner spCity;
    private Button btnSubmit;
    private ArrayAdapter<CharSequence> adapterCities;

    @Override
    public int getLayoutView() {
        return R.layout.activity_settings;
    }

    @Override
    public void initialise() {

        adapterCities = ArrayAdapter.createFromResource(this,
                R.array.array_cities, android.R.layout.simple_spinner_item);
        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sharedPrefUtils = new SharedPrefUtils(this);

    }

    @Override
    public void initViews() {

        spCity = (Spinner) findViewById(R.id.sp_city);
        btnSubmit = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void setupViews() {
        spCity.setAdapter(adapterCities);
        btnSubmit.setOnClickListener(this);
        setSelectedCityInSpinner();
    }

    /**
     * This method is used to get index of city array and set selection in spinner
     * */
    private void setSelectedCityInSpinner(){

        if(sharedPrefUtils.isExist(KEY_CITY_NAME)){

            String []city = getResources().getStringArray(R.array.array_cities);

            String cityName = sharedPrefUtils.getString(KEY_CITY_NAME);

            int index = Arrays.asList(city).indexOf(cityName);
            spCity.setSelection(index);
        }

    }




    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_next:
                moveToDisplayWeatherScreen();
                break;
            default: break;

        }

    }


    /**
     * This method is used to move weather display activity
     * *
     */

    private void moveToDisplayWeatherScreen() {

        if (StaticUtils.isConnectingToInternet(this)) {
            String cityName = (String) spCity.getSelectedItem();
            sharedPrefUtils.saveString(KEY_CITY_NAME, cityName);
            Log.d("City name: " + cityName);
            startActivity(new Intent(SettingsActivity.this, DisplayWeatherActivity.class));
            finish();

        } else {
            Toast.showMsg(this, getString(R.string.internet_issue));
        }
    }


}
