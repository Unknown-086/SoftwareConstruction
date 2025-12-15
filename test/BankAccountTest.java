package test;

import static org.junit.Assert.*;
import org.junit.Test;
import task4.*;

/**
 * Test class for verifying bank transaction system thread safety.
 */
public class BankAccountTest {

    /**
     * Test deposit operation.
     */
    @Test
    public void testDeposit() {
        BankAccount account = new BankAccount("TEST-001", 100.0);
        account.deposit(50.0);
        assertEquals("Balance should be 150 after deposit", 150.0, account.getBalance(), 0.01);
    }

    /**
     * Test withdrawal operation.
     */
    @Test
    public void testWithdrawal() {
        BankAccount account = new BankAccount("TEST-002", 100.0);
        boolean success = account.withdraw(50.0);
        assertTrue("Withdrawal should succeed", success);
        assertEquals("Balance should be 50 after withdrawal", 50.0, account.getBalance(), 0.01);
    }

    /**
     * Test withdrawal with insufficient funds.
     */
    @Test
    public void testInsufficientFunds() {
        BankAccount account = new BankAccount("TEST-003", 100.0);
        boolean success = account.withdraw(150.0);
        assertFalse("Withdrawal should fail with insufficient funds", success);
        assertEquals("Balance should remain 100", 100.0, account.getBalance(), 0.01);
    }

    /**
     * Test concurrent transactions maintain correct balance.
     */
    @Test
    public void testConcurrentTransactions() throws InterruptedException {
        BankAccount account = new BankAccount("TEST-004", 1000.0);
        double initialBalance = account.getBalance();

        // Create clients that only deposit (to have predictable result)
        Runnable depositor = () -> {
            for (int i = 0; i < 10; i++) {
                account.deposit(10.0);
            }
        };

        Thread client1 = new Thread(depositor);
        Thread client2 = new Thread(depositor);

        client1.start();
        client2.start();

        client1.join();
        client2.join();

        // Each client deposits 10 * 10 = 100, so total should be initial + 200
        assertEquals("Balance should be 1200 after concurrent deposits",
                1200.0, account.getBalance(), 0.01);
    }
}
