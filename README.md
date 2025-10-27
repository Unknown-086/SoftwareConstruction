# Lab Eight: Recursive Programming

This lab focuses on designing, testing, and implementing recursive solutions to problems.

## Directory Structure

```
LabEight/
├── README.md
├── Task1-RecursiveFileSearch/
│   ├── src/
│   │   └── RecursiveFileSearch.java
│   ├── test/
│   │   └── RecursiveFileSearchTest.java
│   └── SPECIFICATIONS.md
├── Task2-StringPermutations/
│   ├── src/
│   │   └── StringPermutations.java
│   ├── test/
│   │   └── StringPermutationsTest.java
│   └── SPECIFICATIONS.md
└── Task3-Testing/
    ├── test/
    │   ├── IntegratedRecursiveTests.java
    │   └── TEST_ANALYSIS.md
    └── README.md
```

## Tasks Overview

### Task 1: Recursive File Search
A program that recursively searches for files within a directory and its subdirectories with support for multiple file searches, occurrence counting, and case-sensitive/insensitive matching.

### Task 2: Recursive String Permutations
A program that generates all permutations of a given string using both recursive and iterative approaches, with options for handling duplicate permutations.

### Task 3: Testing Recursive Programs
Comprehensive JUnit test suite for Tasks 1 and 2, covering base cases, recursive steps, and edge conditions.

## Running the Programs

### Task 1
```powershell
cd Task1-RecursiveFileSearch
javac src/RecursiveFileSearch.java
java -cp src RecursiveFileSearch <directory_path> <file_name>
```

**Examples:**
```powershell
# Single file search (case-sensitive)
java -cp src RecursiveFileSearch "C:\Users\Documents" "report.txt" true

# Multiple file search
java -cp src RecursiveFileSearch "C:\Projects" -m "Main.java" "Test.java" false

# Count occurrences
java -cp src RecursiveFileSearch "C:\Workspace" -c "README.md" true
```

### Task 2
```powershell
cd Task2-StringPermutations
javac src/StringPermutations.java
java -cp src StringPermutations <input_string>
```

## Running Tests

Tests use JUnit 5 (Jupiter) configured in VS Code. You can run tests directly from VS Code's Test Explorer or use the Testing view.

**Using VS Code:**
1. Open the Testing view (beaker icon in the sidebar)
2. Click the refresh button to discover tests
3. Run individual tests or entire test classes
4. View results in the Test Explorer

**Using Command Line:**
```powershell
# The tests will use JUnit 5 configured in your VS Code environment
# Compile and run from VS Code Test Explorer for best experience
```
