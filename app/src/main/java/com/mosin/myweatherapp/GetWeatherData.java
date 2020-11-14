package com.mosin.myweatherapp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.mosin.myweatherapp.model.WeatherRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class GetWeatherData {
    private final GetUrlData getUrlData = new GetUrlData();
    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String API_KEY = "aa25fd58d80ade7e1ffefabb192d8fc9";
    private static String temperatureValue;
    private String pressureText;
    private String humidityStr;
    private String windSpeedStr;
    private String icoView;

    public void getData() throws MalformedURLException {
        final URL uri = new URL(WEATHER_URL + "Сургут" + "&units=metric&appid=" + API_KEY);
        getUrlData.getData(uri);
        final android.os.Handler handler = new Handler(Objects.requireNonNull(Looper.myLooper()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = getUrlData.getData(uri);
                Gson gson = new Gson();
                final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayWeather(weatherRequest);
                    }
                });
            }
        }).start();
    }

    private void displayWeather(WeatherRequest weatherRequest) {
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