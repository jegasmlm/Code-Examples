package com.jega.money.dao;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.jega.money.dao.Account;
import com.jega.money.dao.Category;
import com.jega.money.dao.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jegasmlm on 4/2/2015.
 */

@RunWith(AndroidJUnit4.class)
public class AccountTest extends AndroidTestCase {

    private static final Category FOOD = new Category();
    private static final Category TRANSPORT = new Category();
    private static final Category SERVICE = new Category();

    private static final Category OWN_PROPERTY = new Category();

    private static final Category SALARY = new Category(null);
    private static final Category SALES = new Category();
    private static final Category GAINS = new Category();

    @Test
    public void initialBalanceShouldBeZeroIfNotSet(){
        Account account = new Account();
        assertEquals(0.0f, account.getBalance());
    }

    @Test
    public void initialBalance(){
        Account account = new Account(100.0f);
        assertEquals(100.0f, account.getBalance());
    }

    @Test
    public void depositAndWithdraw(){
        Account account = new Account();
        account.deposit(new Entry(100.0f));
        assertEquals("after deposit should be 100 in the account", 100.0f, account.getBalance());
        account.withdraw(new Entry(50.0f));
        assertEquals("after withdraw should be 50 in the account", 50.0f, account.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void BalanceShouldNotBeNegative(){
        Account account = new Account();
        account.withdraw(new Entry(75.0f));
    }

    @Test
    public void transfer(){
        Account from = new Account(100.0f);
        Account to = new Account();
        from.transfer(to, new Entry(25.0f));
        assertEquals(75.0f, from.getBalance());
        assertEquals(25.0f, to.getBalance());
    }

    @Test
    public void transferCanHaveCategoryAndDescription(){
        Account from = new Account(100.0f);
        Account to = new Account();
        from.transfer(to, new Entry(25.0f, OWN_PROPERTY, "purchase of laptop"));
        assertEquals(OWN_PROPERTY, from.getLastWithdraw().getCategory());
        assertEquals("purchase of laptop", from.getLastWithdraw().getDescription());
        assertEquals(OWN_PROPERTY, to.getLastDeposit().getCategory());
        assertEquals("purchase of laptop", to.getLastDeposit().getDescription());
    }

    @Test
    public void totalDepositsByCategories(){
        Account account = new Account((float) 200);

        account.deposit(new Entry(2000.0f, SALARY, null));
        account.deposit(new Entry(200.0f, SALES, "commission for sale"));
        account.deposit(new Entry(150.0f, GAINS, "Apartment rent"));

        assertEquals(2000.0f, account.totalDepositsOf(SALARY));
        assertEquals(200.0f, account.totalDepositsOf(SALES));
        assertEquals(150.0f, account.totalDepositsOf(GAINS));
    }

    @Test
    public void totalWithdrawsByCategories(){
        Account account = new Account((float) 200);

        account.withdraw(new Entry(10.0f, FOOD, "supermarket SA"));
        account.withdraw(new Entry(12.75f, FOOD, "Hot Dog"));
        account.withdraw(new Entry(30.0f, TRANSPORT, "MetroCard Refill"));
        account.withdraw(new Entry(10.0f, TRANSPORT, "taxi"));
        account.withdraw(new Entry(40.0f, SERVICE, "Mobile service"));

        assertEquals(22.75f, account.totalWithdrawsOf(FOOD));
        assertEquals(40.0f, account.totalWithdrawsOf(TRANSPORT));
        assertEquals(40.0f, account.totalWithdrawsOf(SERVICE));
    }

    @Test
    public void forEveryDepositShouldBeAnEntry(){
        Account account = new Account((float) 200);

        account.deposit(new Entry(2000.0f));
        account.deposit(new Entry(200.0f));
        account.deposit(new Entry(150.0f));

        assertEquals(3, account.getDeposits().size());
        assertEquals(2000.0f, account.getDeposits().get(0).getAmount());
        assertEquals(200.0f, account.getDeposits().get(1).getAmount());
        assertEquals(150.0f, account.getDeposits().get(2).getAmount());
    }

    @Test
    public void forEveryWithdrawShouldBeAnEntry(){
        Account account = new Account((float) 200);

        account.withdraw(new Entry(22.0f));
        account.withdraw(new Entry(50.0f));
        account.withdraw(new Entry(10.0f));

        assertEquals(3, account.getWithdraws().size());
        assertEquals(22.0f, account.getWithdraws().get(0).getAmount());
        assertEquals(50.0f, account.getWithdraws().get(1).getAmount());
        assertEquals(10.0f, account.getWithdraws().get(2).getAmount());
    }

}
