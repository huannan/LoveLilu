package com.lovelilu.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.model.event.UpdateListEvent;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.ui.dialog.WarningDialog;
import com.lovelilu.utils.WeatherUtils;
import com.lovelilu.widget.AppToolbar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by huannan on 2016/8/25.
 */
public class DiaryDetailActivity extends SwipeBackActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.iv_image)
    ImageView iv_image;

    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_diary_title)
    TextView tv_diary_title;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_weather)
    TextView tv_weather;
    @BindView(R.id.iv_4)
    ImageView iv4;

    private Diary mDiary;
    private LoadingDialog mProgressDialog;
    private WarningDialog warningDialog;

    @BindView(R.id.rl_candy)
    RelativeLayout rl_candy;
    @BindView(R.id.rl_arty)
    RelativeLayout rl_arty;
    @BindView(R.id.rl_antique)
    RelativeLayout rl_antique;
    @BindView(R.id.rl_default)
    RelativeLayout rl_default;
    @BindView(R.id.rl_love)
    RelativeLayout rl_love;

    @BindView(R.id.rl_refresh)
    BGARefreshLayout rl_refresh;
    private BGAMeiTuanRefreshViewHolder mRefreshViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        setTranslucent();

        mDiary = (Diary) getIntent().getSerializableExtra(Constants.KEY.DIARY);

        initView();
        initBar();
        initDialog();

        initRefresh();
    }

    private void initRefresh() {
        // 为BGARefreshLayout设置代理
        rl_refresh.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        mRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(this, false);
        mRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        mRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        mRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
        rl_refresh.setRefreshViewHolder(mRefreshViewHolder);
    }

    /**
     * 初始化界面
     */
    private void initView() {

        tv_diary_title.setText(mDiary.getTitle());
        tv_date.setText(mDiary.getDate());
        tv_content.setText(mDiary.getContent());
        tv_weather.setText(mDiary.getWeather());

//        WeatherUtils.updateBg(tv_weather, mDiary.getWeather());

        String imageUrl = mDiary.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(mCtx).load(imageUrl).into(iv_image);
        }


        initTheme();
    }

    private void initTheme() {

        int theme = mDiary.getTheme().intValue();
        switch (theme) {

            case Constants.THEME.THEME_CANDY:
                rl_candy.setVisibility(View.VISIBLE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.GONE);

                tv_diary_title.setTextColor(getResources().getColor(R.color.theme_candy_txt));
                tv_content.setTextColor(getResources().getColor(R.color.theme_candy_txt));

                break;
            case Constants.THEME.THEME_ARTY:
                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.VISIBLE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.GONE);

                tv_diary_title.setTextColor(getResources().getColor(R.color.theme_arty_txt));
                tv_content.setTextColor(getResources().getColor(R.color.theme_arty_txt));

                break;
            case Constants.THEME.THEME_ANTIQUE:
                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.VISIBLE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.GONE);

                tv_diary_title.setTextColor(getResources().getColor(R.color.theme_antique_txt));
                tv_content.setTextColor(getResources().getColor(R.color.theme_antique_txt));

                break;

            case Constants.THEME.THEME_MON:
                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.VISIBLE);
                rl_love.setVisibility(View.GONE);

                tv_diary_title.setTextColor(getResources().getColor(R.color.white));
                tv_content.setTextColor(getResources().getColor(R.color.white));

                break;

            case Constants.THEME.THEME_LOVE:
                rl_candy.setVisibility(View.GONE);
                rl_arty.setVisibility(View.GONE);
                rl_antique.setVisibility(View.GONE);
                rl_default.setVisibility(View.GONE);
                rl_love.setVisibility(View.VISIBLE);

                tv_diary_title.setTextColor(getResources().getColor(R.color.white));
                tv_content.setTextColor(getResources().getColor(R.color.white));

                break;
        }

    }

    private void initDialog() {

        mProgressDialog = new LoadingDialog(this, R.style.NoTitleDialog);
        mProgressDialog.setLoadingDesc(getResources().getString(R.string.progress_delete));
        mProgressDialog.setCancelable(false);

    }

    private void initBar() {

        toolbar.setOnLeftButtonClickListener(new AppToolbar.OnLeftButtonClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        toolbar.setOnRightButtonClickListener(new AppToolbar.OnRightButtonClickListener() {
            @Override
            public void onClick() {
                warningDialog = new WarningDialog(mCtx, R.style.NoTitleDialog);
                warningDialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        warningDialog.dismiss();
                        mProgressDialog.show();
                        Diary d = new Diary();
                        d.delete(mDiary.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                mProgressDialog.dismiss();
                                EventBus.getDefault().post(new UpdateListEvent());
                                finish();
                            }
                        });
                    }
                });

                warningDialog.setCanelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        warningDialog.dismiss();
                    }
                });

                warningDialog.show();
            }
        });


//        toolbar.setIvRight2Listener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DiaryDetailActivity.this, AddDiaryActivity.class);
//                intent.putExtra(Constants.KEY.DIARY, mDiary);
//                startActivity(intent);
//                finish();
//            }
//        });

        if (mDiary.getTitle().contains("小楠和小璐")) {
            toolbar.setRightButtonVisibility(false);
        }
    }

    @OnClick(R.id.iv_image)
    public void imageDetail() {
        Intent i = new Intent(this, ImageDetailActivity.class);
        i.putExtra(Constants.KEY.IMAGE_URL, mDiary.getImageUrl());
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

        BmobQuery<Diary> q = new BmobQuery<>();
        q.getObject(mDiary.getObjectId(), new QueryListener<Diary>() {
            @Override
            public void done(Diary diary, BmobException e) {
                rl_refresh.endRefreshing();
                if (diary != null) {
                    mDiary = diary;
                    initView();
                    initBar();
                }
            }
        });

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

}
