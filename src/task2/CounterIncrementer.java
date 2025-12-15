package task2;

/**
 * A Runnable class that increments a shared counter 100 times.
 */
public class CounterIncrementer implements Runnable {
    private final SharedCounter counter;
    private final String threadName;

    public CounterIncrementer(SharedCounter counter, String threadName) {
        this.counter = counter;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            counter.increment();
        }
        System.out.println(threadName + " completed 100 increments");
    }
}
