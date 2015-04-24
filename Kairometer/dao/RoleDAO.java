package com.jega.kairometer.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuanErnesto on 10-01-2015.
 */
public class RoleDAO extends DAO {

    private String name;
    private int priority;
    private ArrayList<ActionDAO> actions;

    public RoleDAO(){
        super();
    }

    public RoleDAO(int id, String name, int priority) {
        super(id);
        this.name = name;
        this.priority = priority;
    }

    public RoleDAO(String name, int priority) {
        super();
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void add(ActionDAO action){
        this.actions.add(action);
    }

    public ActionDAO get(int index){
        return actions.get(index);
    }

    public void set(int index, ActionDAO action){
        this.actions.set(index, action);
    }

    public Boolean remove(ActionDAO action){
        action.recycle();
        return this.actions.remove(action);
    }

    public void clearActions(){
        for(int i=0; i<actions.size(); i++) remove(actions.get(i));
//        for(ActionDAO action : actions) remove(action);
    }

    public void setActions(List<ActionDAO> actions){
        this.actions = (ArrayList<ActionDAO>) actions;
    }

    public List<ActionDAO> getActions(){
        return this.actions;
    }

    public void orderActionsByUsage(){
        int i, j;
        float x;
        ActionDAO aux;

        for(i=1; i<this.actions.size(); i++){
            x = this.actions.get(i).getUsage();
            aux = this.actions.get(i);
            j = i;
            while(j > 0 && this.actions.get(j-1).getUsage() < x) {
                this.actions.set(j, this.actions.get(j-1));
                j = j - 1;
            }
            this.actions.set(j,aux);
        }
    }

    public float getRoleUsage(){
        float avg = 0;

        for(int i=0; i<this.actions.size(); i++){
            avg += this.actions.get(i).getUsage();
        }

        avg = avg / this.actions.size();

        return avg * priority;
    }

    public boolean hasActionActive() {
        for(ActionDAO action : actions)
            if(action.isActive())
                return true;
        return false;
    }
}
