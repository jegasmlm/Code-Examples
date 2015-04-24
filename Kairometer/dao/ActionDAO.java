package com.jega.kairometer.dao;

import com.jega.kairometer.controllers.util.Misc;
import com.jega.kairometer.util.TimeKeeper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by JuanErnesto on 09-01-2015.
 */
public class ActionDAO extends DAO {

    private TimeKeeper timeKeeper;

    public static final int FLEXIBLE = 1;
    public static final int FIXED = 2;

    public static final int DAILY = 7;
    public static final int WEEKLY = 1;

    private long roleId;
    private String name;
    private int type;
    private int priority;
    private float time;
    private boolean active;
    private int frequency;
    private boolean unTrack;
    private long createdTime;

    private ArrayList<RecordDAO> records;

    public ActionDAO(){
        super();
        createdTime = Calendar.getInstance().getTimeInMillis();
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
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

    public float getTime() {
        return time;
    }

    public float getFixedTime(){
        switch (type){
            case FIXED:
                return time*frequency;
            case FLEXIBLE:
                return timeKeeper.getFixedEquivalent(time);
            default:
                throw new IllegalArgumentException("type is not FLEXIBLE nor FIXED");
        }
    }

    public void setTime(float time) {
        if(isRegistered()) notifyTime(time);
        this.time = time;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        if(this.type != type) {
            switch (type){
                case FIXED:
                    if(isRegistered()) this.timeKeeper.transferToFixed(time);
                    this.type = type;
                    break;
                case FLEXIBLE:
                    if(isRegistered()) this.timeKeeper.transferToFlexible(time*frequency);
                    this.type = type;
                    break;
                default:
                    throw new IllegalArgumentException("type is not FLEXIBLE nor FIXED");
            }
        }
    }

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void activate(){
        this.active = true;
        openRecord();
    }

    public void deactivate(){
        this.active = false;
        closeRecord();
    }

    private void openRecord() {
        this.records.add(new RecordDAO(this.id, Calendar.getInstance().getTimeInMillis(), 0));
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        if(isRegistered()) notifyFrequency(frequency);
        this.frequency = frequency;
    }

    public boolean isUnTrack() {
        return unTrack;
    }

    public void setUnTrack(boolean unTrack) {
        this.unTrack = unTrack;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    private void closeRecord(){
        if(getLastRecord().getDeactivationTime() == 0) getLastRecord().setDeactivationTime( (Calendar.getInstance().getTimeInMillis() ) );
    }

    public void register(TimeKeeper timeKeeper) {
        this.timeKeeper = timeKeeper;
        switch (type){
            case FIXED:
                this.timeKeeper.addFixedTime(time*frequency);
                break;
            case FLEXIBLE:
                this.timeKeeper.addFlexibleTime(time);
                break;
            default:
                throw new IllegalArgumentException("type is not FLEXIBLE nor FIXED");
        }
    }

    public void unregister() {
        recycle();
        this.timeKeeper = null;
    }

    public void notifyTime(float newDuration) {
            switch (type){
                case FIXED:
                    this.timeKeeper.updateTotalFixedTime(this.time*frequency, newDuration*frequency);
                    break;
                case FLEXIBLE:
                    this.timeKeeper.updateTotalFlexibleTime((int) this.time, (int) newDuration);
                    break;
                default:
                    throw new IllegalArgumentException("type is not FLEXIBLE nor FIXED");
            }
    }

    private void notifyFrequency(int newFrequency) {
        switch (type){
            case FIXED:
                this.timeKeeper.updateTotalFixedTime(this.time*this.frequency, this.time*newFrequency);
                break;
            default:
                throw new IllegalArgumentException("type is not FIXED");
        }

    }

    private boolean isRegistered() {
        return this.timeKeeper != null;
    }

    public void recycle(){
        switch (type){
            case FIXED:
                this.timeKeeper.removeFixedTime(time*frequency);
                break;
            case FLEXIBLE:
                this.timeKeeper.removeFlexibleTime(time);
                break;
            default:
                throw new IllegalArgumentException("type is not FLEXIBLE nor FIXED");
        }
    }

    public float getUsage(){
        return ( (getFixedTime() - getTimeSpent()) / getFixedTime() ) * priority * 100;
    }

    public long getActivationTime() {
        return getLastRecord().getActivationTime();
    }

    public float timeSinceActivation() {
        return (float)(Calendar.getInstance().getTimeInMillis() - getLastRecord().getActivationTime()) / (1000f*60f*60f);
    }

    public RecordDAO getLastRecord() {
        return records.get( records.size() - 1 );
    }

    public ArrayList<RecordDAO> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<RecordDAO> records) {
        this.records = records;
    }

    public float getTimeSpent(){
        long timeSpent = 0;
        if(isActive()) {
            for (int i = 0; i < records.size() - 1; i++) {
                timeSpent += records.get(i).getDeactivationTime() - records.get(i).getActivationTime();
            }
            timeSpent += Calendar.getInstance().getTimeInMillis() - getLastRecord().getActivationTime();
        }else{
            for (int i = 0; i < records.size(); i++)
                timeSpent += records.get(i).getDeactivationTime() - records.get(i).getActivationTime();
        }
        return  (float)timeSpent / (1000f*60f*60f);
    }

    public float getTimeSpent(long time){
        Calendar startWeekDate = new GregorianCalendar();
        startWeekDate.setTimeInMillis(time);
        startWeekDate.setTimeInMillis(Calendar.SUNDAY);
        startWeekDate.set(Calendar.DAY_OF_WEEK, startWeekDate.getFirstDayOfWeek());
        long timeSpent = 0;
        int i=0;
        for (i = 0; i < records.size() - 1; i++)
            timeSpent += getWeekTime(startWeekDate, records.get(i));
        if(isActive())
            timeSpent += time - getLastRecord().getActivationTime();
        else
            if(records.size() > 0) timeSpent += getWeekTime(startWeekDate, records.get(i));
        return  (float)timeSpent / (1000f*60f*60f);
    }

    private long getWeekTime(Calendar startWeekDate, RecordDAO recordDAO) {
        if(Misc.isInThisWeek(recordDAO.getDeactivationTime()))
            if(Misc.isInThisWeek(recordDAO.getActivationTime()))
                return recordDAO.getDeactivationTime() - recordDAO.getActivationTime();
            else
                return recordDAO.getDeactivationTime() - startWeekDate.getTimeInMillis();
        return 0;
    }
}
