package com.jega.money.dao;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.jega.money.util.BalanceSheet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Created by jegasmlm on 4/3/2015.
 */

@RunWith(AndroidJUnit4.class)
public class BalanceSheetTest extends AndroidTestCase {

    private BalanceSheet balanceSheet = new BalanceSheet();
    private ArrayList<CashAccount> cashAccounts = balanceSheet.getCashAccounts();
    private ArrayList<ReceivableAccount> receivableAccounts = balanceSheet.getReceivableAccounts();
    private ArrayList<PropertyAccount> propertyAccount = balanceSheet.getPropertyAccounts();
    private ArrayList<PayableAccount> payableAccounts = balanceSheet.getPayableAccounts();
    private CapitalAccount capitalAccount = balanceSheet.getCapitalAccount();

    private CashAccount cash = new CashAccount(2000.0f);
    private ReceivableAccount studio = new ReceivableAccount(1000.0f);
    private PayableAccount hospital = new PayableAccount();
    private PayableAccount store = new PayableAccount(600.0f);
    private PropertyAccount laptop = new PropertyAccount(200.0f);

    @Before
    public void setUp(){
        balanceSheet.addCashAccount(cash);
        balanceSheet.addPayableAccount(store);
        balanceSheet.addReceivableAccount(studio);
        balanceSheet.addPayableAccount(hospital);
        balanceSheet.addPropertyAccount(laptop);
    }

    @Test
    public void AtFirst_accountsShouldBeSet(){
        assertNotNull(cashAccounts);
        assertNotNull(receivableAccounts);
        assertNotNull(propertyAccount);
        assertNotNull(payableAccounts);
        assertNotNull(capitalAccount);
    }

    private void balanceSheet_ShouldAlwaysBe_Balanced() {
        assertEquals("assets should be equal to equities", balanceSheet.totalAssets(), balanceSheet.totalEquities());
        assertEquals("debits should be equal to credits", balanceSheet.totalDebits(), balanceSheet.totalCredits());
    }

    @Test
    public void calculateInitialCapital_setInitialBalanced_sheetMustBeBalanced(){

        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerExpense_ShouldAlways_CreditTheCashAccountAndDebitTheOwnerCapital(){
        balanceSheet.registerExpense(new Entry(100.0f), cash);
        assertEquals(100.0f, cash.getLastCredit().getAmount());
        assertEquals(100.0f, capitalAccount.getLastDebit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerPropertyPurchase_ShouldAlways_CreditTheCashAccountAndDebitProperty(){
        balanceSheet.registerPropertyPurchase(new Entry(500.0f), cash, laptop);
        assertEquals(500.0f, cash.getLastCredit().getAmount());
        assertEquals(500.0f, laptop.getLastDebit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerMoneyEarned_ShouldAlways_DebitTheCashAccountAndCreditTheOwnerCapital(){
        balanceSheet.registerMoneyEarned(new Entry(2000.0f), cash);
        assertEquals(2000.0f, cash.getLastDebit().getAmount());
        assertEquals(2000.0f, capitalAccount.getLastCredit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerReceivableMoney_ShouldAlways_DebitTheReceivableAccountAndCreditTheOwnerCapital(){
        balanceSheet.registerReceivableMoney(new Entry(2000.0f), studio);
        assertEquals(2000.0f, studio.getLastDebit().getAmount());
        assertEquals(2000.0f, capitalAccount.getLastCredit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerPayableExpense_ShouldAlways_CreditThePayableAccountAndDebitTheOwnerCapital(){
        balanceSheet.registerPayableExpense(new Entry(600.0f), hospital);
        assertEquals(600.0f, hospital.getLastCredit().getAmount());
        assertEquals(600.0f, capitalAccount.getLastDebit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerPayablePurchase_ShouldAlways_CreditThePayableAccountAndDebitThePropertyAccount(){
        balanceSheet.registerPayablePurchase(new Entry(900.0f), store, laptop);
        assertEquals(900.0f, store.getLastCredit().getAmount());
        assertEquals(900.0f, laptop.getLastDebit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerPropertySale_ShouldAlways_DebitTheCashAccountAndCreditThePropertyAccount(){
        balanceSheet.registerPropertySale(new Entry(100.0f), cash, laptop);
        assertEquals(100.0f, cash.getLastDebit().getAmount());
        assertEquals(100.0f, laptop.getLastCredit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerCollection_ShouldAlways_DebitTheCashAccountAndCreditTheReceivableAccount(){
        balanceSheet.registerCollection(new Entry(800.0f), cash, studio);
        assertEquals(800.0f, cash.getLastDebit().getAmount());
        assertEquals(800.0f, studio.getLastCredit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

    @Test
    public void registerPayment_ShouldAlways_CreditTheCashAccountAndDebitThePayableAccount(){
        balanceSheet.registerPayment(new Entry(400.0f), cash, store);
        assertEquals(400.0f, cash.getLastCredit().getAmount());
        assertEquals(400.0f, store.getLastDebit().getAmount());
        balanceSheet_ShouldAlwaysBe_Balanced();
    }

}
