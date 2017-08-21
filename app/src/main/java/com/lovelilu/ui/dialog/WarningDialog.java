package com.lovelilu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovelilu.R;

public class WarningDialog extends Dialog {
    private final LinearLayout ll_sure;
    private TextView tv_warning, tv_title, tv_cancel, tv_sure;
    private final View v_dividel;

    public WarningDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.dialog_warning);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_warning = (TextView) findViewById(R.id.tv_warning);
        tv_title = (TextView) findViewById(R.id.tv_title);
        v_dividel = findViewById(R.id.v_dividel);
        ll_sure = (LinearLayout)findViewById(R.id.ll_sure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCanelListener(final View.OnClickListener listener) {

        tv_cancel.setOnClickListener(listener);

//        rv_cancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
//            @Override
//            public void onComplete(RippleView rippleView) {
//                listener.onClick(tv_cancel);
//            }
//        });


    }

    public void setSureListener(final View.OnClickListener listener) {
        tv_sure.setOnClickListener(listener);

//        rv_sure.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
//            @Override
//            public void onComplete(RippleView rippleView) {
//                listener.onClick(tv_sure);
//            }
//        });
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setWarning(String warning) {
        tv_warning.setText(warning);
    }

    public void setCancelText(String text) {
        tv_cancel.setText(text);
    }

    public void setSureText(String text) {
        tv_sure.setText(text);
    }

    public void setSureBtnVisibility(int visibility) {
        ll_sure.setVisibility(visibility);
        v_dividel.setVisibility(visibility);
    }
}
