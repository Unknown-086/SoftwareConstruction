import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

/**
 * Task 4: Reentrant Recursive Sum
 * 
 * This class demonstrates reentrant recursive functions that can be safely
 * used in multi-threaded environments, and contrasts them with non-reentrant
 * implementations that use shared mutable state.
 */
public class ReentrantRecursiveSum {

    // Non-reentrant: shared mutable state (BAD for multi-threading)
    private static int nonReentrantSum = 0;

    // Counter for tracking concurrent executions
    private static AtomicInteger concurrentExecutions = new AtomicInteger(0);
    private static AtomicInteger maxConcurrent = new AtomicInteger(0);

    /**
     * REENTRANT VERSION: Recursively sums array elements.
     * Uses only local variables and parameters - safe for concurrent use.
     * 
     * @param arr   the array to sum
     * @param index current index in the array
     * @return sum of elements from index to end of array
     */
    public static int sumArray(int[] arr, int index) {
        // Base case: reached end of array
        if (index >= arr.length) {
            return 0;
        }

        // Recursive case: current element + sum of remaining elements
        // Uses only local variables and parameters - REENTRANT
        return arr[index] + sumArray(arr, index + 1);
    }

    /**
     * Convenient wrapper that starts from index 0.
     * 
     * @param arr the array to sum
     * @return sum of all elements in the array
     */
    public static int sumArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return sumArray(arr, 0);
    }

    /**
     * NON-REENTRANT VERSION: Uses shared mutable state.
     * NOT safe for concurrent use - for demonstration purposes only.
     * 
     * @param arr   the array to sum
     * @param index current index in the array
     * @return sum of elements (stored in static variable)
     */
    public static int sumArrayNonReentrant(int[] arr, int index) {
        // Base case: reached end of array
        if (index >= arr.length) {
            return nonReentrantSum;
        }

        // Uses shared mutable state - NOT REENTRANT
        nonReentrantSum += arr[index];
        return sumArrayNonReentrant(arr, index + 1);
    }

    /**
     * NON-REENTRANT wrapper - must reset static variable before use.
     * 
     * @param arr the array to sum
     * @return sum of all elements (may be incorrect in concurrent scenarios)
     */
    public static int sumArrayNonReentrant(int[] arr) {
        nonReentrantSum = 0; // Reset shared state
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return sumArrayNonReentrant(arr, 0);
    }

    /**
     * SYNCHRONIZED NON-REENTRANT VERSION: Uses synchronization.
     * Safe for concurrent use but slower due to locking.
     * 
     * @param arr   the array to sum
     * @param index current index in the array
     * @return sum of elements
     */
    public static synchronized int sumArraySynchronized(int[] arr, int index) {
        // Base case: reached end of array
        if (index >= arr.length) {
            return nonReentrantSum;
        }

        // Synchronized access to shared state
        nonReentrantSum += arr[index];
        return sumArraySynchronized(arr, index + 1);
    }

    /**
     * SYNCHRONIZED wrapper - resets state safely.
     * 
     * @param arr the array to sum
     * @return sum of all elements (correct even in concurrent scenarios)
     */
    public static synchronized int sumArraySynchronized(int[] arr) {
        nonReentrantSum = 0;
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return sumArraySynchronized(arr, 0);
    }

    /**
     * Demonstrates reentrancy by calling sumArray concurrently.
     * 
     * @param numThreads number of concurrent threads
     * @param arraySize  size of arrays to sum
     * @return results showing consistency
     * @throws InterruptedException if thread execution is interrupted
     */
    public static ConcurrencyTestResult testReentrancy(int numThreads, int arraySize)
            throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();

        // Create test arrays with known sums
        int[][] testArrays = new int[numThreads][];
        int[] expectedSums = new int[numThreads];

        for (int i = 0; i < numThreads; i++) {
            testArrays[i] = new int[arraySize];
            for (int j = 0; j < arraySize; j++) {
                testArrays[i][j] = (i + 1) * 10 + j; // Different values per thread
            }
            // Calculate expected sum
            expectedSums[i] = 0;
            for (int val : testArrays[i]) {
                expectedSums[i] += val;
            }
        }

        long startTime = System.nanoTime();

        // Submit tasks concurrently
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            Future<Integer> future = executor.submit(() -> {
                // Track concurrent executions
                int current = concurrentExecutions.incrementAndGet();
                maxConcurrent.updateAndGet(max -> Math.max(max, current));

                try {
                    return sumArray(testArrays[threadIndex]);
                } finally {
                    concurrentExecutions.decrementAndGet();
                }
            });
            futures.add(future);
        }

        // Collect results
        int[] actualSums = new int[numThreads];
        for (int i = 0; i < numThreads; i++) {
            actualSums[i] = futures.get(i).get();
        }

        long endTime = System.nanoTime();
        executor.shutdown();

        // Check consistency
        boolean allCorrect = true;
        for (int i = 0; i < numThreads; i++) {
            if (actualSums[i] != expectedSums[i]) {
                allCorrect = false;
                break;
            }
        }

        return new ConcurrencyTestResult(
                "Reentrant sumArray()",
                numThreads,
                expectedSums,
                actualSums,
                allCorrect,
                (endTime - startTime) / 1_000_000.0, // Convert to milliseconds
                maxConcurrent.get());
    }

    /**
     * Tests non-reentrant version (expected to fail in concurrent scenario).
     * 
     * @param numThreads number of concurrent threads
     * @param arraySize  size of arrays to sum
     * @return results showing inconsistency
     * @throws InterruptedException if thread execution is interrupted
     */
    public static ConcurrencyTestResult testNonReentrancy(int numThreads, int arraySize)
            throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();

        // Create test arrays
        int[][] testArrays = new int[numThreads][];
        int[] expectedSums = new int[numThreads];

        for (int i = 0; i < numThreads; i++) {
            testArrays[i] = new int[arraySize];
            for (int j = 0; j < arraySize; j++) {
                testArrays[i][j] = (i + 1) * 10 + j;
            }
            expectedSums[i] = 0;
            for (int val : testArrays[i]) {
                expectedSums[i] += val;
            }
        }

        long startTime = System.nanoTime();

        // Submit tasks concurrently - will interfere with each other
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            Future<Integer> future = executor.submit(() -> sumArrayNonReentrant(testArrays[threadIndex]));
            futures.add(future);
        }

        // Collect results
        int[] actualSums = new int[numThreads];
        for (int i = 0; i < numThreads; i++) {
            actualSums[i] = futures.get(i).get();
        }

        long endTime = System.nanoTime();
        executor.shutdown();

        // Check consistency (expected to fail)
        boolean allCorrect = true;
        for (int i = 0; i < numThreads; i++) {
            if (actualSums[i] != expectedSums[i]) {
                allCorrect = false;
                break;
            }
        }

        return new ConcurrencyTestResult(
                "Non-Reentrant (shared state)",
                numThreads,
                expectedSums,
                actualSums,
                allCorrect,
                (endTime - startTime) / 1_000_000.0,
                0 // Not tracking for non-reentrant
        );
    }

    /**
     * Tests synchronized version (safe but slower).
     * 
     * @param numThreads number of concurrent threads
     * @param arraySize  size of arrays to sum
     * @return results showing consistency
     * @throws InterruptedException if thread execution is interrupted
     */
    public static ConcurrencyTestResult testSynchronized(int numThreads, int arraySize)
            throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Integer>> futures = new ArrayList<>();

        // Create test arrays
        int[][] testArrays = new int[numThreads][];
        int[] expectedSums = new int[numThreads];

        for (int i = 0; i < numThreads; i++) {
            testArrays[i] = new int[arraySize];
            for (int j = 0; j < arraySize; j++) {
                testArrays[i][j] = (i + 1) * 10 + j;
            }
            expectedSums[i] = 0;
            for (int val : testArrays[i]) {
                expectedSums[i] += val;
            }
        }

        long startTime = System.nanoTime();

        // Submit tasks concurrently - synchronized access
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            Future<Integer> future = executor.submit(() -> sumArraySynchronized(testArrays[threadIndex]));
            futures.add(future);
        }

        // Collect results
        int[] actualSums = new int[numThreads];
        for (int i = 0; i < numThreads; i++) {
            actualSums[i] = futures.get(i).get();
        }

        long endTime = System.nanoTime();
        executor.shutdown();

        // Check consistency
        boolean allCorrect = true;
        for (int i = 0; i < numThreads; i++) {
            if (actualSums[i] != expectedSums[i]) {
                allCorrect = false;
                break;
            }
        }

        return new ConcurrencyTestResult(
                "Synchronized (locked access)",
                numThreads,
                expectedSums,
                actualSums,
                allCorrect,
                (endTime - startTime) / 1_000_000.0,
                0 // Sequential due to synchronization
        );
    }

    /**
     * Result class for concurrency tests.
     */
    public static class ConcurrencyTestResult {
        public final String testName;
        public final int numThreads;
        public final int[] expectedSums;
        public final int[] actualSums;
        public final boolean allCorrect;
        public final double executionTimeMs;
        public final int maxConcurrentExecutions;

        public ConcurrencyTestResult(String testName, int numThreads, int[] expectedSums,
                int[] actualSums, boolean allCorrect, double executionTimeMs,
                int maxConcurrentExecutions) {
            this.testName = testName;
            this.numThreads = numThreads;
            this.expectedSums = expectedSums;
            this.actualSums = actualSums;
            this.allCorrect = allCorrect;
            this.executionTimeMs = executionTimeMs;
            this.maxConcurrentExecutions = maxConcurrentExecutions;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== ").append(testName).append(" ===\n");
            sb.append("Threads: ").append(numThreads).append("\n");
            sb.append("Execution time: ").append(String.format("%.2f", executionTimeMs)).append(" ms\n");
            if (maxConcurrentExecutions > 0) {
                sb.append("Max concurrent: ").append(maxConcurrentExecutions).append("\n");
            }
            sb.append("Result: ").append(allCorrect ? "ALL CORRECT" : "✗ ERRORS FOUND").append("\n");

            if (!allCorrect) {
                sb.append("\nMismatches:\n");
                for (int i = 0; i < numThreads; i++) {
                    if (expectedSums[i] != actualSums[i]) {
                        sb.append("  Thread ").append(i).append(": expected ")
                                .append(expectedSums[i]).append(", got ").append(actualSums[i]).append("\n");
                    }
                }
            }

            return sb.toString();
        }
    }

    /**
     * Main method for demonstration.
     */
    public static void main(String[] args) {
        System.out.println("=== Reentrant Recursive Sum ===\n");

        // Test basic functionality
        System.out.println("--- Basic Functionality Test ---");
        int[] testArray = { 1, 2, 3, 4, 5 };
        int sum = sumArray(testArray);
        System.out.println("Array: " + Arrays.toString(testArray));
        System.out.println("Sum: " + sum);
        System.out.println("Expected: 15");
        System.out.println("Correct: " + (sum == 15));
        System.out.println();

        // Test with different arrays
        System.out.println("--- Multiple Arrays Test ---");
        int[][] arrays = {
                { 1, 2, 3 },
                { 10, 20, 30 },
                { 100, 200, 300 },
                { 5, 10, 15, 20 }
        };

        for (int[] arr : arrays) {
            int result = sumArray(arr);
            int expected = 0;
            for (int val : arr)
                expected += val;
            System.out.println("Array: " + Arrays.toString(arr) + " Sum: " + result +
                    " (Expected: " + expected + ")");
        }
        System.out.println();

        // Test reentrancy with concurrency
        System.out.println("=== Concurrency Tests ===\n");

        try {
            // Test 1: Reentrant version (should pass)
            System.out.println("Test 1: Reentrant Implementation");
            maxConcurrent.set(0);
            ConcurrencyTestResult result1 = testReentrancy(10, 100);
            System.out.println(result1);

            // Test 2: Non-reentrant version (should fail)
            System.out.println("Test 2: Non-Reentrant Implementation (Expected to fail)");
            ConcurrencyTestResult result2 = testNonReentrancy(10, 100);
            System.out.println(result2);

            // Test 3: Synchronized version (should pass but slower)
            System.out.println("Test 3: Synchronized Implementation");
            ConcurrencyTestResult result3 = testSynchronized(10, 100);
            System.out.println(result3);

            // Performance comparison
            System.out.println("=== Performance Comparison ===");
            System.out.println("Reentrant:     " + String.format("%.2f", result1.executionTimeMs) + " ms");
            System.out.println("Synchronized:  " + String.format("%.2f", result3.executionTimeMs) + " ms");
            System.out.println("Ratio: " + String.format("%.2fx", result3.executionTimeMs / result1.executionTimeMs));
            System.out.println();

            // Stress test
            System.out.println("=== Stress Test (100 threads) ===\n");
            maxConcurrent.set(0);
            ConcurrencyTestResult stressResult = testReentrancy(100, 1000);
            System.out.println(stressResult);

        } catch (Exception e) {
            System.err.println("Error during concurrency test: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
