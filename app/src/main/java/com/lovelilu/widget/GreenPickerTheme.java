package com.lovelilu.widget;

import android.content.Context;

import com.lovelilu.R;

import cn.aigestudio.datepicker.bizs.themes.DPTheme;

/**
 * Created by huannan on 2016/8/28.
 */
public class GreenPickerTheme extends DPTheme {

    private final Context mCtx;

    public GreenPickerTheme(Context context) {
        this.mCtx = context;
    }

    @Override
    public int colorBG() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorBGCircle() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorTitleBG() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorTitle() {
        return mCtx.getResources().getColor(R.color.white);
    }

    @Override
    public int colorToday() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorG() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorF() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorWeekend() {
        return mCtx.getResources().getColor(R.color.base_color);
    }

    @Override
    public int colorHoliday() {
        return mCtx.getResources().getColor(R.color.base_color);
    }
}
