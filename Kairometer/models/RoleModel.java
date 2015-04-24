package com.jega.kairometer.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.jega.kairometer.dao.RoleDAO;
import com.jega.kairometer.dao.DAO;

/**
 * Created by jegasmlm on 2/21/2015.
 */
public class RoleModel extends Model {

    public static final String ROLE_TABLE_NAME = "role";
    public static final String ROLE_COLUMN_ID = "_id";
    public static final String ROLE_COLUMN_NAME = "name";
    public static final String ROLE_COLUMN_PRIORITY = "priority";

    public static final String ROLE_TABLE_CREATE =
            "CREATE TABLE " + ROLE_TABLE_NAME + " (" +
                    ROLE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    ROLE_COLUMN_NAME + " TEXT, " +
                    ROLE_COLUMN_PRIORITY + " INTEGER);";

    private ActionModel actionModel;

    public RoleModel(DatabaseHelper databaseHelper){
        super(databaseHelper);
        tableName = ROLE_TABLE_NAME;
        columnId = ROLE_COLUMN_ID;
        actionModel = new ActionModel(databaseHelper);
    }

    @Override
    public ContentValues getContentValues(DAO data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROLE_COLUMN_NAME, ((RoleDAO) data).getName());
        contentValues.put(ROLE_COLUMN_PRIORITY, ((RoleDAO)data).getPriority());
        return contentValues;
    }

    @Override
    public RoleDAO makeDAO(Cursor cursor) {
        if( isCursorEmpty(cursor) )
            return null;
        else {
            RoleDAO role = new RoleDAO();
            role.setId( Integer.parseInt(cursor.getString(0)) );
            role.setName( cursor.getString(1) );
            role.setPriority( cursor.getInt(2) );
            role.setActions(actionModel.getActionsByRole( role.getId() ));
            return role;
        }
    }
}
