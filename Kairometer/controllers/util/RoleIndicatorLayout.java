package com.jega.kairometer.controllers.util;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jega.kairometer.R;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.DAO;
import com.jega.kairometer.dao.RoleDAO;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 3/1/2015.
 */
public class RoleIndicatorLayout extends DAOLayout {

    private View root;
    private View roleHeader;
    private ViewGroup roleContainer;
    private TextView roleName;
    private ViewGroup priorityIconList;
    private ViewGroup actionList;
    private ImageView expandButton;
    private TextView usageTV;

    private ArrayList<ActionIndicatorLayout> actionIndicatorLayouts;

    public RoleIndicatorLayout(RoleDAO role, Fragment fragment) {
        super(fragment);
        setPadding(Misc.dpToPx(10, resources), Misc.dpToPx(10, resources), Misc.dpToPx(10, resources), 0);
        actionIndicatorLayouts = new ArrayList<ActionIndicatorLayout>();
        DAO = role;
        initLayoutAttributes();
        setLayoutAttributes();
    }

    private void initLayoutAttributes() {
        root = inflater.inflate(R.layout.role_indicator, this);
        roleContainer = (ViewGroup) root.findViewById(R.id.roleContainer);
        roleHeader =  root.findViewById(R.id.roleHeader);
        roleName = (TextView) root.findViewById(R.id.roleName);
        priorityIconList = (ViewGroup) root.findViewById(R.id.rolePriorityIcons);
        actionList = (ViewGroup) root.findViewById(R.id.actionList);
        expandButton = (ImageView) root.findViewById(R.id.expandButton);
        usageTV = (TextView) root.findViewById(R.id.usage);
    }

    private void setLayoutAttributes() {
        RoleDAO role = (RoleDAO) DAO;
        fragment.registerForContextMenu(roleHeader);
        roleHeader.setTag("role");
        roleHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foldRole();
            }
        });
        roleName.setText(role.getName());
        priorityIconList.addView(Misc.imageViewRow(role.getPriority(), activity));
        usageTV.setText(""+role.getRoleUsage());
        addActionListTo(actionList);
        if(role.hasActionActive())
            foldRole();
    }

    public void foldRole() {
        if(actionList.getVisibility() == GONE){
            actionList.setVisibility(VISIBLE);
            expandButton.setImageResource(R.drawable.backup_ic);
        }else{
            actionList.setVisibility(GONE);
            expandButton.setImageResource(R.drawable.dropdown_ic);
        }
    }

    private void addActionListTo(ViewGroup viewGroup) {
        ArrayList<ActionDAO> actions = (ArrayList) ((RoleDAO)DAO).getActions();
        int j=0;
        for(int i=0; i<actions.size(); i++){
            if(!actions.get(i).isUnTrack()) {
                actionIndicatorLayouts.add(new ActionIndicatorLayout(actions.get(i), fragment));
                viewGroup.addView(actionIndicatorLayouts.get(j).getLayout());
                j++;
            }
        }
    }

    public ArrayList<ActionIndicatorLayout> getActionIndicatorLayouts() {
        return actionIndicatorLayouts;
    }

    public void clearActionInicatorLayout(){
        actionIndicatorLayouts.clear();
    }

    public ViewGroup getActionIndicatorLayoutsContainer() {
        return actionList;
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
        this.DAO = (RoleDAO) DAO;
    }

}
