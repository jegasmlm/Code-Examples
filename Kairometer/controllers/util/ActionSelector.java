package com.jega.kairometer.controllers.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.RoleDAO;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.models.RoleModel;
import com.jega.kairometer.R;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 3/13/2015.
 */
public class ActionSelector extends PopupWindow {

    private FragmentActivity activity;
    private ViewGroup listContainer;
    private View rootView;
    private RoleModel roleModel;
    private ArrayList<RoleDAO> roles;
    private RoleDAO selectedRole;
    private OnActionSelectListener onActionSelectListener;

    public interface OnActionSelectListener {
        public void onActionSelect(RoleDAO role, ActionDAO action);
    }

    public void setOnActionSelectListener(OnActionSelectListener onActionSelectListener) {
        this.onActionSelectListener = onActionSelectListener;
    }


    public ActionSelector(Context context) {
        super(context);
        init((FragmentActivity) context);
        listContainer = (ViewGroup) rootView.findViewById(R.id.list);
        roleModel = new RoleModel(new DatabaseHelper(activity));
        roles = (ArrayList) roleModel.getAll();
        populateListWithRoles();
    }

    private void init(FragmentActivity context) {
        activity = (FragmentActivity) context;
        rootView = activity.getLayoutInflater().inflate(R.layout.list, null);
        setContentView(rootView);
        setFocusable(true);
        setHeight(ScrollView.LayoutParams.WRAP_CONTENT);
        setWidth(ScrollView.LayoutParams.WRAP_CONTENT);
    }

    private void populateListWithRoles() {
        listContainer.removeAllViews();
        for(RoleDAO role : roles)
            listContainer.addView(createRoleItem(role));
    }

    private View createRoleItem(final RoleDAO role) {
        View view = activity.getLayoutInflater().inflate(R.layout.item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRole = role;
                populateListWithActions(role);
            }
        });
        ((TextView)view.findViewById(R.id.name)).setText(role.getName());

        return view;
    }

    private void populateListWithActions(RoleDAO role) {
        listContainer.removeAllViews();
        for(ActionDAO action : role.getActions())
            listContainer.addView(createActionItem(action));
    }

    private View createActionItem(final ActionDAO action) {
        View view = activity.getLayoutInflater().inflate(R.layout.item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onActionSelectListener!=null)
                    onActionSelectListener.onActionSelect(selectedRole, action);
                dismiss();
            }
        });
        ((TextView)view.findViewById(R.id.name)).setText(action.getName());

        return view;
    }
}
