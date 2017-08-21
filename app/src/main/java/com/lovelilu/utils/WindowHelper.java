package com.lovelilu.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by huannan on 2016/8/23.
 */
public class WindowHelper {

    /**
     * 设置背景的透明度
     *
     * @param acy     要设置的activity
     * @param bgAlpha 0.0-1.0（0-完全不透明，屏幕都黑了，1-完全可见）
     */
    public static void setBackgroundAlpha(Activity acy, float bgAlpha) {
        WindowManager.LayoutParams lp = acy.getWindow().getAttributes();
        lp.alpha = 1.0f - bgAlpha; //0.0-1.0
        acy.getWindow().setAttributes(lp);
    }

    public static void clearBackgroundAlpha(Activity acy) {
        setBackgroundAlpha(acy, 0);
    }
}
