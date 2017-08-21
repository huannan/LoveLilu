package com.lovelilu.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Anniversary;
import com.lovelilu.ui.activity.AnniversaryDetailActivity;
import com.lovelilu.ui.activity.QuotationDetailActivity;
import com.lovelilu.utils.DayTimeUtils;
import com.lovelilu.utils.ViewShakeUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huannan on 2016/8/25.
 */
public class AnnListAdapter extends BaseQuickAdapter<Anniversary> {

    public static final int TYPE_DAY = 0;
    public static final int TYPE_QUOTATION = 1;
    private final Context mCtx;


    public AnnListAdapter(Context context, int layoutResId, List<Anniversary> data) {
        super(layoutResId, data);
        mCtx = context;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final Anniversary bean) {

        baseViewHolder.setText(R.id.tv_like, bean.getLike() + "");
        baseViewHolder.getView(R.id.ll_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewShakeUtils.shakeView(baseViewHolder.getView(R.id.iv_like), true, 0);

                bean.setLike(bean.getLike() + 1);
                bean.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {

                        e.printStackTrace();

                        baseViewHolder.setText(R.id.tv_like, bean.getLike().toString());
                    }
                });
            }
        });


        switch (bean.getType()) {
            case Constants.ANN_TYPE.TYPE_DAY:

                baseViewHolder.getView(R.id.rl_bg_day).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.rl_bg_quotation).setVisibility(View.GONE);

                baseViewHolder.setText(R.id.tv_day, bean.getTitle());
                baseViewHolder.setText(R.id.tv_date, bean.getDate());

                int days = DayTimeUtils.getAnniversariesDaysBetween(bean.getTime());
                if (days == 0) {
                    baseViewHolder.setText(R.id.tv_num, R.string.today);
                } else {
                    baseViewHolder.setText(R.id.tv_num, days + mCtx.getResources().getString(R.string.day));
                }

                break;
            case Constants.ANN_TYPE.TYPE_QUOTATION:

                baseViewHolder.getView(R.id.rl_bg_day).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.rl_bg_quotation).setVisibility(View.VISIBLE);

                baseViewHolder.setText(R.id.tv_quotation, bean.getQuotation());
                break;
        }

        baseViewHolder.getView(R.id.ll_quotation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim(baseViewHolder.getView(R.id.rl_bg_quotation), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        Intent intent = new Intent(mCtx, QuotationDetailActivity.class);
                        intent.putExtra(Constants.KEY.QUOTATION, bean);
                        mCtx.startActivity(intent);

                    }

                });
            }
        });

        baseViewHolder.getView(R.id.rl_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim(baseViewHolder.getView(R.id.rl_bg_day), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        Intent intent = new Intent(mCtx, AnniversaryDetailActivity.class);
                        intent.putExtra(Constants.KEY.ANNIVERSARY, bean);
                        mCtx.startActivity(intent);

                    }

                });
            }
        });
    }

    //动画
    private void anim(View v, AnimatorListenerAdapter adapter) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                .setDuration(500);
        animator.addListener(adapter);
        animator.start();

    }
}
