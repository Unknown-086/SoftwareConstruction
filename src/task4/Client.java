package task4;

import java.util.Random;

/**
 * A Client thread that performs random deposit and withdrawal operations on a
 * bank account.
 */
public class Client implements Runnable {
    private final BankAccount account;
    private final String clientName;
    private final int transactionCount;
    private final Random random;

    public Client(BankAccount account, String clientName, int transactionCount) {
        this.account = account;
        this.clientName = clientName;
        this.transactionCount = transactionCount;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < transactionCount; i++) {
            // Randomly choose between deposit and withdrawal
            boolean isDeposit = random.nextBoolean();
            double amount = 10 + random.nextInt(90); // Amount between 10 and 100

            if (isDeposit) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }

            // Small delay between transactions
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(clientName + " completed all transactions");
    }
}
