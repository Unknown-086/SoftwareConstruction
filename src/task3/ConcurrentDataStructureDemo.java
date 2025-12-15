package task3;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Lab Task 3: Concurrent Data Structures
 * 
 * This program demonstrates the use of thread-safe data structures
 * (ConcurrentHashMap).
 * Multiple threads concurrently read and write to the shared map without
 * explicit synchronization.
 * ConcurrentHashMap handles thread safety internally.
 */
public class ConcurrentDataStructureDemo {

    public static void main(String[] args) {
        // Create a thread-safe ConcurrentHashMap
        ConcurrentHashMap<String, Integer> sharedMap = new ConcurrentHashMap<>();

        // Initialize with some data
        for (int i = 0; i < 5; i++) {
            sharedMap.put("key" + i, i * 10);
        }

        System.out.println("Initial Map: " + sharedMap);
        System.out.println("\nStarting concurrent read and write operations...\n");

        // Create multiple reader threads
        Thread reader1 = new Thread(new DataReader(sharedMap, "Reader-1", 5));
        Thread reader2 = new Thread(new DataReader(sharedMap, "Reader-2", 5));

        // Create multiple writer threads
        Thread writer1 = new Thread(new DataWriter(sharedMap, "Writer-1", 5));
        Thread writer2 = new Thread(new DataWriter(sharedMap, "Writer-2", 5));

        // Start all threads
        reader1.start();
        writer1.start();
        reader2.start();
        writer2.start();

        // Wait for all threads to complete
        try {
            reader1.join();
            reader2.join();
            writer1.join();
            writer2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll threads completed.");
        System.out.println("Final Map: " + sharedMap);
        System.out.println("\nConcurrentHashMap handled all concurrent operations safely!");
    }
}
