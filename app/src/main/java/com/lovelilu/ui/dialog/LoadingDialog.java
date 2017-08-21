package com.lovelilu.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovelilu.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LoadingDialog extends Dialog {

    private final GifImageView gif_loading;
    private final TextView tv_loadDesc;

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
        setContentView(R.layout.dialog_loading);
        tv_loadDesc = (TextView) findViewById(R.id.tv_loadDesc);
        gif_loading = (GifImageView) findViewById(R.id.gif_loading);

        setCanceledOnTouchOutside(false);

        try {
            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
            // 将gif图资源转化为GifDrawable
            GifDrawable gifDrawable = new GifDrawable(context.getResources(), R.drawable.weini_dialog);
            // gif1加载一个动态图gif
            gif_loading.setImageDrawable(gifDrawable);
            // 如果是普通的图片资源，就像Android的ImageView set图片资源一样简单设置进去即可。
            // gif2加载一个普通的图片（如png，bmp，jpeg等等）
//            gif_weini.setImageResource(R.drawable.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setLoadingDesc(String desc) {
        tv_loadDesc.setText(desc);
    }

}
