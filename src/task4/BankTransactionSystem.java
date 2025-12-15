package task4;

/**
 * Lab Task 4: Simulation of Bank Transaction System
 * 
 * This program simulates a bank account with multiple clients (threads).
 * Each client performs random deposit and withdrawal operations concurrently.
 * The BankAccount class uses synchronized methods to ensure thread safety
 * and maintain accurate balance after all transactions.
 */
public class BankTransactionSystem {

    public static void main(String[] args) {
        // Create a bank account with initial balance
        BankAccount account = new BankAccount("ACC-12345", 1000.0);

        System.out.println("=== Bank Transaction System ===");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Initial Balance: $" + account.getBalance());
        System.out.println("\nStarting transactions with multiple clients...\n");

        // Create multiple client threads
        Thread client1 = new Thread(new Client(account, "Client-1", 5), "Client-1");
        Thread client2 = new Thread(new Client(account, "Client-2", 5), "Client-2");
        Thread client3 = new Thread(new Client(account, "Client-3", 5), "Client-3");

        // Start all client threads
        client1.start();
        client2.start();
        client3.start();

        // Wait for all clients to complete their transactions
        try {
            client1.join();
            client2.join();
            client3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Transaction Summary ===");
        System.out.println("All clients completed their transactions");
        System.out.println("Final Balance: $" + account.getBalance());
        System.out.println("\nAll transactions completed safely with synchronized methods!");
    }
}
