# Task 3: Testing Strategy for Fraction ADT

## Overview
This document outlines the comprehensive testing strategy for the `Fraction` ADT using partition analysis and edge case testing. All tests are implemented in `FractionTest.java` using JUnit 4.

---

## Testing Methodology

### Core Principle
**Creators and Producers are tested using Observers**
- We cannot directly inspect the internal state of the Fraction object (private fields)
- We must use observer methods (`getNumerator()`, `getDenominator()`, `toDouble()`) to verify correctness
- This ensures we're testing the ADT's public interface, not its implementation

---

## Partition Analysis by Method

### 1. Constructor (Creator)

**Input Partitions:**

| Partition | Description | Test Cases |
|-----------|-------------|------------|
| **Numerator** | Positive | `Fraction(3, 4)` |
|               | Negative | `Fraction(-5, 7)` |
|               | Zero | `Fraction(0, 5)` → normalizes to `0/1` |
| **Denominator** | Positive | `Fraction(3, 4)` |
|                 | Negative | `Fraction(5, -7)` → normalizes to `-5/7` |
|                 | Zero | `Fraction(5, 0)` → throws `IllegalArgumentException` |
| **Both Signs** | Both negative | `Fraction(-4, -6)` → normalizes to `2/3` |
| **Simplification** | Already simplified | `Fraction(3, 4)` |
|                    | Needs reduction | `Fraction(6, 9)` → reduces to `2/3` |

**Expected Behaviors:**
- Fractions automatically reduced to lowest terms (GCD)
- Negative sign always in numerator
- Zero fractions normalized to `0/1`
- Zero denominator throws exception

---

### 2. add() - Producer

**Input Partitions:**

| Partition | Description | Test Example | Expected Result |
|-----------|-------------|--------------|-----------------|
| **Both Positive** | Both operands positive | `1/2 + 1/3` | `5/6` |
| **Both Negative** | Both operands negative | `-1/4 + (-1/2)` | `-3/4` |
| **Mixed Signs** | One positive, one negative | `3/4 + (-1/4)` | `1/2` |
| **Result Zero** | Sum equals zero | `1/2 + (-1/2)` | `0/1` |
| **Needs Simplification** | Result requires reduction | `1/6 + 1/3` | `1/2` (from `3/6`) |

**Testing Approach:**
```java
Fraction result = f1.add(f2);
// Use observers to verify
assertEquals(expectedNumerator, result.getNumerator());
assertEquals(expectedDenominator, result.getDenominator());
```

---

### 3. multiply() - Producer

**Input Partitions:**

| Partition | Description | Test Example | Expected Result |
|-----------|-------------|--------------|-----------------|
| **Both Positive** | Both operands positive | `2/3 * 3/4` | `1/2` |
| **Both Negative** | Both operands negative | `-2/5 * -3/7` | `6/35` |
| **Mixed Signs** | One positive, one negative | `3/4 * (-2/5)` | `-3/10` |
| **Multiply by Zero** | One operand is zero | `5/7 * 0/1` | `0/1` |

**Key Property:** Result is automatically simplified through constructor.

---

### 4. subtract() - Producer

**Input Partitions:**

| Partition | Description | Test Example | Expected Result |
|-----------|-------------|--------------|-----------------|
| **Both Positive** | Larger minus smaller | `3/4 - 1/4` | `1/2` |
| **Result Negative** | Smaller minus larger | `1/3 - 2/3` | `-1/3` |
| **Result Zero** | Equal fractions | `5/6 - 5/6` | `0/1` |

---

### 5. divide() - Producer

**Input Partitions:**

| Partition | Description | Test Example | Expected Result |
|-----------|-------------|--------------|-----------------|
| **Both Positive** | Normal division | `1/2 ÷ 1/3` | `3/2` |
| **Mixed Signs** | Negative result | `2/3 ÷ (-4/5)` | `-5/6` |
| **Divide by Zero** | Divisor is zero | `5/7 ÷ 0/1` | `ArithmeticException` |
| **Needs Simplification** | Result requires reduction | `4/6 ÷ 2/3` | `1/1` |

---

### 6. negate() - Producer

**Input Partitions:**

| Partition | Description | Test Example | Expected Result |
|-----------|-------------|--------------|-----------------|
| **Positive** | Negate positive fraction | `-(3/4)` | `-3/4` |
| **Negative** | Negate negative fraction | `-(-2/5)` | `2/5` |
| **Zero** | Negate zero | `-(0/1)` | `0/1` |

---

### 7. reciprocal() - Producer

**Input Partitions:**

| Partition | Description | Test Example | Expected Result |
|-----------|-------------|--------------|-----------------|
| **Positive** | Reciprocal of positive | `reciprocal(3/4)` | `4/3` |
| **Negative** | Reciprocal of negative | `reciprocal(-2/5)` | `-5/2` |
| **Zero** | Reciprocal of zero | `reciprocal(0/1)` | `ArithmeticException` |

**Key Property:** Sign remains in numerator after reciprocal.

---

### 8. Observer Methods

**Methods:** `getNumerator()`, `getDenominator()`, `toDouble()`

These are tested **implicitly** through all creator and producer tests. However, `toDouble()` has explicit tests:

| Test Case | Fraction | Expected Double |
|-----------|----------|-----------------|
| Simple half | `1/2` | `0.5` |
| Repeating decimal | `1/3` | `≈ 0.3333` |
| Negative | `-3/4` | `-0.75` |
| Zero | `0/1` | `0.0` |

---

### 9. equals() and hashCode()

**Partitions:**

| Partition | Test Example | Expected |
|-----------|--------------|----------|
| **Same Object** | `f1.equals(f1)` | `true` |
| **Equivalent Values** | `(1/2).equals(1/2)` | `true` |
| **Simplified Equivalent** | `(1/2).equals(2/4)` | `true` |
| **Different Values** | `(1/2).equals(1/3)` | `false` |
| **Null** | `f1.equals(null)` | `false` |
| **Different Type** | `f1.equals("1/2")` | `false` |

**HashCode Property:** Equal objects must have equal hash codes.

---

## Edge Cases Tested

### 1. Exception Cases
- **Zero Denominator:** Constructor throws `IllegalArgumentException`
- **Division by Zero:** `divide()` throws `ArithmeticException`
- **Reciprocal of Zero:** `reciprocal()` throws `ArithmeticException`

### 2. Special Values
- **Zero Fractions:** Always normalized to `0/1`
- **Whole Numbers:** `5/1` represents integer 5
- **Improper Fractions:** `7/3` > 1 is valid

### 3. Large Numbers
- Test with large numerators/denominators that still fit in `int` range
- Verify simplification works correctly: `1000000/2000000` → `1/2`

### 4. Sign Normalization
- Negative denominator moves sign to numerator
- Both negative results in positive fraction
- Zero always has positive denominator (0/1)

---

## Immutability Testing

**Critical Property:** Fraction is immutable - operations must not modify original objects.

**Test Approach:**
1. Create fractions and store their initial state
2. Perform multiple operations on them
3. Verify original fractions remain unchanged using observers

```java
Fraction f = new Fraction(1, 2);
int originalNum = f.getNumerator();
f.add(new Fraction(1, 3));  // Should not modify f
assertEquals(originalNum, f.getNumerator());
```

---

## Test Coverage Summary

| Category | Number of Test Methods | Key Focus |
|----------|----------------------|-----------|
| **Constructor** | 7 | Simplification, normalization, exceptions |
| **add()** | 5 | All sign combinations, zero result |
| **multiply()** | 4 | Sign combinations, zero |
| **subtract()** | 3 | Positive, negative, zero results |
| **divide()** | 4 | Normal division, zero exception |
| **negate()** | 3 | All sign cases |
| **reciprocal()** | 3 | Sign preservation, zero exception |
| **Observers** | 2 | toDouble accuracy, toString format |
| **equals/hashCode** | 2 | Equivalence, hash consistency |
| **Immutability** | 1 | Operations don't modify originals |
| **Edge Cases** | 3 | Large numbers, whole numbers, improper |
| **TOTAL** | **37 test methods** | Comprehensive coverage |

---

## Key Testing Principles Applied

1. **Black Box Testing:** We test the public interface without knowledge of implementation
2. **Partition Analysis:** Input space divided into equivalence classes
3. **Boundary Testing:** Edge cases like zero, negative signs tested explicitly
4. **Exception Testing:** Invalid inputs verified to throw appropriate exceptions
5. **Property Testing:** Immutability verified across all operations
6. **Observer-Based Verification:** Creators and producers tested using observers only

---

## How to Run Tests

### Using VS Code:
- Open `FractionTest.java`
- Click the green "Run Test" button next to each test method
- Or click "Run All Tests" at the class level
- Use the Test Explorer (beaker icon) to view all tests

### Using Command Line (with JUnit 4):
```bash
# Compile
javac -cp ".;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" src/Fraction.java test/FractionTest.java -d bin

# Run tests
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore FractionTest
```

### Using IDE:
- Right-click `FractionTest.java` → Run as JUnit Test
- All 37 tests should pass

---

## Expected Results

**All tests should pass** if the Fraction implementation is correct.

If tests fail:
1. Check the specific test method that failed
2. Review the partition being tested
3. Use observers to debug the issue
4. Verify representation invariants are maintained
