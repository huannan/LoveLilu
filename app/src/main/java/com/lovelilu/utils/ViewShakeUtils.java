package com.lovelilu.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import com.lovelilu.R;


/**
 * Created by huannan on 2016/6/29.
 * 返回的ObjectAnimator用于控制动画的状态，例如停止动画
 */
public class ViewShakeUtils {

    public static ObjectAnimator shakeView(View view, boolean isScale, int repeatCount) {
        ObjectAnimator animator = tada(view, isScale);
//        ValueAnimator.INFINITE
        animator.setRepeatCount(repeatCount);
        animator.start();
        return animator;
    }

    public static ObjectAnimator nopeView(View view,int repeatCount) {
        ObjectAnimator nopeAnimator = nope(view);
        nopeAnimator.setRepeatCount(repeatCount);
        nopeAnimator.start();
        return nopeAnimator;
    }

    public static ObjectAnimator tada(View view, boolean isScale) {
        return tada(view, 1f, isScale);
    }

    public static ObjectAnimator tada(View view, float shakeFactor, boolean isScale) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );


        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -5f * shakeFactor),
                Keyframe.ofFloat(.2f, -5f * shakeFactor),
                Keyframe.ofFloat(.3f, 5f * shakeFactor),
                Keyframe.ofFloat(.4f, -5f * shakeFactor),
                Keyframe.ofFloat(.5f, 5f * shakeFactor),
                Keyframe.ofFloat(.6f, -5f * shakeFactor),
                Keyframe.ofFloat(.7f, 5f * shakeFactor),
                Keyframe.ofFloat(.8f, -5f * shakeFactor),
                Keyframe.ofFloat(.9f, 5f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        if (isScale) {
            return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                    setDuration(1000);
        } else {
            return ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate).
                    setDuration(1000);
        }

    }

    public static ObjectAnimator nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }

}
