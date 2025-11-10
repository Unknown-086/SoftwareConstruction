import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for IntegerToString (Task 1)
 * Tests recursive integer-to-string conversion with various bases and edge
 * cases.
 */
public class IntegerToStringTest {

    // ========== Basic Functionality Tests ==========

    @Test
    public void testBasicConversions() {
        // Base 10
        assertEquals("42", IntegerToString.stringValue(42, 10));
        assertEquals("0", IntegerToString.stringValue(0, 10));
        assertEquals("123", IntegerToString.stringValue(123, 10));

        // Base 2
        assertEquals("101010", IntegerToString.stringValue(42, 2));
        assertEquals("11111111", IntegerToString.stringValue(255, 2));

        // Base 16
        assertEquals("2A", IntegerToString.stringValue(42, 16));
        assertEquals("FF", IntegerToString.stringValue(255, 16));

        // Base 8
        assertEquals("52", IntegerToString.stringValue(42, 8));
    }

    @Test
    public void testNegativeNumbers() {
        assertEquals("-1", IntegerToString.stringValue(-1, 10));
        assertEquals("-42", IntegerToString.stringValue(-42, 10));
        assertEquals("-101010", IntegerToString.stringValue(-42, 2));
        assertEquals("-2A", IntegerToString.stringValue(-42, 16));
        assertEquals("-52", IntegerToString.stringValue(-42, 8));
        assertEquals("-255", IntegerToString.stringValue(-255, 10));
    }

    @Test
    public void testBase36() {
        assertEquals("Z", IntegerToString.stringValue(35, 36));
        assertEquals("10", IntegerToString.stringValue(36, 36));
        assertEquals("A", IntegerToString.stringValue(10, 36));
        assertEquals("RS", IntegerToString.stringValue(1000, 36));
        assertEquals("9IX", IntegerToString.stringValue(12345, 36));
    }

    @Test
    public void testEdgeCases() {
        // Zero in different bases
        assertEquals("0", IntegerToString.stringValue(0, 2));
        assertEquals("0", IntegerToString.stringValue(0, 10));
        assertEquals("0", IntegerToString.stringValue(0, 36));

        // Integer.MAX_VALUE and Integer.MIN_VALUE
        assertEquals("2147483647", IntegerToString.stringValue(Integer.MAX_VALUE, 10));
        assertEquals("-2147483648", IntegerToString.stringValue(Integer.MIN_VALUE, 10));

        // Large numbers in other bases
        assertNotNull(IntegerToString.stringValue(Integer.MAX_VALUE, 2));
        assertNotNull(IntegerToString.stringValue(Integer.MIN_VALUE, 16));
    }

    @Test
    public void testInvalidBases() {
        // Test base too low
        try {
            IntegerToString.stringValue(42, 1);
            fail("Should throw IllegalArgumentException for base 1");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test base too high
        try {
            IntegerToString.stringValue(42, 37);
            fail("Should throw IllegalArgumentException for base 37");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test negative base
        try {
            IntegerToString.stringValue(42, -5);
            fail("Should throw IllegalArgumentException for negative base");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRecursiveAndIterativeMatch() {
        int[] testValues = { 0, 1, -1, 42, -42, 255, -255, 1000, -1000, 12345, -12345 };
        int[] testBases = { 2, 8, 10, 16, 36 };

        for (int val : testValues) {
            for (int base : testBases) {
                String recursive = IntegerToString.stringValue(val, base);
                String iterative = IntegerToString.stringValueIterative(val, base);
                assertEquals("Mismatch for n=" + val + ", base=" + base, recursive, iterative);
            }
        }
    }

    @Test
    public void testAllBasesRange() {
        // Test that all bases from 2 to 36 work correctly
        int testNumber = 100;
        for (int base = 2; base <= 36; base++) {
            String result = IntegerToString.stringValue(testNumber, base);
            assertNotNull("Result should not be null for base " + base, result);
            assertTrue("Result should not be empty for base " + base, result.length() > 0);
        }

        // Test base equals number pattern (n == base should give "10")
        assertEquals("10", IntegerToString.stringValue(2, 2));
        assertEquals("10", IntegerToString.stringValue(10, 10));
        assertEquals("10", IntegerToString.stringValue(16, 16));
        assertEquals("10", IntegerToString.stringValue(36, 36));
    }

    @Test
    public void testConsistencyAndPatterns() {
        // Same input should always produce same output
        String first = IntegerToString.stringValue(12345, 16);
        for (int i = 0; i < 10; i++) {
            assertEquals(first, IntegerToString.stringValue(12345, 16));
        }

        // Powers of two in binary
        assertEquals("10", IntegerToString.stringValue(2, 2));
        assertEquals("100", IntegerToString.stringValue(4, 2));
        assertEquals("1000", IntegerToString.stringValue(8, 2));

        // Powers of ten in decimal
        assertEquals("10", IntegerToString.stringValue(10, 10));
        assertEquals("100", IntegerToString.stringValue(100, 10));
        assertEquals("1000", IntegerToString.stringValue(1000, 10));
    }
}
