package com.jega.kairometer.controllers.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jega.kairometer.R;
import com.jega.kairometer.dao.DAO;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jegasmlm on 3/1/2015.
 */
public class Misc {

    public static void replaceFragment(FragmentActivity activity, Fragment fragment, String tag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static LinearLayout imageViewRow(int n, Context context){
        Resources resources = context.getResources();
        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        for(int i=0; i<n; i++){
			container.addView(createImageView(DAOLayout.PRIORITY_IC, dpToPx(DAOLayout.PRIORITY_ICON_MARGIN, resources), resources, context));
		}

        return container;
	}

    public static View createImageView(int r, int margin, Resources resources, Context context){
        Drawable drawable = resources.getDrawable(r);

        LinearLayout.LayoutParams ImageViewParams;
        ImageViewParams = new LinearLayout.LayoutParams(drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());
        ImageViewParams.setMargins(margin, 0, margin, 0);

        ImageView imageView = new ImageView(context);

        imageView.setImageDrawable(drawable);
        imageView.setLayoutParams(ImageViewParams);

        return imageView;
    }

    public static int dpToPx(float value, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.getDisplayMetrics());
    }

    public static void loadUpdateForm(DAO DAO, FragmentActivity activity, Fragment fragment, String tag) {
        Bundle args = new Bundle();
        args.putLong("id", DAO.getId());
        fragment.setArguments(args);
        replaceFragment(activity, fragment, tag);
    }

    public static boolean isInThisWeek(long time){
        Calendar startWeekDate = Calendar.getInstance();
        startWeekDate.setFirstDayOfWeek(Calendar.SUNDAY);
        startWeekDate.set(Calendar.DAY_OF_WEEK, startWeekDate.getFirstDayOfWeek());
        Calendar endWeekDate = (Calendar) startWeekDate.clone();
        endWeekDate.add(Calendar.DATE, 7);
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(time);
        if(date.compareTo(startWeekDate) >= 0)
            if(date.compareTo(endWeekDate) < 0)
                return true;
        return false;
    }
}
