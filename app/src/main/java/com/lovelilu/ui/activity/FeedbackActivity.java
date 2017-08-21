package com.lovelilu.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lovelilu.R;
import com.lovelilu.model.Feedback;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.utils.DayTimeUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.widget.AppToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by huannan on 2016/8/25.
 */
public class FeedbackActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.btn_submit)
    Button btn_submit;

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
        setContentView(R.layout.activity_feedback);
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

    private void addFeedback() {

        String title = et_title.getText().toString().trim();
        String content = et_content.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            ToastUtils.showShort(this, R.string.feedback_title_empty);
            return;
        }

        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort(this, R.string.feedback_content_empty);
            return;
        }

        mProgressDialog.show();

        Feedback feedback = new Feedback();
        feedback.setDate(DayTimeUtils.getDate());
        feedback.setTitle(title);
        feedback.setContent(content);


        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        finish();
                    }
                }, 1500);

            }
        });

    }

    private void initDialog() {

        mProgressDialog = new LoadingDialog(this, R.style.NoTitleDialog);
        mProgressDialog.setLoadingDesc(getResources().getString(R.string.progress_feedback));
        mProgressDialog.setCancelable(false);

    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                //发表反馈
                addFeedback();

                break;
        }
    }
}
