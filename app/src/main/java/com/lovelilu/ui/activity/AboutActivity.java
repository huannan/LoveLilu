package com.lovelilu.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.lovelilu.App;
import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.model.UpdateInfo;
import com.lovelilu.model.event.UpdateListEvent;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.ui.dialog.WarningDialog;
import com.lovelilu.utils.ApkUpdateUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.widget.AppToolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huannan on 2016/8/25.
 */
public class AboutActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.tv_app)
    TextView tv_app;

    private LoadingDialog mProgressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        setTranslucent();
        initView();
        initDialog();
        initBar();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        tv_app.setText(getResources().getString(R.string.app_name) + "\n" + App.getVersionName());

        //添加下划线
        tv_update.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_update.getPaint().setAntiAlias(true);//抗锯齿
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


    private void initDialog() {


        mProgressDialog = new LoadingDialog(this, R.style.NoTitleDialog);
        mProgressDialog.setLoadingDesc(getResources().getString(R.string.progress_feedback));
        mProgressDialog.setCancelable(false);

    }

    @OnClick({R.id.tv_update})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_update:

                checkForUpdate();
                break;
        }
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            //广告条切换
            mBanner.next();
            //两秒钟之后继续下一次的轮播
            mHandler.postDelayed(this, 2000);
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //处理些东西，这种一般用于一些预处理，每次有消息来都需要执行的代码
            //返回值代表是否拦截消息的下面写的handleMessage，从源码里面可以看出来
            return false;
        }
    }) {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    {

        Message msg = Message.obtain();
        //不需要我们程序员去回收
        msg.recycle();
        msg.recycle();


        mHandler.postDelayed()
        mHandler.post(run);

        mHandler.removeCallbacks(run);


    }

    private void checkForUpdate() {

        BmobQuery<UpdateInfo> query = new BmobQuery<>(UpdateInfo.class.getSimpleName());
        query.findObjects(new FindListener<UpdateInfo>() {
            @Override
            public void done(List<UpdateInfo> list, BmobException e) {
                if (e != null) {
                } else {

                    UpdateInfo info = list.get(0);
                    if (info.getVersionCode() > App.getVersionCode()) {

                        //有更新
                        ApkUpdateUtils.checkForUpdate(AboutActivity.this, info, new ApkUpdateUtils.OnUpdateListener() {
                            @Override
                            public void onUpdate() {
                                ToastUtils.showShort(AboutActivity.this, R.string.apk_download_ing);
                            }
                        });

                    } else {

                        ToastUtils.showShort(AboutActivity.this, R.string.is_new);

                    }


                }
            }
        });


    }
}
