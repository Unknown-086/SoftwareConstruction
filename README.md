# Lab 11: Abstract Data Types (ADT) - II

This lab focuses on designing, implementing, and testing immutable Abstract Data Types in Java.

---

## Lab Overview

**Objective:** Understand ADT design principles including immutability, representation independence, and comprehensive testing strategies.

**Key Concepts:**
- Immutable data types
- ADT operations classification (Creators, Producers, Observers, Mutators)
- Partition analysis for testing
- Observer-based verification

---

## Completed Tasks

### **Task 1: Fraction ADT Implementation**

**Location:** `src/Fraction.java`

Implemented a fully immutable ADT representing rational numbers (fractions).

**Features:**
- **Automatic simplification** - Fractions reduced to lowest terms using GCD
- **Sign normalization** - Negative signs always in numerator
- **Zero handling** - Zero fractions normalized to `0/1`
- **Exception handling** - Throws exceptions for invalid operations

**Operations Implemented:**

| Category | Methods |
|----------|---------|
| **Creators** | `Fraction(int numerator, int denominator)` |
| **Producers** | `add()`, `multiply()`, `subtract()`, `divide()`, `negate()`, `reciprocal()` |
| **Observers** | `getNumerator()`, `getDenominator()`, `toDouble()`, `toString()`, `equals()`, `hashCode()` |
| **Mutators** | None (immutable ADT) |

**Key Design Principles:**
- Private final fields for representation
- No mutator methods
- All producers return new Fraction objects
- Complete representation independence

---

### **Task 2: ADT Operations Classification**

**Location:** `ADT_Operations_Analysis.md`

Analyzed and classified operations for three Java ADTs using generic notation.

**Analyzed Types:**
1. **ArrayList\<E>** - 11 operations classified
2. **HashSet\<E>** - 10 operations classified
3. **Stack\<E>** - 10 operations classified

**Deliverable:**
- Comprehensive tables with operation names, classifications, and justifications
- 5 detailed justifications per type
- Generic notation (E, K, V) used throughout
- Summary of classification patterns

**Key Insights:**
- Mutable vs immutable ADT operation patterns
- Role of observers in testing
- Importance of producers in immutable ADTs

---

### **Task 3: Comprehensive Testing with JUnit 4**

**Location:** `test/FractionTest.java`

Implemented a complete test suite using partition analysis and JUnit 4.

**Test Coverage:**

| Category | Tests | Coverage |
|----------|-------|----------|
| Constructor | 7 tests | Simplification, normalization, exceptions |
| add() | 5 tests | All sign combinations, zero results |
| multiply() | 4 tests | Sign combinations, zero multiplication |
| subtract() | 3 tests | Positive, negative, zero results |
| divide() | 4 tests | Normal division, zero exception |
| negate() | 3 tests | All sign cases |
| reciprocal() | 3 tests | Sign preservation, zero exception |
| Observers | 2 tests | toDouble(), toString() accuracy |
| equals/hashCode | 2 tests | Equivalence, hash consistency |
| Immutability | 1 test | Operations don't modify originals |
| Edge Cases | 3 tests | Large numbers, whole numbers, improper fractions |
| **TOTAL** | **37 tests** | **100% method coverage** |

**Testing Strategy:**
- **Partition analysis** for systematic test case generation
- **Observer-based verification** for creators and producers
- **Exception testing** for invalid inputs
- **Immutability verification** across all operations

**Documentation:** See `Testing_Strategy.md` for complete partition analysis

---

## Project Structure

```
LabEleven/
├── src/
│   └── Fraction.java                    # Task 1: Fraction ADT implementation
├── test/
│   └── FractionTest.java                # Task 3: JUnit 4 test suite (37 tests)
├── lib/
│   ├── junit-4.13.2.jar                 # JUnit 4 testing framework
│   └── hamcrest-core-1.3.jar            # Hamcrest matchers for JUnit
├── bin/                                  # Compiled output (auto-generated)
├── ADT_Operations_Analysis.md            # Task 2: ADT classification analysis
├── Testing_Strategy.md                   # Task 3: Detailed testing strategy
└── README.md                             # This file
```

---

## How to Run

### **Run Tests in VS Code:**
1. Open `test/FractionTest.java`
2. Click the green "Run Test" button next to any test method
3. Or click "Run All Tests" at the class level
4. View results in the Test Explorer (beaker icon in sidebar)

### **Run Tests from Command Line:**

**Compile:**
```bash
javac -cp ".;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" src/Fraction.java test/FractionTest.java -d bin
```

**Run All Tests:**
```bash
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore FractionTest
```

**Expected Output:** `OK (37 tests)`

---

## Learning Outcomes

By completing this lab, you will understand:

1. **ADT Design Principles:**
   - How to design coherent, adequate operations
   - Importance of immutability in ADTs
   - Achieving representation independence

2. **Operation Classification:**
   - Distinguishing between Creators, Producers, Observers, and Mutators
   - Understanding operation roles in ADT design
   - Generic type notation and reusability

3. **Testing Strategies:**
   - Using partition analysis for systematic test generation
   - Testing immutability and representation invariants
   - Observer-based verification for producers and creators
   - Handling edge cases and exceptions

4. **Software Engineering Practices:**
   - Writing comprehensive specifications
   - Documenting design decisions
   - Creating maintainable, testable code

---

## Key ADT Concepts Applied

### **Immutability:**
- All Fraction objects are immutable once created
- Operations return new objects instead of modifying existing ones
- Verified through dedicated immutability tests

### **Representation Independence:**
- Internal representation (numerator/denominator) completely hidden
- External interface only through public methods
- Clients cannot depend on internal implementation

### **Specification Clarity:**
- All methods have detailed specifications
- Preconditions, postconditions, and exceptions documented
- Standard behaviors explicitly stated (e.g., "0/1 for zero")

### **Testing Rigor:**
- Black-box testing based on specifications only
- Partition analysis for equivalence class coverage
- Observer methods used to verify all state changes

---

## Technologies Used

- **Language:** Java 11
- **Testing Framework:** JUnit 4.13.2
- **Build Tool:** VS Code Java Extension
- **Version Control:** Git

---

## Additional Documentation

- **`ADT_Operations_Analysis.md`** - Complete analysis of ArrayList, HashSet, and Stack operations
- **`Testing_Strategy.md`** - Detailed partition analysis and testing methodology for Fraction ADT

---

## Summary

This lab demonstrates a complete cycle of ADT development: design → implementation → testing. The Fraction ADT serves as an exemplar of immutable data type design with comprehensive specifications, clean implementation, and rigorous testing. All 37 tests pass, validating the correctness of the implementation.
