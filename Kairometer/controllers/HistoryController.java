package com.jega.kairometer.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jega.kairometer.R;
import com.jega.kairometer.controllers.util.Misc;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.RecordDAO;
import com.jega.kairometer.dao.RoleDAO;
import com.jega.kairometer.models.ActionModel;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.models.RecordModel;
import com.jega.kairometer.models.RoleModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by jegasmlm on 3/3/2015.
 */
public class HistoryController extends Fragment {

    private FragmentActivity activity;
    private View rootView;
    private ViewGroup recordList;

    private RecordModel recordModel;
    private ActionModel actionModel;

    private ArrayList<RecordDAO> records;
    private ImageView backButton;
    private View contextMenuView;
    private RoleModel roleModel;
    private View addNewRecordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.rootView = inflater.inflate(R.layout.history_view, container, false);
        this.activity = getActivity();
        loadModels();
        records = (ArrayList) recordModel.getAllRecordsOrdered();
        loadLayoutAttrs();
        setLayoutAttrs();
        createRecordList();
        return rootView;
    }

    private void loadModels() {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        recordModel = new RecordModel(databaseHelper);
        actionModel = new ActionModel(databaseHelper);
        roleModel = new RoleModel(databaseHelper);
    }

    private void loadLayoutAttrs() {
        addNewRecordButton = rootView.findViewById(R.id.addRecordButton);
        recordList = (ViewGroup) rootView.findViewById(R.id.record_list);
        backButton = (ImageView) rootView.findViewById(R.id.backButton);
    }

    private void setLayoutAttrs() {
        addNewRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Misc.replaceFragment(activity, RecordFormController.newForm(), "recordForm");
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                activity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = activity.getMenuInflater();
        contextMenuView = v;
        inflater.inflate(R.menu.item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                Misc.replaceFragment(activity, RecordFormController.newEditForm(contextMenuView.getId()), "history");
                return true;
            case R.id.delete:
                recordModel.delete(contextMenuView.getId());
                ((ViewGroup) contextMenuView.getParent()).removeView(contextMenuView);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void createRecordList(){
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(0);
        for(RecordDAO record : records) {
            if ( !record.isActive() && Misc.isInThisWeek(record.getDeactivationTime()) ) {
                Calendar recordDate = new GregorianCalendar();
                recordDate.setTimeInMillis(record.getDeactivationTime());
                if (!areInTheSameDay(date, recordDate)) {
                    TextView dateText = new TextView(activity);
                    dateText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    dateText.setText(RecordDAO.formatTime(recordDate.getTimeInMillis()));
                    dateText.setPadding(0, Misc.dpToPx(5, activity.getResources()), 0, Misc.dpToPx(5, activity.getResources()));
                    recordList.addView(dateText);
                    date = recordDate;
                }
                ActionDAO action = (ActionDAO) actionModel.get(record.getActionId());
                recordList.addView(createRecordItem(((RoleDAO) roleModel.get(action.getRoleId())).getName(), action.getName(), record));
            }
        }
    }

    private boolean areInTheSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.DAY_OF_MONTH)==date2.get(Calendar.DAY_OF_MONTH) && date1.get(Calendar.MONTH)==date2.get(Calendar.MONTH) && date1.get(Calendar.YEAR)==date2.get(Calendar.YEAR);
    }

    private View createRecordItem(String roleName, String actionName, RecordDAO record) {
        ViewGroup container = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.record_item, null);
        container.setId((int) record.getId());
        registerForContextMenu(container);

        ((TextView) container.findViewById(R.id.actionName)).setText(actionName);
        ((TextView) container.findViewById(R.id.roleName)).setText(roleName);
        ((TextView) container.findViewById(R.id.activation_time)).setText("From: " + RecordDAO.formatTime(record.getActivationTime()));
        ((TextView) container.findViewById(R.id.deactivation_time)).setText("To: " + RecordDAO.formatTime(record.getDeactivationTime()));

        return container;
    }

}
