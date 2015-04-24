package com.jega.kairometer.controllers;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jega.kairometer.controllers.util.Misc;
import com.jega.kairometer.customViews.Qualifier;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.RoleDAO;
import com.jega.kairometer.models.ActionModel;
import com.jega.kairometer.R;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.models.RoleModel;

public class RoleFormController extends Fragment {

    private FragmentActivity activity;
    private View view;
    private RelativeLayout addNewActionButton;
    private View contextMenuView;
	private EditText name;
	private Qualifier priority;
	private LinearLayout activityListContainer;
	private ImageView backButton;
	private Button saveButton;

	private ArrayList<Integer> actionUpdates;
	private ArrayList<ActionDAO> activityList;

	private RoleDAO role;
	
	private RoleModel roleModel;
	private ActionModel actionModel;
    private boolean updateForm = false;
    private long roleId;
    private TextView listValidationText;

    public static RoleFormController newForm(){
        RoleFormController roleFormController = new RoleFormController();

        Bundle args = new Bundle();
        args.putLong("id", -1);
        roleFormController.setArguments(args);

        return roleFormController;
    }

    public static RoleFormController newEditForm(long id){
        RoleFormController roleFormController = new RoleFormController();

        Bundle args = new Bundle();
        args.putLong("id", id);
        roleFormController.setArguments(args);

        return roleFormController;
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.role_form, container, false);
        if (getArguments() != null) {
            roleId = getArguments().getLong("id", -1);
            updateForm = roleId != -1;
        }
        return view;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        roleModel = new RoleModel(databaseHelper);
        actionModel = new ActionModel(databaseHelper);
        role = getRole();
        initFields();
        initLayoutAttributes();
        setLayoutAttributes();
    }

    private void initFields() {
        activityList = new ArrayList<ActionDAO>();
        actionUpdates = new ArrayList<Integer>();
    }

    private void initLayoutAttributes() {
        name = (EditText) view.findViewById(R.id.roleName);
        priority = (Qualifier) view.findViewById(R.id.rolePriority);
        activityListContainer = (LinearLayout) view.findViewById(R.id.activityList);
        listValidationText = (TextView) view.findViewById(R.id.listValidationText);
        backButton = (ImageView) view.findViewById(R.id.backButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        addNewActionButton = (RelativeLayout) view.findViewById(R.id.addActionButton);
    }

    private void setLayoutAttributes() {
        backButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                activity.getSupportFragmentManager().popBackStack();
            }
        });
        saveButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(validate()) {
                    save();
                    activity.getSupportFragmentManager().popBackStack();
                }
            }
        });
        addNewActionButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Misc.hideKeyboard(activity, name);
                addFragmentOnTop(ActionFormController.newForm(), "activityForm");
            }
        });
        if(updateForm) {
            name.setText(role.getName());
            priority.setLevel(role.getPriority());
            loadActionList();
        }
    }

    private boolean validate() {
        if (name.getText().toString().equals("")){
            name.setHintTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        if(activityListContainer.getChildCount() <= 1){
            listValidationText.setVisibility(View.VISIBLE);
            listValidationText.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        return true;
    }

    private RoleDAO getRole() {
        if (updateForm) {
            return (RoleDAO) roleModel.get(roleId);
        }
        else {
            return new RoleDAO();
        }
    }

    private void save() {
        Misc.hideKeyboard(activity, name);
        makeRole();
        if (updateForm) {
            roleModel.update(role);
            storeActions();
        }else{
            this.roleId = roleModel.insert(role);
            storeActions();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = activity.getMenuInflater();
        contextMenuView = v;
        inflater.inflate(R.menu.item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit:
                loadActionForm("roleForm");
                return true;
            case R.id.delete:
                actionUpdates.set(contextMenuView.getId(), 2);
                ((ViewGroup) contextMenuView.getParent()).removeView(contextMenuView);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void loadActionForm(String fromFragment) {
        ActionFormController activityForm = new ActionFormController();
        Bundle args = new Bundle();
        args.putLong("id", activityList.get(contextMenuView.getId()).getId());
        args.putString("from", fromFragment);
        activityForm.setArguments(args);
        addFragmentOnTop(activityForm, "activityForm");
    }

    private void addFragmentOnTop(Fragment fragmentOnTop, String tag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, fragmentOnTop, tag);
        fragmentTransaction.hide(this);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void makeRole(){
        this.role.setName(name.getText().toString());
        this.role.setPriority(priority.getLevel());
	}
	
	public void storeActions(){
		for(int i=0; i< actionUpdates.size(); i++){
            manageAction(i);
        }
	}

    private void manageAction(int index) {
        activityList.get(index).setRoleId(this.roleId);
        if(actionUpdates.get(index) == 0){
            actionModel.insert(activityList.get(index));
        }
        if(actionUpdates.get(index) == 1){
            actionModel.update(activityList.get(index));
        }
        if(actionUpdates.get(index) == 2){
            actionModel.delete(activityList.get(index).getId());
        }
    }

    public void loadActionList(){
		ArrayList<ActionDAO> actions = (ArrayList<ActionDAO>) role.getActions();
		for(int i=0; i<actions.size(); i++){
			addAction(actions.get(i));
		}
	}
	
	public void addAction(ActionDAO action){
        listValidationText.setTextColor(Color.parseColor("#9797A6"));
        listValidationText.setVisibility(View.GONE);
		activityList.add(action);
        if( !actionModel.exists(action) ) actionUpdates.add(0);
        else actionUpdates.add(-1);
        activityListContainer.addView(createActionRow(action));
	}

    private View createActionRow(ActionDAO action) {
        View container = activity.getLayoutInflater().inflate(R.layout.action_item, null);
        container.setId(activityList.size() - 1);
        registerForContextMenu(container);

        ((TextView) container.findViewById(R.id.name)).setText(action.getName());

        renderPriorityIcons(action, (LinearLayout) container.findViewById(R.id.priorityDetail));
        renderTime(action, (LinearLayout) container.findViewById(R.id.durationDetail));

        return container;
    }

    public void updateAction(ActionDAO action){
		activityList.set(contextMenuView.getId(), action);
        if( actionModel.exists(action) ) actionUpdates.set(contextMenuView.getId(), 1);

        ((TextView) contextMenuView.findViewById(R.id.name)).setText(action.getName());
        ((LinearLayout) contextMenuView.findViewById(R.id.priorityDetail)).removeAllViews();
		((LinearLayout) contextMenuView.findViewById(R.id.durationDetail)).removeAllViews();

        renderPriorityIcons(action, (LinearLayout) contextMenuView.findViewById(R.id.priorityDetail) );
        renderTime(action, (LinearLayout) contextMenuView.findViewById(R.id.durationDetail));
	}

    private LinearLayout renderPriorityIcons(ActionDAO action, LinearLayout container) {
        Resources resources = activity.getResources();

        for(int i=0; i< 3; i++){
            int drawableImageResource;
            if(i< action.getPriority())
                drawableImageResource = R.drawable.s_act_star_ic;
            else
                drawableImageResource = R.drawable.s_deact_star_ic;

            container.addView(Misc.createImageView(drawableImageResource, Misc.dpToPx(3, resources), resources, activity));
        }
        return container;
    }

    private void renderTime(ActionDAO action, LinearLayout durationDetail) {
        if(action.getType()== ActionDAO.FLEXIBLE){
            Resources resources = activity.getResources();

            for(int i=0; i< 5; i++){
                int drawableImageResource;
                if(i< (int) action.getTime())
                    drawableImageResource = R.drawable.s_act_rect_ic;
                else
                    drawableImageResource = R.drawable.s_deact_rect_ic;

                durationDetail.addView(Misc.createImageView(drawableImageResource, Misc.dpToPx(1, resources), resources, activity));
            }
        }else{
            addTextViewToContainer(action.getTime() + "", durationDetail);
        }
    }

    public ActionDAO getActivityToModify(){
		return activityList.get(contextMenuView.getId());
	}

    public void addTextViewToContainer(String text, LinearLayout container){
		LinearLayout.LayoutParams textViewParams;
		textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		TextView textView = new TextView(activity);
		textView.setText(text);
		textView.setLayoutParams(textViewParams);

		container.addView(textView);
	}
}
