# Lab 14: Concurrency

This lab provides hands-on experience with concurrency concepts in Java, including multithreading, thread synchronization, and thread-safe operations.

## Lab Structure

```
LabFourteen/
├── src/
│   ├── task1/          # Task 1: Introduction to Multithreading
│   ├── task2/          # Task 2: Thread Synchronization
│   ├── task3/          # Task 3: Concurrent Data Structures
│   └── task4/          # Task 4: Bank Transaction System
├── test/               # JUnit test cases
├── bin/                # Compiled classes
└── README.md
```

## Tasks Implemented

### Task 1: Introduction to Multithreading

**Objective:** Create and execute multiple threads in Java.

**Files:**
- [NumberPrinter.java](src/task1/NumberPrinter.java) - Prints numbers from 1 to 10
- [SquarePrinter.java](src/task1/SquarePrinter.java) - Prints squares of numbers from 1 to 10
- [MultithreadingDemo.java](src/task1/MultithreadingDemo.java) - Main class that runs both threads concurrently

**Key Concepts:**
- Thread creation using `Runnable` interface
- Concurrent thread execution
- Using `Thread.join()` to wait for thread completion

**How to Run:**
```bash
# Compile
javac -d bin src/task1/*.java

# Run
java -cp bin task1.MultithreadingDemo
```

**Expected Output:**
Both threads will run concurrently, printing their outputs. The order may vary due to thread scheduling.

---

### Task 2: Thread Synchronization

**Objective:** Implement thread synchronization to prevent race conditions.

**Files:**
- [SharedCounter.java](src/task2/SharedCounter.java) - Thread-safe counter using `synchronized` keyword
- [CounterIncrementer.java](src/task2/CounterIncrementer.java) - Runnable that increments counter 100 times
- [ThreadSynchronizationDemo.java](src/task2/ThreadSynchronizationDemo.java) - Main class with 3 threads

**Key Concepts:**
- Race conditions and how to prevent them
- Using `synchronized` keyword for thread safety
- Shared resource management

**How to Run:**
```bash
# Compile
javac -d bin src/task2/*.java

# Run
java -cp bin task2.ThreadSynchronizationDemo
```

**Expected Output:**
```
Starting three threads to increment counter...

Thread-1 completed 100 increments
Thread-2 completed 100 increments
Thread-3 completed 100 increments

All threads completed.
Final counter value: 300
Expected value: 300
✓ Synchronization successful!
```

---

### Task 3: Concurrent Data Structures

**Objective:** Implement and use thread-safe data structures.

**Files:**
- [DataReader.java](src/task3/DataReader.java) - Reads data from shared ConcurrentHashMap
- [DataWriter.java](src/task3/DataWriter.java) - Writes data to shared ConcurrentHashMap
- [ConcurrentDataStructureDemo.java](src/task3/ConcurrentDataStructureDemo.java) - Main class with multiple reader/writer threads

**Key Concepts:**
- Thread-safe data structures (`ConcurrentHashMap`)
- Safe concurrent read/write operations without explicit synchronization
- Internal thread safety mechanisms

**How to Run:**
```bash
# Compile
javac -d bin src/task3/*.java

# Run
java -cp bin task3.ConcurrentDataStructureDemo
```

**Expected Output:**
Multiple threads will concurrently read and write to the shared map. The ConcurrentHashMap handles all synchronization internally.

---

### Task 4: Simulation of Bank Transaction System

**Objective:** Simulate a bank transaction system with concurrent deposits and withdrawals.

**Files:**
- [BankAccount.java](src/task4/BankAccount.java) - Thread-safe bank account with synchronized methods
- [Client.java](src/task4/Client.java) - Client thread performing random transactions
- [BankTransactionSystem.java](src/task4/BankTransactionSystem.java) - Main class simulating multiple clients

**Key Concepts:**
- Thread synchronization in real-world scenarios
- Synchronized deposit and withdrawal operations
- Maintaining data consistency with concurrent access
- Atomic operations for financial transactions

**How to Run:**
```bash
# Compile
javac -d bin src/task4/*.java

# Run
java -cp bin task4.BankTransactionSystem
```

**Expected Output:**
```
=== Bank Transaction System ===
Account Number: ACC-12345
Initial Balance: $1000.0

Starting transactions with multiple clients...

Client-1 deposited $45.0 | New Balance: $1045.0
Client-2 withdrew $67.0 | New Balance: $978.0
...
Client-1 completed all transactions
Client-2 completed all transactions
Client-3 completed all transactions

=== Transaction Summary ===
All clients completed their transactions
Final Balance: $XXX.XX

✓ All transactions completed safely with synchronized methods!
```

---

## Running Tests

The lab includes JUnit tests to verify thread synchronization:

**Test Files:**
- [SharedCounterTest.java](test/SharedCounterTest.java) - Tests for Task 2
- [ConcurrentDataStructureTest.java](test/ConcurrentDataStructureTest.java) - Tests for Task 3
- [BankAccountTest.java](test/BankAccountTest.java) - Tests for Task 4

**Test Cases:**

**SharedCounterTest:**
1. `testSingleThreadIncrement()` - Verifies single thread increments correctly
2. `testThreeThreadsSynchronization()` - Verifies three threads reach count of 300
3. `testMultipleIncrements()` - Tests sequential increments

**ConcurrentDataStructureTest:**
1. `testConcurrentWrites()` - Verifies concurrent writes to ConcurrentHashMap
2. `testConcurrentReads()` - Verifies concurrent reads from ConcurrentHashMap

**BankAccountTest:**
1. `testDeposit()` - Tests deposit operation
2. `testWithdrawal()` - Tests withdrawal operation
3. `testInsufficientFunds()` - Tests withdrawal with insufficient funds
4. `testConcurrentTransactions()` - Tests thread-safe concurrent transactions

**How to Run Tests:**
```bash
# Compile tests (assuming JUnit 4 JAR is in lib/)
javac -d bin -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" test/*.java

# Run all tests
java -cp "bin;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore test.SharedCounterTest test.ConcurrentDataStructureTest test.BankAccountTest
```

---

## Key Learning Outcomes

1. **Multithreading Basics:**
   - Creating threads using `Runnable` interface
   - Starting and managing multiple threads
   - Understanding concurrent execution

2. **Thread Synchronization:**
   - Identifying race conditions
   - Using `synchronized` keyword to protect shared resources
   - Ensuring thread safety in concurrent operations

3. **Concurrent Data Structures:**
   - Using `ConcurrentHashMap` for thread-safe operations
   - Understanding when to use concurrent collections
   - Avoiding explicit synchronization with thread-safe data structures

4. **Real-World Applications:**
   - Implementing thread-safe financial transactions
   - Handling concurrent read/write operations
   - Maintaining data consistency in multi-threaded environments

5. **Testing Concurrent Code:**
   - Writing tests for multi-threaded applications
   - Verifying correct synchronization behavior

---

## Notes

- The `synchronized` keyword ensures only one thread can execute the synchronized method at a time
- Without synchronization, race conditions could cause the final counter value to be less than 300
- Thread execution order is non-deterministic and controlled by the JVM scheduler
