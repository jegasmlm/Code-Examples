package com.jega.money.dao;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jegasmlm on 4/3/2015.
 */

@RunWith(AndroidJUnit4.class)
public class EquityAccountTest extends AndroidTestCase {

    @Test
    public void debitsShouldMakeWithdraws(){
        EquityAccount equity = new EquityAccount(100.0f);
        equity.debit(new Entry(25.0f));
        assertEquals(75.0f, equity.getBalance());
    }

    @Test
    public void creditsShouldMakeDeposits(){
        EquityAccount equity = new EquityAccount();
        equity.credit(new Entry(100.f));
        assertEquals(100.0f, equity.getBalance());
    }
}
