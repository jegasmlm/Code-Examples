package com.jega.kairometer.dao;

/**
 * Created by jegasmlm on 2/25/2015.
 */
public class DAO {

    protected long id;

    public DAO(){

    }

    public DAO(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
