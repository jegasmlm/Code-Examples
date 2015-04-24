package com.jega.kairometer.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jega.kairometer.controllers.util.Misc;
import com.jega.kairometer.customViews.InteractiveSpinner;
import com.jega.kairometer.customViews.Qualifier;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.models.ActionModel;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.R;
import com.jega.kairometer.util.TimeKeeper;


public class ActionFormController extends Fragment {

    private FragmentActivity activity = getActivity();

    private View view;
	private EditText name;
	private Qualifier priority;
	private InteractiveSpinner type;
	private Qualifier flexTime;
	private EditText fixedTime;
    private InteractiveSpinner frequency;
    private CheckBox unTrack;

	private LinearLayout fixedDurationSection;
	private RelativeLayout flexDurationRow;
	private FrameLayout durationRow;
	private ImageView backButton;
	private Button saveButton;

	private ActionDAO action;
	ActionModel actionModel;

    private boolean isAnEditForm = false;
    private String previewView;
    private long actionId;
    private TextView availableFixedTime;

    public static Fragment newForm(){
        Fragment fragment = new ActionFormController();

        Bundle args = new Bundle();
        args.putLong("id", -1);
        fragment.setArguments(args);

        return fragment;
    }

    public static Fragment newEditForm(long id){
        Fragment fragment = new ActionFormController();

        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.action_form, container, false);
        if (getArguments() != null) {
            previewView = getArguments().getString("from", "home");
            actionId = getArguments().getLong("id", -1);
            isAnEditForm = actionId != -1;
        }
        return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        actionModel = new ActionModel(new DatabaseHelper(activity));
        this.action = getAction();
        initLayoutAttributes();
        setLayoutAttributes();
        if(isAnEditForm)
            loadDAOIntoLayout();
    }

    private ActionDAO getAction() {
        return isAnEditForm ? previewView.equals("roleForm")
                ? ((RoleFormController) activity.getSupportFragmentManager().findFragmentByTag("roleForm")).getActivityToModify()
                : (ActionDAO) actionModel.get(actionId) : new ActionDAO();
    }

    private void initLayoutAttributes() {
        name = (EditText) view.findViewById(R.id.actionName);
        priority = (Qualifier) view.findViewById(R.id.actionPriority);
        type = (InteractiveSpinner) view.findViewById(R.id.durationType);
        flexTime = (Qualifier) view.findViewById(R.id.flexDuration);
        flexDurationRow = (RelativeLayout) view.findViewById(R.id.flexDurationRow);
        durationRow = (FrameLayout) view.findViewById(R.id.durationRow);
        fixedDurationSection = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.fixed_duration_form, null);
        fixedTime = (EditText) fixedDurationSection.findViewById(R.id.fixedDuration);
        availableFixedTime = (TextView) fixedDurationSection.findViewById(R.id.availableTime);
        frequency = (InteractiveSpinner) fixedDurationSection.findViewById(R.id.frequency);
        unTrack = (CheckBox) view.findViewById(R.id.unTracked);
        backButton = (ImageView) view.findViewById(R.id.backButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
    }

    private void setLayoutAttributes() {
        fixedDurationSection.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        type.setOnChangeListener(new InteractiveSpinner.onChangeListener() {
            @Override
            public void onChange() {
                switchDurationRow();
            }
        });
        availableFixedTime.setText("weekly time available " + (TimeKeeper.TOTAL_TIME - HomeController.timeKeeper.getTotalFixedDuration()));
        backButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                goBack();
            }
        });
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(validate()) {
                    save();
                    goBack();
                }
            }
        });
    }

    private boolean validate() {
        if( name.getText().toString().equals("")) {
            name.setHintTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        if(type.getSelectedItem().toString().equals("Fixed")) {
            if (fixedTime.getText().toString().equals("")) {
                availableFixedTime.setTextColor(Color.parseColor("#FF0000"));
                return false;
            } else if (Float.parseFloat(fixedTime.getText().toString()) > (TimeKeeper.TOTAL_TIME - HomeController.timeKeeper.getTotalFixedDuration())) {
                availableFixedTime.setTextColor(Color.parseColor("#FF0000"));
                return false;
            }
        }
        return true;
    }

    private void goBack() {
        activity.getSupportFragmentManager().popBackStack();
    }

    private void loadDAOIntoLayout() {
        name.setText(this.action.getName());
        priority.setLevel(this.action.getPriority());
        if (this.action.getType() == ActionDAO.FLEXIBLE) {
            flexTime.setLevel((int) this.action.getTime());
        } else {
            type.selectItem(this.action.getType());
            fixedTime.setText(this.action.getTime() + "");
        }
        frequency.selectItem(getFrequencyIndexItem());
        unTrack.setChecked(action.isUnTrack());
    }

    private void save() {
        Misc.hideKeyboard(activity, name);
        makeAction();
        if(isAnEditForm){
            if(getArguments().getString("from", "home").equals("roleForm")){
                ((RoleFormController) activity.getSupportFragmentManager().findFragmentByTag("roleForm")).updateAction(action);
            }else{
                actionModel.update(action);
            }
        }else{
            ((RoleFormController) activity.getSupportFragmentManager().findFragmentByTag("roleForm")).addAction(action);
        }
    }

    private void switchDurationRow() {
        if(type.getSelectedItem().equals("Flexible")){
            durationRow.removeView(fixedDurationSection);
            durationRow.addView(flexDurationRow);
        }else{
            durationRow.removeView(flexDurationRow);
            durationRow.addView(fixedDurationSection);
        }
    }

    public void makeAction(){
		action.setName(name.getText().toString());
		action.setPriority(priority.getLevel());
		if(type.getSelectedItem().equals("Flexible")){
			action.setType(ActionDAO.FLEXIBLE);
			action.setTime((float) flexTime.getLevel());
		}
		else{
			action.setType(ActionDAO.FIXED);
			action.setTime(Float.parseFloat(fixedTime.getText().toString()));
		}
        action.setFrequency(getFrequency());
        action.setUnTrack(unTrack.isChecked());
	}

    public int getFrequency(){
        switch (frequency.getSelectedIndex()){
            case 2:
                return ActionDAO.DAILY;
            case 1:
                return ActionDAO.WEEKLY;
        }
        throw new IllegalArgumentException("Frequency does not exist");
    }

    public int getFrequencyIndexItem(){
        switch(action.getFrequency()){
            case ActionDAO.DAILY:
                return 2;
            case ActionDAO.WEEKLY:
                return 1;
        }
        throw new IllegalArgumentException("Frequency does not exist: " + action.getFrequency());
    }
}
