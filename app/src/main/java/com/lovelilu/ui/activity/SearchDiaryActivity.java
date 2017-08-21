package com.lovelilu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.model.Diary;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.adapter.DefaultListDecoration;
import com.lovelilu.ui.adapter.DiaryListAdapter;
import com.lovelilu.ui.dialog.LoadingDialog;
import com.lovelilu.utils.JSONUtil;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.widget.AppToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by huannan on 2016/8/25.
 */
public class SearchDiaryActivity extends SwipeBackActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.toolbar)
    AppToolbar toolbar;

    @BindView(R.id.rl_refresh)
    BGARefreshLayout rl_refresh;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

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

    private List<Diary> mList = new ArrayList<>();
    private LoadingDialog mProgressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        setTranslucent();
        initView();
        initDialog();
        initBar();

        initList();

        String json = getIntent().getStringExtra(Constants.KEY.JSON_LIST_DIARY);
        mList = JSONUtil.fromJson(json, new TypeToken<List<Diary>>() {
        }.getType());
    }

    /**
     * 初始化界面
     */
    private void initView() {


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
                //发起搜索
                searchListData(false);
            }
        });
    }


    private void initDialog() {

        mProgressDialog = new LoadingDialog(this, R.style.NoTitleDialog);
        mProgressDialog.setLoadingDesc(getResources().getString(R.string.progress_searching));
        mProgressDialog.setCancelable(false);

    }

    private void initList() {
        // 为BGARefreshLayout设置代理
        rl_refresh.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        mRefreshViewHolder = new BGAMeiTuanRefreshViewHolder(this, false);

        mRefreshViewHolder.setPullDownImageResource(R.mipmap.bga_refresh_mt_pull_down);
        mRefreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        mRefreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);

        // 设置下拉刷新和上拉加载更多的风格
//        rl_refresh.setBackgroundColor(getResources().getColor(R.color.bg));

        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.addItemDecoration(new DefaultListDecoration());

        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new DiaryListAdapter(this, R.layout.item_diary, mList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        rv_list.setAdapter(mAdapter);
    }

    private void searchListData(boolean isRefresh) {

        //得到用户输入的关键词
        final String keyWord = toolbar.getSearchView().getText().toString().trim();

        if (TextUtils.isEmpty(keyWord)) {
            ToastUtils.showShort(this, R.string.search_key_empty);
            return;
        }

        if (!isRefresh) {
            mProgressDialog.show();
        }

/*
//        BmobQuery<Diary> eq1 = new BmobQuery<>(Diary.class.getSimpleName());
//        eq1.addWhereContains("title", keyWord);
//        BmobQuery<Diary> eq2 = new BmobQuery<>(Diary.class.getSimpleName());
//        eq2.addWhereEqualTo("content", keyWord);
//        List<BmobQuery<Diary>> queries = new ArrayList<>();
//        queries.add(eq1);
//        queries.add(eq2);
        BmobQuery<Diary> mainQuery = new BmobQuery<>(Diary.class.getSimpleName());
        mainQuery.addWhereContains("title", "日记");
//        mainQuery.or(queries);

        mainQuery.order(mCurOrder);
        mainQuery.findObjects(new FindListener<Diary>() {
            @Override
            public void done(List<Diary> list, BmobException e) {

                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

                if (e != null) {
                    e.printStackTrace();
                    ToastUtils.showShort(SearchDiaryActivity.this, R.string.http_failed);
                } else {
                    ToastUtils.showShort(SearchDiaryActivity.this, list.size() + "" + keyWord);
                    showListData(list);
                }
            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Diary> result = new ArrayList<>();
                for (Diary d : mList) {
                    if (d.getTitle().contains(keyWord) || d.getContent().contains(keyWord)) {
                        result.add(d);
                    }
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        showListData(result);
                    }
                }, 1000);

            }
        }).start();

    }

    private void showListData(List<Diary> list) {

        switch (curState) {
            case STATE_INIT:
                //初始化状态，初始化列表
                rl_refresh.setRefreshViewHolder(mRefreshViewHolder);

                mAdapter.setNewData(list);

                curState = STATE_REFRESH;
                rl_refresh.endRefreshing();

                break;

            case STATE_REFRESH:
                //下拉刷新状态，刷新数据，列表回到最顶端，关闭下拉刷新
                mAdapter.setNewData(list);
                //rv_list.scrollToPosition(0);
                rl_refresh.endRefreshing();
                break;

            case STATE_LOAD_MORE:
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
        searchListData(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }


}
