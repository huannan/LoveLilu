package com.lovelilu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.BannerDiary;
import com.lovelilu.model.Diary;
import com.lovelilu.model.User;
import com.lovelilu.model.event.TabClickEvent;
import com.lovelilu.model.event.UpdateListEvent;
import com.lovelilu.ui.activity.ScanActivity;
import com.lovelilu.ui.activity.SearchDiaryActivity;
import com.lovelilu.ui.activity.UserActivity;
import com.lovelilu.ui.adapter.DefaultListDecoration;
import com.lovelilu.ui.adapter.DiaryListAdapter;
import com.lovelilu.ui.fragment.base.BaseFragment;
import com.lovelilu.utils.DayTimeUtils;
import com.lovelilu.utils.JSONUtil;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.utils.ViewShakeUtils;
import com.lovelilu.widget.AppToolbar;
import com.lovelilu.widget.ImageSliderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huannan on 2016/11/26.
 */

public class DiaryFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.rl_refresh)
    BGARefreshLayout rl_refresh;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

//    @BindView(R.id.loveview)
//    private LoveView loveview;

    @BindView(R.id.ll_loading)
    LinearLayout ll_loading;

    @BindView(R.id.common_loading_content_layout)
    LinearLayout common_loading_content_layout;

    private BGAMeiTuanRefreshViewHolder mRefreshViewHolder;

    //用于记录当前是何种状态，在请求完数据之后根据不同的状态进行不同的操作
    private static final int STATE_INIT = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_LOAD_MORE = 2;
    //用于记录当前的状态
    private int curState = STATE_INIT;
    private DiaryListAdapter mAdapter;

    private static final String ORDER_DESC = "-createdAt";
    private static final String ORDER_INC = "+createdAt";
    private String mCurOrder = ORDER_DESC;
    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private TextView tv_days;
    private RelativeLayout rl_heart;

    private List<Diary> mList = new ArrayList<>();

    @BindView(R.id.rl_content)
    RelativeLayout rl_content;
    private int mDistanceY;
    private GifImageView gif_weini;
    private List<User> mUserList;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        ButterKnife.bind(this, mRootView);

        EventBus.getDefault().register(this);

        initAnimi();

        initBar();
        initList();
        requestDiaryList();

        //更新widget
        Intent intent = new Intent(Constants.ACTION.UPDATE_ACTION);
        getActivity().sendBroadcast(intent);
    }

    private void initAnimi() {
        mRootView.findViewById(R.id.common_loading_content_layout).setVisibility(View.VISIBLE);
        /*ImageView bear = (ImageView) mRootView.findViewById(R.id.common_loading_content_image_bear);
        ImageView cloudBack = (ImageView) mRootView.findViewById(R.id.common_loading_content_image_cloud_back);
        ImageView cloudFront = (ImageView) mRootView.findViewById(R.id.common_loading_content_image_cloud_front);

        Animation bearAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_loading_translate_bear);
        bear.setAnimation(bearAnim);

        Animation cloudBackAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_loading_translate_cloud_back);
        cloudBack.setAnimation(cloudBackAnim);

        Animation cloudFrontAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_loading_translate_cloud_front);
        cloudFront.setAnimation(cloudFrontAnim);*/

        gif_weini = (GifImageView) mRootView.findViewById(R.id.gif_weini);
        try {
            // 如果加载的是gif动图，第一步需要先将gif动图资源转化为GifDrawable
            // 将gif图资源转化为GifDrawable
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.weini_diary);
            // gif1加载一个动态图gif
            gif_weini.setImageDrawable(gifDrawable);
            // 如果是普通的图片资源，就像Android的ImageView set图片资源一样简单设置进去即可。
            // gif2加载一个普通的图片（如png，bmp，jpeg等等）
//            gif_weini.setImageResource(R.drawable.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() {

        Animation close = AnimationUtils.loadAnimation(getActivity(), R.anim.activity_close_exist);
        Animation enter = AnimationUtils.loadAnimation(getActivity(), R.anim.activity_open_enter);
        close.setFillAfter(true);
        enter.setFillAfter(true);

        ll_loading.startAnimation(close);
        rl_content.startAnimation(enter);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_content.setVisibility(View.VISIBLE);
                common_loading_content_layout.setVisibility(View.GONE);
            }
        }, 500);
    }

    /*private void start() {
        TranslateAnimation animation = new TranslateAnimation(0, 1000, 0, 0);
        animation.setFillAfter(true);

        animation.setDuration(2000);

        ll_loading.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                common_loading_content_layout.setVisibility(View.GONE);

                Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.activity_open_enter);
                a.setFillAfter(true);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        rl_content.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rl_content.startAnimation(a);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }*/


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mSliderLayout != null) {
            mSliderLayout.startAutoCycle();
        }
    }


    @Subscribe
    public void onEventMainThread(UpdateListEvent msg) {
        curState = STATE_REFRESH;
        requestDiaryList();
    }

    @Subscribe
    public void onEventMainThread(TabClickEvent event) {
        //回到顶部刷新
        if (event.getTabIndex() == Constants.KEY.TAB_DIARY) {
            if (curState == STATE_REFRESH) {
                rv_list.smoothScrollToPosition(0);
                rl_refresh.beginRefreshing();
            }
        }
    }

    private void initBar() {

//        TintBarUtils.setStatusBarTintColor(this, R.color.base_color);

        toolbar.setOnRightButtonClickListener(new AppToolbar.OnRightButtonClickListener() {
            @Override
            public void onClick() {
                if (curState == STATE_INIT) {
                    return;
                }

                if (mCurOrder.equals(ORDER_DESC)) {
                    mCurOrder = ORDER_INC;
                } else {
                    mCurOrder = ORDER_DESC;
                }
                curState = STATE_REFRESH;
                requestDiaryList();
            }
        });

        toolbar.setOnLeftButtonClickListener(new AppToolbar.OnLeftButtonClickListener() {
            @Override
            public void onClick() {
                Intent per = new Intent(getActivity(), UserActivity.class);
                startActivity(per);
            }
        });


        toolbar.setScanBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scan = new Intent(getActivity(), ScanActivity.class);
                startActivity(scan);
            }
        });

        toolbar.getSearchView().setKeyListener(null);

        toolbar.getSearchView().requestFocus();
//        toolbar.getSearchView().performClick();

        toolbar.setOnSearchViewClickListener(new AppToolbar.OnSearchViewClickListener() {
            @Override
            public void onClick() {

                if (mList.size() == 0) {
                    ToastUtils.showShort(getActivity(), R.string.loading);
                    return;
                }

                Intent intent = new Intent(getActivity(), SearchDiaryActivity.class);
                intent.putExtra(Constants.KEY.JSON_LIST_DIARY, JSONUtil.toJSON(mList));
                startActivity(intent);
            }
        });

        rv_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = toolbar.getTotalHeight();

                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float alpha = (float) mDistanceY / toolbarHeight;

                    if (alpha >= 0.9f) {
                        alpha = 0.9f;
                    }

                    toolbar.setBackgroundAlpha(alpha);
                } else {
                    //上述虽然判断了滑动距离与toolbar高度相等的情况，但是实际测试时发现，标题栏的背景色
                    //很少能达到完全不透明的情况，所以这里又判断了滑动距离大于toolbar高度的情况，
                    //将标题栏的颜色设置为完全不透明状态
                    toolbar.setBackgroundAlpha(0.9f);
                }


            }
        });
    }

    private void initList() {
        // 为BGARefreshLayout设置代理
        rl_refresh.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        mRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(getActivity(), false);

        mRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        mRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        mRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);

        // 设置下拉刷新和上拉加载更多的风格
//        rl_refresh.setBackgroundColor(getResources().getColor(R.color.bg));

        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.addItemDecoration(new DefaultListDecoration());

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new DiaryListAdapter(getActivity(), R.layout.item_diary, mList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        View header = getActivity().getLayoutInflater().inflate(R.layout.rv_header_diary, null);
        mAdapter.addHeaderView(header);

        mSliderLayout = (SliderLayout) header.findViewById(R.id.slider);
        mIndicator = (PagerIndicator) header.findViewById(R.id.custom_indicator);

        rl_heart = (RelativeLayout) header.findViewById(R.id.rl_heart);
        tv_days = (TextView) header.findViewById(R.id.tv_days);

//        View loading = getLayoutInflater().inflate(R.layout.rv_loading_view, null);
//        mAdapter.setLoadingView(loading);

        //启动心动的动画
        mHandler.sendEmptyMessageDelayed(0, 3000);

        //设置在一起的天数
        tv_days.setText(DayTimeUtils.getDays(getActivity()) + "");

        initBanner();

        rv_list.setAdapter(mAdapter);
    }

    private void requestDiaryList() {

        BmobQuery<Diary> query = new BmobQuery<>(Diary.class.getSimpleName());
        query.order(mCurOrder);
        query.findObjects(new FindListener<Diary>() {
            @Override
            public void done(List<Diary> list, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    ToastUtils.showShort(getActivity(), R.string.http_failed);
                } else {
                    showDiaryList(list);
                }
            }
        });

    }

    private void showDiaryList(List<Diary> list) {

        switch (curState) {
            case STATE_INIT:
                //初始化状态，初始化列表
                rl_refresh.setRefreshViewHolder(mRefreshViewHolder);

                mList = list;

                mAdapter.setNewData(list);

                curState = STATE_REFRESH;
                rl_refresh.endRefreshing();

                start();

//                loveview.setVisibility(View.GONE);
                break;

            case STATE_REFRESH:
                //下拉刷新状态，刷新数据，列表回到最顶端，关闭下拉刷新

                mList = list;

                mAdapter.setNewData(list);
                //rv_list.scrollToPosition(0);
                rl_refresh.endRefreshing();
                break;

            case STATE_LOAD_MORE:

                mList.addAll(list);

                mAdapter.addData(list);
                rl_refresh.endLoadingMore();
                break;
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (curState == STATE_INIT) {
            rl_refresh.endRefreshing();
        }

        curState = STATE_REFRESH;
        requestDiaryList();
//        requestBanner();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }


    private static final int OFFSET = 2500;  //每个动画的播放时间间隔

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ViewShakeUtils.shakeView(rl_heart, true, 0);
            mHandler.sendEmptyMessageDelayed(0, OFFSET);
        }
    };

    private void initBanner() {
        mSliderLayout.setSliderTransformDuration(2000, null);
        mSliderLayout.setDuration(5000);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomIndicator(mIndicator);
        mSliderLayout.stopAutoCycle();

        requestBanner();
    }

    private void requestBanner() {
        mSliderLayout.removeAllSliders();
        BmobQuery<BannerDiary> query = new BmobQuery<>();
        query.findObjects(new FindListener<BannerDiary>() {
            @Override
            public void done(List<BannerDiary> urls, BmobException e) {

                if (urls != null) {
                    for (int i = 0; i < urls.size(); i++) {
                        //新建三个展示View，并且添加到SliderLayout
                        ImageSliderView isv = new ImageSliderView(getActivity());
                        isv.image(urls.get(i).getImgUrl());
                        mSliderLayout.addSlider(isv);
                        mSliderLayout.stopAutoCycle();
                        mSliderLayout.setCurrentPosition(0, false);
                    }

                    //4秒钟之后开始自动轮播
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSliderLayout.startAutoCycle();
                        }
                    }, 4000);
                }
            }
        });
    }
}

