package com.lovelilu.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Anniversary;
import com.lovelilu.model.event.UpdateAnniversaryListEvent;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.widget.AppToolbar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddQuotationActivity extends SwipeBackActivity {


    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.et_quotation)
    EditText et_quotation;
    private LoadingDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quotation);

        init();
    }

    private void init() {

        ButterKnife.bind(this);
        setTranslucent();

        initToolbar();
        initDialog();

    }

    private void initToolbar() {

        toolbar.setOnRightButtonClickListener(new AppToolbar.OnRightButtonClickListener() {
            @Override
            public void onClick() {
                addAddQuotation();
            }
        });


        toolbar.setOnLeftButtonClickListener(new AppToolbar.OnLeftButtonClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    private void addAddQuotation() {

        String quotation = et_quotation.getText().toString();
        if (TextUtils.isEmpty(quotation)) {

            ToastUtils.showShort(this, R.string.add_quotation_err);
            return;
        }

        mProgressDialog.show();

        Anniversary a = new Anniversary();
        a.setType(Constants.ANN_TYPE.TYPE_QUOTATION);
        a.setQuotation(quotation);

        a.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //s是返回的ID
                    ToastUtils.showShort(mCtx, R.string.public_success);
                    EventBus.getDefault().post(new UpdateAnniversaryListEvent());
                    finish();
                } else {
                    ToastUtils.showShort(mCtx, R.string.public_failed);
                }

                mProgressDialog.dismiss();
            }
        });

    }

    private void initDialog() {

        mProgressDialog = new LoadingDialog(this, R.style.NoTitleDialog);
        mProgressDialog.setLoadingDesc(getResources().getString(R.string.progress_image_select));
        mProgressDialog.setCancelable(false);

    }
}
