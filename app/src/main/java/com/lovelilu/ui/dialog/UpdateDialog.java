package com.lovelilu.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lovelilu.R;

public class UpdateDialog extends Dialog {

    private final TextView tv_content;
    private final Button btn_update;
    private final TextView tv_cancel;

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_update);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_update = (Button) findViewById(R.id.btn_update);

        tv_cancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_cancel.getPaint().setAntiAlias(true);//抗锯齿
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }


    public void setUpdateButtonListener(final View.OnClickListener listener) {
        btn_update.setOnClickListener(listener);
    }

}
