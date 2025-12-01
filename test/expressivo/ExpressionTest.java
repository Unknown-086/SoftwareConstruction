/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    // Test each Expression variant (Number, Variable, Plus, Times)
    // For each variant, test:
    // - toString() produces correct string representation
    // - equals() correctly identifies equal and unequal expressions
    // - hashCode() is consistent with equals()
    // Test nested expressions to verify recursive behavior
    // Test that order matters in structural equality

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Tests for Number

    @Test
    public void testNumberToString() {
        Expression num1 = new Number(3);
        Expression num2 = new Number(4.2);

        assertEquals("3", num1.toString());
        assertEquals("4.2", num2.toString());
    }

    @Test
    public void testNumberEquals() {
        Expression num1 = new Number(3);
        Expression num2 = new Number(3);
        Expression num3 = new Number(4);

        assertTrue("same number should be equal", num1.equals(num2));
        assertFalse("different numbers should not be equal", num1.equals(num3));
        assertTrue("expression should equal itself", num1.equals(num1));
    }

    @Test
    public void testNumberHashCode() {
        Expression num1 = new Number(3);
        Expression num2 = new Number(3);

        assertEquals("equal numbers should have equal hash codes",
                num1.hashCode(), num2.hashCode());
    }

    // Tests for Variable

    @Test
    public void testVariableToString() {
        Expression var1 = new Variable("x");
        Expression var2 = new Variable("foo");

        assertEquals("x", var1.toString());
        assertEquals("foo", var2.toString());
    }

    @Test
    public void testVariableEquals() {
        Expression var1 = new Variable("x");
        Expression var2 = new Variable("x");
        Expression var3 = new Variable("y");

        assertTrue("same variable should be equal", var1.equals(var2));
        assertFalse("different variables should not be equal", var1.equals(var3));
        assertTrue("expression should equal itself", var1.equals(var1));
    }

    @Test
    public void testVariableHashCode() {
        Expression var1 = new Variable("x");
        Expression var2 = new Variable("x");

        assertEquals("equal variables should have equal hash codes",
                var1.hashCode(), var2.hashCode());
    }

    // Tests for Plus

    @Test
    public void testPlusToString() {
        Expression x = new Variable("x");
        Expression two = new Number(2);
        Expression plus = new Plus(x, two);

        assertEquals("x + 2", plus.toString());
    }

    @Test
    public void testPlusEquals() {
        Expression x = new Variable("x");
        Expression two = new Number(2);
        Expression plus1 = new Plus(x, two);
        Expression plus2 = new Plus(x, two);
        Expression plus3 = new Plus(two, x);

        assertTrue("same structure should be equal", plus1.equals(plus2));
        assertFalse("different order should not be equal", plus1.equals(plus3));
        assertTrue("expression should equal itself", plus1.equals(plus1));
    }

    @Test
    public void testPlusHashCode() {
        Expression x = new Variable("x");
        Expression two = new Number(2);
        Expression plus1 = new Plus(x, two);
        Expression plus2 = new Plus(x, two);

        assertEquals("equal plus expressions should have equal hash codes",
                plus1.hashCode(), plus2.hashCode());
    }

    // Tests for Times

    @Test
    public void testTimesToString() {
        Expression x = new Variable("x");
        Expression three = new Number(3);
        Expression times = new Times(x, three);

        assertEquals("x * 3", times.toString());
    }

    @Test
    public void testTimesEquals() {
        Expression x = new Variable("x");
        Expression three = new Number(3);
        Expression times1 = new Times(x, three);
        Expression times2 = new Times(x, three);
        Expression times3 = new Times(three, x);

        assertTrue("same structure should be equal", times1.equals(times2));
        assertFalse("different order should not be equal", times1.equals(times3));
        assertTrue("expression should equal itself", times1.equals(times1));
    }

    @Test
    public void testTimesHashCode() {
        Expression x = new Variable("x");
        Expression three = new Number(3);
        Expression times1 = new Times(x, three);
        Expression times2 = new Times(x, three);

        assertEquals("equal times expressions should have equal hash codes",
                times1.hashCode(), times2.hashCode());
    }

    // Tests for nested expressions

    @Test
    public void testNestedExpressionToString() {
        // (x + 2) * 3
        Expression x = new Variable("x");
        Expression two = new Number(2);
        Expression three = new Number(3);
        Expression plus = new Plus(x, two);
        Expression times = new Times(plus, three);

        assertEquals("x + 2 * 3", times.toString());
    }

    @Test
    public void testNestedExpressionEquals() {
        // (x + 2) * 3
        Expression x = new Variable("x");
        Expression two = new Number(2);
        Expression three = new Number(3);
        Expression expr1 = new Times(new Plus(x, two), three);
        Expression expr2 = new Times(new Plus(x, two), three);

        assertTrue("same nested structure should be equal", expr1.equals(expr2));
    }

    @Test
    public void testDifferentTypesNotEqual() {
        Expression num = new Number(3);
        Expression var = new Variable("x");
        Expression plus = new Plus(num, var);
        Expression times = new Times(num, var);

        assertFalse("Number should not equal Variable", num.equals(var));
        assertFalse("Plus should not equal Times", plus.equals(times));
    }

}
