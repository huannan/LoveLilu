package com.lovelilu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelilu.R;
import com.lovelilu.ui.activity.base.BaseActivity;
import com.lovelilu.utils.DayTimeUtils;
import com.nan.snowrain.FlakeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.rl_blur)
    RelativeLayout rlBlur;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;

    private Handler mHandler = new Handler();
    private FlakeView flakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        setTranslucent();

        tvDay.setText(DayTimeUtils.getDays(this) + "");

        initRain();

//        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
//        animation.setDuration(1000);
//        animation.setInterpolator(new AccelerateInterpolator());
        rlBlur.setVisibility(View.VISIBLE);
//        rlBlur.startAnimation(animation);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);



    }

    private void initRain() {

        flakeView = new FlakeView(this , 100);
        ll_content.addView(flakeView);
        mHandler.postDelayed(runnableRain, 0);
    }

    Runnable runnableRain = new Runnable() {

        @Override
        public void run() {
            flakeView.addFlakes(15);
            mHandler.postDelayed(runnableRain, 500);
            if(flakeView.getNumFlakes() > 70)
            {
                mHandler.removeCallbacks(runnableRain);
            }
        }
    };
}
