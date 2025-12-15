package test;

import static org.junit.Assert.*;
import org.junit.Test;
import task2.SharedCounter;
import task2.CounterIncrementer;

/**
 * Test class for verifying thread synchronization in SharedCounter.
 */
public class SharedCounterTest {

    /**
     * Test that a single thread correctly increments the counter 100 times.
     */
    @Test
    public void testSingleThreadIncrement() throws InterruptedException {
        SharedCounter counter = new SharedCounter();
        Thread thread = new Thread(new CounterIncrementer(counter, "Test-Thread"));

        thread.start();
        thread.join();

        assertEquals("Single thread should increment counter to 100", 100, counter.getCount());
    }

    /**
     * Test that three threads correctly increment the counter to 300.
     * This verifies that synchronization prevents race conditions.
     */
    @Test
    public void testThreeThreadsSynchronization() throws InterruptedException {
        SharedCounter counter = new SharedCounter();

        Thread thread1 = new Thread(new CounterIncrementer(counter, "Thread-1"));
        Thread thread2 = new Thread(new CounterIncrementer(counter, "Thread-2"));
        Thread thread3 = new Thread(new CounterIncrementer(counter, "Thread-3"));

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        assertEquals("Three threads should increment counter to 300", 300, counter.getCount());
    }

    /**
     * Test that multiple increments in sequence work correctly.
     */
    @Test
    public void testMultipleIncrements() {
        SharedCounter counter = new SharedCounter();

        for (int i = 0; i < 50; i++) {
            counter.increment();
        }

        assertEquals("50 increments should result in count of 50", 50, counter.getCount());
    }
}
