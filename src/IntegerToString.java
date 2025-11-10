/**
 * Task 1: Integer-to-String Conversion using Recursion
 * 
 * This class provides recursive methods to convert integers to their
 * string representation in different bases (2-36).
 */
public class IntegerToString {

    /**
     * Converts an integer to its string representation in the specified base.
     * Supports bases from 2 to 36.
     * 
     * @param n    the integer to convert
     * @param base the base for conversion (2-36)
     * @return string representation of n in the specified base
     * @throws IllegalArgumentException if base is not between 2 and 36
     */
    public static String stringValue(int n, int base) {
        // Input validation
        if (base < 2 || base > 36) {
            throw new IllegalArgumentException("Base must be between 2 and 36, got: " + base);
        }

        // Handle negative numbers
        if (n < 0) {
            // Special case for Integer.MIN_VALUE to avoid overflow
            if (n == Integer.MIN_VALUE) {
                return "-" + stringValueHelper(-(n / base), base) + getDigitChar(-(n % base));
            }
            return "-" + stringValueHelper(-n, base);
        }

        return stringValueHelper(n, base);
    }

    /**
     * Helper method to perform the recursive conversion.
     * 
     * @param n    the positive integer to convert
     * @param base the base for conversion
     * @return string representation of n in the specified base
     */
    private static String stringValueHelper(int n, int base) {
        // Base case: n is less than base
        if (n < base) {
            return String.valueOf(getDigitChar(n));
        }

        // Recursive case: process higher digits first, then append current digit
        return stringValueHelper(n / base, base) + getDigitChar(n % base);
    }

    /**
     * Converts a digit (0-35) to its character representation.
     * 0-9 map to '0'-'9', 10-35 map to 'A'-'Z'.
     * 
     * @param digit the digit to convert (0-35)
     * @return character representation of the digit
     */
    private static char getDigitChar(int digit) {
        if (digit < 10) {
            return (char) ('0' + digit);
        } else {
            return (char) ('A' + (digit - 10));
        }
    }

    /**
     * Iterative version of integer-to-string conversion for performance comparison.
     * 
     * @param n    the integer to convert
     * @param base the base for conversion (2-36)
     * @return string representation of n in the specified base
     * @throws IllegalArgumentException if base is not between 2 and 36
     */
    public static String stringValueIterative(int n, int base) {
        // Input validation
        if (base < 2 || base > 36) {
            throw new IllegalArgumentException("Base must be between 2 and 36, got: " + base);
        }

        // Handle zero
        if (n == 0) {
            return "0";
        }

        // Handle negative numbers
        boolean isNegative = n < 0;
        if (isNegative && n == Integer.MIN_VALUE) {
            // Special handling for Integer.MIN_VALUE
            StringBuilder result = new StringBuilder();
            long absValue = -(long) n;
            while (absValue > 0) {
                result.insert(0, getDigitChar((int) (absValue % base)));
                absValue /= base;
            }
            return "-" + result.toString();
        }

        n = Math.abs(n);

        // Build the result string
        StringBuilder result = new StringBuilder();
        while (n > 0) {
            result.insert(0, getDigitChar(n % base));
            n /= base;
        }

        if (isNegative) {
            result.insert(0, '-');
        }

        return result.toString();
    }

    /**
     * Compares performance between recursive and iterative implementations.
     * 
     * @param n          the integer to convert
     * @param base       the base for conversion
     * @param iterations number of iterations for benchmarking
     */
    public static void comparePerformance(int n, int base, int iterations) {
        // Warm up
        for (int i = 0; i < 1000; i++) {
            stringValue(n, base);
            stringValueIterative(n, base);
        }

        // Test recursive version
        long startRecursive = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            stringValue(n, base);
        }
        long endRecursive = System.nanoTime();

        // Test iterative version
        long startIterative = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            stringValueIterative(n, base);
        }
        long endIterative = System.nanoTime();

        double recursiveTime = (endRecursive - startRecursive) / 1_000_000.0;
        double iterativeTime = (endIterative - startIterative) / 1_000_000.0;

        System.out.println("Performance Comparison for n=" + n + ", base=" + base);
        System.out.println("Iterations: " + iterations);
        System.out.println("Recursive time: " + recursiveTime + " ms");
        System.out.println("Iterative time: " + iterativeTime + " ms");
        System.out.println("Ratio (Recursive/Iterative): " + (recursiveTime / iterativeTime));
        System.out.println();
    }

    /**
     * Main method for demonstration and testing.
     */
    public static void main(String[] args) {
        System.out.println("=== Integer-to-String Conversion Demo ===\n");

        // Basic conversions
        System.out.println("Basic Conversions:");
        System.out.println("42 in base 10: " + stringValue(42, 10));
        System.out.println("42 in base 2: " + stringValue(42, 2));
        System.out.println("42 in base 16: " + stringValue(42, 16));
        System.out.println("255 in base 16: " + stringValue(255, 16));
        System.out.println();

        // Negative numbers
        System.out.println("Negative Numbers:");
        System.out.println("-42 in base 10: " + stringValue(-42, 10));
        System.out.println("-42 in base 2: " + stringValue(-42, 2));
        System.out.println("-255 in base 16: " + stringValue(-255, 16));
        System.out.println();

        // Extended base range (up to 36)
        System.out.println("Extended Base Range (up to 36):");
        System.out.println("1000 in base 36: " + stringValue(1000, 36));
        System.out.println("12345 in base 36: " + stringValue(12345, 36));
        System.out.println();

        // Edge cases
        System.out.println("Edge Cases:");
        System.out.println("0 in base 10: " + stringValue(0, 10));
        System.out.println("Integer.MAX_VALUE in base 10: " + stringValue(Integer.MAX_VALUE, 10));
        System.out.println("Integer.MIN_VALUE in base 10: " + stringValue(Integer.MIN_VALUE, 10));
        System.out.println();

        // Performance comparison
        System.out.println("=== Performance Comparison ===\n");
        comparePerformance(123456789, 10, 100000);
        comparePerformance(123456789, 2, 100000);
        comparePerformance(123456789, 16, 100000);
        comparePerformance(123456789, 36, 100000);

        // Verify both methods produce same results
        System.out.println("=== Verification ===");
        int[] testValues = { 0, 1, -1, 42, -42, 255, -255, 1000, Integer.MAX_VALUE, Integer.MIN_VALUE };
        int[] testBases = { 2, 8, 10, 16, 36 };
        boolean allMatch = true;

        for (int val : testValues) {
            for (int base : testBases) {
                String recursive = stringValue(val, base);
                String iterative = stringValueIterative(val, base);
                if (!recursive.equals(iterative)) {
                    System.out.println("MISMATCH: n=" + val + ", base=" + base);
                    System.out.println("  Recursive: " + recursive);
                    System.out.println("  Iterative: " + iterative);
                    allMatch = false;
                }
            }
        }

        if (allMatch) {
            System.out.println("All test cases match between recursive and iterative versions!");
        }
    }
}
