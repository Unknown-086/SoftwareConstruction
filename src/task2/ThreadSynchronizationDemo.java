package task2;

/**
 * Lab Task 2: Thread Synchronization
 * 
 * This program demonstrates thread synchronization using the synchronized
 * keyword.
 * Three threads each increment a shared counter 100 times.
 * With proper synchronization, the final count should be exactly 300.
 */
public class ThreadSynchronizationDemo {

    public static void main(String[] args) {
        // Create a shared counter
        SharedCounter counter = new SharedCounter();

        // Create three threads that will increment the counter
        Thread thread1 = new Thread(new CounterIncrementer(counter, "Thread-1"));
        Thread thread2 = new Thread(new CounterIncrementer(counter, "Thread-2"));
        Thread thread3 = new Thread(new CounterIncrementer(counter, "Thread-3"));

        System.out.println("Starting three threads to increment counter...\n");

        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();

        // Wait for all threads to complete
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final count
        System.out.println("\nAll threads completed.");
        System.out.println("Final counter value: " + counter.getCount());
        System.out.println("Expected value: 300");

        if (counter.getCount() == 300) {
            System.out.println("Synchronization successful!");
        } else {
            System.out.println("Race condition occurred!");
        }
    }
}
