package com.lovelilu.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Anniversary;
import com.lovelilu.model.event.UpdateAnniversaryListEvent;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.utils.DayTimeUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.widget.AppToolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddAnniversaryActivity extends SwipeBackActivity {


    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.et_day)
    EditText et_day;
    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.btn_time)
    CuboidButton btn_time;

    private LoadingDialog mProgressDialog;
    private String mSelectDate;
    private String mDate = "";
    private long mTimeInMillis = Calendar.getInstance().getTimeInMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anniversary);

        init();
    }

    private void init() {

        ButterKnife.bind(this);
        setTranslucent();

        initToolbar();
        initDialog();

        Calendar c = Calendar.getInstance();
        tv_date.setText(DayTimeUtils.getDate());
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

        String title = et_day.getText().toString();
        if (TextUtils.isEmpty(title)) {

            ToastUtils.showShort(this, R.string.add_anniversary_err_title);
            return;
        }

        if (TextUtils.isEmpty(mDate)) {

            ToastUtils.showShort(this, R.string.add_anniversary_err_date);
            return;
        }

        mProgressDialog.show();

        Anniversary a = new Anniversary();
        a.setType(Constants.ANN_TYPE.TYPE_DAY);
        a.setTitle(title);
        a.setDate(mDate);
        a.setTime(mTimeInMillis);

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


    @OnClick(R.id.btn_time)
    public void openDateSelect() {

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        DatePicker picker = new DatePicker(this);

        if (TextUtils.isEmpty(mSelectDate)) {
            picker.setDate(DayTimeUtils.getYear(), DayTimeUtils.getMon());
        } else {
            String[] info = mSelectDate.split("-");
            picker.setDate(Integer.valueOf(info[0]), Integer.valueOf(info[1]));
        }


        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                mSelectDate = date;
                dialog.dismiss();
                String[] info = mSelectDate.split("-");
                Calendar c = Calendar.getInstance();
                c.set(Integer.valueOf(info[0]), Integer.valueOf(info[1]) - 1, Integer.valueOf(info[2]));

                //String dis = DayTimeUtils.getAnniversariesDaysBetween(c.getTimeInMillis()) + "天";

                mTimeInMillis = c.getTimeInMillis();
                mDate = info[0] + "年"
                        + info[1] + "月"
                        + info[2] + "日";

                tv_date.setText(mDate);
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
}
