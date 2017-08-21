package com.lovelilu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lovelilu.Constants;

public class PreferenceUtils {
    public static final String PREFERENCE_NAME = "love_info";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceUtils mPreferenceUtils;
    private static SharedPreferences.Editor editor;

    private PreferenceUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtils getInstance(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new PreferenceUtils(cxt);
        }
        editor = mSharedPreferences.edit();
        return mPreferenceUtils;
    }

    public void setTitle(String title) {
        editor.putString(Constants.KEY.TITLE, title);
        editor.commit();
    }

    public String getTitle() {
        return mSharedPreferences.getString(Constants.KEY.TITLE, "");
    }

    public void setContent(String content) {
        editor.putString(Constants.KEY.CONTENT, content);
        editor.commit();
    }

    public String getContent() {
        return mSharedPreferences.getString(Constants.KEY.CONTENT, "");
    }

    public void setDateTimes(int year, int month, int day) {
        editor.putInt(Constants.KEY.YEAR, year);
        editor.putInt(Constants.KEY.MONTH, month);
        editor.putInt(Constants.KEY.DAY, day);
        editor.commit();
    }

    public int[] getDateTimes() {
        int year = mSharedPreferences.getInt(Constants.KEY.YEAR, 2016);
        int month = mSharedPreferences.getInt(Constants.KEY.MONTH, 7);
        int day = mSharedPreferences.getInt(Constants.KEY.DAY, 5);
        int[] dateTime = {year, month, day};
        return dateTime;
    }


    public void setUserId(int id) {
        editor.putInt(Constants.KEY.USER_ID, id);
        editor.commit();
    }

    public int getUserId() {
        return mSharedPreferences.getInt(Constants.KEY.USER_ID, Constants.USER_ID.LU_BAO_BAO);
    }


}
