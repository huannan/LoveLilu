package com.lovelilu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.Touch;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lovelilu.R;
import com.lovelilu.model.Photo;
import com.lovelilu.model.UpdateInfo;
import com.lovelilu.model.event.TabClickEvent;
import com.lovelilu.ui.activity.base.BaseActivity;
import com.lovelilu.ui.fragment.AnnFragment;
import com.lovelilu.ui.fragment.MineFragment;
import com.lovelilu.ui.fragment.DiaryFragment;
import com.lovelilu.ui.fragment.HomeFragment;
import com.lovelilu.ui.fragment.base.BaseFragment;
import com.lovelilu.utils.ApkUpdateUtils;
import com.lovelilu.utils.ToastUtils;
import com.lovelilu.widget.menu.SelectTypeDWindow;
import com.startsmake.mainnavigatetabbar.widget.MainNavigateTabBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends BaseActivity {

    @BindString(R.string.tab_home)
    String STRING_TAG_HOME;
    @BindString(R.string.tab_diary)
    String STRING_TAG_DIARY;
    @BindString(R.string.tab_day)
    String STRING_TAG_DAY;
    @BindString(R.string.tab_mine)
    String STRING_TAG_MINE;
    @BindString(R.string.tab_public)
    String STRING_TAG_PUBLIC;

    @BindView(R.id.mainTabBar)
    MainNavigateTabBar mNavigateTabBar;

    @BindView(R.id.tab_post_icon)
    ImageView tab_post_icon;

    private SelectTypeDWindow mSelectTypeDWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {

        setTranslucent();
        initTabs(savedInstanceState);

        checkForUpdate();

        /*UpdateInfo info = new UpdateInfo();
        info.setSize(6500l);
        info.setVersionCode(1);
        info.setVersionName("1.0.0");
        info.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                ToastUtils.showShort(MainActivity.this, "创建成功");
            }
        });*/

    }

    private void checkForUpdate() {

        BmobQuery<UpdateInfo> query = new BmobQuery<>(UpdateInfo.class.getSimpleName());
        query.findObjects(new FindListener<UpdateInfo>() {
            @Override
            public void done(List<UpdateInfo> list, BmobException e) {
                if (e != null) {
                } else {
                    ApkUpdateUtils.checkForUpdate(MainActivity.this, list.get(0), new ApkUpdateUtils.OnUpdateListener() {
                        @Override
                        public void onUpdate() {
                            ToastUtils.showShort(MainActivity.this, R.string.apk_download_ing);
                        }
                    });
                }
            }
        });


    }

    private int curIndex = 0;

    private void initTabs(Bundle savedInstanceState) {

        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

        mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.drawable.tabbar_icon_home_n, R.drawable.tabbar_icon_home_p, STRING_TAG_HOME));
        mNavigateTabBar.addTab(DiaryFragment.class, new MainNavigateTabBar.TabParam(R.drawable.tabbar_icon_diary_n, R.drawable.tabbar_icon_diary_p, STRING_TAG_DIARY));
        mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, STRING_TAG_PUBLIC));
        mNavigateTabBar.addTab(AnnFragment.class, new MainNavigateTabBar.TabParam(R.drawable.tabbar_icon_love_n, R.drawable.tabbar_icon_love_p, STRING_TAG_DAY));
        mNavigateTabBar.addTab(MineFragment.class, new MainNavigateTabBar.TabParam(R.drawable.tabbar_icon_setting_n, R.drawable.tabbar_icon_setting_p, STRING_TAG_MINE));

        mNavigateTabBar.setTabSelectListener(new MainNavigateTabBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(MainNavigateTabBar.ViewHolder holder) {

                if (curIndex == holder.tabIndex) {
                    //同一个页面下再次点击导航栏就回到顶部、刷新
                    EventBus.getDefault().post(new TabClickEvent(holder.tabIndex));
                }

                curIndex = holder.tabIndex;

            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }


    @OnClick(R.id.tab_post_icon)
    public void onClickPublish() {

        if (null == mSelectTypeDWindow) {
            mSelectTypeDWindow = new SelectTypeDWindow(this);
            mSelectTypeDWindow.init();
        }

        mSelectTypeDWindow.showMoreWindow(tab_post_icon, 100, new SelectTypeDWindow.OnMenuClickListener() {
            @Override
            public void onClick(int id) {
                switch (id) {

                    case R.id.ll_diary:
                        gotoPublicDiary();
                        break;
                    case R.id.ll_anniversary:
                        gotoPublicAnniversary();
                        break;
                    case R.id.ll_quotation:
                        gotoPublicQuotation();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void gotoPublicQuotation() {
        Intent intent = new Intent(this, AddQuotationActivity.class);
        startActivity(intent);
    }

    private void gotoPublicAnniversary() {
        Intent intent = new Intent(this, AddAnniversaryActivity.class);
        startActivity(intent);
    }

    private void gotoPublicDiary() {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }

    /*ListView mListView = new ListView(this);
    int toolbarHeight;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //一些适配操作
        WindowManager wm = getWindowManager();//Activity可以直接获取WindowManager
        //  WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final int windowWidth = wm.getDefaultDisplay().getWidth();
        final int windowHeight = wm.getDefaultDisplay().getHeight();

        //手动去计算ListView的宽高
        final int mListViewWidth = windowWidth;
        final int mListViewHeight = windowHeight - toolbarHeight;

        //下面获取ListView的宽高是有问题的
        //  mListView.measure(0, 0);
        //  mListView.getMeasuredWidth();
        //  mListView.getMeasuredHeight();

        //  mListView.getWidth();
        //  mListView.getHeight();
    }*/
}
