package com.lovelilu;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.igexin.sdk.PushManager;
import com.lovelilu.model.User;
import com.lovelilu.service.PushIntentService;
import com.lovelilu.service.PushService;
import com.lovelilu.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by huannan on 2016/8/24.
 */
public class App extends Application {

    private static List<User> mUserList;
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();

        mCtx = this;
        initBmob();
        requestUserList();

        ShareSDK.initSDK(this);

        //初始化SDK
        PushManager.getInstance().initialize(this, PushService.class);
        //第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushIntentService.class);
    }

    public static Context getContext() {
        return mCtx;
    }

    public static List<User> getUserList() {
        return mUserList;
    }

    private static void requestUserList() {

        BmobQuery<User> query = new BmobQuery<>(User.class.getSimpleName());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e != null) {
                } else {
                    if (list != null) {
                        mUserList = list;
                    }
                }
            }
        });
    }


    private void initBmob() {

        BmobConfig bmobConfig = new BmobConfig.Builder(mCtx)
                .setApplicationId("13dd9b9189efc46befdfe2590b431d6c")
                .setConnectTimeout(7)
//                .setUploadBlockSize(1024 * 1024)
                .setFileExpiration(2500)
                .build();

        Bmob.initialize(bmobConfig);

    }


    /**
     * 利用PackageManager获取当前应用的版本名字
     *
     * @return
     */
    public static int getVersionCode() {
        PackageManager pm = mCtx.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mCtx.getPackageName(), 0);
            // 获取版本，以清单文件为准
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // 找不到包的异常,返回-1表示异常
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 利用PackageManager获取当前应用的版本号
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager pm = mCtx.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mCtx.getPackageName(), 0);
            // 获取版本，以清单文件为准
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // 找不到包的异常,返回-1表示异常
            e.printStackTrace();
            return "";
        }
    }


}
