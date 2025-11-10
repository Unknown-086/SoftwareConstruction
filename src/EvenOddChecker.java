/**
 * Task 3: Mutual Recursion - Even and Odd Checker
 * 
 * This class demonstrates mutual recursion where two methods call each other
 * to determine if a number is even or odd. This is an educational example
 * of mutual recursion, though the modulo operator would be more efficient
 * in practice.
 */
public class EvenOddChecker {

    /**
     * Determines if a number is even using mutual recursion.
     * Calls isOdd() for recursive case.
     * 
     * Base case: n == 0 returns true (zero is even)
     * Recursive case: calls isOdd(abs(n) - 1)
     * 
     * @param n the integer to check
     * @return true if n is even, false otherwise
     */
    public static boolean isEven(int n) {
        // Handle negative numbers by using absolute value
        // -4 is even, -3 is odd, so we work with absolute values
        if (n < 0) {
            n = -n; // Convert to positive
            // Special case for Integer.MIN_VALUE to avoid overflow
            if (n < 0) {
                // Integer.MIN_VALUE negated is still negative
                // Integer.MIN_VALUE = -2147483648, which is even
                return true;
            }
        }

        // Base case: 0 is even
        if (n == 0) {
            return true;
        }

        // Recursive case: if n is even, then n-1 is odd
        // So we call isOdd(n-1)
        return isOdd(n - 1);
    }

    /**
     * Determines if a number is odd using mutual recursion.
     * Calls isEven() for recursive case.
     * 
     * Base case: n == 0 returns false (zero is not odd)
     * Recursive case: calls isEven(abs(n) - 1)
     * 
     * @param n the integer to check
     * @return true if n is odd, false otherwise
     */
    public static boolean isOdd(int n) {
        // Handle negative numbers by using absolute value
        // -4 is even, -3 is odd, so we work with absolute values
        if (n < 0) {
            n = -n; // Convert to positive
            // Special case for Integer.MIN_VALUE to avoid overflow
            if (n < 0) {
                // Integer.MIN_VALUE = -2147483648, which is even (so not odd)
                return false;
            }
        }

        // Base case: 0 is not odd
        if (n == 0) {
            return false;
        }

        // Recursive case: if n is odd, then n-1 is even
        // So we call isEven(n-1)
        return isEven(n - 1);
    }

    /**
     * Alternative implementation using standard modulo operator.
     * Provided for comparison and verification.
     * 
     * @param n the integer to check
     * @return true if n is even, false otherwise
     */
    public static boolean isEvenIterative(int n) {
        return n % 2 == 0;
    }

    /**
     * Alternative implementation using standard modulo operator.
     * Provided for comparison and verification.
     * 
     * @param n the integer to check
     * @return true if n is odd, false otherwise
     */
    public static boolean isOddIterative(int n) {
        return n % 2 != 0;
    }

    /**
     * Compares performance between recursive and iterative implementations.
     * 
     * @param n          the number to test
     * @param iterations number of iterations for benchmarking
     */
    public static void comparePerformance(int n, int iterations) {
        // Warm up
        for (int i = 0; i < 10000; i++) {
            isEven(Math.abs(n) % 1000); // Use smaller numbers to avoid stack overflow
            isEvenIterative(n);
        }

        // Test recursive version (use smaller number to avoid stack overflow)
        int testN = Math.abs(n) % 1000;
        long startRecursive = System.nanoTime();
        boolean resultRecursive = false;
        for (int i = 0; i < iterations; i++) {
            resultRecursive = isEven(testN);
        }
        long endRecursive = System.nanoTime();

        // Test iterative version
        long startIterative = System.nanoTime();
        boolean resultIterative = false;
        for (int i = 0; i < iterations; i++) {
            resultIterative = isEvenIterative(n);
        }
        long endIterative = System.nanoTime();

        double recursiveTime = (endRecursive - startRecursive) / 1_000_000.0;
        double iterativeTime = (endIterative - startIterative) / 1_000_000.0;

        System.out.println("Performance Comparison for n=" + testN + " (recursive) vs n=" + n + " (iterative)");
        System.out.println("Iterations: " + iterations);
        System.out.println("Recursive time: " + recursiveTime + " ms");
        System.out.println("Iterative time: " + iterativeTime + " ms");
        System.out.println("Ratio (Recursive/Iterative): " + (recursiveTime / iterativeTime));
        System.out.println("Results match: " + (resultRecursive == resultIterative));
        System.out.println();
    }

    /**
     * Traces the mutual recursion calls for educational purposes.
     * Shows the call sequence for understanding how mutual recursion works.
     * 
     * @param n the number to check
     * @return true if n is even, false otherwise
     */
    public static boolean isEvenWithTrace(int n) {
        return isEvenTrace(n, 0);
    }

    /**
     * Helper method that traces recursive calls.
     * 
     * @param n     the number to check
     * @param depth current recursion depth
     * @return true if n is even, false otherwise
     */
    private static boolean isEvenTrace(int n, int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "isEven(" + n + ")");

        if (n < 0) {
            n = -n;
            if (n < 0) {
                System.out.println(indent + "  Integer.MIN_VALUE, return true");
                return true;
            }
            System.out.println(indent + "  converted to positive: " + n);
        }

        if (n == 0) {
            System.out.println(indent + "  base case: 0 is even, return true");
            return true;
        }

        System.out.println(indent + "  calling isOdd(" + (n - 1) + ")");
        boolean result = isOddTrace(n - 1, depth + 1);
        System.out.println(indent + "  isOdd returned " + result);
        return result;
    }

    /**
     * Helper method that traces recursive calls.
     * 
     * @param n     the number to check
     * @param depth current recursion depth
     * @return true if n is odd, false otherwise
     */
    private static boolean isOddTrace(int n, int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "isOdd(" + n + ")");

        if (n < 0) {
            n = -n;
            if (n < 0) {
                System.out.println(indent + "  Integer.MIN_VALUE, return false");
                return false;
            }
            System.out.println(indent + "  converted to positive: " + n);
        }

        if (n == 0) {
            System.out.println(indent + "  base case: 0 is not odd, return false");
            return false;
        }

        System.out.println(indent + "  calling isEven(" + (n - 1) + ")");
        boolean result = isEvenTrace(n - 1, depth + 1);
        System.out.println(indent + "  isEven returned " + result);
        return result;
    }

    /**
     * Main method for demonstration and testing.
     */
    public static void main(String[] args) {
        System.out.println("=== Mutual Recursion: Even and Odd Checker ===\n");

        // Test positive numbers
        System.out.println("--- Positive Numbers ---");
        System.out.println("0 is even: " + isEven(0));
        System.out.println("0 is odd: " + isOdd(0));
        System.out.println("1 is even: " + isEven(1));
        System.out.println("1 is odd: " + isOdd(1));
        System.out.println("2 is even: " + isEven(2));
        System.out.println("2 is odd: " + isOdd(2));
        System.out.println("10 is even: " + isEven(10));
        System.out.println("10 is odd: " + isOdd(10));
        System.out.println("99 is even: " + isEven(99));
        System.out.println("99 is odd: " + isOdd(99));
        System.out.println("100 is even: " + isEven(100));
        System.out.println("100 is odd: " + isOdd(100));
        System.out.println();

        // Test negative numbers
        System.out.println("--- Negative Numbers ---");
        System.out.println("-1 is even: " + isEven(-1));
        System.out.println("-1 is odd: " + isOdd(-1));
        System.out.println("-2 is even: " + isEven(-2));
        System.out.println("-2 is odd: " + isOdd(-2));
        System.out.println("-10 is even: " + isEven(-10));
        System.out.println("-10 is odd: " + isOdd(-10));
        System.out.println("-99 is even: " + isEven(-99));
        System.out.println("-99 is odd: " + isOdd(-99));
        System.out.println("-100 is even: " + isEven(-100));
        System.out.println("-100 is odd: " + isOdd(-100));
        System.out.println();

        // Test edge cases
        System.out.println("--- Edge Cases ---");
        // Note: Cannot test Integer.MAX_VALUE with recursion (would cause
        // StackOverflowError)
        // Instead, use iterative version to verify correctness
        System.out.println("Integer.MAX_VALUE (" + Integer.MAX_VALUE + ") is even (iterative): "
                + isEvenIterative(Integer.MAX_VALUE));
        System.out.println("Integer.MAX_VALUE (" + Integer.MAX_VALUE + ") is odd (iterative): "
                + isOddIterative(Integer.MAX_VALUE));
        System.out.println("Integer.MIN_VALUE (" + Integer.MIN_VALUE + ") is even: " + isEven(Integer.MIN_VALUE));
        System.out.println("Integer.MIN_VALUE (" + Integer.MIN_VALUE + ") is odd: " + isOdd(Integer.MIN_VALUE));
        System.out.println();

        // Verify consistency with iterative version
        System.out.println("--- Verification with Iterative Version ---");
        int[] testValues = { 0, 1, -1, 2, -2, 10, -10, 99, -99, 100, -100,
                1000, -1000, Integer.MIN_VALUE };
        boolean allMatch = true;

        for (int val : testValues) {
            // For very large numbers, we can't use recursive version (stack overflow)
            // So we only verify for reasonable values
            boolean recEven = isEven(val);
            boolean iterEven = isEvenIterative(val);
            boolean recOdd = isOdd(val);
            boolean iterOdd = isOddIterative(val);

            if (recEven != iterEven || recOdd != iterOdd) {
                System.out.println("MISMATCH for " + val);
                System.out.println("  Recursive: even=" + recEven + ", odd=" + recOdd);
                System.out.println("  Iterative: even=" + iterEven + ", odd=" + iterOdd);
                allMatch = false;
            }
        }

        if (allMatch) {
            System.out.println("All test values match between recursive and iterative versions!");
        }
        System.out.println();

        // Demonstrate mutual recursion with trace
        System.out.println("=== Mutual Recursion Trace ===");
        System.out.println("\nTrace for n = 5:");
        boolean result = isEvenWithTrace(5);
        System.out.println("Final result: 5 is even = " + result);

        System.out.println("\nTrace for n = 4:");
        result = isEvenWithTrace(4);
        System.out.println("Final result: 4 is even = " + result);

        System.out.println("\nTrace for n = -3:");
        result = isEvenWithTrace(-3);
        System.out.println("Final result: -3 is even = " + result);
        System.out.println();

        // Performance comparison
        System.out.println("=== Performance Comparison ===\n");
        comparePerformance(100, 100000);
        comparePerformance(500, 100000);
        comparePerformance(999, 100000);

    }
}
