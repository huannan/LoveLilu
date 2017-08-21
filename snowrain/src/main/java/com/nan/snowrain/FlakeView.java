/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nan.snowrain;

import java.util.ArrayList;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

//import com.jakewharton.nineoldandroids.sample.R;
//import com.nineoldandroids.animation.ValueAnimator;
//import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This class has
 * all of the logic for adding, subtracting, and rendering Droidflakes.
 */
public class FlakeView extends View {

    private final int mBaseSpeed;
    Bitmap droid;       // The bitmap that all flakes use
    Bitmap droid2;
    Bitmap droid3;
    Bitmap droid4;
    Bitmap droid5;
    int numFlakes = 0;  // Current number of flakes
    ArrayList<Flake> flakes = new ArrayList<>(); // List of current flakes

    // Animator used to drive all separate flake animations. Rather than have potentially
    // hundreds of separate animators, we just use one and then update all flakes for each
    // frame of that single animation.
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // Used to track elapsed time for animations and fps
    int frames = 0;     // Used to track frames per second
    Paint textPaint;    // Used for rendering fps text
    float fps = 0;      // frames per second
    Matrix m = new Matrix(); // Matrix used to translate/rotate each flake during rendering
    String fpsString = "";
    String numFlakesString = "";

    /**
     * Constructor. Create objects used throughout the life of the View: the Paint and
     * the animator
     */
    public FlakeView(Context context, int baseSpeed) {
        super(context);

        mBaseSpeed = baseSpeed;

        droid = BitmapFactory.decodeResource(getResources(), R.drawable.icon_1);
        droid2 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_2);
        droid3 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_3);
        droid4 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_4);
        droid5 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_5);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);

        // This listener is where the action is for the flak animations. Every frame of the
        // animation, we calculate the elapsed time and update every flake's position and rotation
        // according to its speed.
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y += (flake.speed * secs);
                    if (flake.y > getHeight()) {
                        // If a flake falls off the bottom, send it back to the top
                        flake.y = 0 - flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                // Force a redraw to see the flakes in their new positions and orientations
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
    }

    public int getNumFlakes() {
        if (numFlakes > 75) {
            return 75;
        }
        return numFlakes;
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }

    /**
     * Add the specified number of droidflakes.
     */
    public void addFlakes(int quantity) {
        if (numFlakes <= 75)//这里定义花瓣的数量以及花色样式
        {
            for (int i = 0; i < quantity; ++i) {
                if (i % 5 == 0)
                    flakes.add(Flake.createFlake(getWidth(), droid, mBaseSpeed));
                else if (i % 5 == 1)
                    flakes.add(Flake.createFlake(getWidth(), droid2, mBaseSpeed));
                else if (i % 5 == 2)
                    flakes.add(Flake.createFlake(getWidth(), droid3, mBaseSpeed));
                else if (i % 5 == 3)
                    flakes.add(Flake.createFlake(getWidth(), droid4, mBaseSpeed));
                else
                    flakes.add(Flake.createFlake(getWidth(), droid5, mBaseSpeed));
            }
            setNumFlakes(numFlakes + quantity);
        }
    }

    /**
     * Subtract the specified number of droidflakes. We just take them off the end of the
     * list, leaving the others unchanged.
     */
    public void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            int index = numFlakes - i - 1;
            if (index < 0 || index >= numFlakes) {
                return;
            }
            flakes.remove(index);
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Reset list of droidflakes, then restart it with 8 flakes
        flakes.clear();
        numFlakes = 0;
        addFlakes(0);
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to rotate around its center),
        // rotate by its current rotation, translate by its location, then draw its bitmap
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width / 2, -flake.height / 2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }
        // fps counter: count how many frames we draw and once a second calculate the
        // frames per second
        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = deltaTime / 1000f;
            fps = frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }
        //pfs和总的花瓣数量

//        canvas.drawText(numFlakesString, getWidth() - 200, getHeight() - 50, textPaint);
//        canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);
    }

    public void pause() {
        // Make sure the animator's not spinning in the background when the activity is paused.
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

}
