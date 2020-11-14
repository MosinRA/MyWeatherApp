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

    private String result;

    String getData(URL uri) {
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
            result = getLines(in);

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

