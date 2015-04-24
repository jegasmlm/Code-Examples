package com.jega.money.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by jegasmlm on 4/14/2015.
 */
public class Animations {
    public static final int DURATION = 300;

    public static ValueAnimator animateWeight(final View view, final float from, final float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ((LinearLayout.LayoutParams) view.getLayoutParams()).weight = (Float) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (from == 0)
                    view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (to == 0)
                    view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(DURATION);
        return anim;
    }

    public static void hideByWeight(final View view) {
        final float from = ((LinearLayout.LayoutParams)view.getLayoutParams()).weight;
        final float to = 0;
        ValueAnimator valueAnimator = animateWeight(view, from, to);
        valueAnimator.start();
    }

    public static void showByWeight(final View view) {
        final float from = 0f;
        final float to = 1f;
        ValueAnimator valueAnimator = animateWeight(view, from, to);
        valueAnimator.start();
    }
}
