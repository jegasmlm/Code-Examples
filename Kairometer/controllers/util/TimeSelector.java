package com.jega.kairometer.controllers.util;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jega.kairometer.customViews.InteractiveSpinner;
import com.jega.kairometer.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by jegasmlm on 3/12/2015.
 */
public class TimeSelector extends PopupWindow {

    private View rootView;
    private Button saveButton;
    private Button cancelButton;
    private InteractiveSpinner day;
    private InteractiveSpinner month;
    private InteractiveSpinner year;
    private InteractiveSpinner hour;
    private InteractiveSpinner minute;
    private InteractiveSpinner second;
    private Calendar time;
    private OnSaveListener onSaveListener;

    public interface OnSaveListener {
        public void onSave(long time);
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public TimeSelector(Context context) {
        super(context);
        init(context);
        loadLayoutAttrs();
        time = Calendar.getInstance();
        setLayoutAttrs();
        loadTimeInLayout();
    }

    public TimeSelector(Context context, long time) {
        super(context);
        init(context);
        loadLayoutAttrs();
        setTimeInMillis(time);
        setLayoutAttrs();
        loadTimeInLayout();
    }

    private void init(Context context) {
        rootView = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.time_selector, null);
        setContentView(rootView);
        setFocusable(true);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void loadLayoutAttrs() {
        day = (InteractiveSpinner) rootView.findViewById(R.id.day);
        month = (InteractiveSpinner) rootView.findViewById(R.id.month);
        year = (InteractiveSpinner) rootView.findViewById(R.id.year);
        hour = (InteractiveSpinner) rootView.findViewById(R.id.hour);
        minute = (InteractiveSpinner) rootView.findViewById(R.id.minute);
        second = (InteractiveSpinner) rootView.findViewById(R.id.second);
        saveButton = (Button) rootView.findViewById(R.id.saveButton);
        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
    }

    private void setLayoutAttrs() {
        month.setOnChangeListener(new InteractiveSpinner.onChangeListener() {
            @Override
            public void onChange() {
                updateMaxDay();
            }
        });
        year.setOnChangeListener(new InteractiveSpinner.onChangeListener() {
            @Override
            public void onChange() {
                updateMaxDay();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar date = new GregorianCalendar();
                date.set(Calendar.DAY_OF_MONTH, day.getSelectedIndex()-1);
                date.set(Calendar.MONTH, month.getSelectedIndex()-1);
                date.set(Calendar.YEAR, year.getSelectedIndex()-1);
                date.set(Calendar.HOUR_OF_DAY, hour.getSelectedIndex()-1);
                date.set(Calendar.MINUTE, minute.getSelectedIndex()-1);
                date.set(Calendar.SECOND, second.getSelectedIndex()-1);
                if(onSaveListener!=null)
                    onSaveListener.onSave(date.getTimeInMillis());
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        updateMaxDay();
    }

    private void loadTimeInLayout() {
        day.selectItem(time.get(Calendar.DAY_OF_MONTH) + 1);
        month.selectItem(time.get(Calendar.MONTH) + 1);
        year.selectItem(time.get(Calendar.YEAR) + 1);
        hour.selectItem(time.get(Calendar.HOUR_OF_DAY) + 1);
        minute.selectItem(time.get(Calendar.MINUTE) + 1);
        second.selectItem(time.get(Calendar.SECOND) + 1);
    }

    private void updateMaxDay() {
        day.setMax(new GregorianCalendar(1, month.getSelectedIndex()-1, year.getSelectedIndex()).getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time){
        this.time = time;
    }

    public void setTimeInMillis(long millis) {
        this.time = new GregorianCalendar();
        this.time.setTimeInMillis(millis);
    }
}
