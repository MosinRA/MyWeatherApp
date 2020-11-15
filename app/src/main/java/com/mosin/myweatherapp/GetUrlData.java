package com.mosin.myweatherapp;


import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class GetUrlData {
    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String API_KEY = "762ee61f52313fbd10a4eb54ae4d4de2";
    private String result;

    String getData() throws MalformedURLException {
        final URL uri = new URL("https://api.openweathermap.org/data/2.5/weather?q=Surgut&units=metric&appid=762ee61f52313fbd10a4eb54ae4d4de2");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные
                    String result2 = getLines(in);
                    result = result2;


                } catch (FileNotFoundException e) {
                    Log.e("D", "Fail URL", e);
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("D", "Fail connection", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
//            }
//        }).start();
        return result;
    }

    private String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData.toString();
    }
}

