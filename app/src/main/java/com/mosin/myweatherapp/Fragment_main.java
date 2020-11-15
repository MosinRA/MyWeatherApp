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
import com.mosin.myweatherapp.interfaces.IOpenWeatherMap;
import com.mosin.myweatherapp.model.WeatherRequest;
import com.squareup.picasso.Picasso;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_main extends Fragment {
    private IOpenWeatherMap openWeather;
    private final String WEATHER_URL = "https://api.openweathermap.org/";
    private final String API_KEY = "762ee61f52313fbd10a4eb54ae4d4de2";
    private final String MERRIC = "metric";
    private TextView showTempView, showWindSpeed, showPressure, showHumidity, cityName, dateNow;
    private ImageView icoWeather, pic;
    SharedPreferences sharedPreferences;
    private String cityChoice, icoView;
    private boolean wind, pressure, humidity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        findView(view);
        initSettingSwitch();
        initRetorfit();
        requestRetrofit(cityChoice, MERRIC, API_KEY);
        setPic();
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
        pic = view.findViewById(R.id.picas_pic);
    }

    public void initSettingSwitch() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        wind = sharedPreferences.getBoolean("Wind", false);
        pressure = sharedPreferences.getBoolean("Pressure", false);
        humidity = sharedPreferences.getBoolean("Humidity", false);
        cityChoice = sharedPreferences.getString("cityName", cityChoice);
        cityName.setText(cityChoice);
    }

    private void initRetorfit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_URL) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        openWeather = retrofit.create(IOpenWeatherMap.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    private void requestRetrofit(String city, String metric, String keyApi) {
        openWeather.loadWeather(city, metric, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null) {
                            String temperatureValue = String.format(Locale.getDefault(), "%.1f °", response.body().getMain().getTemp());
                            String windSpeedStr = String.format(Locale.getDefault(), "%.0f", response.body().getWind().getSpeed());
                            String pressureText = String.format(Locale.getDefault(), "%.0f", response.body().getMain().getPressure());
                            String humidityStr = String.format(Locale.getDefault(), "%d", response.body().getMain().getHumidity());
                            icoView = response.body().getWeather()[0].getIcon();

                            showTempView.setText(temperatureValue);
                            if (wind) {
                                showWindSpeed.setText(String.format("%s %s м/с", getResources().getString(R.string.wind_speed), windSpeedStr));
                            } else {
                                showWindSpeed.setVisibility(View.GONE);
                            }
                            if (pressure) {
                                showPressure.setText(String.format("%s %s мм рт.ст.", getResources().getString(R.string.pressure), pressureText));
                            } else {
                                showPressure.setVisibility(View.GONE);
                            }
                            if (humidity) {
                                showHumidity.setText(String.format("%s %s %%", getResources().getString(R.string.humidity), humidityStr));
                            } else {
                                showHumidity.setVisibility(View.GONE);
                            }
                            setIcoViewImage();
                        } else if (response.code() == 404) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(R.string.exclamation)
                                    .setMessage(R.string.msg_to_er_url)
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .setPositiveButton(R.string.ok_button, null);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        cityName.setText("");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.exclamation)
                                .setMessage(R.string.msg_to_er_internet)
                                .setIcon(R.mipmap.ic_launcher_round)
                                .setPositiveButton(R.string.ok_button, null);
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
    }

    private void setIcoViewImage() {
        if (icoView.equals("01d")) {
            icoWeather.setImageResource(R.drawable.clear_sky_d);
        } else if (icoView.equals("01n")) {
            icoWeather.setImageResource(R.drawable.clear_sky_n);
        } else if (icoView.equals("02d") || icoView.equals("03d") || icoView.equals("04d")) {
            icoWeather.setImageResource(R.drawable.few_clouds_d);
        } else if (icoView.equals("02n") || icoView.equals("03n") || icoView.equals("04n")) {
            icoWeather.setImageResource(R.drawable.few_clouds_n);
        } else if (icoView.equals("09d") || icoView.equals("10d")) {
            icoWeather.setImageResource(R.drawable.rain_d);
        } else if (icoView.equals("09n") || icoView.equals("10n")) {
            icoWeather.setImageResource(R.drawable.rain_n);
        } else if (icoView.equals("13n") || icoView.equals("13d")) {
            icoWeather.setImageResource(R.drawable.snow);
        } else if (icoView.equals("50n") || icoView.equals("50d")) {
            icoWeather.setImageResource(R.drawable.mist);
        }
    }

    private void dateInit() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        dateNow.setText(dateText);
    }

    private void setPic() {
        Picasso.get()
                .load("https://www.dorogavrim.ru/img/flagi/goroda/flag_" + cityChoice.toLowerCase() + ".jpg")
                .into(pic);
    }
}
