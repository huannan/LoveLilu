package com.lovelilu.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.ui.activity.MainActivity;
import com.lovelilu.utils.DayTimeUtils;

public class LoveWidgetProvider extends AppWidgetProvider {

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        updateWidget(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        updateWidget(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);// 保证系统能正常运行
        for (int appWidgetId : appWidgetIds) {
            // 启动Activyty
            Intent actIntent = new Intent(context, MainActivity.class);
            PendingIntent actPendingIntent = PendingIntent.getActivity(context, 0, actIntent, 0);

            // 发送广播
            // 利用广播创建PendingIntent对象
            Intent broIntent = new Intent(Constants.ACTION.UPDATE_ACTION);
            // 增加下面一行，可以使得停止的应用也可以接收到
            broIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            // 创建RemoteViews对象，设置监听器
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_love);
            remoteViews.setOnClickPendingIntent(R.id.ll_root, actPendingIntent);
            remoteViews.setTextViewText(R.id.tv_day_des, DayTimeUtils.getDays(context) + "天");
            // 更新APPWidget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }

    private void updateWidget(Context ctx) {
        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.widget_love);
        remoteViews.setTextViewText(R.id.tv_day_des, DayTimeUtils.getDays(ctx) + "天");

        // 利用ComponentName（代表整个APPWidget）来更新
        ComponentName componentName = new ComponentName(ctx, LoveWidgetProvider.class);
        AppWidgetManager.getInstance(ctx).updateAppWidget(componentName, remoteViews);
    }

}
