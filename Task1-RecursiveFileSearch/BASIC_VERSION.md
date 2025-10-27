# Task 1: Recursive File Search - Basic Version

## ✅ Implementation Complete

### What's Implemented

This is the **basic version** of the Recursive File Search program that meets all the core requirements:

1. ✅ Takes two command-line arguments: directory path and file name
2. ✅ Implements recursive function to search directories and subdirectories
3. ✅ Displays messages with full path when file is found
4. ✅ Shows "not found" message when file doesn't exist
5. ✅ Follows good coding practices (meaningful names, comments, modular code)
6. ✅ Implements comprehensive error handling
7. ✅ Uses efficient data structures (ArrayList) and recursive algorithm
8. ✅ Includes full specifications and 20 unit tests

---

## 📁 Files Created

```
Task1-RecursiveFileSearch/
├── src/
│   └── RecursiveFileSearch.java          (Main implementation)
├── test/
│   └── RecursiveFileSearchTest.java      (20 JUnit 5 tests)
├── SPECIFICATIONS.md                      (Detailed specifications)
└── BASIC_VERSION.md                       (This file)
```

---

## 🔧 How to Compile and Run

### Compile the Program
```powershell
cd "d:\VS_Code\Software Construction\LabEight\Task1-RecursiveFileSearch"
javac src\RecursiveFileSearch.java
```

### Run the Program

**Basic Usage:**
```powershell
java -cp src RecursiveFileSearch <directory_path> <file_name>
```

**Examples:**
```powershell
# Search for a Java file in the current directory
java -cp src RecursiveFileSearch "." "Main.java"

# Search for a text file in Documents folder
java -cp src RecursiveFileSearch "C:\Users\YourName\Documents" "report.txt"

# Search in parent directory structure
java -cp src RecursiveFileSearch ".." "README.md"

# Search in LabEight folder
java -cp src RecursiveFileSearch "d:\VS_Code\Software Construction\LabEight" "RecursiveFileSearch.java"
```

### Run Tests (VS Code)

1. Open VS Code's **Test Explorer** (Testing icon in sidebar)
2. Click refresh to discover tests
3. Run all tests or individual test methods
4. View results in the Test Explorer

---

## 🧪 Test Coverage (20 Tests)

### Base Case Tests (7 tests)
- ✅ Test 1: Null directory throws exception
- ✅ Test 2: Null file name throws exception
- ✅ Test 3: Empty file name throws exception
- ✅ Test 4: Whitespace file name throws exception
- ✅ Test 5: Non-existent directory throws exception
- ✅ Test 6: File path instead of directory throws exception
- ✅ Test 7: Empty directory returns empty list

### Single-Level Directory Tests (3 tests)
- ✅ Test 8: Find single file in root directory
- ✅ Test 9: File not found in root directory
- ✅ Test 10: Case sensitivity (exact match only)

### Recursive/Multi-Level Tests (4 tests)
- ✅ Test 11: Find file in subdirectory
- ✅ Test 12: Find file in deeply nested directories
- ✅ Test 13: Multiple occurrences at different levels
- ✅ Test 14: Mixed files and subdirectories

### Edge Case Tests (6 tests)
- ✅ Test 15: Special characters in filename
- ✅ Test 16: File without extension
- ✅ Test 17: Case sensitivity verification
- ✅ Test 18: Empty subdirectories
- ✅ Test 19: Large directory structure performance
- ✅ Test 20: Returns list (not null) when nothing found

---

## 🎯 Key Features (Basic Version)

### 1. Recursive Algorithm
```
Base Cases:
- Null/invalid inputs → throw exception
- Empty directory → return empty list
- Unreadable directory → return empty list

Recursive Step:
- For each item in directory:
  - If file matches name → add to results
  - If subdirectory → recursively search
```

### 2. Error Handling
- Validates all inputs before processing
- Handles SecurityException for permission issues
- Graceful degradation for unreadable directories
- Clear error messages for users

### 3. User-Friendly Output
- Formatted results with full file paths
- Shows number of occurrences found
- Displays search time
- Clear success/failure messages
- Usage instructions for incorrect arguments

### 4. Good Coding Practices
- **Meaningful variable names**: `foundFiles`, `targetFile`, `searchFileName`
- **Comprehensive comments**: Javadoc for all public methods
- **Modular design**: Separate methods for search, display, validation
- **Clean code**: Proper spacing, indentation, logical organization

---

## 📊 Complexity Analysis

- **Time Complexity**: O(n) where n = total files + directories
- **Space Complexity**: O(d) where d = maximum directory depth (recursion stack)
- **Best Case**: O(1) - file found immediately in root
- **Worst Case**: O(n) - file not found, must search entire tree

---

## 🔍 Example Output

### Successful Search
```
============================================================
Recursive File Search
============================================================
Directory: d:\VS_Code\Software Construction\LabEight
File Name: RecursiveFileSearch.java
============================================================

Searching...

============================================================
Search Results for: "RecursiveFileSearch.java"
============================================================

✓ Found 1 occurrence(s) of "RecursiveFileSearch.java":

1. d:\VS_Code\Software Construction\LabEight\Task1-RecursiveFileSearch\src\RecursiveFileSearch.java

============================================================
Search completed in 15 ms
```

### File Not Found
```
============================================================
Search Results for: "missing.txt"
============================================================

❌ File not found: "missing.txt"
The file was not found in the specified directory or its subdirectories.

============================================================
Search completed in 8 ms
```

### Multiple Occurrences
```
============================================================
Search Results for: "README.md"
============================================================

✓ Found 3 occurrence(s) of "README.md":

1. d:\VS_Code\Software Construction\LabEight\README.md
2. d:\VS_Code\Software Construction\LabEight\Task1-RecursiveFileSearch\README.md
3. d:\VS_Code\Software Construction\LabEight\Task2-StringPermutations\README.md

============================================================
```

---

## ⚠️ Important Notes

### Current Version (Basic)
- ✅ Searches for **one file** at a time
- ✅ **Case-sensitive** matching only (exact match required)
- ✅ Returns **all occurrences** found

### Coming in Enhanced Version
- 🔜 Multiple file search in single run
- 🔜 Count mode (just count, don't list all)
- 🔜 Case-insensitive option

---

## 🧪 How to Test

### Manual Testing Examples

**Test 1: Search in current directory**
```powershell
java -cp src RecursiveFileSearch "." "RecursiveFileSearch.java"
```

**Test 2: Search in parent directory**
```powershell
java -cp src RecursiveFileSearch ".." "README.md"
```

**Test 3: File not found**
```powershell
java -cp src RecursiveFileSearch "." "ThisFileDoesNotExist.txt"
```

**Test 4: Invalid directory**
```powershell
java -cp src RecursiveFileSearch "C:\DoesNotExist" "file.txt"
```

**Test 5: Wrong number of arguments**
```powershell
java -cp src RecursiveFileSearch "."
```

---

## ✨ Code Quality Highlights

1. **Specifications Written First**: Detailed specs in SPECIFICATIONS.md
2. **Test-Driven Approach**: 20 comprehensive JUnit tests
3. **Defensive Programming**: Validates all inputs
4. **Error Recovery**: Graceful handling of edge cases
5. **Documentation**: Javadoc comments throughout
6. **User Experience**: Clear, formatted output

---

## 🚀 Next Steps

Once you're satisfied with the basic version, I'll implement the **three mandatory enhancements**:

1. **Multiple file search** - Search for several files in one run
2. **Occurrence counter** - Count mode that just returns the count
3. **Case sensitivity option** - Toggle between case-sensitive/insensitive

Ready to proceed with enhancements? 🎯
