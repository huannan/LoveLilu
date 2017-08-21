package com.lovelilu.utils;

import android.widget.TextView;

import com.lovelilu.R;

/**
 * Created by huannan on 2016/8/26.
 */
public class WeatherUtils {

    public static void updateBg(TextView tvWeather, String weather) {
        if (weather.contains("晴")) {
            tvWeather.setBackgroundResource(R.drawable.shape_yellow);
        }

        if (weather.contains("雨")) {
            tvWeather.setBackgroundResource(R.drawable.shape_blue);
        }

        if (weather.contains("阴")) {
            tvWeather.setBackgroundResource(R.drawable.shape_grey);
        }

        if (weather.contains("云")) {
            tvWeather.setBackgroundResource(R.drawable.shape_purple);
        }
    }

}
