# Task 2: Deconstructing and Classifying ADT Operations

**Objective:** To understand and classify the operations of common Abstract Data Types.

**Types Analyzed:** 
1. ArrayList<E>
2. HashSet<E>
3. Stack<E>

---

## 1. ArrayList<E>

**Note:** When instantiated as `ArrayList<Integer>`, the type parameter `E` becomes `Integer`.

| Method | Classification | Brief Justification |
|--------|---------------|---------------------|
| `ArrayList()` | Creator | Constructor that creates a new empty ArrayList instance. Creates the ADT from scratch with no parameters. |
| `ArrayList(Collection<? extends E> c)` | Creator | Constructor that creates a new ArrayList containing elements from the specified collection. Creates a new ADT instance from existing data. |
| `add(E e)` | Mutator | **[DETAILED]** Appends the specified element to the end of this list. This is a mutator because it modifies the internal state of the ArrayList by increasing its size and adding a new element. The list object itself is changed, and calling `size()` before and after will show different values. |
| `add(int index, E element)` | Mutator | **[DETAILED]** Inserts the specified element at the specified position, shifting subsequent elements. This is a mutator because it directly modifies the list's internal structure by inserting an element and changing the indices of existing elements. The list's state is permanently altered. |
| `remove(int index)` | Mutator | **[DETAILED]** Removes the element at the specified position and returns it. This is a mutator because it changes the list by decreasing its size and removing an element. It modifies the internal array structure and shifts remaining elements. |
| `get(int index)` | Observer | **[DETAILED]** Returns the element at the specified position without modifying the list. This is an observer because it only reads and returns information about the list's state without changing anything. Multiple calls to `get(index)` will return the same element. |
| `size()` | Observer | **[DETAILED]** Returns the number of elements in the list. This is an observer because it simply reports information about the list's current state without modifying it. It provides read-only access to the list's size property. |
| `isEmpty()` | Observer | Returns `true` if the list contains no elements. Pure observer that checks state without modification. |
| `contains(Object o)` | Observer | Returns `true` if the list contains the specified element. Searches through the list without changing it. |
| `clear()` | Mutator | Removes all elements from the list. Changes the list's state by emptying it completely. |
| `subList(int fromIndex, int toIndex)` | Producer | Returns a view of a portion of this list. Creates a new List view without modifying the original. |

---

## 2. HashSet<E>

**Note:** When instantiated as `HashSet<String>`, the type parameter `E` becomes `String`.

| Method | Classification | Brief Justification |
|--------|---------------|---------------------|
| `HashSet()` | Creator | Constructor that creates a new empty HashSet. Creates the ADT instance from scratch with default initial capacity. |
| `HashSet(Collection<? extends E> c)` | Creator | **[DETAILED]** Constructor that creates a new HashSet containing elements from the specified collection. This is a creator because it constructs a new HashSet instance from existing data, establishing the initial state of the ADT. It instantiates a new object rather than modifying an existing one. |
| `add(E e)` | Mutator | **[DETAILED]** Adds the specified element to the set if it's not already present. This is a mutator because it modifies the internal state of the HashSet by inserting a new element. The set's size may increase, and its internal hash table structure is updated. The object itself is changed. |
| `remove(Object o)` | Mutator | **[DETAILED]** Removes the specified element from the set if present. This is a mutator because it changes the set's state by deleting an element and potentially reorganizing the internal hash table. The set's size decreases, and subsequent operations will reflect this change. |
| `contains(Object o)` | Observer | **[DETAILED]** Returns `true` if the set contains the specified element. This is an observer because it only checks membership without modifying the set. It performs a read-only operation on the internal hash table to locate the element. The set remains unchanged after this operation. |
| `size()` | Observer | **[DETAILED]** Returns the number of elements in the set. This is an observer because it simply reports the current count of elements without altering the set's state. It provides read-only access to the set's size property, and calling it multiple times returns consistent results unless the set is modified. |
| `isEmpty()` | Observer | Returns `true` if the set contains no elements. Pure observer that checks emptiness without modification. |
| `clear()` | Mutator | Removes all elements from the set. Changes the set's state by emptying it and resetting its size to zero. |
| `iterator()` | Producer | Returns an iterator over the elements in the set. Creates a new Iterator object without modifying the set. |
| `clone()` | Producer | Returns a shallow copy of this HashSet instance. Creates a new HashSet object with the same elements. |

---

## 3. Stack<E>

**Note:** When instantiated as `Stack<Character>`, the type parameter `E` becomes `Character`.

| Method | Classification | Brief Justification |
|--------|---------------|---------------------|
| `Stack()` | Creator | Constructor that creates an empty stack. Creates a new Stack ADT instance with no elements. |
| `push(E item)` | Mutator | **[DETAILED]** Pushes an item onto the top of the stack. This is a mutator because it modifies the stack's internal state by adding an element to the top position. The stack grows in size, and the new element becomes the most recently added item. The stack object itself is permanently changed. |
| `pop()` | Mutator | **[DETAILED]** Removes and returns the object at the top of the stack. This is a mutator because it changes the stack by removing the top element and decreasing its size. It modifies the internal structure, and subsequent `peek()` calls will return a different element. The stack's state is altered. |
| `peek()` | Observer | **[DETAILED]** Returns the object at the top of the stack without removing it. This is an observer because it only reads the top element without modifying the stack. Multiple calls to `peek()` will return the same element unless the stack is modified by other operations. It provides read-only access to the top element. |
| `empty()` | Observer | **[DETAILED]** Tests if the stack is empty. This is an observer because it only checks the stack's state without changing it. It returns a boolean indicating whether the stack has elements, performing a read-only operation. The stack remains unchanged after calling this method. |
| `search(Object o)` | Observer | **[DETAILED]** Returns the 1-based position of an object on the stack. This is an observer because it searches through the stack to find an element's position without modifying the stack's contents. It performs a read-only traversal of the internal structure. The stack's state remains unchanged. |
| `size()` | Observer | Returns the number of elements in the stack. Inherited from Vector, reports the count without modification. |
| `clear()` | Mutator | Removes all elements from the stack. Changes the stack's state by emptying it completely. |
| `contains(Object o)` | Observer | Returns `true` if the stack contains the specified element. Searches without modifying the stack. |
| `add(E e)` | Mutator | Appends the specified element (inherited from Vector). Modifies the stack by adding an element. |

---

## Summary of Classifications

### ArrayList<E>
- **Creators:** 2 (constructors)
- **Producers:** 1 (`subList`)
- **Observers:** 4 (`get`, `size`, `isEmpty`, `contains`)
- **Mutators:** 4 (`add` variations, `remove`, `clear`)

### HashSet<E>
- **Creators:** 2 (constructors)
- **Producers:** 2 (`iterator`, `clone`)
- **Observers:** 3 (`contains`, `size`, `isEmpty`)
- **Mutators:** 3 (`add`, `remove`, `clear`)

### Stack<E>
- **Creators:** 1 (constructor)
- **Producers:** 0
- **Observers:** 4 (`peek`, `empty`, `search`, `size`, `contains`)
- **Mutators:** 4 (`push`, `pop`, `clear`, `add`)

---

## Key Insights

1. **Mutators vs Producers:** Collections like ArrayList, HashSet, and Stack are mutable ADTs, so they have many mutators. In contrast, immutable ADTs (like String or our Fraction class) would have producers instead of mutators.

2. **Observers are Essential:** All ADTs need observers to query their state without modification. These are critical for testing and using the ADT effectively.

3. **Stack is Unique:** Stack has no producers because it's designed to be modified in-place. Operations like `push` and `pop` directly change the stack rather than creating new stack instances.

4. **Generic Design:** Using generic type parameters (E, K, V) allows these ADTs to work with any object type, making them reusable and type-safe.
