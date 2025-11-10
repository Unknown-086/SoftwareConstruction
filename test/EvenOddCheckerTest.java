import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for EvenOddChecker (Task 3)
 * Tests mutual recursion for even/odd checking with focused, comprehensive
 * coverage.
 */
public class EvenOddCheckerTest {

    // ========== Basic Functionality Tests ==========

    @Test
    public void testBasicEvenNumbers() {
        assertTrue("0 should be even", EvenOddChecker.isEven(0));
        assertTrue("2 should be even", EvenOddChecker.isEven(2));
        assertTrue("4 should be even", EvenOddChecker.isEven(4));
        assertTrue("100 should be even", EvenOddChecker.isEven(100));
        assertTrue("1000 should be even", EvenOddChecker.isEven(1000));
    }

    @Test
    public void testBasicOddNumbers() {
        assertTrue("1 should be odd", EvenOddChecker.isOdd(1));
        assertTrue("3 should be odd", EvenOddChecker.isOdd(3));
        assertTrue("5 should be odd", EvenOddChecker.isOdd(5));
        assertTrue("99 should be odd", EvenOddChecker.isOdd(99));
        assertTrue("1001 should be odd", EvenOddChecker.isOdd(1001));
    }

    // ========== Mutual Exclusivity Tests ==========

    @Test
    public void testMutualExclusivity() {
        // A number cannot be both even and odd
        for (int i = 0; i <= 100; i++) {
            boolean even = EvenOddChecker.isEven(i);
            boolean odd = EvenOddChecker.isOdd(i);
            assertFalse("Number " + i + " cannot be both even and odd", even && odd);
            assertTrue("Number " + i + " must be either even or odd", even || odd);
        }
    }

    // ========== Negative Number Tests ==========

    @Test
    public void testNegativeNumbers() {
        // Negative even numbers
        assertTrue("-2 should be even", EvenOddChecker.isEven(-2));
        assertTrue("-4 should be even", EvenOddChecker.isEven(-4));
        assertTrue("-100 should be even", EvenOddChecker.isEven(-100));

        // Negative odd numbers
        assertTrue("-1 should be odd", EvenOddChecker.isOdd(-1));
        assertTrue("-3 should be odd", EvenOddChecker.isOdd(-3));
        assertTrue("-99 should be odd", EvenOddChecker.isOdd(-99));

        // Check mutual exclusivity for negatives
        assertFalse("-2 should not be odd", EvenOddChecker.isOdd(-2));
        assertFalse("-3 should not be even", EvenOddChecker.isEven(-3));
    }

    // ========== Edge Cases ==========

    @Test
    public void testEdgeCases() {
        // Zero
        assertTrue("0 is even", EvenOddChecker.isEven(0));
        assertFalse("0 is not odd", EvenOddChecker.isOdd(0));

        // One
        assertFalse("1 is not even", EvenOddChecker.isEven(1));
        assertTrue("1 is odd", EvenOddChecker.isOdd(1));

        // Large numbers (but reasonable to avoid stack overflow)
        assertTrue("10000 should be even", EvenOddChecker.isEven(10000));
        assertTrue("9999 should be odd", EvenOddChecker.isOdd(9999));
    }

    // ========== Consistency Tests ==========

    @Test
    public void testConsistencyBetweenMethods() {
        // If isEven(n) is true, then isOdd(n) must be false
        int[] testValues = { 0, 1, 2, 3, 4, 5, 10, 15, 20, 99, 100,
                -1, -2, -10, -15, -100 };

        for (int n : testValues) {
            boolean even = EvenOddChecker.isEven(n);
            boolean odd = EvenOddChecker.isOdd(n);

            if (even) {
                assertFalse(n + " is even, so it should not be odd", odd);
            } else {
                assertTrue(n + " is not even, so it must be odd", odd);
            }
        }
    }

    // ========== Mathematical Properties ==========

    @Test
    public void testMathematicalProperties() {
        // Even + Even = Even
        assertTrue("Even numbers: 2, 4, 6, 8, 10",
                EvenOddChecker.isEven(2) && EvenOddChecker.isEven(4) &&
                        EvenOddChecker.isEven(6) && EvenOddChecker.isEven(8));

        // Odd numbers: consecutive odd pattern
        assertTrue("Odd numbers: 1, 3, 5, 7, 9",
                EvenOddChecker.isOdd(1) && EvenOddChecker.isOdd(3) &&
                        EvenOddChecker.isOdd(5) && EvenOddChecker.isOdd(7));

        // Alternating pattern
        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                assertTrue(i + " should be even", EvenOddChecker.isEven(i));
            } else {
                assertTrue(i + " should be odd", EvenOddChecker.isOdd(i));
            }
        }
    }

    // ========== Range Tests ==========

    @Test
    public void testWideRange() {
        // Test a wide range of numbers
        int evenCount = 0;
        int oddCount = 0;

        for (int i = -100; i <= 100; i++) {
            if (EvenOddChecker.isEven(i)) {
                evenCount++;
            }
            if (EvenOddChecker.isOdd(i)) {
                oddCount++;
            }
        }

        // From -100 to 100 is 201 numbers
        // Should have approximately equal even and odd numbers
        assertEquals("Total numbers accounted", 201, evenCount + oddCount);

        // -100, -98, ..., -2, 0, 2, ..., 98, 100 = 101 even numbers
        // -99, -97, ..., -1, 1, 3, ..., 97, 99 = 100 odd numbers
        assertEquals("Even count in range -100 to 100", 101, evenCount);
        assertEquals("Odd count in range -100 to 100", 100, oddCount);
    }
}
