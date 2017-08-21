package com.lovelilu.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by huannan on 2016/8/26.
 */
public class KeyBoardUtils {

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
