package expressivo;

/**
 * An immutable representation of a variable in an expression.
 * Variables are case-sensitive nonempty strings of letters.
 */
public class Variable implements Expression {

    private final String name;

    // Abstraction function:
    // AF(name) = the variable with name 'name'
    //
    // Representation invariant:
    // name is nonempty and contains only letters [A-Za-z]
    //
    // Safety from rep exposure:
    // All fields are private, final, and immutable (String is immutable)

    /**
     * Creates a Variable with the given name.
     * 
     * @param name the variable name, must be nonempty and contain only letters
     */
    public Variable(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Variable name must be nonempty");
        }
        if (!name.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Variable name must contain only letters");
        }
        this.name = name;
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert name != null : "name must not be null";
        assert !name.isEmpty() : "name must be nonempty";
        assert name.matches("[A-Za-z]+") : "name must contain only letters";
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Variable)) {
            return false;
        }
        Variable that = (Variable) thatObject;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
