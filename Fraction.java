/**
 * An immutable Abstract Data Type representing a rational number (fraction).
 * 
 * A Fraction consists of a numerator and denominator, both integers.
 * 
 * Representation Invariant:
 * - denominator > 0 (always positive)
 * - gcd(|numerator|, denominator) = 1 (fraction is always in reduced form)
 * - if numerator is 0, then denominator is 1 (standard form for zero)
 * 
 * Abstraction Function:
 * - AF(numerator, denominator) = numerator / denominator
 * 
 * Safety from Representation Exposure:
 * - All fields are private and final
 * - No mutator methods exist
 * - All producer methods return new Fraction objects
 */
public class Fraction {

    // Representation
    private final int numerator;
    private final int denominator;

    // ==================== CREATORS ====================

    /**
     * Creates a new Fraction representing numerator/denominator.
     * 
     * Specification:
     * - Fractions are automatically reduced to lowest terms
     * - Negative signs are normalized to the numerator
     * - Zero fractions are normalized to 0/1
     * - Denominator of zero throws IllegalArgumentException
     * 
     * @param numerator   the numerator of the fraction
     * @param denominator the denominator of the fraction
     * @throws IllegalArgumentException if denominator is zero
     */
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }

        // Handle zero numerator: normalize to 0/1
        if (numerator == 0) {
            this.numerator = 0;
            this.denominator = 1;
            return;
        }

        // Normalize sign: move negative sign to numerator
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        // Reduce to lowest terms
        int gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    // ==================== PRODUCERS ====================

    /**
     * Returns a new Fraction that is the sum of this fraction and other.
     * 
     * Specification:
     * - Result is automatically simplified
     * - Does not modify this fraction or other
     * 
     * @param other the fraction to add to this fraction
     * @return a new Fraction representing this + other
     */
    public Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator +
                other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Returns a new Fraction that is the product of this fraction and other.
     * 
     * Specification:
     * - Result is automatically simplified
     * - Does not modify this fraction or other
     * 
     * @param other the fraction to multiply with this fraction
     * @return a new Fraction representing this * other
     */
    public Fraction multiply(Fraction other) {
        int newNumerator = this.numerator * other.numerator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Returns a new Fraction that is the difference of this fraction and other.
     * 
     * Specification:
     * - Result is automatically simplified
     * - Does not modify this fraction or other
     * 
     * @param other the fraction to subtract from this fraction
     * @return a new Fraction representing this - other
     */
    public Fraction subtract(Fraction other) {
        int newNumerator = this.numerator * other.denominator -
                other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Returns a new Fraction that is the quotient of this fraction divided by
     * other.
     * 
     * Specification:
     * - Result is automatically simplified
     * - Does not modify this fraction or other
     * - Division by zero (other.numerator == 0) throws ArithmeticException
     * 
     * @param other the fraction to divide this fraction by
     * @return a new Fraction representing this / other
     * @throws ArithmeticException if other represents zero
     */
    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        int newNumerator = this.numerator * other.denominator;
        int newDenominator = this.denominator * other.numerator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Returns a new Fraction that is the negation of this fraction.
     * 
     * Specification:
     * - Does not modify this fraction
     * - Result is already in simplified form
     * 
     * @return a new Fraction representing -this
     */
    public Fraction negate() {
        return new Fraction(-this.numerator, this.denominator);
    }

    /**
     * Returns a new Fraction that is the reciprocal of this fraction.
     * 
     * Specification:
     * - Result is automatically simplified with sign in numerator
     * - Does not modify this fraction
     * - Reciprocal of zero throws ArithmeticException
     * 
     * @return a new Fraction representing 1/this
     * @throws ArithmeticException if this fraction is zero
     */
    public Fraction reciprocal() {
        if (this.numerator == 0) {
            throw new ArithmeticException("Cannot take reciprocal of zero");
        }
        return new Fraction(this.denominator, this.numerator);
    }

    // ==================== OBSERVERS ====================

    /**
     * Returns the numerator of this fraction.
     * 
     * Specification:
     * - Returns the numerator in reduced form
     * - Negative fractions have negative numerator
     * 
     * @return the numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Returns the denominator of this fraction.
     * 
     * Specification:
     * - Returns the denominator in reduced form
     * - Always positive (negative sign is in numerator)
     * 
     * @return the denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Returns the decimal (floating-point) value of this fraction.
     * 
     * @return the double representation of this fraction
     */
    public double toDouble() {
        return (double) numerator / denominator;
    }

    /**
     * Returns a string representation of this fraction.
     * 
     * @return a string in the format "numerator/denominator"
     */
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    /**
     * Compares this fraction to another object for equality.
     * Two fractions are equal if they represent the same rational number.
     * 
     * Specification:
     * - Fractions are compared in reduced form
     * - 1/2 equals 2/4 (both reduce to 1/2)
     * 
     * @param obj the object to compare with
     * @return true if obj is a Fraction with the same value, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Fraction))
            return false;
        Fraction other = (Fraction) obj;
        // Since fractions are always in reduced form, direct comparison works
        return this.numerator == other.numerator &&
                this.denominator == other.denominator;
    }

    /**
     * Returns a hash code for this fraction.
     * Fractions that are equal have the same hash code.
     * 
     * @return hash code for this fraction
     */
    @Override
    public int hashCode() {
        return 31 * numerator + denominator;
    }

    // ==================== HELPER METHODS ====================

    /**
     * Computes the greatest common divisor of two positive integers.
     * Uses Euclid's algorithm.
     * 
     * @param a first positive integer
     * @param b second positive integer
     * @return gcd(a, b)
     */
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
