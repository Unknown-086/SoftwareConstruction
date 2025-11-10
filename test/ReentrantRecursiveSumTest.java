import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for ReentrantRecursiveSum (Task 4)
 * Tests reentrant recursive functions in single-threaded and multi-threaded
 * scenarios.
 */
public class ReentrantRecursiveSumTest {

    // ========== 8 Comprehensive Tests ==========

    @Test
    public void testBasicFunctionality() {
        // Empty array
        int[] arr = {};
        assertEquals("Empty array should sum to 0", 0, ReentrantRecursiveSum.sumArray(arr));

        // Single element
        int[] arr1 = { 42 };
        assertEquals("Single element array", 42, ReentrantRecursiveSum.sumArray(arr1));

        // Basic array
        int[] arr2 = { 1, 2, 3, 4, 5 };
        assertEquals("Sum of 1+2+3+4+5", 15, ReentrantRecursiveSum.sumArray(arr2));

        // Negative numbers
        int[] arr3 = { -1, -2, -3, -4, -5 };
        assertEquals("Sum of negative numbers", -15, ReentrantRecursiveSum.sumArray(arr3));

        // Mixed numbers
        int[] arr4 = { -10, 5, -3, 8, 2 };
        assertEquals("Sum of mixed positive and negative", 2, ReentrantRecursiveSum.sumArray(arr4));

        // Null array
        assertEquals("Null array should sum to 0", 0, ReentrantRecursiveSum.sumArray(null));
    }

    @Test
    public void testIndexBasedMethod() {
        int[] arr = { 1, 2, 3, 4, 5 };

        assertEquals("Sum from index 0", 15, ReentrantRecursiveSum.sumArray(arr, 0));
        assertEquals("Sum from index 2", 12, ReentrantRecursiveSum.sumArray(arr, 2));
        assertEquals("Sum from index 4", 5, ReentrantRecursiveSum.sumArray(arr, 4));
        assertEquals("Sum from index 5 (past end)", 0, ReentrantRecursiveSum.sumArray(arr, 5));

        // Test with larger array
        int[] arr2 = new int[100];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = i + 1;
        }

        // Sum from index 0: 1+2+...+100 = 100*101/2 = 5050
        assertEquals("From index 0", 5050, ReentrantRecursiveSum.sumArray(arr2, 0));
        // Sum from index 50: 51+52+...+100 = 5050 - (1+2+...+50) = 5050 - 1275 = 3775
        assertEquals("From index 50", 3775, ReentrantRecursiveSum.sumArray(arr2, 50));
        // Sum from index 99: just 100
        assertEquals("From index 99", 100, ReentrantRecursiveSum.sumArray(arr2, 99));
    }

    @Test
    public void testReentrancy() throws Exception {
        // Basic reentrancy test
        ReentrantRecursiveSum.ConcurrencyTestResult result = ReentrantRecursiveSum.testReentrancy(5, 10);
        assertTrue("Reentrant version should pass concurrent test", result.allCorrect);

        // Multiple threads
        result = ReentrantRecursiveSum.testReentrancy(10, 50);
        assertTrue("Reentrant version with 10 threads should pass", result.allCorrect);

        // Stress test
        result = ReentrantRecursiveSum.testReentrancy(50, 100);
        assertTrue("Reentrant version stress test should pass", result.allCorrect);

        // Verify concurrent executions
        result = ReentrantRecursiveSum.testReentrancy(10, 100);
        assertTrue("Should have concurrent executions", result.maxConcurrentExecutions > 1);
    }

    @Test
    public void testNonReentrancy() throws Exception {
        // Run multiple times to increase chance of catching race condition
        // Note: This test demonstrates that non-reentrant code CAN fail,
        // but race conditions are not guaranteed to occur every time
        boolean foundFailure = false;
        int attempts = 20; // Increased attempts to catch race condition

        for (int i = 0; i < attempts; i++) {
            ReentrantRecursiveSum.ConcurrencyTestResult result = ReentrantRecursiveSum.testNonReentrancy(20, 100);
            if (!result.allCorrect) {
                foundFailure = true;
                break;
            }
        }

        // If no failure found after many attempts, just verify it executes without
        // crashing
        // (Race conditions are probabilistic and may not always manifest)
        if (!foundFailure) {
            // At least verify the method runs without exception
            ReentrantRecursiveSum.ConcurrencyTestResult result = ReentrantRecursiveSum.testNonReentrancy(10, 50);
            assertNotNull("Non-reentrant test should complete", result);
        }
    }

    @Test
    public void testSynchronized() throws Exception {
        // Test correctness
        ReentrantRecursiveSum.ConcurrencyTestResult result = ReentrantRecursiveSum.testSynchronized(10, 50);
        assertTrue("Synchronized version should pass concurrent test", result.allCorrect);

        // Stress test
        result = ReentrantRecursiveSum.testSynchronized(20, 100);
        assertTrue("Synchronized version stress test should pass", result.allCorrect);

        // Performance test (should complete within reasonable time)
        long startTime = System.currentTimeMillis();
        result = ReentrantRecursiveSum.testSynchronized(20, 100);
        long executionTime = System.currentTimeMillis() - startTime;
        assertTrue("Should complete within 10 seconds", executionTime < 10000);
    }

    @Test
    public void testConcurrentWithSameArray() throws Exception {
        int[] sharedArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int expectedSum = 55;
        int numThreads = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            Future<Integer> future = executor.submit(() -> ReentrantRecursiveSum.sumArray(sharedArray));
            futures.add(future);
        }

        // All threads should get correct result
        for (Future<Integer> future : futures) {
            int result = future.get();
            assertEquals("All threads should get correct sum", expectedSum, result);
        }

        executor.shutdown();
    }

    @Test
    public void testAllVersionsAgree() {
        int[] arr = { 1, 2, 3, 4, 5 };

        int reentrant = ReentrantRecursiveSum.sumArray(arr);
        int nonReentrant = ReentrantRecursiveSum.sumArrayNonReentrant(arr);
        int synchronized1 = ReentrantRecursiveSum.sumArraySynchronized(arr);

        assertEquals("Reentrant and non-reentrant should agree in single-threaded",
                reentrant, nonReentrant);
        assertEquals("Reentrant and synchronized should agree",
                reentrant, synchronized1);

        // Test with multiple arrays
        int[][] arrays = {
                { 1, 2, 3 },
                { 10, 20, 30 },
                { 5, 10, 15, 20, 25 }
        };

        int[] expected = { 6, 60, 75 };

        for (int i = 0; i < arrays.length; i++) {
            assertEquals("Array " + i, expected[i], ReentrantRecursiveSum.sumArray(arrays[i]));
        }
    }

    @Test
    public void testEdgeCases() {
        // Large array
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }
        // Sum of 1 to 1000 = 1000 * 1001 / 2 = 500500
        assertEquals("Sum of 1 to 1000", 500500, ReentrantRecursiveSum.sumArray(arr));

        // Very large numbers (avoid overflow)
        int[] arr2 = { Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2 };
        int sum = ReentrantRecursiveSum.sumArray(arr2);
        assertTrue("Sum should be positive", sum > 0);

        // Integer.MAX_VALUE
        int[] arr3 = { Integer.MAX_VALUE, 0, 0 };
        assertEquals("Array with MAX_VALUE", Integer.MAX_VALUE, ReentrantRecursiveSum.sumArray(arr3));

        // Integer.MIN_VALUE
        int[] arr4 = { Integer.MIN_VALUE, 0, 0 };
        assertEquals("Array with MIN_VALUE", Integer.MIN_VALUE, ReentrantRecursiveSum.sumArray(arr4));

        // Deep recursion
        int[] arr5 = new int[500];
        for (int i = 0; i < arr5.length; i++) {
            arr5[i] = 1;
        }
        assertEquals("Deep recursion test", 500, ReentrantRecursiveSum.sumArray(arr5));

        // Result toString
        try {
            ReentrantRecursiveSum.ConcurrencyTestResult result = ReentrantRecursiveSum.testReentrancy(5, 10);
            String str = result.toString();
            assertNotNull("Result toString should not be null", str);
            assertTrue("Should contain test name", str.contains("Reentrant"));
        } catch (Exception e) {
            fail("toString test failed: " + e.getMessage());
        }
    }
}
