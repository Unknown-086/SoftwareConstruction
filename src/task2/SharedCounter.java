package task2;

/**
 * A shared counter class that uses synchronization to ensure thread safety.
 * Multiple threads can safely increment the counter without race conditions.
 */
public class SharedCounter {
    private int count = 0;

    /**
     * Synchronized method to increment the counter.
     * Only one thread can execute this method at a time.
     */
    public synchronized void increment() {
        count++;
    }

    /**
     * Get the current count value.
     * 
     * @return the current count
     */
    public synchronized int getCount() {
        return count;
    }
}
