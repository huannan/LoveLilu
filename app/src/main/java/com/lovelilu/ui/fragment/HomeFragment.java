package com.lovelilu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.lovelilu.model.BannerHome;
import com.lovelilu.model.Photo;
import com.lovelilu.model.event.TabClickEvent;
import com.lovelilu.model.event.UpdateListEvent;
import com.lovelilu.ui.activity.ScanActivity;
import com.lovelilu.ui.activity.SearchPhotoActivity;
import com.lovelilu.ui.activity.UserActivity;
import com.lovelilu.ui.adapter.DefaultListDecoration;
import com.lovelilu.ui.adapter.PhotoListAdapter;
import com.lovelilu.ui.fragment.base.BaseFragment;
import com.lovelilu.utils.DayTimeUtils;
import com.lovelilu.utils.JSONUtil;
import com.lovelilu.utils.ToastUtils;
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

public class HomeFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

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
    private PhotoListAdapter mAdapter;

    private static final String ORDER_DESC = "-createdAt";
    private static final String ORDER_INC = "+createdAt";
    private String mCurOrder = ORDER_DESC;
    private SliderLayout mSliderLayout;
    private PagerIndicator mIndicator;
    private TextView tv_day;
//    private RelativeLayout rl_heart;

    private List<Photo> mList = new ArrayList<>();

    @BindView(R.id.rl_content)
    RelativeLayout rl_content;
    private int mDistanceY;
    private GifImageView gif_weini;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        requestPhotoList();

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
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.weini_home);
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
        requestPhotoList();
    }


    @Subscribe
    public void onEventMainThread(TabClickEvent event) {
        //回到顶部刷新
        if (event.getTabIndex() == Constants.KEY.TAB_HOME) {
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
                requestPhotoList();
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

                Intent intent = new Intent(getActivity(), SearchPhotoActivity.class);
                intent.putExtra(Constants.KEY.JSON_LIST_PHOTO, JSONUtil.toJSON(mList));
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

//        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));

//        rv_list.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_list.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.addItemDecoration(new DefaultListDecoration());

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new PhotoListAdapter(getActivity(), R.layout.item_photo, mList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        View header = getActivity().getLayoutInflater().inflate(R.layout.rv_header_home, null);
        mAdapter.addHeaderView(header);

        mSliderLayout = (SliderLayout) header.findViewById(R.id.slider);
        mIndicator = (PagerIndicator) header.findViewById(R.id.custom_indicator);

//        rl_heart = (RelativeLayout) header.findViewById(R.id.rl_heart);
        tv_day = (TextView) header.findViewById(R.id.tv_day);

//        View loading = getLayoutInflater().inflate(R.layout.rv_loading_view, null);
//        mAdapter.setLoadingView(loading);

        //启动心动的动画
        mHandler.sendEmptyMessageDelayed(0, 3000);

        //设置在一起的天数
        tv_day.setText(DayTimeUtils.getDays(getActivity()) + "");

        initBanner();

        rv_list.setAdapter(mAdapter);
    }

    private void requestPhotoList() {

        BmobQuery<Photo> query = new BmobQuery<>(Photo.class.getSimpleName());
        query.order(mCurOrder);
        query.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                if (e != null) {
                    ToastUtils.showShort(getActivity(), R.string.http_failed);
                } else {
                    showDiaryList(list);
                }
            }
        });

    }

    private void showDiaryList(List<Photo> list) {

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
        requestPhotoList();
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

//            ViewShakeUtils.shakeView(rl_heart, true, 0);
//            mHandler.sendEmptyMessageDelayed(0, OFFSET);
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

        /*SaveListener<String> l = new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {


            }
        };

        BannerHome h1 = new BannerHome("http://i1.piimg.com/583017/4986d502981b5d7e.jpg");
        BannerHome h2 = new BannerHome("http://i1.piimg.com/583017/9047c6ba3bed3620.jpg");
        BannerHome h3 = new BannerHome("http://i1.piimg.com/583017/b79112a2888c189a.jpg");
        BannerHome h4 = new BannerHome("http://i1.piimg.com/583017/48b7a64f6b534f86.png");

        BannerDiary d1 = new BannerDiary("http://p1.bqimg.com/583017/f73f87c407b6373d.jpg");
        BannerDiary d2 = new BannerDiary("http://p1.bqimg.com/583017/64efede9c9392a25.jpg");
        BannerDiary d3 = new BannerDiary("http://p1.bqimg.com/583017/43482a473a3e502f.jpg");
        BannerDiary d4 = new BannerDiary("http://p1.bqimg.com/583017/9e511efd411df21c.png");

        BannerAnn a1 = new BannerAnn("http://p1.bqimg.com/583017/e16cf9353e21f07c.png");
        BannerAnn a2 = new BannerAnn("http://p1.bqimg.com/583017/455f00842b8e06db.jpg");
        BannerAnn a3 = new BannerAnn("http://p1.bqimg.com/583017/5cb435e2b72c21a4.jpg");
        BannerAnn a4 = new BannerAnn("http://p1.bqimg.com/583017/3b63235304565243.png");

        h1.save(l);
        h2.save(l);
        h3.save(l);
        h4.save(l);

        d1.save(l);
        d2.save(l);
        d3.save(l);
        d4.save(l);

        a1.save(l);
        a2.save(l);
        a3.save(l);
        a4.save(l);*/

        mSliderLayout.removeAllSliders();
        BmobQuery<BannerHome> query = new BmobQuery<>();
        query.findObjects(new FindListener<BannerHome>() {
            @Override
            public void done(List<BannerHome> urls, BmobException e) {

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

