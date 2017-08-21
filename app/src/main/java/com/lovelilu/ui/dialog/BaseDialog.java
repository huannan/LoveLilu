package com.lovelilu.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.lovelilu.R;


/**
 * Created by huannan on 2016/7/14.
 * 对话框的基类
 */
public abstract class BaseDialog extends AlertDialog implements View.OnClickListener {

    protected BaseDialog(Context context) {
        // 通过父类的构造函数统一设置对话框的背景
        super(context, R.style.DialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        init();
    }

    public abstract void findView();

    public abstract void init();

    public abstract void processClick(View v);

    @Override
    public void onClick(View view) {
        processClick(view);
    }
}
