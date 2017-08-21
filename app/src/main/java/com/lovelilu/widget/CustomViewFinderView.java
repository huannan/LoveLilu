package com.lovelilu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.dalong.scanqrcodelib.core.ViewFinderView;

/**
 * 自定义扫描样式
 * Created by zhouweilong on 2016/11/2.
 */
public class CustomViewFinderView extends ViewFinderView {

    public CustomViewFinderView(Context context) {
        super(context);
        init();
    }

    public CustomViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        //正方形
        setSquareViewFinder(true);
        //是否移动扫描线
        setIsMoveScanLine(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
