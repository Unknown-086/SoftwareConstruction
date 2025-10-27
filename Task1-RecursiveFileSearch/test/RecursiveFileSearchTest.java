import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Unit tests for RecursiveFileSearch class.
 * 
 * Tests cover:
 * - Base cases (null inputs, invalid directories, empty directories)
 * - Recursive cases (single-level, multi-level directories)
 * - Edge cases (file not found, multiple occurrences)
 * - Error handling (invalid inputs, non-existent directories)
 * 
 * @author Lab Eight
 * @version 1.0
 */
@DisplayName("Recursive File Search Tests")
public class RecursiveFileSearchTest {

    @TempDir
    Path tempDir;

    // Test files and directories will be created here
    private File testDirectory;

    @BeforeEach
    @DisplayName("Set up test environment")
    void setUp() throws IOException {
        testDirectory = tempDir.toFile();
    }

    // ==================== Base Case Tests ====================

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
    @DisplayName("Test 3: Empty file name should throw IllegalArgumentException")
    void testEmptyFileName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(testDirectory, "");
        });

        assertTrue(exception.getMessage().contains("File name cannot be null or empty"));
    }

    @Test
    @DisplayName("Test 4: Whitespace-only file name should throw IllegalArgumentException")
    void testWhitespaceFileName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(testDirectory, "   ");
        });

        assertTrue(exception.getMessage().contains("File name cannot be null or empty"));
    }

    @Test
    @DisplayName("Test 5: Non-existent directory should throw IllegalArgumentException")
    void testNonExistentDirectory() {
        File nonExistent = new File(testDirectory, "does-not-exist");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(nonExistent, "test.txt");
        });

        assertTrue(exception.getMessage().contains("Directory does not exist"));
    }

    @Test
    @DisplayName("Test 6: File path instead of directory should throw IllegalArgumentException")
    void testFileInsteadOfDirectory() throws IOException {
        File testFile = new File(testDirectory, "notadirectory.txt");
        testFile.createNewFile();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RecursiveFileSearch.searchFile(testFile, "test.txt");
        });

        assertTrue(exception.getMessage().contains("Path is not a directory"));
    }

    @Test
    @DisplayName("Test 7: Empty directory should return empty list")
    void testEmptyDirectory() {
        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "nonexistent.txt");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    // ==================== Single-Level Directory Tests ====================

    @Test
    @DisplayName("Test 8: Find single file in root directory")
    void testFindSingleFileInRoot() throws IOException {
        // Create a test file
        File targetFile = new File(testDirectory, "target.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 9: File not found in root directory")
    void testFileNotFoundInRoot() throws IOException {
        // Create some files but not the one we're looking for
        new File(testDirectory, "file1.txt").createNewFile();
        new File(testDirectory, "file2.txt").createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "missing.txt");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Test 10: Multiple files with same name in root directory")
    void testMultipleSameNameFilesInRoot() throws IOException {
        // This test verifies case sensitivity - should only match exact name
        File target1 = new File(testDirectory, "test.txt");
        File different = new File(testDirectory, "Test.txt"); // Different case
        File target2 = new File(testDirectory, "other.txt");

        target1.createNewFile();
        different.createNewFile();
        target2.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "test.txt");

        assertEquals(1, results.size());
        assertEquals(target1.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    // ==================== Multi-Level Directory Tests (Recursive)
    // ====================

    @Test
    @DisplayName("Test 11: Find file in subdirectory")
    void testFindFileInSubdirectory() throws IOException {
        // Create subdirectory
        File subDir = new File(testDirectory, "subdir");
        subDir.mkdir();

        // Create target file in subdirectory
        File targetFile = new File(subDir, "target.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 12: Find file in deeply nested directories")
    void testFindFileInDeeplyNestedDirectories() throws IOException {
        // Create deep directory structure: root/level1/level2/level3/
        File level1 = new File(testDirectory, "level1");
        File level2 = new File(level1, "level2");
        File level3 = new File(level2, "level3");

        level1.mkdir();
        level2.mkdir();
        level3.mkdir();

        // Create target file at deepest level
        File targetFile = new File(level3, "deep.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "deep.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 13: Find multiple occurrences at different levels")
    void testMultipleOccurrencesAtDifferentLevels() throws IOException {
        // Create file in root
        File rootFile = new File(testDirectory, "common.txt");
        rootFile.createNewFile();

        // Create subdirectories
        File subDir1 = new File(testDirectory, "dir1");
        File subDir2 = new File(testDirectory, "dir2");
        File subDir3 = new File(subDir1, "nested");

        subDir1.mkdir();
        subDir2.mkdir();
        subDir3.mkdir();

        // Create same-named files in different locations
        File file1 = new File(subDir1, "common.txt");
        File file2 = new File(subDir2, "common.txt");
        File file3 = new File(subDir3, "common.txt");

        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "common.txt");

        assertEquals(4, results.size()); // Found in 4 different locations
    }

    @Test
    @DisplayName("Test 14: Search in directory with mixed files and subdirectories")
    void testMixedFilesAndSubdirectories() throws IOException {
        // Create files in root
        new File(testDirectory, "root1.txt").createNewFile();
        new File(testDirectory, "root2.txt").createNewFile();
        File targetRoot = new File(testDirectory, "target.txt");
        targetRoot.createNewFile();

        // Create subdirectories with files
        File subDir1 = new File(testDirectory, "subdir1");
        File subDir2 = new File(testDirectory, "subdir2");
        subDir1.mkdir();
        subDir2.mkdir();

        new File(subDir1, "sub1.txt").createNewFile();
        File targetSub = new File(subDir2, "target.txt");
        targetSub.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");

        assertEquals(2, results.size());
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Test 15: Search for file with special characters in name")
    void testFileWithSpecialCharacters() throws IOException {
        // Create file with special characters (but valid in filename)
        File specialFile = new File(testDirectory, "file-name_2024.txt");
        specialFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "file-name_2024.txt");

        assertEquals(1, results.size());
        assertEquals(specialFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 16: Search for file without extension")
    void testFileWithoutExtension() throws IOException {
        File noExtFile = new File(testDirectory, "README");
        noExtFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "README");

        assertEquals(1, results.size());
        assertEquals(noExtFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 17: Case sensitivity - different case should not match")
    void testCaseSensitivity() throws IOException {
        File lowerCase = new File(testDirectory, "readme.txt");
        File upperCase = new File(testDirectory, "README.txt");
        File mixedCase = new File(testDirectory, "ReadMe.txt");

        lowerCase.createNewFile();
        upperCase.createNewFile();
        mixedCase.createNewFile();

        // Search for lowercase version
        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "readme.txt");

        // Should only find exact match
        assertEquals(1, results.size());
        assertEquals(lowerCase.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 18: Empty subdirectories should not cause issues")
    void testEmptySubdirectories() throws IOException {
        // Create empty subdirectories
        File emptyDir1 = new File(testDirectory, "empty1");
        File emptyDir2 = new File(testDirectory, "empty2");
        File nestedEmpty = new File(emptyDir1, "nested-empty");

        emptyDir1.mkdir();
        emptyDir2.mkdir();
        nestedEmpty.mkdir();

        // Create target file in root
        File targetFile = new File(testDirectory, "target.txt");
        targetFile.createNewFile();

        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());
    }

    @Test
    @DisplayName("Test 19: Large directory structure performance")
    void testLargeDirectoryStructure() throws IOException {
        // Create a moderately large directory structure
        for (int i = 0; i < 10; i++) {
            File dir = new File(testDirectory, "dir" + i);
            dir.mkdir();

            // Create some files in each directory
            for (int j = 0; j < 5; j++) {
                new File(dir, "file" + j + ".txt").createNewFile();
            }
        }

        // Create target file in one of the directories
        File targetFile = new File(new File(testDirectory, "dir5"), "target.txt");
        targetFile.createNewFile();

        long startTime = System.currentTimeMillis();
        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "target.txt");
        long endTime = System.currentTimeMillis();

        assertEquals(1, results.size());
        assertEquals(targetFile.getAbsolutePath(), results.get(0).getAbsolutePath());

        // Should complete in reasonable time (< 1 second)
        assertTrue((endTime - startTime) < 1000);
    }

    @Test
    @DisplayName("Test 20: Search returns list not null even when nothing found")
    void testReturnsListNotNull() {
        List<File> results = RecursiveFileSearch.searchFile(testDirectory, "nonexistent.txt");

        assertNotNull(results);
        assertTrue(results instanceof List);
    }
}
