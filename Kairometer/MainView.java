package com.jega.kairometer;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jega.kairometer.controllers.HomeController;
import com.jega.kairometer.controllers.TutorialController;
import com.jega.kairometer.models.DatabaseHelper;

import java.util.Calendar;

public class MainView extends FragmentActivity {

    private boolean firstTime;
    private FragmentManager fragmentManager;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
        loadSystem();

		//Hide actionBar from android theme
		ActionBar actionBar = getActionBar();
		if(actionBar != null) actionBar.hide();
		
//		resetSystem();

        if(firstTime){
            firstTime = false;
            //Setting the fragment transaction
            FragmentTransaction fragmentTransaction;
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content, new TutorialController(), "tutorial");
            fragmentTransaction.commit();

        }else{
            //Setting the fragment transaction
            FragmentTransaction fragmentTransaction;
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content, new HomeController(), "home");
            fragmentTransaction.commit();

        }
	}

    private void loadSystem() {
        SharedPreferences system = getSharedPreferences("systemFile", 0);
        firstTime = system.getBoolean("firstTime", true);
    }

    @Override
    public void onPause(){
        super.onPause();
        saveTime();
    }

    private void saveTime() {
        SharedPreferences system = getSharedPreferences("systemFile", 0);
        SharedPreferences.Editor editor = system.edit();
        editor.putLong("stopTime", Calendar.getInstance().getTimeInMillis());
        editor.putBoolean("firstTime", firstTime);
        editor.commit();
    }
	
	public void resetSystem(){
		this.deleteDatabase(DatabaseHelper.DATABASE_NAME);
	    SharedPreferences system = getSharedPreferences("systemFile", 0);
	    SharedPreferences.Editor editor = system.edit();
	    editor.putLong("stopTime", 0);
	    editor.commit();
	}
}
