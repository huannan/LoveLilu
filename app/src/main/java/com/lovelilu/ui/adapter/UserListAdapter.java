package com.lovelilu.ui.adapter;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.User;
import com.lovelilu.model.event.UpdateUserEvent;
import com.lovelilu.ui.activity.UserActivity;
import com.lovelilu.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by huannan on 2016/8/25.
 */
public class UserListAdapter extends BaseQuickAdapter<User> {

    private final Context mCtx;
    private List<User> mUserList;


    public UserListAdapter(Context context, int layoutResId, List<User> data) {
        super(layoutResId, data);
        mCtx = context;
    }

    @Override
    protected void convert(final com.chad.library.adapter.base.BaseViewHolder baseViewHolder, final User bean) {


        ImageView iv_avatar = baseViewHolder.getView(R.id.iv_avatar);
        ImageView iv_blur = baseViewHolder.getView(R.id.iv_blur);
        ImageView iv_gender = baseViewHolder.getView(R.id.iv_gender);
        Picasso.with(mCtx).load(bean.getAvatarUrl()).into(iv_avatar);
        Picasso.with(mCtx).load(bean.getAvatarBlurUrl()).into(iv_blur);

        baseViewHolder.setText(R.id.tv_name, "大名：" + bean.getName());
        baseViewHolder.setText(R.id.tv_signature, "个性签名：" + bean.getSign());

        if (bean.getUserId() == Constants.USER_ID.LU_BAO_BAO) {
            iv_gender.setImageResource(R.drawable.icon_girl);
        } else {
            iv_gender.setImageResource(R.drawable.icon_boy);
        }


        baseViewHolder.getView(R.id.rl_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.getInstance(mCtx).setUserId(bean.getUserId());
                EventBus.getDefault().post(new UpdateUserEvent());

                UserActivity activity = (UserActivity) mContext;
                activity.finish();
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
