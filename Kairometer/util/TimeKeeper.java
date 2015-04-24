package com.jega.kairometer.util;

import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.RoleDAO;

import java.util.ArrayList;

/**
 * Created by JuanErnesto on 10-01-2015.
 */
public class TimeKeeper {
    public static final float TOTAL_TIME = 168;
    private float totalFlexibleTime;
    private float totalFixedTime;

    public TimeKeeper() {
        this.totalFixedTime = 0;
        this.totalFlexibleTime = 0;
    }

    public void addFlexibleTime(float time){
        totalFlexibleTime += time;
    }

    public void removeFlexibleTime(float time){
        totalFlexibleTime -= time;
    }

    public void addFixedTime(float time){
        totalFixedTime += time;
    }

    public void removeFixedTime(float time){
        totalFixedTime -= time;
    }

    public void updateTotalFlexibleTime(float oldTime, float newTime) {
        removeFlexibleTime(oldTime);
        addFlexibleTime(newTime);
    }

    public void updateTotalFixedTime(float oldTime, float newTime) {
        removeFixedTime(oldTime);
        addFixedTime(newTime);
    }

    public void transferToFlexible(float time){
        addFlexibleTime(getFlexEquivalent(time));
        removeFixedTime(time);
    }

    public void transferToFixed(float time){
        addFixedTime(getFixedEquivalent(time));
        removeFlexibleTime(time);
    }

    public float getTotalFlexibleDuration() {
        return totalFlexibleTime;
    }

    public float getTotalFixedDuration() {
        return totalFixedTime;
    }

    public float getFixedEquivalent(float time){
        return (TOTAL_TIME - totalFixedTime) * (time / totalFlexibleTime);
    }

    public float getFlexEquivalent(float time){
        return ( time / (TOTAL_TIME - totalFixedTime) ) * totalFlexibleTime;
    }

    public void registerRoles(ArrayList<RoleDAO> roleList){
        for(int i=0; i<roleList.size(); i++)
            registerRole(roleList.get(i));
    }

    public void registerRole(RoleDAO role){
        registerActions( (ArrayList) role.getActions() );
    }

    public void registerActions(ArrayList<ActionDAO> actions){
        for(int i=0; i<actions.size(); i++)
            registerAction(actions.get(i));
    }

    public void registerAction(ActionDAO action){
        action.register(this);
    }

    public void unRegisterActions(ArrayList<ActionDAO> actions){
        for(int i=0; i<actions.size(); i++)
            unRegisterAction(actions.get(i));
    }

    public void unRegisterAction(ActionDAO action){
        action.unregister();
    }
}
