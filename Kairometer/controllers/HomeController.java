package com.jega.kairometer.controllers;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jega.kairometer.R;
import com.jega.kairometer.controllers.util.ActionIndicatorLayout;
import com.jega.kairometer.controllers.util.Misc;
import com.jega.kairometer.controllers.util.RoleIndicatorLayout;
import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.RoleDAO;
import com.jega.kairometer.models.ActionModel;
import com.jega.kairometer.models.DatabaseHelper;
import com.jega.kairometer.models.RoleModel;
import com.jega.kairometer.util.TimeKeeper;

public class HomeController extends Fragment {

    private FragmentActivity activity;
    private View rootView;
    private View contextMenuView;
    private RelativeLayout addNewRoleButton;
    private LinearLayout roleListContainer;
    private Button historyButton;

    private RoleModel roleModel;
    private ActionModel actionModel;
    public static TimeKeeper timeKeeper;
    private ArrayList<RoleDAO> roles;

    private ArrayList<RoleIndicatorLayout> roleIndicatorLayouts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.rootView = inflater.inflate(R.layout.home_view, container, false);
        roleIndicatorLayouts = new ArrayList<RoleIndicatorLayout>();
        loadLayout();
        return rootView;
    }

    private void loadLayout() {
        roleListContainer = (LinearLayout) rootView.findViewById(R.id.roleListContainer);
        addNewRoleButton = (RelativeLayout) rootView.findViewById(R.id.addRoleButton);
        addNewRoleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Misc.replaceFragment(activity, RoleFormController.newForm(), "roleForm");
            }
        });
        historyButton = (Button) rootView.findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Misc.replaceFragment(activity, new HistoryController(), "history");
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        iniModels();
        loadData();
        orderRolesByUsage();
        addNewRoleListTo(roleListContainer);
    }

    private void iniModels() {
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        roleModel = new RoleModel(databaseHelper);
        actionModel = new ActionModel(databaseHelper);
    }

    private void loadData() {
        roles = (ArrayList) roleModel.getAll();
        timeKeeper = new TimeKeeper();
        timeKeeper.registerRoles(roles);
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
        if( contextMenuView.getTag().equals("action") ){
            return manageActionIndicatorContextMenu(item);
        }else{
            return manageRoleIndicatorContextMenu(item);
        }
    }

    public boolean manageRoleIndicatorContextMenu(MenuItem item) {
        RoleIndicatorLayout roleIndicatorLayout = (RoleIndicatorLayout) contextMenuView.getParent().getParent();
        RoleDAO role = (RoleDAO) roleIndicatorLayout.getDAO();
        switch (item.getItemId()) {
            case R.id.edit:
                Misc.replaceFragment(activity, RoleFormController.newEditForm(role.getId()), "roleForm");
//                Misc.loadUpdateForm(role, activity, new RoleFormController(), "roleForm");
                return true;
            case R.id.delete:
                roleListContainer.removeView(roleIndicatorLayout.getLayout());
                roleIndicatorLayouts.remove(roleIndicatorLayout);
                role.clearActions();
                roles.remove(role);
                roleModel.delete(role);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public boolean manageActionIndicatorContextMenu(MenuItem item) {
        ActionIndicatorLayout actionIndicatorLayout = (ActionIndicatorLayout)(contextMenuView.getParent().getParent());
        RoleIndicatorLayout roleIndicatorLayout = (RoleIndicatorLayout) actionIndicatorLayout.getParent().getParent().getParent();
        RoleDAO role = (RoleDAO) roleIndicatorLayout.getDAO();
        ActionDAO action = (ActionDAO) (actionIndicatorLayout).getDAO();
        switch (item.getItemId()) {
            case R.id.edit:
                Misc.replaceFragment(activity, ActionFormController.newEditForm(action.getId()), "roleForm");
//                Misc.loadUpdateForm(action, activity, new ActionFormController(), "actionForm");
                return true;
            case R.id.delete:
                ViewGroup actionIndicationLayoutsContainer = roleIndicatorLayout.getActionIndicatorLayoutsContainer();
                View actionIndicationLayoutContainer = actionIndicatorLayout.getLayout();
                actionIndicationLayoutsContainer.removeView(actionIndicationLayoutContainer);
                roleIndicatorLayout.getActionIndicatorLayouts().remove(actionIndicatorLayout);
                role.remove(action);
                actionModel.delete(action);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        updateActions();
    }

    public void addNewRoleListTo(ViewGroup viewGroup){
        for(int i=0; i< roles.size(); i++){
            roleIndicatorLayouts.add( new RoleIndicatorLayout(roles.get(i), this) );
            roleIndicatorLayouts.get(i).foldRole();
            viewGroup.addView(roleIndicatorLayouts.get(i).getLayout());
        }
    }

    public void updateActions(){
        for(int i=0; i< roles.size(); i++){
            ArrayList<ActionDAO> actions = (ArrayList<ActionDAO>) roles.get(i).getActions();
            for(int j=0; j<actions.size(); j++){
                actionModel.update(actions.get(j));
            }
        }
    }

    private void orderRolesByUsage(){
        int i, j;
        float x;
        RoleDAO aux;

        for(i=0; i<roles.size(); i++)
            roles.get(i).orderActionsByUsage();

        for(i=1; i<roles.size(); i++){
            x = roles.get(i).getRoleUsage();
            aux = roles.get(i);
            j = i;
            while(j > 0 && roles.get(j-1).getRoleUsage() < x) {
                roles.set(j, roles.get(j-1));
                j = j - 1;
            }
            roles.set(j,aux);
        }
    }
}
