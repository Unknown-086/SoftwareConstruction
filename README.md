# Lab 12: Expression ADT - Differentiation and Simplification

## Overview
Implementation of a mathematical expression system that supports parsing, differentiation, and simplification of polynomial expressions containing addition, multiplication, numbers, and variables.

## Problem 1: Expression ADT (COMPLETE)

### What We've Implemented

**Four Expression Variants:**
- `Number.java` - Numeric constants (e.g., 3, 4.2)
- `Variable.java` - Named variables (e.g., x, foo)
- `Plus.java` - Addition operation (e.g., x + 2)
- `Times.java` - Multiplication operation (e.g., x * 3)

**Key Features:**
- Immutable recursive Abstract Data Type
- Structural equality (order matters: x+2 ≠ 2+x)
- Complete documentation (AF, RI, rep exposure)
- Representation invariant checking

**Operations Implemented:**
- `toString()` - Human-readable string representation
- `equals()` - Structural equality comparison
- `hashCode()` - Hash code consistent with equals

### Testing
- 15 comprehensive unit tests in `ExpressionTest.java`
- Tests cover all variants, nested expressions, and edge cases
- All tests passing

### File Structure
```
src/expressivo/
  ├── Expression.java       # Interface with ADT definition
  ├── Number.java          # Numeric constants
  ├── Variable.java        # Variables
  ├── Plus.java            # Addition
  ├── Times.java           # Multiplication
  ├── Commands.java        # Command interface (Problem 3-4)
  └── Main.java            # Console interface

test/expressivo/
  ├── ExpressionTest.java  # 15 unit tests
  └── CommandsTest.java    # (Problem 3-4)
```

## Running the Code

### Compile All Sources
```powershell
cd "d:\VS_Code\Software Construction\LabTwelve"
javac -d bin src/expressivo/*.java
```

### Run Tests
```powershell
javac -cp "bin;lib/*" -d bin test/expressivo/*.java
java -ea -cp "bin;lib/*" org.junit.runner.JUnitCore expressivo.ExpressionTest
```

### Expected Output
```
JUnit version 4.13.2
...............
Time: 0.xxx

OK (15 tests)
```

## Project Status

- [x] **Problem 1: Expression ADT** - COMPLETE
  - [x] Four variant classes (Number, Variable, Plus, Times)
  - [x] toString() implementation
  - [x] equals() and hashCode() implementation
  - [x] Comprehensive tests (15 tests)
  - [x] Full documentation (AF, RI, checkRep)

- [ ] **Problem 2: Parser** - NOT STARTED
  - [ ] Implement Expression.parse()
  - [ ] ANTLR grammar integration
  - [ ] Parser tests

- [ ] **Problem 3: Differentiation** - NOT STARTED
  - [ ] Implement Commands.differentiate()
  - [ ] Derivative rules for each variant
  - [ ] Differentiation tests

- [ ] **Problem 4: Simplification** - NOT STARTED
  - [ ] Implement Commands.simplify()
  - [ ] Variable substitution
  - [ ] Simplification tests

## Expression Examples

```java
// Creating expressions programmatically
Expression x = new Variable("x");
Expression two = new Number(2);
Expression three = new Number(3);

// x + 2
Expression sum = new Plus(x, two);
System.out.println(sum);  // Output: x + 2

// (x + 2) * 3
Expression product = new Times(sum, three);
System.out.println(product);  // Output: x + 2 * 3

// Structural equality
Expression sum2 = new Plus(x, two);
System.out.println(sum.equals(sum2));  // Output: true

Expression reversed = new Plus(two, x);
System.out.println(sum.equals(reversed));  // Output: false (order matters)
```

## Design Decisions

### toString() Format
- Numbers: Integers without decimals ("3"), decimals with point ("4.2")
- Variables: Plain name ("x", "foo")
- Operators: Spaces around symbols ("x + 2", "x * 3")
- No parentheses (structure implicit in AST)

### Structural Equality
- Order matters: `x + 2` ≠ `2 + x`
- Type matters: `Plus(3, 4)` ≠ `Times(3, 4)`
- Grouping determined by AST structure

### Hash Code Strategy
- Plus uses prime 31
- Times uses prime 37
- Ensures different operators with same operands have different hashes

## Documentation

Detailed implementation report: `PROBLEM1_COMPLETE.md`

## Requirements

- Java 8 or higher
- JUnit 4 (included in `lib/`)
- ANTLR 4 (included in `lib/antlr.jar`)
