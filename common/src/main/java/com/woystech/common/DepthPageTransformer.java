package com.woystech.common;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by firok on 7/24/2016.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) {// [-Infinity,-1)
            // This view is way off-screen to the left.
            //   view.setAlpha(0);
            ViewHelper.setAlpha(view, 0);
        } else if (position <= 0) {
            // Use the default slide transition when moving to the left view
            //view.setAlpha(1);
            ViewHelper.setAlpha(view, 1);
            //view.setTranslationX(0);
            ViewHelper.setTranslationX(view, 0);
            //view.setScaleX(1);
            ViewHelper.setScaleX(view, 1);
            //view.setScaleY(1);
            ViewHelper.setScaleY(view, 1);
        } else if (position <= 1) { // (0,1]
            // Fade the view out.
            //view.setAlpha(1 - position);
            ViewHelper.setAlpha(view, 1 - position);

            // Counteract the default slide transition
            //view.setTranslationX(pageWidth * -position);
            ViewHelper.setTranslationX(view, pageWidth * -position);

            // Scale the view down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            //view.setScaleX(scaleFactor);
            //view.setScaleY(scaleFactor);
            ViewHelper.setScaleX(view, scaleFactor);
            ViewHelper.setScaleY(view, scaleFactor);

        } else { // (1,+Infinity]
            // This view is way off-screen to the right.
            //view.setAlpha(0);
            ViewHelper.setAlpha(view, 0);
        }

    }
}
