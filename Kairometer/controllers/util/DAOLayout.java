package com.jega.kairometer.controllers.util;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jega.kairometer.dao.DAO;
import com.jega.kairometer.R;

/**
 * Created by jegasmlm on 3/1/2015.
 */
public abstract class DAOLayout extends RelativeLayout {

    public static final float PRIORITY_ICON_MARGIN = 2f;
    public static final int PRIORITY_IC = R.drawable.priority_ic;
    protected Fragment fragment;
    protected FragmentActivity activity;
    protected Resources resources;
    protected LayoutInflater inflater;
    protected com.jega.kairometer.dao.DAO DAO;

    public DAOLayout(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.resources = activity.getResources();
        this.inflater = activity.getLayoutInflater();
    }

    public DAO getDAO() {
        return DAO;
    }

    public void setDAO(DAO DAO) {
        this.DAO = DAO;
    }

    public abstract View getLayout();
    public abstract DAO getData();
    public abstract void setData(DAO DAO);
}
