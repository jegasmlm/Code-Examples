package com.jega.kairometer.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jega.kairometer.dao.DAO;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 2/21/2015.
 */
public abstract class Model {

    protected String tableName;
    protected String columnId;

    private DatabaseHelper databaseHelper;

    public Model(){

    }

    public Model(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public SQLiteDatabase getReadableDatabase() {
        return databaseHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return databaseHelper.getWritableDatabase();
    }

    public Cursor query(String query) {
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        return cursor;
    }

    public boolean isCursorEmpty(Cursor cursor) {
        return cursor.getCount() == 0;
    }

    public long insert (DAO data){
        return getWritableDatabase().insert(tableName, null, getContentValues(data));
    }

    public DAO get(long id) {
        return makeDAO(query("select * from " + tableName + " where " + columnId + " = " + id));
    }

    public ArrayList<DAO> getAll() {
        ArrayList<DAO> list = new ArrayList<DAO>();
        Cursor cursor = query("select  * from " + tableName);
        if(cursor.moveToFirst()) do list.add(makeDAO(cursor)); while (cursor.moveToNext());
        return list;
    }

    public int update(DAO data) {
        return getWritableDatabase().update(tableName, getContentValues(data), columnId + " = ?", new String[]{String.valueOf(data.getId())});
    }

    public void delete(DAO data) {
        getWritableDatabase().delete(tableName, columnId + " = ?", new String[]{String.valueOf(data.getId())});
        getWritableDatabase().close();
    }

    public void delete(long id) {
        getWritableDatabase().delete(tableName, columnId + " = ?", new String[]{String.valueOf(id)});
        getWritableDatabase().close();
    }

    public int getCount() {
        Cursor cursor = query("SELECT  * FROM " + tableName);
        cursor.close();

        return cursor.getCount();
    }

    public boolean exists(DAO DAO){
        if(get(DAO.getId()) == null )
            return false;
        else
            return true;
    }

    public abstract DAO makeDAO(Cursor cursor);

    public abstract ContentValues getContentValues(DAO data);
}
