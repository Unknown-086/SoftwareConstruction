package expressivo;

/**
 * An immutable representation of a numeric constant in an expression.
 * Represents nonnegative numbers in decimal representation.
 */
public class Number implements Expression {

    private final double value;

    // Abstraction function:
    // AF(value) = the numeric constant with value 'value'
    //
    // Representation invariant:
    // value >= 0
    //
    // Safety from rep exposure:
    // All fields are private, final, and immutable (double is primitive)

    /**
     * Creates a Number with the given value.
     * 
     * @param value the numeric value, must be nonnegative
     */
    public Number(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Number must be nonnegative");
        }
        this.value = value;
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert value >= 0 : "value must be nonnegative";
    }

    @Override
    public String toString() {
        // Remove unnecessary trailing zeros and decimal point for integers
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Number)) {
            return false;
        }
        Number that = (Number) thatObject;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}
