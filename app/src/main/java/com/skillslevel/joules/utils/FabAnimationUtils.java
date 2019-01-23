package com.skillslevel.joules.utils;


import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.skillslevel.joules.R;

public class FabAnimationUtils {
    private static final long DEFAULT_DURATION = 200L;
    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    public static void scaleIn(final View fab){
        scaleIn(fab, DEFAULT_DURATION, null);
    }

    public static void scaleIn(final View fab, long duration, final ScaleCallback callback) {
        Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.scale);
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                if (callback != null) callback.onAnimationStart();
            }

            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.INVISIBLE);
                if (callback != null) callback.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //
            }
        });
        fab.startAnimation(anim);


    }

    public static void scaleOut(final View fab) {
        scaleOut(fab, DEFAULT_DURATION, null);
    }

    public static void scaleOut(final View fab, final ScaleCallback callback) {
        scaleOut(fab, DEFAULT_DURATION, callback);
    }

    public static void scaleOut(final View fab, long duration, final ScaleCallback callback) {
            Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.slide_out_to_right);
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(duration);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    if (callback != null) callback.onAnimationStart();
                }

                public void onAnimationEnd(Animation animation) {
                    fab.setVisibility(View.INVISIBLE);
                    if (callback != null) callback.onAnimationEnd();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    //
                }
            });
            fab.startAnimation(anim);

    }


    public interface ScaleCallback {
        void onAnimationStart();

        void onAnimationEnd();
    }

}
