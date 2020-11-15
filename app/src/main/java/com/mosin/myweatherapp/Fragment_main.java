package com.mosin.myweatherapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.mosin.myweatherapp.model.WeatherRequest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class Fragment_main extends Fragment {
    final GetWeatherData getWeatherData = new GetWeatherData();
    private TextView showTempView, showWindSpeed, showPressure, showHumidity, cityName, dateNow;
    private ImageView icoWeather;
    SharedPreferences sharedPreferences;
    private String cityChoice, temperatureValue, pressureText, humidityStr, windSpeedStr;
    private boolean wind, pressure, humidity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getWeatherData.getData();
        findView(view);
        initSettingSwitch();
        displayWeather();
        dateInit();
    }

    public void findView(View view) {
        showTempView = view.findViewById(R.id.showTempViewFragmentShowCityInfo);
        showWindSpeed = view.findViewById(R.id.windSpeedView);
        showPressure = view.findViewById(R.id.pressureView);
        showHumidity = view.findViewById(R.id.humidityView);
        icoWeather = view.findViewById(R.id.weatherIcoView);
        cityName = view.findViewById(R.id.cityNameView);
        dateNow = view.findViewById(R.id.date_view);
    }

    public void initSettingSwitch() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        wind = sharedPreferences.getBoolean("Wind", false);
        pressure = sharedPreferences.getBoolean("Pressure", false);
        humidity = sharedPreferences.getBoolean("Humidity", false);
        cityChoice = sharedPreferences.getString("cityName", cityChoice);
        cityName.setText(cityChoice);
    }

    private void displayWeather(){

        getWeatherData.getData();

        showTempView.setText(String.format("%s °", getWeatherData.getTemperatureValue()));
        if (wind) {
            showWindSpeed.setText(String.format("%s %s м/с", getResources().getString(R.string.wind_speed), getWeatherData.getWindSpeedStr()));
        } else {
            showWindSpeed.setVisibility(View.GONE);
        }
        if (pressure) {
            showPressure.setText(String.format("%s %s мб", getResources().getString(R.string.pressure), getWeatherData.getPressureText()));
        } else {
            showPressure.setVisibility(View.GONE);
        }
        if (humidity) {
            showHumidity.setText(String.format("%s %s %%", getResources().getString(R.string.humidity), getWeatherData.getHumidityStr()));
        } else {
            showHumidity.setVisibility(View.GONE);
        }
//        setIcoViewImage();
    }
//
//    private void setIcoViewImage() {
//        String icoView = getWeatherData.getIcoView();
//        if (icoView.equals("01d")) {
//            icoWeather.setImageResource(R.drawable.clear_sky_d);
//        } else if (icoView.equals("01n")) {
//            icoWeather.setImageResource(R.drawable.clear_sky_n);
//        } else if (icoView.equals("02d") || icoView.equals("03d") || icoView.equals("04d")) {
//            icoWeather.setImageResource(R.drawable.few_clouds_d);
//        } else if (icoView.equals("02n") || icoView.equals("03n") || icoView.equals("04n")) {
//            icoWeather.setImageResource(R.drawable.few_clouds_n);
//        } else if (icoView.equals("09d") || icoView.equals("10d")) {
//            icoWeather.setImageResource(R.drawable.rain_d);
//        } else if (icoView.equals("09n") || icoView.equals("10n")) {
//            icoWeather.setImageResource(R.drawable.rain_n);
//        } else if (icoView.equals("13n") || icoView.equals("13d")) {
//            icoWeather.setImageResource(R.drawable.snow);
//        } else if (icoView.equals("50n") || icoView.equals("50d")) {
//            icoWeather.setImageResource(R.drawable.mist);
//        }
//    }

    private void dateInit() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        dateNow.setText(dateText);
    }
}