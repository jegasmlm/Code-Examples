package com.jega.kairometer.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.jega.kairometer.dao.ActionDAO;
import com.jega.kairometer.dao.DAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jegasmlm on 2/21/2015.
 */
public class ActionModel extends Model {

    public static final String ACTION_TABLE_NAME = "action";
    public static final String ACTION_COLUMN_ID = "_id";
    public static final String ACTION_COLUMN_ROLE_ID = "role_id";
    public static final String ACTION_COLUMN_NAME = "name";
    public static final String ACTION_COLUMN_TYPE = "type";
    public static final String ACTION_COLUMN_PRIORITY = "priority";
    public static final String ACTION_COLUMN_TIME = "time";
    public static final String ACTION_COLUMN_ACTIVE = "active";
    public static final String ACTION_COLUMN_FREQUENCY = "frequency";
    public static final String ACTION_COLUMN_UN_TRACK = "untracked";
    public static final String ACTION_COLUMN_CREATED_TIME = "created_time";

    public static final String ACTION_TABLE_CREATE =
            "CREATE TABLE " + ACTION_TABLE_NAME + " (" +
                    ACTION_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    ACTION_COLUMN_ROLE_ID + " INTEGER, " +
                    ACTION_COLUMN_NAME + " TEXT, " +
                    ACTION_COLUMN_TYPE + " INTEGER, " +
                    ACTION_COLUMN_PRIORITY + " INTEGER, " +
                    ACTION_COLUMN_TIME + " DOUBLE, " +
                    ACTION_COLUMN_ACTIVE + " INTEGER, " +
                    ACTION_COLUMN_FREQUENCY + " INTEGER, " +
                    ACTION_COLUMN_UN_TRACK + " INTEGER, " +
                    ACTION_COLUMN_CREATED_TIME + " INTEGER, " +
                    " FOREIGN KEY(" + ACTION_COLUMN_ROLE_ID + ") REFERENCES " + RoleModel.ROLE_TABLE_NAME + "(" + RoleModel.ROLE_COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE );";

    private RecordModel recordModel;

    public ActionModel(DatabaseHelper databaseHelper){
        super(databaseHelper);
        tableName = ACTION_TABLE_NAME;
        columnId = ACTION_COLUMN_ID;
        recordModel = new RecordModel(databaseHelper);
    }

    public List<ActionDAO> getActionsByRole(long roleId){
        List<ActionDAO> list = new ArrayList<ActionDAO>();
        Cursor cursor = query("SELECT  * FROM " + tableName + " WHERE " + ACTION_COLUMN_ROLE_ID + " = " + roleId);
        if(cursor.moveToFirst()) do list.add(makeDAO(cursor)); while (cursor.moveToNext());
        return list;
    }

    @Override
    public ContentValues getContentValues(DAO data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACTION_COLUMN_ROLE_ID, ((ActionDAO) data).getRoleId());
        contentValues.put(ACTION_COLUMN_NAME, ((ActionDAO) data).getName());
        contentValues.put(ACTION_COLUMN_TYPE, ((ActionDAO) data).getType());
        contentValues.put(ACTION_COLUMN_PRIORITY, ((ActionDAO) data).getPriority());
        contentValues.put(ACTION_COLUMN_TIME, ((ActionDAO) data).getTime());
        contentValues.put(ACTION_COLUMN_ACTIVE, ((ActionDAO) data).isActive() ? 1 : 0);
        contentValues.put(ACTION_COLUMN_FREQUENCY, ((ActionDAO) data).getFrequency());
        contentValues.put(ACTION_COLUMN_UN_TRACK, ((ActionDAO) data).isUnTrack() ? 1 : 0);
        contentValues.put(ACTION_COLUMN_CREATED_TIME, ((ActionDAO) data).getCreatedTime());
        return contentValues;
    }

    @Override
    public ActionDAO makeDAO(Cursor cursor) {
        if(isCursorEmpty(cursor))
            return null;
        else{
            ActionDAO action = new ActionDAO();
            action.setId(Integer.parseInt(cursor.getString(0)));
            action.setRoleId(cursor.getInt(1));
            action.setName(cursor.getString(2));
            action.setType(cursor.getInt(3));
            action.setPriority(cursor.getInt(4));
            action.setTime(cursor.getFloat(5));
            action.setActive(cursor.getInt(6) == 1);
            action.setFrequency(cursor.getInt(7));
            action.setRecords( (ArrayList) recordModel.getRecordsByAction( action.getId() ) );
            action.setUnTrack(cursor.getInt(8) == 1);
            action.setCreatedTime(cursor.getLong(9));
            return action;
        }
    }
}
