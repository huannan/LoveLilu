package com.lovelilu.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.lovelilu.Constants;
import com.lovelilu.R;
import com.lovelilu.ui.activity.base.BaseActivity;
import com.lovelilu.utils.ToastUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by huannan on 2016/8/25.
 */
public class ImageDetailActivity extends BaseActivity {

    @BindView(R.id.pv_image)
    PhotoView pv_image;
    @BindView(R.id.gif_weini)
    GifImageView gif_weini;
    private String mImageUrl;
    private String mImageDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);

        init();
    }


    public void init() {

        setTranslucent();
        mImageUrl = getIntent().getStringExtra(Constants.KEY.IMAGE_URL);
        mImageDesc = getIntent().getStringExtra(Constants.KEY.IMAGE_DESC);

        if (!TextUtils.isEmpty(mImageDesc)) {
            ToastUtils.showLong(this, mImageDesc);
        }

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


        initPhotoView();
    }

    private void initPhotoView() {
        pv_image.enable();
        pv_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //加载图片
        Picasso.with(this).load(mImageUrl).into(pv_image, new Callback() {
            @Override
            public void onSuccess() {
                gif_weini.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                ToastUtils.showShort(ImageDetailActivity.this, R.string.http_failed);
            }
        });

    }


}
