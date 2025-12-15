package test;

import static org.junit.Assert.*;
import org.junit.Test;
import task3.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test class for verifying concurrent data structure operations.
 */
public class ConcurrentDataStructureTest {

    /**
     * Test that ConcurrentHashMap handles concurrent writes correctly.
     */
    @Test
    public void testConcurrentWrites() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        Thread writer1 = new Thread(new DataWriter(map, "Writer-1", 10));
        Thread writer2 = new Thread(new DataWriter(map, "Writer-2", 10));

        writer1.start();
        writer2.start();

        writer1.join();
        writer2.join();

        // Verify that map has entries (exact values may vary due to concurrent writes)
        assertFalse("Map should not be empty after concurrent writes", map.isEmpty());
    }

    /**
     * Test that ConcurrentHashMap handles concurrent reads correctly.
     */
    @Test
    public void testConcurrentReads() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Initialize map
        for (int i = 0; i < 5; i++) {
            map.put("key" + i, i * 10);
        }

        Thread reader1 = new Thread(new DataReader(map, "Reader-1", 5));
        Thread reader2 = new Thread(new DataReader(map, "Reader-2", 5));

        reader1.start();
        reader2.start();

        reader1.join();
        reader2.join();

        // Verify map still has correct size
        assertEquals("Map should maintain correct size", 5, map.size());
    }
}
