package com.lovelilu.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.lovelilu.R;
import com.squareup.picasso.Picasso;

/**
 * Created by huannan on 2016/3/31.
 * 自定义Slider的显示，只使用URL来加载
 */
public class ImageSliderView extends BaseSliderView {

    private Picasso mpPicasso;
    private OnSliderClickListener mListener;

    public ImageSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.v_slider, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }

    @Override
    protected void bindEventAndShow(View v, final ImageView targetImageView) {
        if (mpPicasso == null) {
            mpPicasso = Picasso.with(mContext);
        }
        mpPicasso.load(getUrl()).into(targetImageView);

        targetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onclick(targetImageView);
                }
            }
        });
    }

    public void setOnSliderClickListener(OnSliderClickListener listener) {

        mListener = listener;

    }

    public interface OnSliderClickListener {

        void onclick(ImageView v);

    }

}
