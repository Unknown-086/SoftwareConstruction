package task3;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

/**
 * A Runnable class that writes data to a shared ConcurrentHashMap.
 */
public class DataWriter implements Runnable {
    private final ConcurrentHashMap<String, Integer> sharedMap;
    private final String threadName;
    private final int writeCount;
    private final Random random;

    public DataWriter(ConcurrentHashMap<String, Integer> sharedMap, String threadName, int writeCount) {
        this.sharedMap = sharedMap;
        this.threadName = threadName;
        this.writeCount = writeCount;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < writeCount; i++) {
            String key = "key" + (i % 5); // Write to keys 0-4
            Integer newValue = random.nextInt(100);
            sharedMap.put(key, newValue);
            System.out.println(threadName + " wrote: " + key + " = " + newValue);

            try {
                Thread.sleep(70); // Small delay to show concurrent access
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " completed writing");
    }
}
