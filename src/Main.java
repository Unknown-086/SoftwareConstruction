/**
 * Main class to demonstrate and test the Fraction ADT.
 * This program shows various operations on Fraction objects.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Fraction ADT Demonstration ===\n");

        // Test 1: Basic Fraction Creation
        System.out.println("--- Test 1: Creating Fractions ---");
        Fraction f1 = new Fraction(3, 4);
        Fraction f2 = new Fraction(1, 2);
        System.out.println("f1 = " + f1); // 3/4
        System.out.println("f2 = " + f2); // 1/2
        System.out.println();

        // Test 2: Automatic Simplification
        System.out.println("--- Test 2: Automatic Simplification ---");
        Fraction f3 = new Fraction(6, 9);
        System.out.println("Fraction(6, 9) = " + f3 + " (simplified to 2/3)");
        Fraction f4 = new Fraction(12, 8);
        System.out.println("Fraction(12, 8) = " + f4 + " (simplified to 3/2)");
        System.out.println();

        // Test 3: Sign Normalization
        System.out.println("--- Test 3: Sign Normalization ---");
        Fraction f5 = new Fraction(-5, 7);
        System.out.println("Fraction(-5, 7) = " + f5 + " (negative in numerator)");
        Fraction f6 = new Fraction(5, -7);
        System.out.println("Fraction(5, -7) = " + f6 + " (normalized to -5/7)");
        Fraction f7 = new Fraction(-4, -6);
        System.out.println("Fraction(-4, -6) = " + f7 + " (both negative becomes positive 2/3)");
        System.out.println();

        // Test 4: Addition
        System.out.println("--- Test 4: Addition ---");
        Fraction sum = f1.add(f2);
        System.out.println(f1 + " + " + f2 + " = " + sum); // 3/4 + 1/2 = 5/4

        Fraction f8 = new Fraction(1, 6);
        Fraction f9 = new Fraction(1, 3);
        Fraction sum2 = f8.add(f9);
        System.out.println(f8 + " + " + f9 + " = " + sum2 + " (simplified from 3/6)");
        System.out.println();

        // Test 5: Multiplication
        System.out.println("--- Test 5: Multiplication ---");
        Fraction product = f1.multiply(f2);
        System.out.println(f1 + " * " + f2 + " = " + product); // 3/4 * 1/2 = 3/8

        Fraction f10 = new Fraction(2, 3);
        Fraction f11 = new Fraction(3, 4);
        Fraction product2 = f10.multiply(f11);
        System.out.println(f10 + " * " + f11 + " = " + product2 + " (simplified to 1/2)");
        System.out.println();

        // Test 6: Subtraction
        System.out.println("--- Test 6: Subtraction ---");
        Fraction diff = f1.subtract(f2);
        System.out.println(f1 + " - " + f2 + " = " + diff); // 3/4 - 1/2 = 1/4
        System.out.println();

        // Test 7: Division
        System.out.println("--- Test 7: Division ---");
        Fraction quotient = f1.divide(f2);
        System.out.println(f1 + " ÷ " + f2 + " = " + quotient); // 3/4 ÷ 1/2 = 3/2
        System.out.println();

        // Test 8: Negation
        System.out.println("--- Test 8: Negation ---");
        Fraction neg = f1.negate();
        System.out.println("Negate of " + f1 + " = " + neg); // -3/4
        Fraction negNeg = neg.negate();
        System.out.println("Negate of " + neg + " = " + negNeg); // back to 3/4
        System.out.println();

        // Test 9: Reciprocal
        System.out.println("--- Test 9: Reciprocal ---");
        Fraction recip = f1.reciprocal();
        System.out.println("Reciprocal of " + f1 + " = " + recip); // 4/3
        Fraction recip2 = new Fraction(-2, 5).reciprocal();
        System.out.println("Reciprocal of -2/5 = " + recip2); // -5/2
        System.out.println();

        // Test 10: Observers
        System.out.println("--- Test 10: Observers ---");
        System.out.println(f1 + " -> Numerator: " + f1.getNumerator() + ", Denominator: " + f1.getDenominator());
        System.out.println(f1 + " as decimal: " + f1.toDouble());
        System.out.println(f2 + " as decimal: " + f2.toDouble());
        System.out.println();

        // Test 11: Zero Fractions
        System.out.println("--- Test 11: Zero Fractions ---");
        Fraction zero = new Fraction(0, 5);
        System.out.println("Fraction(0, 5) = " + zero + " (normalized to 0/1)");
        Fraction zero2 = new Fraction(0, -3);
        System.out.println("Fraction(0, -3) = " + zero2 + " (normalized to 0/1)");
        System.out.println();

        // Test 12: Equality
        System.out.println("--- Test 12: Equality ---");
        Fraction f12 = new Fraction(1, 2);
        Fraction f13 = new Fraction(2, 4);
        System.out.println(f12 + " equals " + f13 + "? " + f12.equals(f13) + " (both simplify to 1/2)");
        System.out.println(f12 + " equals " + f1 + "? " + f12.equals(f1));
        System.out.println();

        // Test 13: Immutability
        System.out.println("--- Test 13: Immutability ---");
        Fraction original = new Fraction(1, 2);
        System.out.println("Original fraction: " + original);
        Fraction result1 = original.add(new Fraction(1, 3));
        System.out.println("After add operation: " + original + " (unchanged)");
        Fraction result2 = original.multiply(new Fraction(2, 1));
        System.out.println("After multiply operation: " + original + " (unchanged)");
        System.out.println("Result of add: " + result1);
        System.out.println("Result of multiply: " + result2);
        System.out.println();

        // Test 14: Complex Expression
        System.out.println("--- Test 14: Complex Expression ---");
        // Calculate: (1/2 + 1/3) * 2/1 - 1/4
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);
        Fraction c = new Fraction(2, 1);
        Fraction d = new Fraction(1, 4);
        Fraction result = a.add(b).multiply(c).subtract(d);
        System.out.println("(1/2 + 1/3) * 2/1 - 1/4 = " + result);
        System.out.println("As decimal: " + result.toDouble());
        System.out.println();

        // Test 15: Exception Handling
        System.out.println("--- Test 15: Exception Handling ---");
        try {
            Fraction invalid = new Fraction(5, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly threw exception for zero denominator: " + e.getMessage());
        }

        try {
            Fraction zeroFraction = new Fraction(0, 1);
            Fraction invalidDiv = f1.divide(zeroFraction);
        } catch (ArithmeticException e) {
            System.out.println("Correctly threw exception for division by zero: " + e.getMessage());
        }

        try {
            Fraction zeroFraction = new Fraction(0, 1);
            Fraction invalidRecip = zeroFraction.reciprocal();
        } catch (ArithmeticException e) {
            System.out.println("Correctly threw exception for reciprocal of zero: " + e.getMessage());
        }

        System.out.println("\n=== All demonstrations completed successfully! ===");
    }
}
