package com.lovelilu.ui.dialog;

import android.content.Context;
import android.view.View;

import com.dd.CircularProgressButton;
import com.lovelilu.R;

/**
 * Created by huannan on 2016/8/26.
 */
public class WeatherDialog extends BaseDialog {

    private Context mCtx;
    private View view_root;
    private CircularProgressButton btn_pro;

    private OnWeatherListener mListener;
    private final String weather;

    protected WeatherDialog(Context context, String lastWeather, OnWeatherListener listener) {
        super(context);

        weather = lastWeather;
        mListener = listener;
        mCtx = context;
    }

    @Override
    public void findView() {
        view_root = View.inflate(mCtx, R.layout.dialog_edit_weather, null);

//        btn_pro = (CircularProgressButton) view_root.findViewById(R.id.btn_pro);
    }

    @Override
    public void init() {

    }

    @Override
    public void processClick(View v) {

    }

    interface OnWeatherListener {
        void onChange(String weather);
    }

    public static void show(Context context, String lastWeather, OnWeatherListener listener) {

        WeatherDialog dialog = new WeatherDialog(context, lastWeather, listener);
        dialog.show();

    }
}
