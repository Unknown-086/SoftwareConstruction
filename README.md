# Lab 10: Graph ADT Implementation

## Overview
Implementation of a weighted directed graph Abstract Data Type (ADT) with String vertex labels, featuring two distinct representations.

## Implementations

### 1. ConcreteVerticesGraph
- **Design:** Vertex-centric representation
- **Structure:** List of Vertex objects, each maintaining its own edges
- **Vertex Class:** Mutable helper class with incoming/outgoing edge maps
- **Best For:** Frequent edge queries, graphs with many edges per vertex

### 2. ConcreteEdgesGraph
- **Design:** Edge-centric representation
- **Structure:** Set of vertices + List of Edge objects
- **Edge Class:** Immutable helper class representing directed weighted edges
- **Best For:** Frequent vertex operations, sparse graphs

## Features
- **Graph Operations:** add, remove, set (edges), vertices, sources, targets
- **Documentation:** Complete AF, RI, and rep exposure safety for all classes
- **Validation:** checkRep() ensures representation invariants
- **Debugging:** toString() provides human-readable graph representations
- **Testing:** 44 comprehensive tests (13 shared + 10 + 8 specific tests)

## Running the Code

### Compile
```powershell
javac -d bin src/graph/*.java
```

### Run Tests
```powershell
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore graph.ConcreteVerticesGraphTest
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore graph.ConcreteEdgesGraphTest
```

### Run toString() Demo
```powershell
java -ea -cp bin graph.ToStringDemo
```

## File Structure
```
src/graph/
  ├── Graph.java                      # Interface specification
  ├── ConcreteVerticesGraph.java      # Vertex-centric implementation
  ├── ConcreteEdgesGraph.java         # Edge-centric implementation
  └── ToStringDemo.java               # Demo program
test/graph/
  ├── GraphStaticTest.java            # Tests for static methods
  ├── GraphInstanceTest.java          # 13 core interface tests
  ├── ConcreteVerticesGraphTest.java  # 10 Vertex-specific tests
  └── ConcreteEdgesGraphTest.java     # 8 Edge-specific tests
```

## Reports
- `TASK1_TEST_REPORT.md` - Detailed test implementation documentation
- `TASK2_IMPLEMENTATION_REPORT.md` - Complete implementation analysis

## Test Results
All 44 tests passing  
No compilation errors  
Full representation invariant validation
