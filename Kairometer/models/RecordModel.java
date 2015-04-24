package com.jega.kairometer.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.jega.kairometer.dao.DAO;
import com.jega.kairometer.dao.RecordDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jegasmlm on 2/21/2015.
 */
public class RecordModel extends Model {

    public static final String RECORD_TABLE_NAME = "record";
    public static final String RECORD_COLUMN_ID = "_id";
    public static final String RECORD_COLUMN_ACTION_ID = "action_id";
    public static final String RECORD_COLUMN_ACTIVATION_TIME = "activation_time";
    public static final String RECORD_COLUMN_DEACTIVATION_TIME = "deactivation_time";

    public static final String RECORD_TABLE_CREATE =
            "CREATE TABLE " + RECORD_TABLE_NAME + " (" +
                    RECORD_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    RECORD_COLUMN_ACTION_ID + " INTEGER, " +
                    RECORD_COLUMN_ACTIVATION_TIME + " INTEGER, " +
                    RECORD_COLUMN_DEACTIVATION_TIME + " INTEGER, " +
                    " FOREIGN KEY(" + RECORD_COLUMN_ACTION_ID + ") REFERENCES " + ActionModel.ACTION_TABLE_NAME + "(" + ActionModel.ACTION_COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE );";

    public RecordModel(DatabaseHelper databaseHelper){
        super(databaseHelper);
        tableName = RECORD_TABLE_NAME;
        columnId = RECORD_COLUMN_ID;
    }

    public List<RecordDAO> getRecordsByAction(long actionId){
        List<RecordDAO> list = new ArrayList<RecordDAO>();
        Cursor cursor = query("SELECT  * FROM " + tableName + " WHERE " + RECORD_COLUMN_ACTION_ID + " = " + actionId);
        if(cursor.moveToFirst()) do list.add(makeDAO(cursor)); while (cursor.moveToNext());
        return list;
    }

    public List<RecordDAO> getAllRecordsOrdered(){
        List<RecordDAO> list = new ArrayList<RecordDAO>();
        Cursor cursor = query("SELECT  * FROM " + tableName + " ORDER BY " + RECORD_COLUMN_DEACTIVATION_TIME + " DESC");
        if(cursor.moveToFirst()) do list.add(makeDAO(cursor)); while (cursor.moveToNext());
        return list;
    }

    @Override
    public ContentValues getContentValues(DAO data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RECORD_COLUMN_ACTION_ID, ((RecordDAO)data).getActionId());
        contentValues.put(RECORD_COLUMN_ACTIVATION_TIME, ((RecordDAO)data).getActivationTime());
        contentValues.put(RECORD_COLUMN_DEACTIVATION_TIME, ((RecordDAO)data).getDeactivationTime());
        return contentValues;
    }

    @Override
    public RecordDAO makeDAO(Cursor cursor) {
        RecordDAO record = new RecordDAO();
        record.setId(Integer.parseInt(cursor.getString(0)));
        record.setActionId(cursor.getInt(1));
        record.setActivationTime(cursor.getLong(2));
        record.setDeactivationTime(cursor.getLong(3));
        return record;
    }
}
