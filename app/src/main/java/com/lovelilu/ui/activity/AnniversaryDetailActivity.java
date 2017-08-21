package com.lovelilu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Anniversary;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.widget.AppToolbar;
import com.nan.snowrain.FlakeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huannan on 2016/8/25.
 */
public class AnniversaryDetailActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;
    private Anniversary mAnniversary;

    private Handler mHandler = new Handler();
    private FlakeView flakeView;

    @BindView(R.id.rl_content)
    RelativeLayout rl_content;

    @BindView(R.id.tv_ann)
    TextView tv_ann;
    @BindView(R.id.tv_date)
    TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_detail);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        setTranslucent();
        initView();
        initBar();

//        initRain();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        mAnniversary = (Anniversary) getIntent().getSerializableExtra(Constants.KEY.ANNIVERSARY);
        tv_date.setText(mAnniversary.getDate());
        tv_ann.setText(mAnniversary.getTitle());

    }


    private void initBar() {

        toolbar.setOnLeftButtonClickListener(new AppToolbar.OnLeftButtonClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        toolbar.setRightButtonVisibility(false);

    }

    private void initRain() {

        flakeView = new FlakeView(this, 50);
        rl_content.addView(flakeView);
        mHandler.postDelayed(runnableRain, 0);
    }

    Runnable runnableRain = new Runnable() {

        @Override
        public void run() {
            flakeView.addFlakes(15);
            mHandler.postDelayed(runnableRain, 500);
            if (flakeView.getNumFlakes() > 70) {
                mHandler.removeCallbacks(runnableRain);
            }
        }
    };

    @Override
    protected void onDestroy() {

        mHandler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
