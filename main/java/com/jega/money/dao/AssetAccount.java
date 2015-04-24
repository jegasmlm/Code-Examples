package com.jega.money.dao;

import static com.jega.money.dao.Account.*;

/**
 * Created by jegasmlm on 4/3/2015.
 */
public class AssetAccount extends Account implements AccountOperations {
    public AssetAccount(float amount) {
        super(amount);
    }

    public AssetAccount() {
        super();
    }

    public void debit(Entry entry) {
        deposit(entry);
    }

    public void credit(Entry entry) {
        withdraw(entry);
    }

    public float totalDebits() {
        return totalDeposits();
    }

    public float totalCredits() {
        return totalWithdraws();
    }

    public Entry getLastCredit() {
        return getLastWithdraw();
    }

    public Entry getLastDebit() {
        return getLastDeposit();
    }
}
