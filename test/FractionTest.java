import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Comprehensive test suite for the Fraction ADT.
 * Tests all public methods using partition analysis and edge case testing.
 * 
 * Testing Strategy:
 * - Creators and Producers are tested using Observers (getNumerator,
 * getDenominator, toDouble)
 * - Each method is tested with partitions covering key scenarios
 * - Edge cases and exception scenarios are explicitly tested
 */
public class FractionTest {

    // ==================== CONSTRUCTOR (CREATOR) TESTS ====================

    /**
     * Testing Strategy for Constructor:
     * Partitions:
     * - numerator: positive, negative, zero
     * - denominator: positive, negative (normalized), zero (exception)
     * - simplification: already simplified, needs reduction
     */

    @Test
    public void testConstructorPositiveFraction() {
        // Test: 3/4 (already simplified)
        Fraction f = new Fraction(3, 4);
        assertEquals(3, f.getNumerator());
        assertEquals(4, f.getDenominator());
    }

    @Test
    public void testConstructorNegativeNumerator() {
        // Test: -5/7 (negative in numerator)
        Fraction f = new Fraction(-5, 7);
        assertEquals(-5, f.getNumerator());
        assertEquals(7, f.getDenominator());
    }

    @Test
    public void testConstructorNegativeDenominator() {
        // Test: 5/-7 should normalize to -5/7
        Fraction f = new Fraction(5, -7);
        assertEquals(-5, f.getNumerator());
        assertEquals(7, f.getDenominator());
    }

    @Test
    public void testConstructorBothNegative() {
        // Test: -4/-6 should normalize and simplify to 2/3
        Fraction f = new Fraction(-4, -6);
        assertEquals(2, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }

    @Test
    public void testConstructorSimplification() {
        // Test: 6/9 should simplify to 2/3
        Fraction f = new Fraction(6, 9);
        assertEquals(2, f.getNumerator());
        assertEquals(3, f.getDenominator());

        // Test: 12/8 should simplify to 3/2
        Fraction f2 = new Fraction(12, 8);
        assertEquals(3, f2.getNumerator());
        assertEquals(2, f2.getDenominator());
    }

    @Test
    public void testConstructorZeroNumerator() {
        // Test: 0/5 should normalize to 0/1
        Fraction f = new Fraction(0, 5);
        assertEquals(0, f.getNumerator());
        assertEquals(1, f.getDenominator());

        // Test: 0/-3 should also normalize to 0/1
        Fraction f2 = new Fraction(0, -3);
        assertEquals(0, f2.getNumerator());
        assertEquals(1, f2.getDenominator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorZeroDenominator() {
        // Test: denominator of zero should throw exception
        new Fraction(5, 0);
    }

    // ==================== ADD (PRODUCER) TESTS ====================

    /**
     * Testing Strategy for add():
     * Partitions:
     * - operands: both positive, both negative, mixed signs
     * - result: positive, negative, zero, needs simplification
     */

    @Test
    public void testAddPositiveFractions() {
        // Test: 1/2 + 1/3 = 5/6
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testAddNegativeFractions() {
        // Test: -1/4 + (-1/2) = -3/4
        Fraction f1 = new Fraction(-1, 4);
        Fraction f2 = new Fraction(-1, 2);
        Fraction result = f1.add(f2);
        assertEquals(-3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testAddMixedSigns() {
        // Test: 3/4 + (-1/4) = 1/2
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(-1, 4);
        Fraction result = f1.add(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testAddResultingInZero() {
        // Test: 1/2 + (-1/2) = 0/1
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(-1, 2);
        Fraction result = f1.add(f2);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testAddWithSimplification() {
        // Test: 1/6 + 1/3 = 1/2 (result needs simplification from 3/6)
        Fraction f1 = new Fraction(1, 6);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.add(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    // ==================== MULTIPLY (PRODUCER) TESTS ====================

    /**
     * Testing Strategy for multiply():
     * Partitions:
     * - operands: both positive, both negative, mixed signs, one is zero
     * - result: needs simplification, already simplified
     */

    @Test
    public void testMultiplyPositiveFractions() {
        // Test: 2/3 * 3/4 = 1/2
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(3, 4);
        Fraction result = f1.multiply(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testMultiplyNegativeFractions() {
        // Test: -2/5 * -3/7 = 6/35
        Fraction f1 = new Fraction(-2, 5);
        Fraction f2 = new Fraction(-3, 7);
        Fraction result = f1.multiply(f2);
        assertEquals(6, result.getNumerator());
        assertEquals(35, result.getDenominator());
    }

    @Test
    public void testMultiplyMixedSigns() {
        // Test: 3/4 * (-2/5) = -3/10
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(-2, 5);
        Fraction result = f1.multiply(f2);
        assertEquals(-3, result.getNumerator());
        assertEquals(10, result.getDenominator());
    }

    @Test
    public void testMultiplyByZero() {
        // Test: 5/7 * 0/1 = 0/1
        Fraction f1 = new Fraction(5, 7);
        Fraction f2 = new Fraction(0, 1);
        Fraction result = f1.multiply(f2);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    // ==================== SUBTRACT (PRODUCER) TESTS ====================

    /**
     * Testing Strategy for subtract():
     * Partitions:
     * - operands: both positive, both negative, mixed signs
     * - result: positive, negative, zero
     */

    @Test
    public void testSubtractPositiveFractions() {
        // Test: 3/4 - 1/4 = 1/2
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 4);
        Fraction result = f1.subtract(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testSubtractResultingInNegative() {
        // Test: 1/3 - 2/3 = -1/3
        Fraction f1 = new Fraction(1, 3);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.subtract(f2);
        assertEquals(-1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testSubtractResultingInZero() {
        // Test: 5/6 - 5/6 = 0/1
        Fraction f1 = new Fraction(5, 6);
        Fraction f2 = new Fraction(5, 6);
        Fraction result = f1.subtract(f2);
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    // ==================== DIVIDE (PRODUCER) TESTS ====================

    /**
     * Testing Strategy for divide():
     * Partitions:
     * - operands: both positive, mixed signs
     * - divisor: zero (exception), non-zero
     * - result: needs simplification
     */

    @Test
    public void testDividePositiveFractions() {
        // Test: 1/2 ÷ 1/3 = 3/2
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction result = f1.divide(f2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testDivideMixedSigns() {
        // Test: 2/3 ÷ (-4/5) = -5/6
        Fraction f1 = new Fraction(2, 3);
        Fraction f2 = new Fraction(-4, 5);
        Fraction result = f1.divide(f2);
        assertEquals(-5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        // Test: division by zero fraction should throw exception
        Fraction f1 = new Fraction(5, 7);
        Fraction zero = new Fraction(0, 1);
        f1.divide(zero);
    }

    @Test
    public void testDivideWithSimplification() {
        // Test: 4/6 ÷ 2/3 = 1/1 (should simplify)
        Fraction f1 = new Fraction(4, 6);
        Fraction f2 = new Fraction(2, 3);
        Fraction result = f1.divide(f2);
        assertEquals(1, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    // ==================== NEGATE (PRODUCER) TESTS ====================

    /**
     * Testing Strategy for negate():
     * Partitions:
     * - original: positive, negative, zero
     */

    @Test
    public void testNegatePositiveFraction() {
        // Test: -(3/4) = -3/4
        Fraction f = new Fraction(3, 4);
        Fraction result = f.negate();
        assertEquals(-3, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testNegateNegativeFraction() {
        // Test: -(-2/5) = 2/5
        Fraction f = new Fraction(-2, 5);
        Fraction result = f.negate();
        assertEquals(2, result.getNumerator());
        assertEquals(5, result.getDenominator());
    }

    @Test
    public void testNegateZero() {
        // Test: -(0/1) = 0/1
        Fraction f = new Fraction(0, 1);
        Fraction result = f.negate();
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    // ==================== RECIPROCAL (PRODUCER) TESTS ====================

    /**
     * Testing Strategy for reciprocal():
     * Partitions:
     * - original: positive, negative, zero (exception)
     * - result: sign normalization
     */

    @Test
    public void testReciprocalPositiveFraction() {
        // Test: reciprocal of 3/4 = 4/3
        Fraction f = new Fraction(3, 4);
        Fraction result = f.reciprocal();
        assertEquals(4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testReciprocalNegativeFraction() {
        // Test: reciprocal of -2/5 = -5/2
        Fraction f = new Fraction(-2, 5);
        Fraction result = f.reciprocal();
        assertEquals(-5, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testReciprocalOfZero() {
        // Test: reciprocal of zero should throw exception
        Fraction zero = new Fraction(0, 1);
        zero.reciprocal();
    }

    // ==================== OBSERVER TESTS ====================

    /**
     * Testing Strategy for Observers:
     * - Tested implicitly through all producer/creator tests
     * - Additional tests for toDouble() accuracy
     */

    @Test
    public void testToDouble() {
        // Test: 1/2 = 0.5
        Fraction f1 = new Fraction(1, 2);
        assertEquals(0.5, f1.toDouble(), 0.0001);

        // Test: 1/3 ≈ 0.333...
        Fraction f2 = new Fraction(1, 3);
        assertEquals(0.3333, f2.toDouble(), 0.0001);

        // Test: -3/4 = -0.75
        Fraction f3 = new Fraction(-3, 4);
        assertEquals(-0.75, f3.toDouble(), 0.0001);

        // Test: 0/1 = 0.0
        Fraction f4 = new Fraction(0, 1);
        assertEquals(0.0, f4.toDouble(), 0.0001);
    }

    @Test
    public void testToString() {
        // Test string representation
        Fraction f1 = new Fraction(3, 4);
        assertEquals("3/4", f1.toString());

        Fraction f2 = new Fraction(-5, 7);
        assertEquals("-5/7", f2.toString());

        Fraction f3 = new Fraction(0, 1);
        assertEquals("0/1", f3.toString());
    }

    // ==================== EQUALS AND HASHCODE TESTS ====================

    /**
     * Testing Strategy for equals():
     * Partitions:
     * - same object, equivalent fractions, different fractions
     * - non-Fraction objects
     */

    @Test
    public void testEquals() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 4); // equivalent after simplification
        Fraction f4 = new Fraction(1, 3);

        // Test reflexivity
        assertEquals(f1, f1);

        // Test equality with same values
        assertEquals(f1, f2);

        // Test equality with simplified equivalent
        assertEquals(f1, f3);

        // Test inequality
        assertNotEquals(f1, f4);

        // Test with null
        assertNotEquals(f1, null);

        // Test with different type
        assertNotEquals(f1, "1/2");
    }

    @Test
    public void testHashCode() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(2, 4); // equivalent after simplification

        // Equal objects must have same hash code
        assertEquals(f1.hashCode(), f2.hashCode());
        assertEquals(f1.hashCode(), f3.hashCode());
    }

    // ==================== IMMUTABILITY TESTS ====================

    /**
     * Testing Strategy for Immutability:
     * - Ensure operations don't modify original fractions
     */

    @Test
    public void testImmutability() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);

        // Store original values
        int originalNum1 = f1.getNumerator();
        int originalDen1 = f1.getDenominator();
        int originalNum2 = f2.getNumerator();
        int originalDen2 = f2.getDenominator();

        // Perform operations
        f1.add(f2);
        f1.multiply(f2);
        f1.subtract(f2);
        f1.divide(f2);
        f1.negate();
        f1.reciprocal();

        // Verify original fractions unchanged
        assertEquals(originalNum1, f1.getNumerator());
        assertEquals(originalDen1, f1.getDenominator());
        assertEquals(originalNum2, f2.getNumerator());
        assertEquals(originalDen2, f2.getDenominator());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    public void testLargeFractions() {
        // Test with large numbers that still fit in int
        Fraction f1 = new Fraction(1000000, 2000000);
        assertEquals(1, f1.getNumerator());
        assertEquals(2, f1.getDenominator());
    }

    @Test
    public void testWholeNumbers() {
        // Test: 5/1 = 5
        Fraction f = new Fraction(5, 1);
        assertEquals(5, f.getNumerator());
        assertEquals(1, f.getDenominator());
        assertEquals(5.0, f.toDouble(), 0.0001);
    }

    @Test
    public void testImproperFraction() {
        // Test: 7/3 (improper fraction)
        Fraction f = new Fraction(7, 3);
        assertEquals(7, f.getNumerator());
        assertEquals(3, f.getDenominator());
    }
}
