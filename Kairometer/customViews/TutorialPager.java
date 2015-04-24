package com.jega.kairometer.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by jegasmlm on 3/28/2015.
 */

public class TutorialPager extends ViewPager {

    float mStartDragX;
    OnSwipeOutListener mListener;

    public TutorialPager(Context context) {
        super(context);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x && getCurrentItem() == 0) {
                    mListener.onSwipeOutAtStart();
                } else if (mStartDragX > x && getCurrentItem() == getAdapter().getCount() - 1) {
                    mListener.onSwipeOutAtEnd();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnSwipeOutListener {
        public void onSwipeOutAtStart();
        public void onSwipeOutAtEnd();
    }

}