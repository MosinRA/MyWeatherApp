package com.mosin.myweatherapp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.mosin.myweatherapp.model.WeatherRequest;

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Objects;

public class GetWeatherData {
    private final GetUrlData getUrlData = new GetUrlData();

    private static String temperatureValue;
    private static String pressureText;
    private static String humidityStr;
    private static String windSpeedStr;
    private static String icoView;


    public void getData() {
        final android.os.Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                final String result = getUrlData.getData();
                Gson gson = new Gson();
                final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayWeather(weatherRequest);
                    }
                });
            } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }}).start();
    }

    public void displayWeather(WeatherRequest weatherRequest) {
        temperatureValue = String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp());
        pressureText = String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getPressure());
        humidityStr = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getHumidity());
        windSpeedStr = String.format(Locale.getDefault(), "%.0f", weatherRequest.getWind().getSpeed());
        icoView = weatherRequest.getWeather()[0].getIcon();

    }

    public String getIcoView() {
        return icoView;
    }

    public String getTemperatureValue() {
        return temperatureValue;
    }

    public String getPressureText() {
        return pressureText;
    }

    public String getHumidityStr() {
        return humidityStr;
    }

    public String getWindSpeedStr() {
        return windSpeedStr;
    }
}