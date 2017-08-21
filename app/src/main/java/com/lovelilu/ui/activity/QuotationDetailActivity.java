package com.lovelilu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
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
public class QuotationDetailActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;
    @BindView(R.id.tv_quotation)
    TextView tv_quotation;
    private Anniversary mQuotation;

    @BindView(R.id.ll_quotation)
    LinearLayout ll_quotation;

    private Handler mHandler = new Handler();
    private FlakeView flakeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_detail);
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

        mQuotation = (Anniversary) getIntent().getSerializableExtra(Constants.KEY.QUOTATION);
        tv_quotation.setText(mQuotation.getQuotation());

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
        ll_quotation.addView(flakeView);
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
