package com.jega.money.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jega.money.R;

/**
 * Created by jegasmlm on 4/15/2015.
 */
public class AccountFormLayout extends LinearLayout{

    public static final String TITTLE_SUFIX = " Account";
    private final TextView tittle;
    private final ViewGroup form;
    private final TextView saveButton;

    public AccountFormLayout(Context context, String accountType, int form) {
        super(context);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);

        this.tittle = (TextView) LayoutInflater.from(context).inflate(R.layout.account_form_tittle, null);
        this.tittle.setText(accountType + TITTLE_SUFIX);
        addView(this.tittle);

        this.form = (ViewGroup) LayoutInflater.from(context).inflate(form, null);
        addView(this.form);
        ((LayoutParams)this.form.getLayoutParams()).height = 0;
        ((LayoutParams)this.form.getLayoutParams()).weight = 1;

        this.saveButton = (TextView) LayoutInflater.from(context).inflate(R.layout.save_button, null);
        addView(this.saveButton);
    }
}
