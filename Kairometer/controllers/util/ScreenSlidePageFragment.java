package com.jega.kairometer.controllers.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jega.kairometer.R;

/**
 * Created by jegasmlm on 3/28/2015.
 */

public class ScreenSlidePageFragment extends Fragment {

    private ImageView image;
    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.tutorial_view, container, false);
        image = (ImageView) rootView.findViewById(R.id.imageName);
        this.image.setImageResource(getArguments().getInt("imageResource"));
        return rootView;
    }

    public static ScreenSlidePageFragment newInstance(int resource){
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("imageResource", resource);
        screenSlidePageFragment.setArguments(args);
        return screenSlidePageFragment;
    }
}
