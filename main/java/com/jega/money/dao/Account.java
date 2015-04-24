package com.jega.money.dao;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 4/2/2015.
 */
public class Account {

    private float initialBalance;
    private ArrayList<Entry> deposits = new ArrayList<Entry>();
    private ArrayList<Entry> withdraws = new ArrayList<Entry>();

    private String description;

    public Account() {
    }

    public Account(float initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void setInitialBalance(float initialBalance) {
        this.initialBalance = initialBalance;
    }

    public ArrayList<Entry> getDeposits() {
        return deposits;
    }

    public ArrayList<Entry> getWithdraws() {
        return withdraws;
    }

    public float getBalance() {
        return initialBalance + totalDeposits() - totalWithdraws();
    }

    public float totalDeposits(){
        return totalEntries(deposits);
    }

    public float totalDepositsOf(Category category) {
        return totalEntriesByCategory(deposits, category);
    }

    public float totalWithdraws(){
        return totalEntries(withdraws);
    }

    public float totalWithdrawsOf(Category category) {
        return totalEntriesByCategory(withdraws, category);
    }

    private float totalEntries(ArrayList<Entry> entries) {
        float sum = 0;
        for(Entry entry : entries)
            sum += entry.getAmount();
        return sum;
    }

    private float totalEntriesByCategory(ArrayList<Entry> entries, Category category) {
        float sum = 0;
        for(Entry entry : entries)
            if(entry.getCategory() == category)
                sum += entry.getAmount();
        return sum;
    }

    public void deposit(Entry entry) {
        deposits.add(entry);
    }

    public void withdraw(Entry entry) {
        if(entry.getAmount() > getBalance())
            throw new IllegalArgumentException("you have only " + getBalance() + " to withdraw from");
        withdraws.add(entry);
    }

    public void transfer(Account to, Entry entry) {
        withdraw(entry);
        to.deposit(entry);
    }

    public Entry getLastWithdraw() {
        return withdraws.get(withdraws.size() - 1);
    }

    public Entry getLastDeposit() {
        return deposits.get(deposits.size() - 1);
    }

    public interface AccountOperations{
        public abstract float totalDebits();
        public abstract float totalCredits();
    }
}
