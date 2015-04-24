package com.jega.money.dao;

import static com.jega.money.dao.Account.AccountOperations;

/**
 * Created by jegasmlm on 4/3/2015.
 */
public class EquityAccount extends Account implements AccountOperations {
    public EquityAccount() {

    }

    public EquityAccount(float amount) {
        super(amount);
    }

    public void debit(Entry entry) {
        withdraw(entry);
    }

    public void credit(Entry entry) {
        deposit(entry);
    }

    public float totalDebits() {
        return totalWithdraws();
    }

    public float totalCredits() {
        return totalDeposits();
    }

    public Entry getLastDebit() {
        return getLastWithdraw();
    }

    public Entry getLastCredit(){
        return getLastDeposit();
    }
}
