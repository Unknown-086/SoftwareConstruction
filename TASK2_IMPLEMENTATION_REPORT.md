# Task 2: Implementation Report
**Weighted Directed Graph with String Labels - Two Implementations**

---

## Overview
Implemented two distinct representations of the Graph<String> ADT:
1. **ConcreteVerticesGraph** - Vertex-centric representation with mutable Vertex objects
2. **ConcreteEdgesGraph** - Edge-centric representation with immutable Edge objects

Both implementations fully satisfy the Graph interface contract and include complete documentation, representation invariants, and defensive programming practices.

---

# Part 1: ConcreteVerticesGraph Implementation

## Architecture
**Representation:** List of Vertex objects, where each Vertex maintains its own incoming and outgoing edges.

### Abstraction Function (AF)
```
AF(vertices) = a directed weighted graph where:
  - Each Vertex in vertices represents a vertex in the graph with its label
  - The edges of the graph are represented by the targets maps in each Vertex object
```

### Representation Invariant (RI)
- `vertices != null`
- All elements in vertices are non-null
- No two Vertex objects have the same label (unique labels)
- For each Vertex v in vertices:
  - All targets in v exist as vertices in the graph
  - All sources in v exist as vertices in the graph

### Safety from Rep Exposure
- `vertices` is private and final, never returned directly
- `vertices()` returns a new Set containing only the String labels (not Vertex objects)
- `sources()` and `targets()` return defensive copies of the edge maps
- `Vertex` class is package-private and not exposed to clients

---

## Implemented Methods

### 1. `add(String vertex)`
**Implementation:**
- Searches for existing vertex using `findVertex()` helper
- Returns false if vertex already exists
- Creates new Vertex object and adds to list if not present
- Calls `checkRep()` before returning true

**Complexity:** O(n) where n is the number of vertices

### 2. `set(String source, String target, int weight)`
**Implementation:**
- Auto-creates source and target vertices if they don't exist
- If weight > 0: adds or updates edge using `setTarget()` and `setSource()`
- If weight = 0: removes edge using `removeTarget()` and `removeSource()`
- Returns previous edge weight (0 if edge didn't exist)
- Maintains bidirectional edge information (outgoing in source, incoming in target)

**Key Design:** Synchronizes both source's targets and target's sources to maintain consistency

**Complexity:** O(n) for vertex lookup, O(1) for edge operations

### 3. `remove(String vertex)`
**Implementation:**
- Returns false if vertex doesn't exist
- Iterates through all vertices to remove edges involving the removed vertex
- Uses `removeTarget()` and `removeSource()` for cleanup
- Removes vertex from list

**Complexity:** O(n²) - iterates through n vertices, each may have edges to remove

### 4. `vertices()`
**Implementation:**
- Creates new HashSet and populates with labels from all vertices
- Returns unmodifiable view using `Collections.unmodifiableSet()`
- Prevents client modification of internal representation

**Complexity:** O(n)

### 5. `sources(String target)`
**Implementation:**
- Finds target vertex using `findVertex()`
- Returns empty map if vertex doesn't exist
- Returns defensive copy of sources map using `new HashMap<>()`

**Complexity:** O(n) for lookup, O(m) for copying m sources

### 6. `targets(String source)`
**Implementation:**
- Finds source vertex using `findVertex()`
- Returns empty map if vertex doesn't exist
- Returns defensive copy of targets map

**Complexity:** O(n) for lookup, O(m) for copying m targets

### 7. `toString()`
**Implementation:**
```
Graph with <n> vertices:
  A: [→B(5), →C(3)]
  B: [→C(2)]
  C: []
```
- Displays vertex count
- Lists each vertex with its outgoing edges in format: `→target(weight)`

### 8. `checkRep()`
**Implementation:**
- Asserts `vertices != null`
- Checks no null vertices in list
- Verifies no duplicate labels using HashSet
- Validates all edge references (targets and sources) exist in graph
- Called after every mutating operation

---

# Part 2: Vertex Class (Mutable Helper)

## Architecture
**Representation:** String label + two HashMaps (targets, sources)

### Abstraction Function
```
AF(label, targets, sources) = a vertex labeled 'label' with:
  - Outgoing edges to each vertex in targets.keySet() with weights targets.values()
  - Incoming edges from each vertex in sources.keySet() with weights sources.values()
```

### Representation Invariant
- `label != null` and `label` is not empty
- `targets != null`, `sources != null`
- All keys in targets and sources are non-null
- All values in targets and sources are positive (> 0)

### Safety from Rep Exposure
- `label` is private, final, and immutable (String)
- `targets` and `sources` are private and final
- `getTargets()` and `getSources()` return defensive copies
- All modifications go through controlled methods with validation

---

## Vertex Methods

### 1. `Vertex(String label)` - Constructor
**Implementation:**
- Validates label is non-null and non-empty
- Throws IllegalArgumentException if invalid
- Initializes empty targets and sources maps
- Calls `checkRep()`

### 2. `getLabel()`
**Returns:** The vertex label (immutable String)

### 3. `getTargets()`
**Returns:** Defensive copy of targets map using `new HashMap<>(targets)`

### 4. `getSources()`
**Returns:** Defensive copy of sources map using `new HashMap<>(sources)`

### 5. `setTarget(String target, int weight)`
**Implementation:**
- Validates weight > 0, throws IllegalArgumentException if not
- Adds or updates edge in targets map
- Returns previous weight (0 if new edge)

### 6. `setSource(String source, int weight)`
**Implementation:**
- Validates weight > 0
- Adds or updates edge in sources map
- Returns previous weight

### 7. `removeTarget(String target)`
**Implementation:**
- Removes entry from targets map
- Returns previous weight (0 if edge didn't exist)

### 8. `removeSource(String source)`
**Implementation:**
- Removes entry from sources map
- Returns previous weight

### 9. `toString()`
**Format:** `A: [→B(5), →C(3)]`
- Shows vertex label and all outgoing edges

### 10. `checkRep()`
**Validates:**
- Label is non-null and non-empty
- Maps are non-null
- All map keys are non-null
- All map values are positive

---

# Part 3: ConcreteEdgesGraph Implementation

## Architecture
**Representation:** Set of vertices (Strings) + List of Edge objects

### Abstraction Function
```
AF(vertices, edges) = a directed weighted graph where:
  - The set of vertices in the graph = vertices
  - For each Edge e in edges, there exists a directed edge from
    e.getSource() to e.getTarget() with weight e.getWeight()
```

### Representation Invariant
- `vertices != null`
- `edges != null`
- No null elements in vertices
- No null elements in edges
- For all edges e: e.getSource() and e.getTarget() are in vertices
- No duplicate edges (same source and target pair)
- All edge weights are positive

### Safety from Rep Exposure
- `vertices` and `edges` are private and final
- `vertices()` returns unmodifiable defensive copy
- `sources()` and `targets()` return new HashMap objects
- Edge objects are immutable, safe to return
- Never return vertices or edges sets directly

---

## Implemented Methods

### 1. `add(String vertex)`
**Implementation:**
- Checks if vertex already exists using `vertices.contains()`
- Returns false if already present
- Adds vertex to set, calls `checkRep()`, returns true

**Complexity:** O(1) average (HashSet)

### 2. `set(String source, String target, int weight)`
**Implementation:**
- Auto-adds both source and target to vertices set (Set handles duplicates)
- Searches for existing edge using `findEdge()`
- If edge exists: stores previous weight, removes old edge
- If weight > 0: creates new Edge object and adds to list
- Returns previous weight

**Key Design:** Immutable edges require remove-and-add for updates

**Complexity:** O(e) where e is the number of edges

### 3. `remove(String vertex)`
**Implementation:**
- Returns false if vertex doesn't exist
- Removes vertex from set
- Uses Iterator to safely remove all connected edges
- Checks both source and target of each edge

**Complexity:** O(e) for edge removal

### 4. `vertices()`
**Implementation:**
- Creates new HashSet as defensive copy
- Wraps in `Collections.unmodifiableSet()`
- Double protection against modification

**Complexity:** O(n)

### 5. `sources(String target)`
**Implementation:**
- Creates empty HashMap
- Iterates through all edges
- Adds edge to result if its target matches
- Returns new map (no need for unmodifiable since it's freshly created)

**Complexity:** O(e)

### 6. `targets(String source)`
**Implementation:**
- Creates empty HashMap
- Iterates through all edges
- Adds edge to result if its source matches
- Returns new map

**Complexity:** O(e)

### 7. `toString()`
**Implementation:**
```
Graph with <n> vertices and <e> edges:
Vertices: [A, B, C]
Edges:
  A → B (5)
  B → C (3)
```
- Shows vertex and edge counts
- Lists all vertices
- Lists all edges with format: `source → target (weight)`

### 8. `checkRep()`
**Implementation:**
- Asserts vertices and edges are non-null
- Checks no null elements in either collection
- Validates all edge endpoints exist in vertices set
- Checks for duplicate edges (nested loop)
- Called after every mutating operation

---

# Part 4: Edge Class (Immutable Helper)

## Architecture
**Representation:** Three final fields (source, target, weight)

### Abstraction Function
```
AF(source, target, weight) = a directed edge from vertex 'source'
                              to vertex 'target' with positive weight 'weight'
```

### Representation Invariant
- `source != null`
- `target != null`
- `weight > 0`

### Safety from Rep Exposure
- All fields are private and final
- `source` and `target` are Strings (immutable type)
- `weight` is int (primitive type)
- No mutator methods exist (immutable class)
- Constructor validates all inputs

---

## Edge Methods

### 1. `Edge(String source, String target, int weight)` - Constructor
**Implementation:**
- Validates source is non-null
- Validates target is non-null
- Validates weight > 0
- Throws IllegalArgumentException for any violation
- Calls `checkRep()` after initialization

**Immutability:** All fields assigned once in constructor

### 2. `getSource()`
**Returns:** The source vertex (String is immutable)

### 3. `getTarget()`
**Returns:** The target vertex (String is immutable)

### 4. `getWeight()`
**Returns:** The edge weight (primitive int)

### 5. `connectsVertices(String src, String tgt)`
**Implementation:**
- Helper method for finding edges
- Returns `source.equals(src) && target.equals(tgt)`
- Direction matters (directed graph)

**Usage:** Used by `findEdge()` in ConcreteEdgesGraph

### 6. `toString()`
**Format:** `A → B (5)`
- Uses arrow symbol to show direction
- Includes weight in parentheses

### 7. `checkRep()`
**Validates:**
- Source is non-null
- Target is non-null
- Weight is positive

---

# Part 5: Test Implementation

## ConcreteVerticesGraphTest (10 Tests)

### Graph-Level Tests
1. **testToStringWithGraph** - Verifies toString output contains vertex information

### Vertex Class Tests
2. **testVertexConstructorValid** - Tests successful vertex creation
3. **testVertexConstructorNullLabel** - Tests exception for null label
4. **testVertexSetTarget** - Tests adding new outgoing edge
5. **testVertexUpdateTarget** - Tests updating existing edge weight
6. **testVertexSetTargetInvalidWeight** - Tests exception for non-positive weight
7. **testVertexRemoveTarget** - Tests removing outgoing edge
8. **testVertexSetSource** - Tests adding incoming edge
9. **testVertexRemoveSource** - Tests removing incoming edge
10. **testVertexDefensiveCopy** - Tests that returned maps don't affect internal state

**Coverage:** Constructor validation, edge operations (add/update/remove), defensive copying

---

## ConcreteEdgesGraphTest (8 Tests)

### Graph-Level Tests
1. **testToStringWithGraph** - Verifies toString output format

### Edge Class Tests
2. **testEdgeConstructorValid** - Tests successful edge creation
3. **testEdgeConstructorNullSource** - Tests exception for null source
4. **testEdgeConstructorNullTarget** - Tests exception for null target
5. **testEdgeConstructorInvalidWeight** - Tests exception for non-positive weight
6. **testEdgeGetters** - Tests all getter methods
7. **testEdgeConnectsVertices** - Tests helper method for edge matching
8. **testEdgeToString** - Tests string representation format

**Coverage:** Constructor validation, getters, helper methods, immutability

---

# Part 6: Comparison of Two Implementations

## Design Trade-offs

| Aspect | ConcreteVerticesGraph | ConcreteEdgesGraph |
|--------|----------------------|-------------------|
| **Primary Structure** | List of Vertex objects | Set + List (vertices + edges) |
| **Edge Storage** | Distributed (each vertex stores its edges) | Centralized (edges in one list) |
| **Mutability** | Mutable Vertex class | Immutable Edge class |
| **Edge Lookup** | O(1) via HashMap in Vertex | O(e) linear search through edges |
| **Vertex Lookup** | O(n) linear search | O(1) via HashSet |
| **sources()/targets()** | O(n) + O(1) map copy | O(e) scan all edges |
| **Edge Update** | O(1) map update | O(e) remove + add |
| **Memory Overhead** | Each vertex stores 2 maps | Central edge list |
| **Consistency** | Must synchronize source & target | Edges are independent |

## Performance Analysis

**ConcreteVerticesGraph Best For:**
- Frequent edge queries from specific vertices
- Graphs with many edges per vertex
- When edge updates are common

**ConcreteEdgesGraph Best For:**
- Frequent vertex addition/removal
- Sparse graphs (few edges)
- When edge immutability is important

---

# Part 7: Documentation Quality

## All Classes Include:

### ✅ Abstraction Function (AF)
- Clear mapping from representation to abstract value
- Documented for: ConcreteVerticesGraph, ConcreteEdgesGraph, Vertex, Edge

### ✅ Representation Invariant (RI)
- Comprehensive constraints on rep fields
- Covers nullness, validity, and consistency requirements
- Documented for all four classes

### ✅ Rep Exposure Prevention
- Explicit documentation of defensive programming techniques
- Use of defensive copying (Vertex maps, vertices set)
- Use of immutability (Edge class, String labels)
- Use of unmodifiable wrappers (vertices() method)

### ✅ checkRep() Implementation
- Validates all RI constraints using assertions
- Called after constructor and all mutating operations
- Includes descriptive assertion messages

### ✅ toString() Implementation
- Human-readable format for debugging
- Shows graph structure clearly
- Includes vertex/edge counts and relationships

---

# Part 8: Test Execution Results

## Total Test Coverage
- **GraphInstanceTest:** 13 inherited tests (for both implementations)
- **ConcreteVerticesGraphTest:** +10 specific tests = **23 total tests**
- **ConcreteEdgesGraphTest:** +8 specific tests = **21 total tests**

## Test Results
✅ **All 44 tests passing**
- ConcreteVerticesGraph: 23/23 passed
- ConcreteEdgesGraph: 21/21 passed
- No compilation errors
- All checkRep() assertions enabled and passing

---

# Conclusion

Successfully implemented two complete, well-documented implementations of the Graph<String> ADT:

1. **ConcreteVerticesGraph** with mutable Vertex helper class
   - Vertex-centric design
   - Fast edge lookups
   - Bidirectional edge tracking

2. **ConcreteEdgesGraph** with immutable Edge helper class
   - Edge-centric design
   - Fast vertex operations
   - Simpler consistency model

Both implementations:
- ✅ Satisfy all Graph interface contracts
- ✅ Include comprehensive AF, RI, and rep exposure documentation
- ✅ Implement checkRep() with full validation
- ✅ Provide useful toString() methods
- ✅ Pass all tests (44 total)
- ✅ Use defensive programming to prevent rep exposure

The implementations demonstrate understanding of:
- Abstract Data Types (ADT)
- Representation invariants and abstraction functions
- Defensive copying and immutability
- Test-driven development
- Multiple representations for the same specification
