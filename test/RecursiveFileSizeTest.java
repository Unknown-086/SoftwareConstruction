import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for RecursiveFileSize (Task 2)
 * Tests recursive directory size calculation with various scenarios.
 */
public class RecursiveFileSizeTest {

    private File testDir;
    private File subDir1;
    private File subDir2;
    private File nestedDir;

    @Before
    public void setUp() throws IOException {
        // Create a temporary test directory structure
        testDir = new File("test_directory_" + System.currentTimeMillis());
        testDir.mkdir();

        subDir1 = new File(testDir, "subdir1");
        subDir1.mkdir();

        subDir2 = new File(testDir, "subdir2");
        subDir2.mkdir();

        nestedDir = new File(subDir1, "nested");
        nestedDir.mkdir();

        // Create test files with known sizes
        createFile(testDir, "file1.txt", 100); // 100 bytes
        createFile(testDir, "file2.log", 50); // 50 bytes
        createFile(testDir, "file3.tmp", 75); // 75 bytes
        createFile(subDir1, "file4.java", 200); // 200 bytes
        createFile(subDir1, "file5.class", 150); // 150 bytes
        createFile(subDir2, "file6.txt", 300); // 300 bytes
        createFile(nestedDir, "file7.txt", 250); // 250 bytes
        createFile(nestedDir, "file8.log", 100); // 100 bytes
    }

    @After
    public void tearDown() {
        // Clean up test directory
        deleteDirectory(testDir);
    }

    // ========== Helper Methods ==========

    private void createFile(File dir, String name, int size) throws IOException {
        File file = new File(dir, name);
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < size; i++) {
                writer.write('A');
            }
        }
    }

    private void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    // ========== 8 Comprehensive Tests ==========

    @Test
    public void testBasicDirectorySize() {
        // Total: 100 + 50 + 75 + 200 + 150 + 300 + 250 + 100 = 1225 bytes
        long size = RecursiveFileSize.getDirectorySize(testDir);
        assertEquals("Total size should be 1225 bytes", 1225, size);

        // Test nested directory
        long nestedSize = RecursiveFileSize.getDirectorySize(nestedDir);
        assertEquals("Nested directory size", 350, nestedSize);

        // Test subdirectory
        long subDir1Size = RecursiveFileSize.getDirectorySize(subDir1);
        assertEquals("Subdirectory 1 size", 700, subDir1Size);
    }

    @Test
    public void testFileTypeExclusion() {
        Set<String> exclusions = new HashSet<>();

        // Exclude .tmp files
        exclusions.add(".tmp");
        long sizeWithoutTmp = RecursiveFileSize.getDirectorySize(testDir, exclusions);
        assertEquals("Size excluding .tmp files", 1150, sizeWithoutTmp);

        // Exclude .log files
        exclusions.clear();
        exclusions.add(".log");
        long sizeWithoutLog = RecursiveFileSize.getDirectorySize(testDir, exclusions);
        assertEquals("Size excluding .log files", 1075, sizeWithoutLog);

        // Exclude multiple types
        exclusions.add(".tmp");
        exclusions.add(".class");
        long sizeMultiple = RecursiveFileSize.getDirectorySize(testDir, exclusions);
        assertEquals("Size excluding multiple types", 850, sizeMultiple);
    }

    @Test
    public void testMutualRecursion() {
        RecursiveFileSize.TraversalStats stats = RecursiveFileSize.getDirectorySizeMutualRecursion(testDir, null);

        assertEquals("Total size via mutual recursion", 1225, stats.totalSize);
        assertEquals("File count", 8, stats.fileCount);
        assertEquals("Directory count", 4, stats.directoryCount);
        assertEquals("Excluded files", 0, stats.excludedFileCount);

        // Test with exclusions
        Set<String> exclusions = new HashSet<>();
        exclusions.add(".log");
        exclusions.add(".tmp");

        RecursiveFileSize.TraversalStats statsFiltered = RecursiveFileSize.getDirectorySizeMutualRecursion(testDir,
                exclusions);

        assertEquals("Filtered size", 1000, statsFiltered.totalSize);
        assertEquals("Non-excluded files", 5, statsFiltered.fileCount);
        assertEquals("Excluded file count", 3, statsFiltered.excludedFileCount);
    }

    @Test
    public void testEmptyAndSingleFileDirectories() throws IOException {
        // Empty directory
        File emptyDir = new File(testDir, "empty");
        emptyDir.mkdir();
        long emptySize = RecursiveFileSize.getDirectorySize(emptyDir);
        assertEquals("Empty directory should have size 0", 0, emptySize);

        // Single file directory
        File singleFileDir = new File(testDir, "singleFile");
        singleFileDir.mkdir();
        createFile(singleFileDir, "only.txt", 500);
        long singleSize = RecursiveFileSize.getDirectorySize(singleFileDir);
        assertEquals("Single file directory", 500, singleSize);

        // Verify with mutual recursion
        RecursiveFileSize.TraversalStats stats = RecursiveFileSize.getDirectorySizeMutualRecursion(emptyDir, null);
        assertEquals("Empty via mutual recursion", 0, stats.totalSize);
        assertEquals("File count in empty", 0, stats.fileCount);
    }

    @Test
    public void testInputValidation() {
        // Test null folder
        try {
            RecursiveFileSize.getDirectorySize(null);
            fail("Should throw IllegalArgumentException for null");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test non-existent folder
        try {
            File nonExistent = new File("nonexistent_xyz_123");
            RecursiveFileSize.getDirectorySize(nonExistent);
            fail("Should throw IllegalArgumentException for non-existent");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test file instead of directory
        try {
            File file = new File(testDir, "notadir.txt");
            createFile(testDir, "notadir.txt", 100);
            RecursiveFileSize.getDirectorySize(file);
            fail("Should throw IllegalArgumentException for file");
        } catch (IllegalArgumentException | IOException e) {
            // Expected
        }
    }

    @Test
    public void testFormatSize() {
        // Bytes
        assertEquals("0 B", RecursiveFileSize.formatSize(0));
        assertEquals("500 B", RecursiveFileSize.formatSize(500));
        assertEquals("1023 B", RecursiveFileSize.formatSize(1023));

        // Kilobytes
        assertEquals("1.00 KB", RecursiveFileSize.formatSize(1024));
        assertEquals("1.50 KB", RecursiveFileSize.formatSize(1536));

        // Megabytes
        assertEquals("1.00 MB", RecursiveFileSize.formatSize(1024 * 1024));
        assertEquals("2.50 MB", RecursiveFileSize.formatSize(1024 * 1024 * 2 + 1024 * 512));

        // Gigabytes
        assertEquals("1.00 GB", RecursiveFileSize.formatSize(1024L * 1024L * 1024L));
    }

    @Test
    public void testConsistencyBetweenMethods() {
        // Basic and mutual recursion should give same results
        long basicSize = RecursiveFileSize.getDirectorySize(testDir);
        RecursiveFileSize.TraversalStats stats = RecursiveFileSize.getDirectorySizeMutualRecursion(testDir, null);
        assertEquals("Methods should agree", basicSize, stats.totalSize);

        // Test with exclusions
        Set<String> exclusions = new HashSet<>();
        exclusions.add(".class");
        exclusions.add(".log");

        long filteredSize = RecursiveFileSize.getDirectorySize(testDir, exclusions);
        RecursiveFileSize.TraversalStats filteredStats = RecursiveFileSize.getDirectorySizeMutualRecursion(testDir,
                exclusions);
        assertEquals("Filtered methods should agree", filteredSize, filteredStats.totalSize);

        // Multiple calls should give same result
        long size1 = RecursiveFileSize.getDirectorySize(testDir);
        long size2 = RecursiveFileSize.getDirectorySize(testDir);
        assertEquals("Multiple calls should be consistent", size1, size2);
    }

    @Test
    public void testEdgeCases() throws IOException {
        // Zero-byte files
        File zeroDir = new File(testDir, "zerofiles");
        zeroDir.mkdir();
        createFile(zeroDir, "empty1.txt", 0);
        createFile(zeroDir, "empty2.txt", 0);
        createFile(zeroDir, "nonempty.txt", 100);
        long zeroSize = RecursiveFileSize.getDirectorySize(zeroDir);
        assertEquals("Directory with zero-byte files", 100, zeroSize);

        // Case-insensitive exclusion
        File caseDir = new File(testDir, "casetest");
        caseDir.mkdir();
        createFile(caseDir, "file.LOG", 100);
        createFile(caseDir, "file.Log", 100);
        createFile(caseDir, "file.log", 100);
        createFile(caseDir, "file.txt", 100);

        Set<String> exclusions = new HashSet<>();
        exclusions.add(".log");
        long caseSize = RecursiveFileSize.getDirectorySize(caseDir, exclusions);
        assertEquals("Case-insensitive exclusion", 100, caseSize);

        // TraversalStats toString
        RecursiveFileSize.TraversalStats stats = RecursiveFileSize.getDirectorySizeMutualRecursion(testDir, null);
        String str = stats.toString();
        assertNotNull("Stats toString not null", str);
        assertTrue("Contains size info", str.contains("Total Size"));
    }
}
