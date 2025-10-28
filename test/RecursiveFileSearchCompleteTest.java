import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ESSENTIAL TEST SUITE for RecursiveFileSearch class.
 * 
 * This is the ESSENTIAL test file covering critical functionality:
 * - Part 1: Base Case Tests (4 tests)
 * - Part 2: Single-Level Directory Tests (2 tests)
 * - Part 3: Multi-Level/Recursive Tests (3 tests)
 * - Part 4: Edge Cases (2 tests)
 * - Part 5: Enhancement 1 - Case-Insensitive (2 tests)
 * - Part 6: Enhancement 2 - Multiple Files (4 tests)
 * - Part 7: Enhancement 3 - Count Occurrences (3 tests)
 * 
 * TOTAL: 20 Essential Test Cases
 * 
 * Tests directly call functions with parameters (not through CLI).
 * All tests use JUnit 5 (Jupiter) configured in VS Code.
 * 
 * @author Abdul Hadi ( 464594 )
 * @version 2.1 (Essential Test Suite)
 */
@DisplayName("Essential Recursive File Search Test Suite")
public class RecursiveFileSearchCompleteTest {

    @TempDir
    Path tempDir;

    private File testDirectory;

    @BeforeEach
    @DisplayName("Set up test environment")
    void setUp() throws IOException {
        testDirectory = tempDir.toFile();
    }

    // ==================== PART 1: BASE CASE TESTS (4 tests) ====================

    @Test
    @DisplayName("Test 1: Null directory should throw IllegalArgumentException")
    void testNullDirectory() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(null, "test.txt");
        });

        assertTrue(exception.getMessage().contains("Directory cannot be null"));
    }

    @Test
    @DisplayName("Test 2: Null file name should throw IllegalArgumentException")
    void testNullFileName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(testDirectory, null);
        });

        assertTrue(exception.getMessage().contains("File name cannot be null or empty"));
    }

    @Test
    @DisplayName("Test 3: Non-existent directory should throw IllegalArgumentException")
    void testNonExistentDirectory() {
        File nonExistent = new File(testDirectory, "does-not-exist");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(nonExistent, "test.txt");
        });

        assertTrue(exception.getMessage().contains("Directory does not exist"));
    }

    @Test
    @DisplayName("Test 4: Empty directory should return empty list")
    void testEmptyDirectory() {
        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "nonexistent.txt");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    // ==================== PART 2: SINGLE-LEVEL DIRECTORY TESTS (2 tests)
    // ====================

    @Test
    @DisplayName("Test 5: Find single file in root directory")
    void testFindSingleFileInRoot() throws IOException {
        File targetFile = new File(testDirectory, "target.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 6: File not found in root directory")
    void testFileNotFoundInRoot() throws IOException {
        new File(testDirectory, "file1.txt").createNewFile();
        new File(testDirectory, "file2.txt").createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "missing.txt");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    // ==================== PART 3: MULTI-LEVEL/RECURSIVE TESTS (3 tests)
    // ====================

    @Test
    @DisplayName("Test 7: Find file in subdirectory")
    void testFindFileInSubdirectory() throws IOException {
        File subDir = new File(testDirectory, "subdir");
        subDir.mkdir();

        File targetFile = new File(subDir, "target.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 8: Find file in deeply nested directories")
    void testFindFileInDeeplyNestedDirectories() throws IOException {
        File level1 = new File(testDirectory, "level1");
        File level2 = new File(level1, "level2");
        File level3 = new File(level2, "level3");

        level1.mkdir();
        level2.mkdir();
        level3.mkdir();

        File targetFile = new File(level3, "deep.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "deep.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 9: Find multiple occurrences at different levels")
    void testMultipleOccurrencesAtDifferentLevels() throws IOException {
        File rootFile = new File(testDirectory, "common.txt");
        rootFile.createNewFile();

        File subDir1 = new File(testDirectory, "dir1");
        File subDir2 = new File(testDirectory, "dir2");
        File subDir3 = new File(subDir1, "nested");

        subDir1.mkdir();
        subDir2.mkdir();
        subDir3.mkdir();

        File file1 = new File(subDir1, "common.txt");
        File file2 = new File(subDir2, "common.txt");
        File file3 = new File(subDir3, "common.txt");

        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "common.txt");

        assertEquals(4, results.size());
    }

    // ==================== PART 4: EDGE CASES (2 tests) ====================

    @Test
    @DisplayName("Test 10: Search for file with special characters in name")
    void testFileWithSpecialCharacters() throws IOException {
        File specialFile = new File(testDirectory, "file-name_2024.txt");
        specialFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "file-name_2024.txt");

        assertEquals(1, results.size());
        assertEquals(specialFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 11: Case sensitivity verification")
    void testCaseSensitivityVerification() throws IOException {
        File lowerCase = new File(testDirectory, "readme.txt");
        File upperCase = new File(testDirectory, "README.txt");
        File mixedCase = new File(testDirectory, "ReadMe.txt");

        lowerCase.createNewFile();
        upperCase.createNewFile();
        mixedCase.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "readme.txt");

        assertEquals(1, results.size());
        assertEquals(lowerCase.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    // ==================== PART 5: ENHANCEMENT 1 - CASE-INSENSITIVE (2 tests)
    // ====================

    @Test
    @DisplayName("Enhancement 1.1: Case-sensitive search (explicit parameter)")
    void testCaseSensitiveSearch() throws IOException {
        File lowerCase = new File(testDirectory, "readme.txt");
        File upperCase = new File(testDirectory, "README.txt");
        File mixedCase = new File(testDirectory, "ReadMe.txt");

        lowerCase.createNewFile();
        upperCase.createNewFile();
        mixedCase.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "readme.txt", true);

        assertEquals(1, results.size(), "Should find only exact match");
        assertEquals(lowerCase.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Enhancement 1.2: Case-insensitive search finds all variations")
    void testCaseInsensitiveSearch() throws IOException {
        // Create files in different directories to avoid Windows case-insensitive
        // filesystem issue
        File dir1 = new File(testDirectory, "dir1");
        File dir2 = new File(testDirectory, "dir2");
        File dir3 = new File(testDirectory, "dir3");
        dir1.mkdir();
        dir2.mkdir();
        dir3.mkdir();

        File lowerCase = new File(dir1, "readme.txt");
        File upperCase = new File(dir2, "README.txt");
        File mixedCase = new File(dir3, "ReadMe.txt");

        lowerCase.createNewFile();
        upperCase.createNewFile();
        mixedCase.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "readme.txt", false);

        assertEquals(3, results.size(), "Should find all case variations");
    }

    // ==================== PART 6: ENHANCEMENT 2 - MULTIPLE FILES (4 tests)

    @Test
    @DisplayName("Enhancement 2.1: Search for multiple files successfully")
    void testSearchMultipleFiles() throws IOException {
        File file1 = new File(testDirectory, "file1.txt");
        File file2 = new File(testDirectory, "file2.pdf");
        File file3 = new File(testDirectory, "file3.doc");

        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        List<String> searchFiles = Arrays.asList("file1.txt", "file2.pdf", "file3.doc");
        Map<String, List<File>> results = RecursiveFileSearch.searchMultipleFiles(
                testDirectory, searchFiles, true);

        assertEquals(3, results.size(), "Should have results for 3 files");
        assertEquals(1, results.get("file1.txt").size());
        assertEquals(1, results.get("file2.pdf").size());
        assertEquals(1, results.get("file3.doc").size());
    }

    @Test
    @DisplayName("Enhancement 2.2: Multiple files - some found, some not found")
    void testSearchMultipleFilesSomeNotFound() throws IOException {
        File file1 = new File(testDirectory, "found1.txt");
        File file2 = new File(testDirectory, "found2.txt");

        file1.createNewFile();
        file2.createNewFile();

        List<String> searchFiles = Arrays.asList("found1.txt", "notfound.txt", "found2.txt");
        Map<String, List<File>> results = RecursiveFileSearch.searchMultipleFiles(
                testDirectory, searchFiles, true);

        assertEquals(3, results.size(), "Should have entries for all 3 search files");
        assertEquals(1, results.get("found1.txt").size());
        assertEquals(0, results.get("notfound.txt").size(), "Not found should have empty list");
        assertEquals(1, results.get("found2.txt").size());
    }

    @Test
    @DisplayName("Enhancement 2.3: Multiple files in nested directories")
    void testSearchMultipleFilesInNestedDirs() throws IOException {
        File subDir1 = new File(testDirectory, "dir1");
        File subDir2 = new File(testDirectory, "dir2");
        subDir1.mkdir();
        subDir2.mkdir();

        File rootFile = new File(testDirectory, "file1.txt");
        File dir1File = new File(subDir1, "file2.txt");
        File dir2File = new File(subDir2, "file3.txt");

        rootFile.createNewFile();
        dir1File.createNewFile();
        dir2File.createNewFile();

        List<String> searchFiles = Arrays.asList("file1.txt", "file2.txt", "file3.txt");
        Map<String, List<File>> results = RecursiveFileSearch.searchMultipleFiles(
                testDirectory, searchFiles, true);

        assertEquals(3, results.size());
        assertEquals(1, results.get("file1.txt").size());
        assertEquals(1, results.get("file2.txt").size());
        assertEquals(1, results.get("file3.txt").size());
    }

    @Test
    @DisplayName("Enhancement 2.4: Multiple files - null list throws exception")
    void testSearchMultipleFilesNullList() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchMultipleFiles(testDirectory, null, true);
        });

        assertTrue(exception.getMessage().contains("File names list cannot be null or empty"));
    }

    // ==================== PART 7: ENHANCEMENT 3 - COUNT OCCURRENCES (3 tests)

    @Test
    @DisplayName("Enhancement 3.1: Count multiple occurrences across directories")
    void testCountMultipleOccurrences() throws IOException {
        File subDir1 = new File(testDirectory, "dir1");
        File subDir2 = new File(testDirectory, "dir2");
        File subDir3 = new File(testDirectory, "dir3");

        subDir1.mkdir();
        subDir2.mkdir();
        subDir3.mkdir();

        new File(testDirectory, "file.txt").createNewFile();
        new File(subDir1, "file.txt").createNewFile();
        new File(subDir2, "file.txt").createNewFile();
        new File(subDir3, "file.txt").createNewFile();

        int count = RecursiveFileSearch.countFileOccurrences(testDirectory, "file.txt", true);

        assertEquals(4, count, "Should count 4 occurrences");
    }

    @Test
    @DisplayName("Enhancement 3.2: Count with case-sensitive")
    void testCountCaseSensitive() throws IOException {
        File lowerCase = new File(testDirectory, "file.txt");
        File upperCase = new File(testDirectory, "FILE.txt");
        File mixedCase = new File(testDirectory, "File.txt");

        lowerCase.createNewFile();
        upperCase.createNewFile();
        mixedCase.createNewFile();

        int count = RecursiveFileSearch.countFileOccurrences(testDirectory, "file.txt", true);

        assertEquals(1, count, "Case-sensitive should count only exact match");
    }

    @Test
    @DisplayName("Enhancement 3.3: Count with case-insensitive")
    void testCountCaseInsensitive() throws IOException {
        // Create files in different directories to avoid Windows case-insensitive
        // filesystem issue
        File dir1 = new File(testDirectory, "folder1");
        File dir2 = new File(testDirectory, "folder2");
        File dir3 = new File(testDirectory, "folder3");
        dir1.mkdir();
        dir2.mkdir();
        dir3.mkdir();

        File lowerCase = new File(dir1, "file.txt");
        File upperCase = new File(dir2, "FILE.txt");
        File mixedCase = new File(dir3, "File.txt");

        lowerCase.createNewFile();
        upperCase.createNewFile();
        mixedCase.createNewFile();

        int count = RecursiveFileSearch.countFileOccurrences(testDirectory, "file.txt", false);

        assertEquals(3, count, "Case-insensitive should count all variations");
    }
}
