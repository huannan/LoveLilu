package com.lovelilu.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.model.Photo;
import com.lovelilu.ui.activity.DiaryDetailActivity;
import com.lovelilu.ui.activity.ImageDetailActivity;
import com.lovelilu.utils.ViewShakeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huannan on 2016/8/25.
 */
public class PhotoListAdapter extends BaseQuickAdapter<Photo> {

    private final Context mCtx;


    public PhotoListAdapter(Context context, int layoutResId, List<Photo> data) {
        super(layoutResId, data);
        mCtx = context;
    }

    @Override
    protected void convert(final com.chad.library.adapter.base.BaseViewHolder baseViewHolder, final Photo bean) {

        baseViewHolder.setText(R.id.tv_desc, bean.getDesc());

        TextView tv_time = baseViewHolder.getView(R.id.tv_time);
        String date = bean.getDate();
        if (TextUtils.isEmpty(date)) {
            tv_time.setVisibility(View.GONE);
        } else {
            tv_time.setVisibility(View.VISIBLE);
            tv_time.setText(bean.getDate());
        }

        ImageView iv_photo = baseViewHolder.getView(R.id.iv_photo);
        Picasso.with(mCtx).load(bean.getSmallImageUrl()).into(iv_photo);

        baseViewHolder.getView(R.id.rl_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim(baseViewHolder.getView(R.id.rl_bg), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        Intent intent = new Intent(mCtx, ImageDetailActivity.class);
                        intent.putExtra(Constants.KEY.IMAGE_URL, bean.getImageUrl());
                        intent.putExtra(Constants.KEY.IMAGE_DESC, bean.getDesc());

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
