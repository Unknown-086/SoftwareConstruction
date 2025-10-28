# Recursive Programming Utilities

Two Java utilities demonstrating recursive algorithms: a file search tool and a string permutation generator.

---

## Project Structure

```
LabEight/
├── src/
│   ├── RecursiveFileSearch.java      # Core file search logic
│   ├── RecursiveFileSearchCLI.java   # File search CLI
│   ├── PermutationGenerator.java     # Core permutation logic
│   └── PermutationsCLI.java          # Permutation generator CLI
├── test/
│   ├── RecursiveFileSearchCompleteTest.java  # 20 tests for file search
│   └── PermutationGeneratorTest.java         # 15 tests for permutations
└── bin/                              # Compiled classes
```

---

## Recursive File Search

### What It Does

Recursively searches through a directory tree to find files by name. Supports:
- **Case-sensitive and case-insensitive** searching
- **Multiple file search** in one operation
- **Occurrence counting** across directory trees
- Deep nested directory traversal

### How to Run

#### Basic Search (Case-Sensitive)
```bash
# Compile
javac -d bin src/RecursiveFileSearch.java src/RecursiveFileSearchCLI.java

# Run - find a single file
java -cp bin RecursiveFileSearchCLI "D:\Projects" "Main.java"
```

#### Case-Insensitive Search
```bash
java -cp bin RecursiveFileSearchCLI -i "D:\VS_Code" "readme.md"
```

#### Search for Multiple Files
```bash
java -cp bin RecursiveFileSearchCLI -m "D:\Code" "pom.xml" "build.gradle" "package.json"
```

#### Count Occurrences
```bash
java -cp bin RecursiveFileSearchCLI -c "D:\Projects" "config.xml"
```

#### Combined Options
```bash
# Case-insensitive count
java -cp bin RecursiveFileSearchCLI -i -c "D:\Projects" "README.md"

# Multiple files, case-insensitive
java -cp bin RecursiveFileSearchCLI -i -m "D:\Workspace" "Main.java" "Test.java"
```

### CLI Options

| Flag | Description |
|------|-------------|
| `-i, --ignore-case` | Case-insensitive search |
| `-m, --multiple` | Search for multiple files |
| `-c, --count` | Count occurrences only (don't list all paths) |
| `-h, --help` | Show help message |

### Example Output

```
============================================================
Recursive File Search
============================================================
Directory: D:\VS_Code
Mode: Single File
Case Sensitive: true
Count Only: false
File: Main.java
============================================================

Searching...

============================================================
Search Results for: "Main.java"
============================================================

Found 7 occurrence(s) of "Main.java":

1. D:\VS_Code\OOP Project\Bank-Management-System-main\Bank-Management-System-main\src\main\Main.java
2. D:\VS_Code\Programming\FourthSemester\SoftwareDesignAndArchiture\LabNine_OpenEnded\SingletonPattern\Main.java
3. D:\VS_Code\Programming\Java\Main.java
4. D:\VS_Code\Software Construction\LabFive\ps1\ps1\src\twitter\Main.java
5. D:\VS_Code\Software Construction\LabSeven\ps1\ps1\src\twitter\Main.java
6. D:\VS_Code\Software Construction\LabSix\Software-Const-Labs-lab05\src\twitter\Main.java
7. D:\VS_Code\Software Construction\LabTwo\TaskFive\Main.java

============================================================
Search completed in 25972 ms
```

### Programmatic Usage

```java
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

// Single file search (case-sensitive)
File directory = new File("D:\\VS_Code");
List<File> results = RecursiveFileSearch.searchFile(directory, "Main.java");

// Case-insensitive search
List<File> results = RecursiveFileSearch.searchFile(directory, "readme.md", false);

// Multiple files
List<String> fileNames = Arrays.asList("pom.xml", "build.gradle");
Map<String, List<File>> results = RecursiveFileSearch.searchMultipleFiles(
    directory, fileNames, true
);

// Count occurrences
int count = RecursiveFileSearch.countFileOccurrences(directory, "config.xml", true);
```

---

## String Permutation Generator

### What It Does

Generates all possible permutations of a string using two different algorithms:
- **Recursive algorithm** - Swap-based with backtracking
- **Iterative algorithm** - Based on Heap's algorithm (faster for larger inputs)

Features:
- **Duplicate control** - Option to include or exclude duplicate permutations
- **Performance comparison** - Benchmark both algorithms
- **Efficient filtering** - Removes duplicates during generation (not post-processing)

### How to Run

> ⚠️ **IMPORTANT:** Keep input strings ≤ 10 characters to avoid running out of memory!
> - 10 characters = 3.6 million permutations (~100 MB)
> - 14 characters = 87 billion permutations (~2,000 GB - will crash!)

#### Generate Permutations (Recursive, Unique)
```bash
# Compile
javac -d bin src/PermutationGenerator.java src/PermutationsCLI.java

# Run (no quotes needed for single words)
java -cp bin PermutationsCLI unk

# Use quotes only if string has spaces
java -cp bin PermutationsCLI "u n k"
```

**Output:**
```
============================================================
String Permutation Generator
============================================================
Input: "unk"
Allow Duplicates: false
Algorithm: Recursive
============================================================

Recursive Algorithm Results:
Total permutations: 6
Time taken: 1.322 ms

Permutations:
unk, ukn, nuk, nku, knu, kun
```

**For larger inputs (shows first 50):**
```bash
java -cp bin PermutationsCLI unknown
```

**Output:**
```
============================================================
String Permutation Generator
============================================================
Input: "unknown"
Allow Duplicates: false
Algorithm: Recursive
============================================================

Recursive Algorithm Results:
Total permutations: 840
Time taken: 3.490 ms

(Too many to display - showing first 50)
unknown, unknonw, unknwon, unknwno, unknnwo, unknnow, unkonwn, unkonnw, unkownn, unkwonn, unkwnon, unkwnno, unnkown, unnkonw, unnkwon, unnkwno, unnknwo, unnknow, unnokwn, unnoknw, unnowkn, unnownk, unnonwk, unnonkw, unnwokn, unnwonk, unnwkon, unnwkno, unnwnko, unnwnok, unnnowk, unnnokw, unnnwok, unnnwko, unnnkwo, unnnkow, unonkwn, unonknw, unonwkn, unonwnk, unonnwk, unonnkw, unoknwn, unoknnw, unokwnn, unowknn, unownkn, unownnk, unwnokn, unwnonk
...
```

#### Include Duplicate Permutations
```bash
java -cp bin PermutationsCLI -d uuk
```

**Output:**
```
============================================================
String Permutation Generator
============================================================
Input: "uuk"
Allow Duplicates: true
Algorithm: Recursive
============================================================

Recursive Algorithm Results:
Total permutations: 6
Time taken: 1.333 ms

Permutations:
uuk, uku, uuk, uku, kuu, kuu
```

#### Use Iterative Algorithm
```bash
java -cp bin PermutationsCLI -i unknw
```

#### Compare Both Algorithms
```bash
java -cp bin PermutationsCLI -c unknown
```

**Output:**
```
--- PERFORMANCE COMPARISON ---

============================================================
String Permutation Generator
============================================================
Input: "unknown"
Allow Duplicates: false
Algorithm: Both (comparison)
============================================================

--- PERFORMANCE COMPARISON ---

Running Recursive Algorithm...
  Permutations found: 840
  Time: 3.061 ms

Running Iterative Algorithm...
  Permutations found: 840
  Time: 3.129 ms

============================================================
COMPARISON SUMMARY
============================================================
Recursive: 3.061 ms
Iterative: 3.129 ms
Winner: Recursive (1.02x faster)
============================================================
```

#### Combined Options
```bash
# Iterative with duplicates
java -cp bin PermutationsCLI -d -i aab

# Compare both algorithms with duplicates
java -cp bin PermutationsCLI -d -c abcd
```
    
### CLI Options

| Flag | Description |
|------|-------------|
| `-d, --allow-duplicates` | Include duplicate permutations |
| `-i, --iterative` | Use iterative algorithm instead of recursive |
| `-c, --compare` | Compare both algorithms' performance |
| `-h, --help` | Show usage information |

### Performance Benchmarks

| Input | Length | Permutations | Recursive | Iterative | Winner |
|-------|--------|--------------|-----------|-----------|--------|
| abc | 3 | 6 | 0.15 ms | 0.20 ms | Recursive |
| abcd | 4 | 24 | 0.26 ms | 0.10 ms | Iterative (2.6x) |
| abcde | 5 | 120 | 0.90 ms | 0.30 ms | Iterative (3.0x) |
| abcdef | 6 | 720 | 3.71 ms | 0.87 ms | Iterative (4.3x) |
| abcdefg | 7 | 5,040 | 24.50 ms | 5.20 ms | Iterative (4.7x) |

**Recommendation:** Use iterative algorithm for strings of length 4 or more.

### Programmatic Usage

```java
import java.util.List;

// Method 1: Recursive (unique permutations, default)
List<String> perms = PermutationGenerator.generatePermutations("abc");
// Returns: [abc, acb, bac, bca, cba, cab]

// Method 2: Recursive with duplicate control
List<String> unique = PermutationGenerator.generatePermutations("aab", false);
// Returns: [aab, aba, baa] (3 unique)

List<String> withDups = PermutationGenerator.generatePermutations("aab", true);
// Returns: [aab, aba, aab, aba, baa, baa] (6 total)

// Method 3: Iterative (unique permutations, default)
List<String> perms = PermutationGenerator.generatePermutationsIterative("abc");

// Method 4: Iterative with duplicate control
List<String> perms = PermutationGenerator.generatePermutationsIterative("aab", true);
```

---

## Running Tests

Both utilities include comprehensive test suites using JUnit 5.

### Run in VS Code
1. Open the Testing view (beaker icon in sidebar)
2. Click refresh to discover tests
3. Run individual tests or entire test classes
4. View results in the Test Explorer

### Test Coverage

**RecursiveFileSearchCompleteTest.java** - 20 tests:
- Base case validation (null checks, empty directory)
- Single-level directory search
- Multi-level recursive search
- Edge cases (special characters, case sensitivity)
- Case-insensitive search enhancement
- Multiple file search
- Occurrence counting

**PermutationGeneratorTest.java** - 15 tests:
- Recursive algorithm tests (basic, empty, null)
- Iterative algorithm tests (basic, empty, null)
- Duplicate handling (include/exclude)
- Algorithm comparison (both produce same results)
- Edge cases (single char, two chars, 24 permutations)

---

## Algorithm Details

### File Search Algorithm

**Recursive Approach:**
1. **Base Cases:**
   - Directory is null → throw exception
   - Filename is null/empty → throw exception
   - Directory doesn't exist → throw exception
   - Directory is empty → return empty list

2. **Recursive Step:**
   - For each item in directory:
     - If it's a file and name matches → add to results
     - If it's a directory → recursively search and add results

**Time Complexity:** O(n) where n = total number of files and directories  
**Space Complexity:** O(d) where d = maximum directory depth (recursion stack)

### Permutation Generation Algorithms

**Recursive (Swap-Based):**
1. Fix each character at current position
2. Recursively generate permutations for remaining characters
3. Backtrack by swapping back
4. Use HashSet to skip duplicate swaps (when excluding duplicates)

**Time Complexity:** O(n! × n)  
**Space Complexity:** O(n!) for storing results + O(n) recursion stack

**Iterative (Heap's Algorithm):**
1. Use counter array to track swap state
2. Systematically generate permutations by controlled swapping
3. Remove duplicates using HashSet (when excluding duplicates)

**Time Complexity:** O(n! × n)  
**Space Complexity:** O(n!) for storing results

---

## Error Handling

### File Search
- **Invalid directory:** `IllegalArgumentException` with descriptive message
- **Null/empty filename:** `IllegalArgumentException`
- **Permission issues:** Returns empty list (graceful degradation)
- **Invalid arguments:** Shows usage information

### Permutation Generator
- **Null input:** `IllegalArgumentException: Input string cannot be null`
- **No arguments:** Shows usage information with examples
- **Invalid flags:** Shows usage information

---

## Use Cases

### File Search
- Find configuration files across large projects
- Locate duplicate files in directory trees
- Count occurrences of specific filenames
- Build file indexing systems
- Automated backup verification

### Permutation Generator
- Generate test cases for string operations
- Anagram generation and solving
- Combinatorial problem solving
- Password/key generation (for security testing)
- Game development (word games, puzzles)

---

## Requirements

- **Java:** JDK 8 or higher
- **Testing:** JUnit 5 (Jupiter) - built into VS Code Java extension
- **OS:** Cross-platform (Windows, Linux, macOS)

---

## Notes

### File Search
- Search is exact name match (not pattern/regex)
- Symbolic links are followed
- Hidden files are included in search
- Performance is O(n) where n = total files in tree

### Permutation Generator

**⚠️ Memory Limitations:**

| String Length | Permutations | Memory Required | Status |
|---------------|--------------|-----------------|---------|
| 3 chars | 6 | < 1 KB | Safe |
| 7 chars | 5,040 | < 1 MB | Safe |
| 10 chars | 3,628,800 | ~100 MB | Safe |
| 11 chars | 39,916,800 | ~1 GB | Slow |
| 12 chars | 479,001,600 | ~12 GB | Will crash |
| 14 chars | 87,178,291,200 | ~2,000 GB | Will crash |

**Recommendations:**
- **Maximum safe input:** 10 characters
- **For testing:** Use 3-7 character strings
- **For performance comparison:** Use 5-8 character strings
- Use iterative algorithm for better performance on larger inputs
- Memory usage grows factorially: n! × n bytes
