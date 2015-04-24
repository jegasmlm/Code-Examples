package com.jega.kairometer.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jega.kairometer.R;
import com.jega.kairometer.controllers.util.ActionSelector;
import com.jega.kairometer.controllers.util.TimeSelector;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.RecordDAO;
import com.jega.kairometer.dao.RoleDAO;
import com.jega.kairometer.models.ActionModel;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.models.RecordModel;
import com.jega.kairometer.models.RoleModel;

/**
 * Created by jegasmlm on 3/12/2015.
 */
public class RecordFormController extends Fragment {

    public static final int NEW_ID = -1;

    private View rootView;

    private long id;
    private FragmentActivity activity;
    private TextView actionTextView;
    private TextView roleTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private RecordModel recordModel;
    private RecordDAO record;
    private ActionModel actionModel;
    private RoleModel roleModel;
    private ActionDAO action;
    private RoleDAO role;
    private boolean isAnEditForm;
    private ImageView backButton;
    private Button saveButton;
    private View fromContainer;
    private View toContainer;
    private View actionContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.record_form, container, false);
        loadArguments();
        iniLayoutAttrs();
        setLayoutAttrs();
        iniModels();
        iniDAOs();
        if(isAnEditForm)
            loadDAOsIntoLayout();
        return rootView;
    }

    public static RecordFormController newForm(){
        RecordFormController recordFormController = new RecordFormController();

        Bundle args = new Bundle();
        args.putLong("id", NEW_ID);
        recordFormController.setArguments(args);

        return recordFormController;
    }

    public static RecordFormController newEditForm(long id){
        RecordFormController recordFormController = new RecordFormController();

        Bundle args = new Bundle();
        args.putLong("id", id);
        recordFormController.setArguments(args);

        return recordFormController;
    }

    private void loadArguments(){
        this.id = getArguments().getLong("id");
        this.isAnEditForm = id != NEW_ID;
    }

    private void iniLayoutAttrs() {
        this.activity = getActivity();
        backButton = (ImageView) rootView.findViewById(R.id.backButton);
        saveButton = (Button) rootView.findViewById(R.id.saveButton);
        actionTextView = (TextView) rootView.findViewById(R.id.actionName);
        roleTextView = (TextView) rootView.findViewById(R.id.roleName);
        fromTextView = (TextView) rootView.findViewById(R.id.from);
        toTextView = (TextView) rootView.findViewById(R.id.to);
        actionContainer = rootView.findViewById(R.id.actionContainer);
        fromContainer = rootView.findViewById(R.id.fromContainer);
        toContainer = rootView.findViewById(R.id.toContainer);
    }

    private void iniModels() {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        recordModel = new RecordModel(databaseHelper);
        actionModel = new ActionModel(databaseHelper);
        roleModel = new RoleModel(databaseHelper);
    }

    private void iniDAOs() {
        if(isAnEditForm) {
            this.record = (RecordDAO) recordModel.get(this.id);
            this.action = (ActionDAO) actionModel.get(record.getActionId());
            this.role = (RoleDAO) roleModel.get(action.getRoleId());
        }else {
            record = new RecordDAO();
            action = new ActionDAO();
            role = new RoleDAO();
        }
    }

    private void setLayoutAttrs() {
        fromTextView.setText("-");
        toTextView.setText("-");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                goBack();

            }
        });
        actionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActionSelector();
            }
        });
        fromContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeSelector(new TimeSelector.OnSaveListener() {
                    @Override
                    public void onSave(long time) {
                        record.setActivationTime(time);
                        fromTextView.setText(RecordDAO.formatTime(time));
                    }
                });
            }
        });
        toContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeSelector(new TimeSelector.OnSaveListener() {
                    @Override
                    public void onSave(long time) {
                        record.setDeactivationTime(time);
                        toTextView.setText(RecordDAO.formatTime(time));
                    }
                });
            }
        });
    }

    private void goBack() {
        activity.getSupportFragmentManager().popBackStack();
    }

    private void save() {
        if(isAnEditForm)
            recordModel.update(record);
        else
            recordModel.insert(record);
    }

    private void createActionSelector() {
        ActionSelector actionSelector = new ActionSelector(activity);
        actionSelector.setOnActionSelectListener(new ActionSelector.OnActionSelectListener() {
            @Override
            public void onActionSelect(RoleDAO role, ActionDAO action) {
                record.setActionId(action.getId());
                actionTextView.setText(action.getName());
                roleTextView.setText(role.getName());
            }
        });
        actionSelector.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    private void createTimeSelector(TimeSelector.OnSaveListener onSaveListener) {
        TimeSelector timeSelector;
        if(isAnEditForm)
            timeSelector = new TimeSelector(activity, record.getDeactivationTime());
        else
            timeSelector = new TimeSelector(activity);
        timeSelector.setOnSaveListener(onSaveListener);
        timeSelector.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    private void loadDAOsIntoLayout() {
        actionTextView.setText(action.getName());
        roleTextView.setText(role.getName());
        fromTextView.setText(RecordDAO.formatTime(record.getActivationTime()));
        toTextView.setText(RecordDAO.formatTime(record.getDeactivationTime()));
    }

}
