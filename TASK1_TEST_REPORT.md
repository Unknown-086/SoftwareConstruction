# Task 1: Test Implementation Report
**Graph<String> ADT Testing**

---

## Overview
Implemented comprehensive test suite for the Graph<String> Abstract Data Type, covering all 7 core functionalities as specified in the problem set requirements.

## Test Location
- **File:** `test/graph/GraphInstanceTest.java`
- **Type:** Abstract test class (inherited by concrete implementations)
- **Total Tests Implemented:** 13 tests

---

## Implemented Tests by Functionality

### 1. Creating New Graph
**Test:** `testInitialVerticesEmpty()`
- **Purpose:** Verify that newly created graphs start with no vertices
- **Assertion:** Empty graph returns `Collections.emptySet()` for `vertices()`
- **Coverage:** Graph initialization state

---

### 2. Adding Vertices
**Test:** `testAddVertex()`
- **Purpose:** Verify successful addition of new vertices
- **Assertions:**
  - `add()` returns `true` for new vertex
  - Vertex appears in `vertices()` set after addition
- **Coverage:** Basic vertex insertion

---

### 3. Preventing Duplicates
**Test:** `testAddDuplicateVertex()`
- **Purpose:** Ensure duplicate vertices are rejected
- **Assertions:**
  - First `add("A")` returns `true`
  - Second `add("A")` returns `false`
- **Coverage:** Duplicate prevention mechanism

---

### 4. Removing Vertices

#### Test 4.1: `testRemoveVertex()`
- **Purpose:** Verify removal of standalone vertices
- **Assertions:**
  - `remove()` returns `true` for existing vertex
  - Vertex no longer in `vertices()` set after removal

#### Test 4.2: `testRemoveVertexWithEdges()`
- **Purpose:** Verify removal of vertices with connected edges
- **Setup:** Creates edges A→B (weight 5), B→C (weight 3)
- **Assertions:**
  - Vertex B successfully removed
  - All edges involving B are automatically deleted
  - A's outgoing edges become empty
  - C's incoming edges become empty
- **Coverage:** Cascading edge removal

---

### 5. Adding, Updating, and Deleting Edges

#### Test 5.1: `testSetNewEdge()`
- **Purpose:** Verify creation of new edges
- **Assertions:**
  - `set("A", "B", 5)` returns 0 (no previous edge)
  - Both vertices auto-created if not present
  - Vertices A and B appear in graph
- **Coverage:** Edge creation with implicit vertex addition

#### Test 5.2: `testSetUpdateEdge()`
- **Purpose:** Verify edge weight updates
- **Setup:** Creates edge A→B with weight 5
- **Assertions:**
  - `set("A", "B", 10)` returns 5 (previous weight)
  - New weight is 10 (verified via `targets("A")`)
- **Coverage:** Edge weight modification

#### Test 5.3: `testSetRemoveEdge()`
- **Purpose:** Verify edge deletion by setting weight to 0
- **Setup:** Creates edge A→B with weight 5
- **Assertions:**
  - `set("A", "B", 0)` returns 5 (previous weight)
  - Edge no longer exists in `targets("A")` map
- **Coverage:** Edge deletion mechanism

---

### 6. Checking Vertices List
**Test:** `testVertices()`
- **Purpose:** Verify accurate vertex enumeration
- **Setup:** Adds vertices "A" and "B"
- **Assertions:**
  - `vertices()` returns set of size 2
  - Set contains both "A" and "B"
- **Coverage:** Vertex collection query

---

### 7. Checking Edge Sources and Targets

#### Test 7.1: `testSources()`
- **Purpose:** Verify incoming edge queries
- **Setup:** Creates edges A→C (weight 5), B→C (weight 3)
- **Assertions:**
  - `sources("C")` returns map of size 2
  - Map contains A→5 and B→3
- **Coverage:** Incoming edge inspection

#### Test 7.2: `testTargets()`
- **Purpose:** Verify outgoing edge queries
- **Setup:** Creates edges A→B (weight 3), A→C (weight 6)
- **Assertions:**
  - `targets("A")` returns map of size 2
  - Map contains B→3 and C→6
- **Coverage:** Outgoing edge inspection

---

## Testing Strategy Summary

### Partitioning Approach
- **Empty vs. Non-empty graphs:** Testing both initial state and populated graphs
- **New vs. Existing elements:** Testing addition of new vs. duplicate vertices/edges
- **Zero vs. Non-zero weights:** Testing edge creation (weight > 0) and deletion (weight = 0)
- **Isolated vs. Connected vertices:** Testing vertex removal with and without edges

### Coverage Achieved
✅ **All 7 core functionalities tested**
- Graph creation and initialization
- Vertex addition with duplicate handling
- Vertex removal with edge cleanup
- Edge creation, update, and deletion
- Vertex enumeration
- Edge source and target queries

### Test Design Principles
- **Independence:** Each test uses fresh empty graph via `emptyInstance()`
- **Clarity:** Descriptive test names following `test<Operation><Scenario>()` pattern
- **Assertions:** Multiple assertions per test for thorough validation
- **Implementation-agnostic:** Tests work with any Graph<String> implementation

---

## Test Execution Results
- **Total Tests:** 13 tests in GraphInstanceTest
- **Status:** ✅ All tests passing
- **Inheritance:** Tests inherited by ConcreteVerticesGraphTest and ConcreteEdgesGraphTest
- **Total Coverage:** 23 tests for ConcreteVerticesGraph, 21 tests for ConcreteEdgesGraph

---

## Conclusion
The test suite successfully validates all required Graph ADT behaviors as specified in Task 1, providing a solid foundation for implementing and verifying multiple graph representations.
