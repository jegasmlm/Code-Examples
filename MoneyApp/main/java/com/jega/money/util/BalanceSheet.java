package com.jega.money.util;

import com.jega.money.dao.Account;
import com.jega.money.dao.AssetAccount;
import com.jega.money.dao.CapitalAccount;
import com.jega.money.dao.CashAccount;
import com.jega.money.dao.Entry;
import com.jega.money.dao.NotePayableAccount;
import com.jega.money.dao.PayableAccount;
import com.jega.money.dao.PropertyAccount;
import com.jega.money.dao.ReceivableAccount;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 4/3/2015.
 */
public class BalanceSheet {
    private ArrayList<CashAccount> cashAccounts;
    private ArrayList<ReceivableAccount> receivableAccounts;
    private ArrayList<PropertyAccount> propertyAccounts;
    private ArrayList<PayableAccount> payableAccounts;
    private CapitalAccount capitalAccount;

    public BalanceSheet(){
        cashAccounts = new ArrayList<>();
        receivableAccounts = new ArrayList<>();
        propertyAccounts = new ArrayList<>();
        payableAccounts = new ArrayList<>();
        capitalAccount = new CapitalAccount();
    }

    public ArrayList<CashAccount> getCashAccounts() {
        return cashAccounts;
    }

    public CapitalAccount getCapitalAccount() {
        return capitalAccount;
    }

    public ArrayList<PayableAccount> getPayableAccounts() {
        return payableAccounts;
    }

    public ArrayList<PropertyAccount> getPropertyAccounts() {
        return propertyAccounts;
    }

    public ArrayList<ReceivableAccount> getReceivableAccounts() {
        return receivableAccounts;
    }

    public void addCashAccount(CashAccount cashAccount) {
        cashAccounts.add(cashAccount);
        calculateInitialCapital();
    }

    public void addPayableAccount(PayableAccount payableAccount) {
        payableAccounts.add(payableAccount);
        calculateInitialCapital();
    }

    public void addPropertyAccount(PropertyAccount propertyAccount) {
        propertyAccounts.add(propertyAccount);
        calculateInitialCapital();
    }

    public void addReceivableAccount(ReceivableAccount receivableAccount) {
        receivableAccounts.add(receivableAccount);
        calculateInitialCapital();
    }

    public void registerExpense(Entry entry, CashAccount cashAccount) {
        cashAccount.credit(entry);
        capitalAccount.debit(entry);
    }

    public float sumDebitsFrom(ArrayList... multipleAccounts){
        float sum = 0;
        for(ArrayList<Account.AccountOperations> accounts : multipleAccounts)
            for(Account.AccountOperations account : accounts)
                sum += account.totalDebits();

        return sum;
    }

    public float sumCreditsFrom(ArrayList... multipleAccounts){
        float sum = 0;
        for(ArrayList<Account.AccountOperations> accounts : multipleAccounts)
            for(Account.AccountOperations account : accounts)
                sum += account.totalCredits();

        return sum;
    }

    public float sumBalanceFrom(ArrayList... multipleAccounts){
        float sum = 0;
        for(ArrayList<Account> accounts : multipleAccounts)
            for(Account account : accounts)
                sum += account.getBalance();

        return sum;
    }

    public float totalCredits() {
        return sumCreditsFrom(cashAccounts, receivableAccounts, propertyAccounts, payableAccounts) + capitalAccount.totalCredits();
    }

    public boolean isBalanced() {
        return ( totalDebits() == totalCredits() ) && ( totalAssets() == totalEquities() );
    }

    public float totalDebits() {
        return sumDebitsFrom(cashAccounts, receivableAccounts, propertyAccounts, payableAccounts) + capitalAccount.totalDebits();
    }

    public float totalAssets() {
        return sumBalanceFrom(cashAccounts, receivableAccounts, propertyAccounts);
    }

    public float totalEquities() {
        return sumBalanceFrom(payableAccounts) + capitalAccount.getBalance();
    }

    public void calculateInitialCapital() {
        if(!isBalanced())
            capitalAccount.setInitialBalance( totalAssets() - totalLiabilities() );
    }

    public float totalLiabilities() {
        return sumBalanceFrom(payableAccounts);
    }

    public void registerPropertyPurchase(Entry entry, CashAccount cashAccount, PropertyAccount propertyAccount) {
        cashAccount.credit(entry);
        propertyAccount.debit(entry);
    }

    public void registerMoneyEarned(Entry entry, CashAccount cashAccount) {
        cashAccount.debit(entry);
        capitalAccount.credit(entry);
    }

    public void registerPayableExpense(Entry entry, PayableAccount payableAccount) {
        payableAccount.credit(entry);
        capitalAccount.debit(entry);
    }

    public void registerPropertySale(Entry entry, CashAccount cashAccount, PropertyAccount propertyAccount) {
        cashAccount.debit(entry);
        propertyAccount.credit(entry);
    }

    public void registerCollection(Entry entry, CashAccount cashAccount, ReceivableAccount receivableAccount) {
        cashAccount.debit(entry);
        receivableAccount.credit(entry);
    }

    public void registerPayablePurchase(Entry entry, PayableAccount payableAccount, PropertyAccount propertyAccount) {
        payableAccount.credit(entry);
        propertyAccount.debit(entry);
    }

    public void registerPayment(Entry entry, CashAccount cashAccount, PayableAccount payableAccount) {
        cashAccount.credit(entry);
        payableAccount.debit(entry);
    }

    public void registerReceivableMoney(Entry entry, ReceivableAccount receivableAccount) {
        receivableAccount.debit(entry);
        capitalAccount.credit(entry);
    }

    public float totalCash() {
        return sumBalanceFrom(cashAccounts);
    }

    public float totalReceivables() {
        return sumBalanceFrom(receivableAccounts);
    }

    public float totalProperties() {
        return sumBalanceFrom(propertyAccounts);
    }

    public float totalPayables() {
        return sumBalanceFrom(payableAccounts);
    }
}
