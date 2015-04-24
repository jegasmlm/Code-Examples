package com.jega.kairometer.controllers.util;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jega.kairometer.R;
import com.jega.kairometer.customViews.ProgressBar;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.DAO;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.models.RecordModel;
import com.jega.kairometer.util.TimeKeeper;

import java.util.Calendar;

/**
 * Created by jegasmlm on 3/1/2015.
 */
public class ActionIndicatorLayout extends DAOLayout {

    private View root;
    private ViewGroup actionContainer;
    private TextView actionName;
    private TextView notificationTV;
    private ProgressBar progressBar;
    private ViewGroup priorityIconList;
    private TextView usageTV;

    private Handler mHandler;

    private Runnable runnable = new Runnable(){
        @Override
        public void run() {
            updateNotification();
            mHandler.postDelayed(runnable, 1000);
        }
    };
    private RecordModel recordModel;

    public ActionIndicatorLayout(ActionDAO action, Fragment fragment) {
        super(fragment);
        recordModel = new RecordModel(new DatabaseHelper(activity));
        setPadding(0, 0, 0, Misc.dpToPx(5, resources));
        DAO = action;
        mHandler = new Handler();
        mHandler.post(runnable);
        initLayoutAttributes();
        setLayoutAttributes();
    }

    private void initLayoutAttributes() {
        root = inflater.inflate(R.layout.action_indicator_view, this);
        actionContainer = (ViewGroup) root.findViewById(R.id.actionContainer);
        actionName = (TextView) root.findViewById(R.id.actionName);
        notificationTV = (TextView) root.findViewById(R.id.hrsLeft);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        priorityIconList = (ViewGroup) root.findViewById(R.id.actionPriorityIcons);
        usageTV = (TextView) root.findViewById(R.id.usage);
    }

    private void setLayoutAttributes() {
        final ActionDAO action = (ActionDAO) DAO;
        progressBar.setTag("action");
        fragment.registerForContextMenu(progressBar);
        actionName.setText(action.getName());
        progressBar.setSize(getAvailableTime());
        progressBar.setProgress(action.getTimeSpent(Calendar.getInstance().getTimeInMillis()));
        if (action.isActive()) {
            progressBar.activate();
            progressBar.setProgress(progressBar.getProgress() + action.timeSinceActivation());
        }
        progressBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar) v;
                if(progressBar.isEnabled()) {
                    if (progressBar.isActive()){
                        progressBar.deactivate();
                        action.deactivate();
                        recordModel.update(action.getLastRecord());
                    }else{
                        progressBar.activate();
                        action.activate();
                        action.getLastRecord().setId(recordModel.insert(action.getLastRecord()));
                    }
                }

            }
        });
        priorityIconList.addView(Misc.imageViewRow(action.getPriority(), activity));
        updateNotification();
        usageTV.setText(getAvailableTime()+"");
    }

    public void updateNotification() {
        float timeLeft = progressBar.getSize() - progressBar.getProgress();
        String notification;
        if(timeLeft > 1)
            notification = (int)timeLeft + " hrs left";
        else if(timeLeft == 1)
            notification = (int)timeLeft + " hr left";
        else if(timeLeft >= 0)
            notification = "less than 1 hr left";
        else if(timeLeft >= -1){
            notification = "over less than 1 hr";
        }else
            notification = "over " + (int)timeLeft + " hr";
        notificationTV.setText(notification);
    }

    @Override
    public View getLayout() {
        return root;
    }

    @Override
    public DAO getData() {
        return this.DAO;
    }

    @Override
    public void setData(DAO DAO) {
        this.DAO = (ActionDAO) DAO;
    }

    public boolean isNewThisWeek(){
        return Misc.isInThisWeek(((ActionDAO) DAO).getCreatedTime());
    }

    private float getAvailableTime() {
        if(isNewThisWeek()) {
            Calendar endWeekDate = Calendar.getInstance();
            endWeekDate.setFirstDayOfWeek(Calendar.SUNDAY);
            endWeekDate.set(Calendar.DAY_OF_WEEK, endWeekDate.getFirstDayOfWeek());
            endWeekDate.set(Calendar.HOUR_OF_DAY, 0);
            endWeekDate.set(Calendar.MINUTE, 0);
            endWeekDate.set(Calendar.SECOND, 0);
            endWeekDate.set(Calendar.MILLISECOND, 0);
            endWeekDate.add(Calendar.DATE, 7);
            float timeLeft = (float) (endWeekDate.getTimeInMillis() - ((ActionDAO) DAO).getCreatedTime()) / (1000f * 60f * 60f);
            return timeLeft * ((ActionDAO) DAO).getFixedTime() / TimeKeeper.TOTAL_TIME;
        }else{
            return ((ActionDAO) DAO).getFixedTime();
        }
    }
}
