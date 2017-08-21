package com.lovelilu.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelilu.R;
import com.lovelilu.utils.UIUtils;


/**
 * 自定义的导航栏
 */
public class AppToolbar extends Toolbar {

    private TextView toolbar_title;
    private EditText toolbar_searchview;
    private ImageView toolbar_leftButton;
    private ImageView toolbar_rightButton;
    private ImageView iv_scan;
    private View mChildView;
    private boolean showSearchView;
    private Drawable left_button_icon;
    private Drawable right_button_icon;
    private String title;
    private AlphaAnimation mAlphaAnimation;
    private RelativeLayout rl_search;
    private RelativeLayout rl_bg;
    private float mBgAlpha;

    public AppToolbar(Context context) {
        this(context, null, 0);
    }

    public AppToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //通过代码得到布局文件当中一些属性的值
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.AppToolbar, defStyleAttr, 0);
        showSearchView = a.getBoolean(R.styleable.AppToolbar_showSearchView, false);
        left_button_icon = a.getDrawable(R.styleable.AppToolbar_leftButtonIcon);
        right_button_icon = a.getDrawable(R.styleable.AppToolbar_rightButtonIcon);
        title = a.getString(R.styleable.AppToolbar_myTitle);
        mBgAlpha = a.getFloat(R.styleable.AppToolbar_bgAlpha, 1);
        a.recycle();

        //初始界面
        initView();

        //初始化动画
        initAnimation();

        //初始监听器
        initListener();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        mAlphaAnimation = new AlphaAnimation(1.0f, 1.2f);
        mAlphaAnimation.setDuration(500);

    }

    /**
     * 初始化布局
     */
    private void initView() {

        if (mChildView == null) {
            mChildView = View.inflate(getContext(), R.layout.toolbar, null);

            toolbar_title = (TextView) mChildView.findViewById(R.id.toolbar_title);
            toolbar_searchview = (EditText) mChildView.findViewById(R.id.toolbar_searchview);
            toolbar_leftButton = (ImageView) mChildView.findViewById(R.id.toolbar_leftButton);
            toolbar_rightButton = (ImageView) mChildView.findViewById(R.id.toolbar_rightButton);
            iv_scan = (ImageView) mChildView.findViewById(R.id.iv_scan);
            rl_search = (RelativeLayout) mChildView.findViewById(R.id.rl_search);
            rl_bg = (RelativeLayout) mChildView.findViewById(R.id.rl_bg);

            if (showSearchView) {

                //自动弹出输入法
                rl_search.setFocusable(false);
                rl_search.setFocusableInTouchMode(false);

            }

            //添加自定义的布局到Toolbar
            addView(mChildView);

            //设置标题、搜索框、左右按钮是否显示，并且设置按钮的图标
            if (showSearchView) {
                showSearchview();
                hideTitle();
            } else {
                hideSearchview();
                showTitle();
                if (title != null) {
                    toolbar_title.setText(title);
                }
            }

            if (left_button_icon != null) {
                toolbar_leftButton.setImageDrawable(left_button_icon);
            }

            if (right_button_icon != null) {
                toolbar_rightButton.setImageDrawable(right_button_icon);
            }

            setBackgroundAlpha(mBgAlpha);
        }

    }

    /**
     * 重写设置标题的方法
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        toolbar_title.setText(title);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        toolbar_title.setText(resId);
    }

    /**
     * 设置左右按钮的图标
     *
     * @param d
     */
    public void setLeftButtonIconDrawable(Drawable d) {
        toolbar_leftButton.setImageDrawable(d);
    }

    public void setRightButtonIconDrawable(Drawable d) {
        toolbar_rightButton.setImageDrawable(d);
    }

    /**
     * 标题与搜索框的切换
     */
    public void setShowSearchView() {
        hideTitle();
        showSearchview();
    }

    public void setShowTitleView(String title) {
        hideSearchview();
        showTitle();
        toolbar_title.setText(title);
    }

    public EditText getSearchView() {
        return toolbar_searchview;
    }

    /**
     * 左右按钮的监听
     */
    private void initListener() {
        toolbar_leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar_leftButton.startAnimation(mAlphaAnimation);
                if (onLeftButtonClickListener != null) {
                    onLeftButtonClickListener.onClick();
                }
            }
        });

        toolbar_rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar_rightButton.startAnimation(mAlphaAnimation);
                if (onRightButtonClickListener != null) {
                    onRightButtonClickListener.onClick();
                }
            }
        });

        toolbar_searchview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchViewClickListener != null) {
                    onSearchViewClickListener.onClick();
                }
            }
        });
    }

    public interface OnLeftButtonClickListener {
        void onClick();
    }

    public interface OnRightButtonClickListener {
        void onClick();

    }

    public interface OnSearchViewClickListener {
        void onClick();

    }

    private OnLeftButtonClickListener onLeftButtonClickListener;
    private OnRightButtonClickListener onRightButtonClickListener;
    private OnSearchViewClickListener onSearchViewClickListener;

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listener) {
        onLeftButtonClickListener = listener;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listener) {
        onRightButtonClickListener = listener;
    }

    public void setOnSearchViewClickListener(OnSearchViewClickListener listener) {
        onSearchViewClickListener = listener;
    }


    /**
     * 设置标题或者搜索框是否显示
     */
    private void showTitle() {
        toolbar_title.setVisibility(View.VISIBLE);
    }

    private void hideTitle() {
        toolbar_title.setVisibility(View.GONE);
    }

    private void showSearchview() {
        toolbar_searchview.setVisibility(View.VISIBLE);
    }

    private void hideSearchview() {
        rl_search.setVisibility(View.GONE);
        toolbar_searchview.setVisibility(View.GONE);
    }

    public void setScanBtnListener(OnClickListener listener) {
        iv_scan.setOnClickListener(listener);
    }


    public void setRightButtonVisibility(boolean visibility) {
        if (visibility) {
            toolbar_rightButton.setVisibility(View.VISIBLE);
        } else {
            toolbar_rightButton.setVisibility(View.GONE);
        }
    }


    public void setBackgroundAlpha(float alpha) {
//        if (alpha <= 0.0) {
//            setVisibility(View.GONE);
//        } else {
//            setVisibility(View.VISIBLE);
//        }
        rl_bg.setAlpha(alpha);

        toolbar_searchview.setAlpha(alpha);
    }

    public int getTotalHeight() {
        return getBottom() + UIUtils.dip2px(200 + 181, getContext());
    }

}
