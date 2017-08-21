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
import com.lovelilu.App;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.model.User;
import com.lovelilu.ui.activity.DiaryDetailActivity;
import com.lovelilu.utils.ShareUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.utils.ViewShakeUtils;
import com.lovelilu.utils.WeatherUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huannan on 2016/8/25.
 */
public class DiaryListAdapter extends BaseQuickAdapter<Diary> {

    private final Context mCtx;
    private final List<User> mUserList;


    public DiaryListAdapter(Context context, int layoutResId, List<Diary> data) {
        super(layoutResId, data);
        mCtx = context;
        mUserList = App.getUserList();
    }

    @Override
    protected void convert(final com.chad.library.adapter.base.BaseViewHolder baseViewHolder, final Diary diary) {

        baseViewHolder.setText(R.id.tv_name, mUserList.get(diary.getUserId()).getName());
        ImageView iv_avatar = baseViewHolder.getView(R.id.iv_avatar);
        Picasso.with(mCtx).load(mUserList.get(diary.getUserId()).getAvatarUrl()).into(iv_avatar);

        baseViewHolder.setText(R.id.tv_title, diary.getTitle());

        if (diary.getContent().length() < 100) {
            baseViewHolder.setText(R.id.tv_content, diary.getContent());
        } else {
            baseViewHolder.setText(R.id.tv_content, diary.getContent().substring(0, 100));
        }

        String[] s = diary.getCreatedAt().split(" ");
        baseViewHolder.setText(R.id.tv_time, diary.getDate());

        baseViewHolder.setText(R.id.tv_like, diary.getLike() + "");
        baseViewHolder.setText(R.id.tv_weather, diary.getWeather());
//        WeatherUtils.updateBg((TextView) baseViewHolder.getView(R.id.tv_weather), diary.getWeather());

        CircleImageView iv_image = baseViewHolder.getView(R.id.iv_image);
        if (TextUtils.isEmpty(diary.getImageUrl())) {
            iv_image.setVisibility(View.GONE);
        } else {
            Picasso.with(mCtx).load(diary.getImageUrl()).into(iv_image);
            iv_image.setVisibility(View.VISIBLE);
        }

        baseViewHolder.getView(R.id.rl_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim(baseViewHolder.getView(R.id.rl_bg), new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Intent intent = new Intent(mCtx, DiaryDetailActivity.class);
                        intent.putExtra(Constants.KEY.DIARY, diary);
                        mCtx.startActivity(intent);
                    }
                });
            }
        });

        baseViewHolder.getView(R.id.ll_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewShakeUtils.shakeView(baseViewHolder.getView(R.id.iv_like), true, 0);

                diary.setLike(diary.getLike() + 1);
                diary.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        baseViewHolder.setText(R.id.tv_like, diary.getLike() + "");
                    }
                });
            }
        });

        //分享
        ImageView iv_share = baseViewHolder.getView(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.showShare(mCtx, diary.getTitle(), diary.getContent(), diary.getImageUrl());
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
