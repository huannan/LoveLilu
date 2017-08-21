package com.lovelilu.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.lovelilu.R;

/**
 * Created by huannan on 2016/8/25.
 */
public class LoveToolbar extends Toolbar {

    private View mChildView;

    private TextView tv_title;

    private ImageView iv_icon;

    private RippleView rv_iv_left;
    private RippleView rv_iv_right;
    private RippleView rv_iv_right_2;
    private RippleView rv_tv_left;
    private RippleView rv_tv_right;

    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_right_2;

    private TextView tv_left;
    private TextView tv_right;
    private LinearLayout ll_bg;

    public LoveToolbar(Context context) {
        this(context, null, 0);
    }

    public LoveToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(attrs, defStyleAttr);
    }

    /**
     * 初始化布局
     */
    private void initView(@Nullable AttributeSet attrs, int defStyleAttr) {

        if (mChildView == null) {
            mChildView = View.inflate(getContext(), R.layout.view_toolbar, null);

//            ViewUtils.inject(this, mChildView);
            //添加自定义的布局到Toolbar
            addView(mChildView);

            tv_title = (TextView) mChildView.findViewById(R.id.tv_title);
            tv_left = (TextView) mChildView.findViewById(R.id.tv_left);
            tv_right = (TextView) mChildView.findViewById(R.id.tv_right);
            iv_icon = (ImageView) mChildView.findViewById(R.id.iv_icon);
            rv_iv_left = (RippleView) mChildView.findViewById(R.id.rv_iv_left);
            rv_iv_right = (RippleView) mChildView.findViewById(R.id.rv_iv_right);
            rv_iv_right_2 = (RippleView) mChildView.findViewById(R.id.rv_iv_right_2);
            rv_tv_left = (RippleView) mChildView.findViewById(R.id.rv_tv_left);
            rv_tv_right = (RippleView) mChildView.findViewById(R.id.rv_tv_right);
            iv_left = (ImageView) mChildView.findViewById(R.id.iv_left);
            iv_right = (ImageView) mChildView.findViewById(R.id.iv_right);
            iv_right_2 = (ImageView) mChildView.findViewById(R.id.iv_right_2);
            ll_bg = (LinearLayout) mChildView.findViewById(R.id.ll_bg);
        }


        //通过代码得到布局文件当中一些属性的值
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.LoveToolbar, defStyleAttr, 0);

        uodateUI(a);


        a.recycle();
    }

    private void uodateUI(TintTypedArray a) {
        String title = a.getString(R.styleable.LoveToolbar_title_txt);
        if (!TextUtils.isEmpty(title)) {
            setTvTitle(title);
        }

        String left_txt = a.getString(R.styleable.LoveToolbar_left_btn_txt);
        if (!TextUtils.isEmpty(left_txt)) {
            setTvLeft(left_txt);
        }

        String right_txt = a.getString(R.styleable.LoveToolbar_right_btn_txt);
        if (!TextUtils.isEmpty(right_txt)) {
            setTvRight(right_txt);
        }

        Drawable left_drawable = a.getDrawable(R.styleable.LoveToolbar_left_btn_drawable);
        if (left_drawable != null) {
            setIvLeft(left_drawable);
        }

        Drawable right_drawable = a.getDrawable(R.styleable.LoveToolbar_right_btn_drawable);
        if (right_drawable != null) {
            setIvRight(right_drawable);
        }

        Drawable right2_drawable = a.getDrawable(R.styleable.LoveToolbar_right2_btn_drawable);
        if (right2_drawable != null) {
            setIvRight2(right2_drawable);
        }
    }

    public void setTvTitle(String str) {
        tv_title.setText(str);
        setTvTitleVisibility(true);
    }

    public void setTvTitle(int resId) {
        tv_title.setText(resId);
    }

    public void setTvTitleColor(int color) {
        tv_title.setTextColor(color);
    }

    public void setTvTitleListener(OnClickListener listener) {
        tv_title.setOnClickListener(listener);
    }

    public boolean setTvTitleVisibility(Boolean visible) {
        if (visible)
            tv_title.setVisibility(View.VISIBLE);
        else
            tv_title.setVisibility(View.INVISIBLE);
        return visible;
    }

    public void setIvLeft(int resId) {
        iv_left.setImageResource(resId);
        setIvLeftVisibility(true);
    }

    public void setIvLeft(Drawable drawable) {
        iv_left.setImageDrawable(drawable);
        setIvLeftVisibility(true);
    }

    public ImageView getIvIcon() {
        return iv_icon;
    }

    public boolean setIvIconVisibility(Boolean visible) {
        if (visible)
            iv_icon.setVisibility(View.VISIBLE);
        else
            iv_icon.setVisibility(View.INVISIBLE);
        return visible;
    }


    public void setIvLeftListener(final OnClickListener listener) {
//        iv_left.setOnClickListener(listener);
        rv_iv_left.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onClick(iv_left);
            }
        });
    }

    public boolean setIvLeftVisibility(Boolean visible) {
        if (visible) {
            iv_left.setVisibility(View.VISIBLE);
            rv_iv_left.setVisibility(View.VISIBLE);
        } else {
            iv_left.setVisibility(View.INVISIBLE);
            rv_iv_left.setVisibility(View.INVISIBLE);
        }
        return visible;
    }

    public void setIvRight(int resId) {
        iv_right.setImageResource(resId);
        setIvRightVisibility(true);
    }

    public void setIvRight(Drawable drawable) {
        iv_right.setImageDrawable(drawable);
        setIvRightVisibility(true);
    }

    public ImageView getIvRight() {
        return iv_right;
    }

    public void setIvRightListener(final OnClickListener listener) {
//        iv_right.setOnClickListener(listener);
        rv_iv_right.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onClick(iv_right);
            }
        });
    }

    public boolean setIvRightVisibility(Boolean visible) {
        if (visible) {
            iv_right.setVisibility(View.VISIBLE);
            rv_iv_right.setVisibility(View.VISIBLE);
        } else {
            iv_right.setVisibility(View.INVISIBLE);
            rv_iv_right.setVisibility(View.INVISIBLE);
        }
        return visible;
    }

    public void setTvLeft(int resId) {
        tv_left.setText(resId);
        setTvLeftVisibility(true);
    }

    public void setTvLeft(String s) {
        tv_left.setText(s);
        setTvLeftVisibility(true);
    }

    public void setTvLeftListener(final OnClickListener listener) {
//        tv_left.setOnClickListener(listener);
        rv_tv_left.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onClick(tv_left);
            }
        });
    }

    public boolean setTvLeftVisibility(Boolean visible) {
        if (visible) {
            tv_left.setVisibility(View.VISIBLE);
            rv_tv_left.setVisibility(View.VISIBLE);
        } else {
            tv_left.setVisibility(View.INVISIBLE);
            rv_tv_left.setVisibility(View.INVISIBLE);
        }
        return visible;
    }

    public void setTvRight(int resId) {
        tv_right.setText(resId);
        setTvRightVisibility(true);
    }

    public void setTvRight(String s) {
        tv_right.setText(s);
        setTvRightVisibility(true);
    }

    public void setTvRightListener(final OnClickListener listener) {
//        tv_right.setOnClickListener(listener);
        rv_tv_right.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onClick(tv_right);
            }
        });
    }

    public boolean setTvRightVisibility(Boolean visible) {
        if (visible) {
            tv_right.setVisibility(View.VISIBLE);
            rv_tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.INVISIBLE);
            rv_tv_right.setVisibility(View.INVISIBLE);
        }
        return visible;
    }


    public void setIvRight2(int resId) {
        iv_right_2.setImageResource(resId);
        setIvRight2Visibility(true);
    }

    public void setIvRight2(Drawable drawable) {
        iv_right_2.setImageDrawable(drawable);
        setIvRight2Visibility(true);
    }

    public void setIvRight2Listener(final OnClickListener listener) {
//        iv_left.setOnClickListener(listener);
        rv_iv_right_2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onClick(iv_right_2);
            }
        });
    }

    public boolean setIvRight2Visibility(Boolean visible) {
        if (visible) {
            iv_right_2.setVisibility(View.VISIBLE);
            rv_iv_right_2.setVisibility(View.VISIBLE);
        } else {
            iv_right_2.setVisibility(View.INVISIBLE);
            rv_iv_right_2.setVisibility(View.INVISIBLE);
        }
        return visible;
    }


    public TextView getTvTitle() {
        return tv_title;
    }

    public void setBgColor(int color) {
        ll_bg.setBackgroundColor(color);
    }

    public void setPaddindTop(int height) {
        ll_bg.setPadding(0, height, 0, 0);
    }


    public ImageView getIvRight2() {
        return iv_right_2;
    }
}
