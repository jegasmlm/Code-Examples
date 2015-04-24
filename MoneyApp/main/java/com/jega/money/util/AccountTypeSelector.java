package com.jega.money.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jega.money.R;

/**
 * Created by jegasmlm on 4/7/2015.
 */
public class AccountTypeSelector extends LinearLayout {
    private final ViewGroup buttonsContainer;
    private int columns;

    public AccountTypeSelector(Context context, int columns) {
        super(context);

        this.columns = columns;

        LayoutInflater.from(context).inflate(R.layout.account_type_selector, this);

        buttonsContainer = (ViewGroup) findViewById(R.id.accountTypeButtonsContainer);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
        setId(R.id.accountTypeSelector);
        setOrientation(VERTICAL);
    }

    @Override
    public void addView(View view){
        int n = buttonsContainer.getChildCount();
        if(n == 0)
            buttonsContainer.addView(view);
        else if(n < columns){
            buttonsContainer.addView(view);
            ((RelativeLayout.LayoutParams)view.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, buttonsContainer.getChildAt(n - 1).getId());
        }
        else if(n % columns == 0){
            buttonsContainer.addView(view);
            ((RelativeLayout.LayoutParams)view.getLayoutParams()).addRule(RelativeLayout.BELOW, buttonsContainer.getChildAt(n - columns).getId());
        }
        else{
            buttonsContainer.addView(view);
            ((RelativeLayout.LayoutParams)view.getLayoutParams()).addRule(RelativeLayout.BELOW, buttonsContainer.getChildAt(n - columns).getId());
            ((RelativeLayout.LayoutParams)view.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF, buttonsContainer.getChildAt(n - 1).getId());
        }
    }
}
