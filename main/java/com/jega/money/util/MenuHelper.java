package com.jega.money.util;

import android.content.Context;
import android.content.Intent;

import com.jega.money.R;
import com.jega.money.view.AddAccountView;
import com.jega.money.view.AddTransactionView;

/**
 * Created by jegasmlm on 4/15/2015.
 */
public class MenuHelper {

    public static void loadView(int id, Context context) {
        switch(id){
            case R.id.addAccount:{
                Intent intent = new Intent(context, AddAccountView.class);
                context.startActivity(intent);
            }break;

            case R.id.addTransaction:{
                Intent intent = new Intent(context, AddTransactionView.class);
                context.startActivity(intent);
            }break;
        }
    }
}
