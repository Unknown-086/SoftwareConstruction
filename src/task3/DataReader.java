package task3;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A Runnable class that reads data from a shared ConcurrentHashMap.
 */
public class DataReader implements Runnable {
    private final ConcurrentHashMap<String, Integer> sharedMap;
    private final String threadName;
    private final int readCount;

    public DataReader(ConcurrentHashMap<String, Integer> sharedMap, String threadName, int readCount) {
        this.sharedMap = sharedMap;
        this.threadName = threadName;
        this.readCount = readCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < readCount; i++) {
            String key = "key" + (i % 5); // Read from keys 0-4
            Integer value = sharedMap.get(key);
            System.out.println(threadName + " read: " + key + " = " + value);

            try {
                Thread.sleep(50); // Small delay to show concurrent access
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " completed reading");
    }
}
