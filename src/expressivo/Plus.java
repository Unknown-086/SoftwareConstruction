package expressivo;

/**
 * An immutable representation of an addition operation in an expression.
 * Represents the sum of two expressions.
 */
public class Plus implements Expression {

    private final Expression left;
    private final Expression right;

    // Abstraction function:
    // AF(left, right) = the expression 'left + right'
    //
    // Representation invariant:
    // left != null and right != null
    //
    // Safety from rep exposure:
    // All fields are private and final
    // Expression is immutable, so sharing references is safe

    /**
     * Creates a Plus expression representing left + right.
     * 
     * @param left  the left operand
     * @param right the right operand
     */
    public Plus(Expression left, Expression right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Operands must not be null");
        }
        this.left = left;
        this.right = right;
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert left != null : "left must not be null";
        assert right != null : "right must not be null";
    }

    @Override
    public String toString() {
        return left.toString() + " + " + right.toString();
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Plus)) {
            return false;
        }
        Plus that = (Plus) thatObject;
        return this.left.equals(that.left) && this.right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return 31 * left.hashCode() + right.hashCode();
    }
}
