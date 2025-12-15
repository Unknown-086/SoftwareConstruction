package task4;

/**
 * A thread-safe BankAccount class that handles deposits and withdrawals.
 * Uses synchronized methods to ensure thread safety during transactions.
 */
public class BankAccount {
    private double balance;
    private final String accountNumber;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    /**
     * Synchronized method to deposit money into the account.
     * 
     * @param amount the amount to deposit
     */
    public synchronized void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited $" + amount +
                    " | New Balance: $" + balance);
        }
    }

    /**
     * Synchronized method to withdraw money from the account.
     * 
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful, false if insufficient funds
     */
    public synchronized boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " withdrew $" + amount +
                    " | New Balance: $" + balance);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() + " failed to withdraw $" + amount +
                    " | Insufficient funds. Balance: $" + balance);
            return false;
        }
    }

    /**
     * Get the current balance of the account.
     * 
     * @return the current balance
     */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Get the account number.
     * 
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
}
