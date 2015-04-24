package com.jega.kairometer.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jega.kairometer.R;
import com.jega.kairometer.controllers.util.Misc;
import com.jega.kairometer.controllers.util.ScreenSlidePageFragment;
import com.jega.kairometer.customViews.TutorialPager;


/**
 * Created by jegasmlm on 3/28/2015.
 */
public class TutorialController extends Fragment {

    private TutorialPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        mPager = new TutorialPager(activity);
        mPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPager.setId(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnSwipeOutListener(new TutorialPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {

            }

            @Override
            public void onSwipeOutAtEnd() {
                Misc.replaceFragment(activity, new HomeController(), "home");
            }
        });
        return mPager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private int[] images = new int[] { R.drawable.tutorial_01, R.drawable.tutorial_02, R.drawable.tutorial_03, R.drawable.tutorial_04 };

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.newInstance(images[position]);
        }

        @Override
        public int getCount() {
            return images.length;
        }
    }
}
