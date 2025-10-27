# Task 1: Recursive File Search - Specifications

## Overview
A Java program that recursively searches for files within a directory structure, supporting multiple file searches, occurrence counting, and flexible search options.

---

## Class: RecursiveFileSearch

### Purpose
Provides recursive file searching capabilities with configurable search options including case sensitivity and multiple file support.

---

## Method Specifications

### 1. `searchFile`

```java
/**
 * Recursively searches for a file in a directory and its subdirectories.
 * 
 * @param directory the directory to search in (must be a valid directory)
 * @param fileName the name of the file to search for (must not be null or empty)
 * @param caseSensitive whether the search should be case-sensitive
 * @return a List of Path objects representing all matching files found
 * @throws IllegalArgumentException if directory is null, fileName is null or empty,
 *                                  or directory does not exist or is not a directory
 */
public static List<Path> searchFile(File directory, String fileName, boolean caseSensitive)
```

**Base Cases:**
- If directory is null → throw IllegalArgumentException
- If fileName is null or empty → throw IllegalArgumentException
- If directory doesn't exist or isn't a directory → throw IllegalArgumentException
- If directory is empty (no files/subdirectories) → return empty list
- If directory is not readable (permissions issue) → return empty list

**Recursive Step:**
- For each item in the directory:
  - If it's a file and matches the fileName (considering case sensitivity) → add to results
  - If it's a directory → recursively search in that directory and add results

**Example:**
```
searchFile(new File("C:/Users/Docs"), "report.txt", true)
→ Returns: [C:/Users/Docs/report.txt, C:/Users/Docs/2023/report.txt]
```

---

### 2. `searchMultipleFiles`

```java
/**
 * Recursively searches for multiple files in a directory and its subdirectories.
 * 
 * @param directory the directory to search in (must be a valid directory)
 * @param fileNames a list of file names to search for (must not be null or empty)
 * @param caseSensitive whether the search should be case-sensitive
 * @return a Map where keys are file names and values are Lists of Paths where found
 * @throws IllegalArgumentException if directory is null, fileNames is null or empty,
 *                                  or directory does not exist or is not a directory
 */
public static Map<String, List<Path>> searchMultipleFiles(File directory, 
                                                           List<String> fileNames, 
                                                           boolean caseSensitive)
```

**Base Cases:**
- If directory is null → throw IllegalArgumentException
- If fileNames is null or empty → throw IllegalArgumentException
- If directory doesn't exist or isn't a directory → throw IllegalArgumentException
- If directory is empty → return map with empty lists for each fileName

**Recursive Step:**
- For each fileName in fileNames:
  - Call searchFile to find all occurrences
  - Store results in map with fileName as key

**Example:**
```
searchMultipleFiles(new File("C:/Docs"), Arrays.asList("file1.txt", "file2.pdf"), false)
→ Returns: {"file1.txt": [path1, path2], "file2.pdf": [path3]}
```

---

### 3. `countFileOccurrences`

```java
/**
 * Counts the number of times a specific file appears in a directory and subdirectories.
 * 
 * @param directory the directory to search in (must be a valid directory)
 * @param fileName the name of the file to count (must not be null or empty)
 * @param caseSensitive whether the search should be case-sensitive
 * @return the count of matching files found
 * @throws IllegalArgumentException if directory is null, fileName is null or empty,
 *                                  or directory does not exist or is not a directory
 */
public static int countFileOccurrences(File directory, String fileName, boolean caseSensitive)
```

**Base Cases:**
- If directory is null → throw IllegalArgumentException
- If fileName is null or empty → throw IllegalArgumentException
- If directory doesn't exist or isn't a directory → throw IllegalArgumentException
- If directory is empty → return 0
- If directory is not readable → return 0

**Recursive Step:**
- Initialize count = 0
- For each item in the directory:
  - If it's a file and matches fileName → increment count
  - If it's a directory → count += recursive call on that directory
- Return total count

**Example:**
```
countFileOccurrences(new File("C:/Projects"), "README.md", true)
→ Returns: 5 (if README.md appears 5 times in the directory tree)
```

---

### 4. `matchesFileName` (Helper Method)

```java
/**
 * Helper method to check if a file name matches the search criteria.
 * 
 * @param actualFileName the actual file name to check
 * @param searchFileName the file name being searched for
 * @param caseSensitive whether the comparison should be case-sensitive
 * @return true if the file names match according to the case sensitivity setting
 */
private static boolean matchesFileName(String actualFileName, 
                                       String searchFileName, 
                                       boolean caseSensitive)
```

**Base Cases:**
- If either parameter is null → return false

**Logic:**
- If caseSensitive: return actualFileName.equals(searchFileName)
- If not caseSensitive: return actualFileName.equalsIgnoreCase(searchFileName)

---

### 5. `main` (Command Line Interface)

```java
/**
 * Main method to run the file search from command line.
 * 
 * Usage: 
 *   Single file: java RecursiveFileSearch <directory> <fileName> [case-sensitive]
 *   Multiple files: java RecursiveFileSearch <directory> -m <file1> <file2> ... [case-sensitive]
 *   Count occurrences: java RecursiveFileSearch <directory> -c <fileName> [case-sensitive]
 * 
 * @param args command line arguments as described above
 */
public static void main(String[] args)
```

**Arguments:**
- `args[0]`: directory path (required)
- `args[1]`: fileName OR "-m" for multiple files OR "-c" for count mode
- `args[2+]`: additional file names (if -m mode) OR case-sensitive flag
- Last arg can be "true" or "false" for case sensitivity (default: true)

**Error Handling:**
- Invalid number of arguments → print usage message
- Directory doesn't exist → print error message
- IOException during search → print error with stack trace

---

## Design Decisions

### 1. **Return Type Choices**
- `searchFile` returns `List<Path>` to allow finding all occurrences
- `searchMultipleFiles` returns `Map<String, List<Path>>` for organized results
- `countFileOccurrences` returns `int` for simple counting

### 2. **Case Sensitivity**
- Implemented as a boolean parameter for flexibility
- Allows users to choose based on their file system (Windows vs Linux)

### 3. **Error Handling Strategy**
- Use IllegalArgumentException for invalid inputs (fail-fast)
- Return empty results for directories with permission issues (graceful degradation)
- Provide clear error messages in main method for user guidance

### 4. **Recursion Strategy**
- Use directory traversal with recursive descent
- Each directory level processes its own files and delegates subdirectories
- Natural fit for tree-like directory structure

### 5. **Performance Considerations**
- Use Files.list() for efficient directory traversal
- Avoid unnecessary object creation in recursive calls
- Short-circuit evaluation in matching logic

---

## Testing Requirements

### Test Coverage Areas

1. **Base Cases:**
   - Empty directories
   - Null/invalid inputs
   - Non-existent directories
   - Files (not directories) as input

2. **Recursive Cases:**
   - Single-level directories
   - Multi-level nested directories
   - Files at different nesting levels
   - Large directory trees

3. **Feature Tests:**
   - Case-sensitive vs case-insensitive matching
   - Multiple file search
   - Occurrence counting
   - Files with same name in different directories

4. **Edge Cases:**
   - Very deep directory nesting
   - Circular symbolic links (if supported)
   - Special characters in file names
   - Files with no extensions
   - Hidden files

5. **Error Cases:**
   - Null directory
   - Null/empty file name
   - Non-existent directory path
   - File passed instead of directory
   - Permission denied scenarios

---

## Time Complexity Analysis

- **Best Case:** O(1) - File found in first position of root directory
- **Average Case:** O(n) - where n is total number of files and directories
- **Worst Case:** O(n) - Must traverse entire directory tree
- **Space Complexity:** O(d) - where d is maximum depth of directory tree (recursion stack)

---

## Example Usage

```java
// Case-sensitive single file search
List<Path> results = RecursiveFileSearch.searchFile(
    new File("C:/Projects"), 
    "Main.java", 
    true
);

// Case-insensitive multiple file search
Map<String, List<Path>> multiResults = RecursiveFileSearch.searchMultipleFiles(
    new File("C:/Documents"),
    Arrays.asList("report.pdf", "summary.docx", "data.xlsx"),
    false
);

// Count occurrences
int count = RecursiveFileSearch.countFileOccurrences(
    new File("C:/Workspace"),
    "README.md",
    true
);
```
