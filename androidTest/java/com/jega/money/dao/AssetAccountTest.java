package com.jega.money.dao;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jegasmlm on 4/3/2015.
 */

@RunWith(AndroidJUnit4.class)
public class AssetAccountTest extends AndroidTestCase {

    private static final Category food = new Category();

    @Test
    public void debitsShouldMakeDeposits(){
        AssetAccount asset = new AssetAccount();
        asset.debit(new Entry(100.f));
        assertEquals(100.0f, asset.getBalance());
    }

    @Test
    public void creditsShouldMakeWithdraws(){
        AssetAccount asset = new AssetAccount(100.0f);
        asset.credit(new Entry(25.0f));
        assertEquals(75.0f, asset.getBalance());
    }

}
