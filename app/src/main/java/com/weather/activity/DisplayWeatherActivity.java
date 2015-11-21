package com.weather.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.weather.model.Weather;
import com.weather.utils.FileUtils;
import com.weather.utils.Log;
import com.weather.utils.SharedPrefUtils;
import com.weather.utils.StaticUtils;
import com.weather.utils.Toast;
import com.weather.webservices.VolleySingleton;
import com.weather.webservices.WeatherAPI;
import com.weather.webservices.WebserviceUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is extend BaseActivity which override initiali method. In this activity load city information from api then will display into lect cities and other parameter related to weather.
 */
public class DisplayWeatherActivity extends BaseActivity implements View.OnClickListener {

    private SharedPrefUtils sharedPrefUtils;

    private View llViewContainer;
    private NetworkImageView ivWeatherIcon;
    private TextView tvCurrentTemp;
    private TextView tvCityName;
    private TextView tvDate;
    private TextView tvDescription;
    private ImageButton ivBtnSettings;

    private TextView tvCloudCover, tvFeelsLike, tvHumidity, tvPressure, tvVisibility, tvUVIndex, tvWindSpeed,tvWindDir,  tvTempC, tvTempF;
            private TextView tvSunriseSet, tvMoonriseSet;



    @Override
    public int getLayoutView() {
        return R.layout.activity_display_weather;
    }

    @Override
    public void initialise() {
        sharedPrefUtils = new SharedPrefUtils(this);
    }

    @Override
    public void initViews() {
        llViewContainer = findViewById(R.id.ll_view_container);
        ivWeatherIcon = (NetworkImageView) findViewById(R.id.iv_weather_icon);
        tvCurrentTemp = (TextView) findViewById(R.id.tv_curent_temp);
        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        ivBtnSettings = (ImageButton) findViewById(R.id.iv_btn_setting);

        tvCloudCover= (TextView) findViewById(R.id.tv_cloud_cover);
        tvFeelsLike= (TextView) findViewById(R.id.tv_feels_like);
        tvHumidity= (TextView) findViewById(R.id.tv_humidity);
        tvPressure= (TextView) findViewById(R.id.tv_pressure);
        tvVisibility= (TextView) findViewById(R.id.tv_visibility);
        tvWindSpeed= (TextView) findViewById(R.id.tv_wind_speed);
        tvWindDir= (TextView) findViewById(R.id.tv_wind_dir);
        tvTempC= (TextView) findViewById(R.id.tv_temp_in_cel);
        tvTempF= (TextView) findViewById(R.id.tv_temp_in_for);
        tvUVIndex = (TextView)findViewById(R.id.tv_uv_index);


        tvSunriseSet= (TextView) findViewById(R.id.tv_sun_rise_set);
        tvMoonriseSet= (TextView) findViewById(R.id.tv_moon_rise_set);

    }

    @Override
    public void setupViews() {

        ivBtnSettings.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCityAndMoveToSettings();
    }

    /**
     *
     * This method is used to check city value and move to next screen
     * **/
    private void checkCityAndMoveToSettings(){

        if (sharedPrefUtils.isExist(KEY_CITY_NAME)) {
            String cityName = sharedPrefUtils.getString(KEY_CITY_NAME);
            if(!cityName.equalsIgnoreCase(tvCityName.getText().toString()))
            {

                checkInternetAndCallAPI();
            }

        } else {
            startActivity(new Intent(DisplayWeatherActivity.this, SettingsActivity.class));
            finish();
        }
    }

    /**
     * This method is usewd to check internet and call api if internet is working then call api other wise load data from local
     * *
     */
    private void checkInternetAndCallAPI() {
        llViewContainer.setVisibility(View.INVISIBLE);
        if (StaticUtils.isConnectingToInternet(this)) {

            callWeatherAPI();
        } else {
            Toast.showMsg(this, getString(R.string.internet_issue));

            String serverResponse = FileUtils.readResponseFromFile(this);
            parseServerResponse(serverResponse);
        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_btn_setting:

                Intent settingIntent = new Intent(DisplayWeatherActivity.this, SettingsActivity.class);
                startActivityForResult(settingIntent, REQUEST_SETTING);

                break;
            default:

        }

    }


    /**
     * This method is used to get weather api url
     * *
     */
    private String getWeatherApiKey() {
        WeatherAPI weatherAPI = new WeatherAPI(this);
        String cityName = sharedPrefUtils.getString(KEY_CITY_NAME);
        Log.d("City name: " + cityName);
        weatherAPI.setKey(WebserviceUrl.FREE_WEATHER_KEY);
        weatherAPI.setQ(cityName);
        weatherAPI.setFormat(WebserviceUrl.FORMATE_JSON);
        weatherAPI.setNum_of_days(WebserviceUrl.NO_OF_DAYS);

        return weatherAPI.createWeatherUrl();

    }

    /**
     * This method is used to call weather api using volly library
     */
    private void callWeatherAPI() {

        showProgress(getString(R.string.please_wait), null);
        String weatherUrl = getWeatherApiKey();

        Log.d("Weather url: " + weatherUrl);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, weatherUrl, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("weather Response : " + response.toString());
                parseServerResponse(response.toString());
                dismissProgress();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.showMsg(DisplayWeatherActivity.this, error.getMessage());

dismissProgress();

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);


    }


    /**
     * This method is used to parse server response
     * *
     */
    private void parseServerResponse(String serverResponse) {
        Weather weather = new Weather();
        try {

            JSONObject serverJsonObject = new JSONObject(serverResponse);
            JSONObject serverJsonData = serverJsonObject.getJSONObject("data");

            JSONObject currentCondiationData = serverJsonData.getJSONArray("current_condition").getJSONObject(0);

            weather.setCloudcover(currentCondiationData.getString("cloudcover"));
            weather.setFeelsLikeC(currentCondiationData.getString("FeelsLikeC"));
            weather.setFeelsLikeF(currentCondiationData.getString("FeelsLikeF"));
            weather.setHumidity(currentCondiationData.getString("humidity") );
            weather.setObservation_time(currentCondiationData.getString("observation_time"));
            weather.setPrecipMM(currentCondiationData.getString("precipMM"));
            weather.setPressure(currentCondiationData.getString("pressure"));
            weather.setTemp_C(currentCondiationData.getString("temp_C"));
            weather.setTemp_F(currentCondiationData.getString("temp_F"));
            weather.setVisibility(currentCondiationData.getString("visibility"));

            weather.setWeatherDesc(currentCondiationData.getJSONArray("weatherDesc").getJSONObject(0).getString("value"));
            weather.setWeatherIconUrl(currentCondiationData.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value"));


            weather.setWinddir16Point(currentCondiationData.getString("winddir16Point"));
            weather.setWinddirDegree(currentCondiationData.getString("winddirDegree"));
            weather.setWindspeedKmph(currentCondiationData.getString("windspeedKmph"));

            weather.setWindspeedMiles(currentCondiationData.getString("windspeedMiles"));


            JSONObject weatherData = serverJsonData.getJSONArray("weather").getJSONObject(0);
            JSONObject astronomyData = weatherData.getJSONArray("astronomy").getJSONObject(0);

            weather.setMoonrise(astronomyData.getString("moonrise"));
            weather.setMoonset(astronomyData.getString("moonset"));
            weather.setSunrise(astronomyData.getString("sunrise"));
            weather.setSunset(astronomyData.getString("sunset"));

            weather.setDate(weatherData.getString("date"));

            weather.setMaxtempC(weatherData.getString("maxtempC"));
            weather.setMintempC(weatherData.getString("mintempC"));

            weather.setMaxtempF(weatherData.getString("maxtempF"));
            weather.setMintempF(weatherData.getString("mintempF"));
            weather.setUvIndex(weatherData.getString("uvIndex"));


        } catch (JSONException e) {
            onException(e);
        }
        FileUtils.writeResponseInToFile(this, serverResponse);
        setAllDataOnView(weather);
    }

    /**
     * This method is used to display all weather data into layout (Textview)
     * *
     */
    private void setAllDataOnView(Weather weather) {

        llViewContainer.setVisibility(View.VISIBLE);
        tvCityName.setText(sharedPrefUtils.getString(KEY_CITY_NAME));
        ivWeatherIcon.setImageUrl(weather.getWeatherIconUrl(), VolleySingleton.getInstance(this).getImageLoader());

        String currentTemp = weather.getTemp_C() + "℃ | " + weather.getTemp_F() + "℉";

        tvCurrentTemp.setText(currentTemp);
        tvDate.setText(StaticUtils.getFormatedDate(this, weather.getDate()));
        tvDescription.setText(weather.getWeatherDesc());



        tvCloudCover.setText(weather.getCloudcover()+ "%");


        String feelsTemp = weather.getFeelsLikeC() + "℃ | " + weather.getFeelsLikeF() + "℉";

        tvFeelsLike.setText(feelsTemp);
        tvHumidity.setText(weather.getHumidity()+"%");
        tvPressure.setText(weather.getPressure() + " "+getString(R.string.mb));
        tvVisibility.setText(weather.getVisibility()+" "+getString(R.string.km));
        tvUVIndex.setText(weather.getUvIndex());
        String windSpeed = weather.getWindspeedKmph()+" "+getString(R.string.kmph)+" | "+weather.getWindspeedMiles()+" "+getString(R.string.miles);

        tvWindSpeed.setText(windSpeed);
        tvWindDir.setText(weather.getWinddir16Point());
        String minMaxTempC = weather.getMintempC() + "℃ ~ " + weather.getMaxtempC() + "℃";

        tvTempC.setText(minMaxTempC);

        String minMaxTempF = weather.getMintempF() + "℉ ~ " + weather.getMaxtempF() + "℉";
        tvTempF.setText(minMaxTempF);

        tvSunriseSet.setText(weather.getSunrise()+"/"+weather.getSunset());
        tvMoonriseSet.setText(weather.getMoonrise()+"/"+weather.getMoonset());

    }


}
