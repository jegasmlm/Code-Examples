package com.jega.money.dao;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

/**
 * Created by jegasmlm on 4/3/2015.
 */

@RunWith(AndroidJUnit4.class)
public class EntryTest extends AndroidTestCase {

    @Test
    public void entryShouldSaveTheDateAutomatically(){
        Calendar currentTime = Calendar.getInstance();
        Entry entry = new Entry(0.0f);
        assertEquals(true, currentTime.getTimeInMillis() - entry.getDate().getTimeInMillis() <= 1000);
    }

}
