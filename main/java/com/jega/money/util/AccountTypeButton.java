package com.jega.money.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jega.money.R;

/**
 * Created by jegasmlm on 4/7/2015.
 */
public class AccountTypeButton extends LinearLayout {


    public AccountTypeButton(Context context, String tittle, int iconRes) {
        super(context);

        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        int padding = (int) context.getResources().getDimension(R.dimen.addAccountButtonPadding);
        setPadding(padding, padding, padding, padding);
        setId(R.id.addAccountButton);
        setOrientation(VERTICAL);

        LayoutInflater.from(context).inflate(R.layout.account_type_button, this);

        ((TextView)findViewById(R.id.tittle)).setText(tittle);
        ((ImageView)findViewById(R.id.icon)).setImageResource(iconRes);
    }
}
