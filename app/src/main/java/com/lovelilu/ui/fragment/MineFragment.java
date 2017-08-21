package com.lovelilu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Like;
import com.lovelilu.model.User;
import com.lovelilu.model.event.UpdateListEvent;
import com.lovelilu.model.event.UpdateUserEvent;
import com.lovelilu.ui.activity.AboutActivity;
import com.lovelilu.ui.activity.FeedbackActivity;
import com.lovelilu.ui.activity.ImageDetailActivity;
import com.lovelilu.ui.activity.ScanActivity;
import com.lovelilu.ui.activity.UserActivity;
import com.lovelilu.ui.dialog.WarningDialog;
import com.lovelilu.ui.fragment.base.BaseFragment;
import com.lovelilu.utils.PreferenceUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.utils.ViewShakeUtils;
import com.lovelilu.widget.AppToolbar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huannan on 2016/11/26.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;
    @BindView(R.id.iv_blur)
    ImageView ivBlur;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tv_like)
    TextView tv_like;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.rl_3)
    RelativeLayout rl3;
    @BindView(R.id.rl_4)
    RelativeLayout rl4;
    @BindView(R.id.rl_5)
    RelativeLayout rl5;
    @BindView(R.id.rl_6)
    RelativeLayout rl6;
    @BindView(R.id.rl_7)
    RelativeLayout rl7;
    @BindView(R.id.iv_5)
    ImageView iv_5;
    @BindView(R.id.gif_good)
    GifImageView gif_good;

    private WarningDialog warningDialog;
    private List<User> mUserList;
    private Like mLike;

    private int curUserId;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private WarningDialog selectUserDialog;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);

        initUser();

        initLike();
        initDialog();
    }

    private void initDialog() {

        warningDialog = new WarningDialog(getActivity(), R.style.NoTitleDialog);
        warningDialog.setWarning(getResources().getString(R.string.exit_warning));
        warningDialog.setSureText(getResources().getString(R.string.exit_sure));
        warningDialog.setCancelText(getResources().getString(R.string.exit_cancel));
        warningDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningDialog.dismiss();

                System.exit(0);

            }
        });
        warningDialog.setCanelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog.dismiss();
            }
        });

        //选择用户
        selectUserDialog = new WarningDialog(getActivity(), R.style.NoTitleDialog);
        selectUserDialog.setTitle(getResources().getString(R.string.select_user_title));
        selectUserDialog.setWarning(getResources().getString(R.string.select_user_warning));
        selectUserDialog.setSureText(getResources().getString(R.string.nanbaobao));
        selectUserDialog.setCancelText(getResources().getString(R.string.lubaobao));
        selectUserDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUserDialog.dismiss();
                curUserId = 1;
                PreferenceUtils.getInstance(getActivity()).setUserId(curUserId);
                showUser(curUserId);
            }
        });
        selectUserDialog.setCanelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserDialog.dismiss();
                curUserId = 0;
                PreferenceUtils.getInstance(getActivity()).setUserId(curUserId);
                showUser(curUserId);
            }
        });

    }

    private void initLike() {

        try {
            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
            // 将gif图资源转化为GifDrawable
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.weini_good);
            // gif1加载一个动态图gif
            gif_good.setImageDrawable(gifDrawable);
            // 如果是普通的图片资源，就像Android的ImageView set图片资源一样简单设置进去即可。
            // gif2加载一个普通的图片（如png，bmp，jpeg等等）
//            gif_weini.setImageResource(R.drawable.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_like.setVisibility(View.GONE);

        BmobQuery<Like> query = new BmobQuery<>(Like.class.getSimpleName());
        query.findObjects(new FindListener<Like>() {
            @Override
            public void done(List<Like> list, BmobException e) {
                if (e != null) {
                } else {

                    if (list != null) {
                        if (list.size() != 0) {
                            mLike = list.get(0);
                            tv_like.setVisibility(View.VISIBLE);
                            tv_like.setText(mLike.getLike() + "次");
                        }
                    }

                }
            }
        });

    }

    private void initUser() {

        curUserId = PreferenceUtils.getInstance(getActivity()).getUserId();

        BmobQuery<User> query = new BmobQuery<>(User.class.getSimpleName());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e != null) {
                    ToastUtils.showShort(getActivity(), R.string.http_failed);
                } else {
                    mUserList = list;

                    showUser(curUserId);
                }
            }
        });
    }

    private void showUser(int userId) {

        if (mUserList != null) {
            User user = mUserList.get(userId);
            Picasso.with(getActivity()).load(user.getAvatarUrl()).into(ivAvatar);
            Picasso.with(getActivity()).load(user.getAvatarBlurUrl()).into(ivBlur);

            tvName.setText(user.getName());
            tvSignature.setText(user.getSign());

            if (userId == 0) {
                ivGender.setImageResource(R.drawable.icon_girl);
            } else {
                ivGender.setImageResource(R.drawable.icon_boy);
            }
        }
    }

    @OnClick({R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5, R.id.rl_6, R.id.rl_7, R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_1:

                //选择用户
                selectUserDialog.show();

                break;
            case R.id.rl_2:

                Intent per = new Intent(getActivity(), UserActivity.class);
                startActivity(per);

                break;
            case R.id.rl_3:

                Intent scan = new Intent(getActivity(), ScanActivity.class);
                startActivity(scan);

                break;
            case R.id.rl_4:

                Intent feedback = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(feedback);

                break;
            case R.id.rl_5:

                updateLike();


                break;
            case R.id.rl_6:

                //关于
                Intent about = new Intent(getActivity(), AboutActivity.class);
                startActivity(about);

                break;
            case R.id.rl_7:

                //退出APP警告
                warningDialog.show();


                break;

            case R.id.iv_avatar:

                if (mUserList != null) {
                    Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
                    intent.putExtra(Constants.KEY.IMAGE_URL, mUserList.get(curUserId).getAvatarBigUrl());
                    startActivity(intent);
                }

                break;
        }
    }

    private void updateLike() {

/*      Like l = new Like();
        l.setLike(1);
        l.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });*/

        gif_good.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gif_good.setVisibility(View.GONE);
            }
        }, 1500);


        ViewShakeUtils.shakeView(iv_5, true, 0);

        if (mLike != null) {
            mLike.setLike(mLike.getLike() + 1);
            mLike.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    e.printStackTrace();
                    tv_like.setText(mLike.getLike() + "次");
                }
            });
        }

    }


    @Subscribe
    public void onEventMainThread(UpdateUserEvent event) {
        int userId = PreferenceUtils.getInstance(getActivity()).getUserId();
        curUserId = userId;
        showUser(userId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }
}

