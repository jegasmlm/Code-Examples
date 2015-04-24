package com.jega.kairometer.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jegasmlm on 2/22/2015.
 */
public class RecordDAO extends DAO {

    private long actionId;
    private long activationTime;
    private long deactivationTime;
    private boolean active;

    public RecordDAO() {
        super();
    }

    public RecordDAO(long id, int actionId, long activationTime, long deactivationTime) {
        super(id);
        this.actionId = actionId;
        this.activationTime = activationTime;
        this.deactivationTime = deactivationTime;
    }

    public RecordDAO(long actionId, long activationTime, long deactivationTime) {
        this.actionId = actionId;
        this.activationTime = activationTime;
        this.deactivationTime = deactivationTime;
    }

    public static String formatTime(long time){
        SimpleDateFormat date_format = new SimpleDateFormat("MMM/dd/yy HH:mm:ss");
        Date date = new Date(time);
        return date_format.format(date);
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long activationId) {
        this.actionId = activationId;
    }

    public long getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(long activationTime) {
        this.activationTime = activationTime;
    }

    public long getDeactivationTime() {
        return deactivationTime;
    }

    public void setDeactivationTime(long deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    public boolean isActive() {
        return deactivationTime == 0;
    }
}
