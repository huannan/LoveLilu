package com.lovelilu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lovelilu.R;
import com.lovelilu.model.User;
import com.lovelilu.ui.activity.swipeback.app.SwipeBackActivity;
import com.lovelilu.ui.adapter.DefaultListDecoration;
import com.lovelilu.ui.adapter.UserListAdapter;
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
public class UserActivity extends SwipeBackActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

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
    private UserListAdapter mAdapter;


    private List<User> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        setTranslucent();
        initView();
        initBar();

        initList();
        requestUserList();
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
        toolbar.setRightButtonVisibility(false);

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
        mAdapter = new UserListAdapter(this, R.layout.item_user, mList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        rv_list.setAdapter(mAdapter);
    }

    private void requestUserList() {

        BmobQuery<User> query = new BmobQuery<>(User.class.getSimpleName());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e != null) {
                } else {
                    if (list != null) {
                        showDiaryList(list);
                    }
                }
            }
        });


    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (curState == STATE_INIT) {
            rl_refresh.endRefreshing();
        }

        curState = STATE_REFRESH;
        requestUserList();
//        requestBanner();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    private void showDiaryList(List<User> list) {

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

}
