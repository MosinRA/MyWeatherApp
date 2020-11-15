package com.mosin.myweatherapp;

import android.util.Log;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetUrlData {
    private String result;


    private boolean errConnection;

    String getData(URL uri) {
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные
            String result2 = getLines(in);
            result = result2;
        }
        catch (RuntimeException e) {
            Log.e("D", "Null exception", e);
            e.printStackTrace();
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        } catch (FileNotFoundException e) {
            Log.e("D", "Fail URL", e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("D", "Fail connection", e);
            e.printStackTrace();
            errConnection = true;
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

    public void setErrConnection(boolean errConnection) {
        this.errConnection = errConnection;
    }

    public boolean isErrConnection() {
        return errConnection;
    }
}

