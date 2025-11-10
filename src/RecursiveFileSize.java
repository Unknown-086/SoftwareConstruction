import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Task 2: Recursive File Size Calculator
 * 
 * This class provides recursive methods to calculate the total size of files
 * in a directory tree, with support for filtering file types and using
 * mutual recursion between traversal methods.
 */
public class RecursiveFileSize {

    // Default excluded file extensions (can be customized)
    private static final Set<String> DEFAULT_EXCLUDED_EXTENSIONS = new HashSet<>();

    static {
        DEFAULT_EXCLUDED_EXTENSIONS.add(".tmp");
        DEFAULT_EXCLUDED_EXTENSIONS.add(".log");
        DEFAULT_EXCLUDED_EXTENSIONS.add(".bak");
    }

    /**
     * Basic recursive method to compute directory size.
     * Includes all files without filtering.
     * 
     * @param folder the directory to analyze
     * @return total size in bytes
     * @throws IllegalArgumentException if folder is null or not a directory
     */
    public static long getDirectorySize(File folder) {
        // Input validation
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null");
        }
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + folder.getAbsolutePath());
        }

        return getDirectorySizeHelper(folder);
    }

    /**
     * Helper method for basic recursive directory size calculation.
     * 
     * @param folder the directory to analyze
     * @return total size in bytes
     */
    private static long getDirectorySizeHelper(File folder) {
        long totalSize = 0;

        File[] files = folder.listFiles();
        if (files == null) {
            // Permission denied or I/O error
            return 0;
        }

        for (File file : files) {
            if (file.isFile()) {
                // Base case: it's a file, add its size
                totalSize += file.length();
            } else if (file.isDirectory()) {
                // Recursive case: it's a directory, recurse into it
                totalSize += getDirectorySizeHelper(file);
            }
        }

        return totalSize;
    }

    /**
     * Enhanced version with file type exclusion.
     * 
     * @param folder             the directory to analyze
     * @param excludedExtensions set of file extensions to exclude (e.g., ".tmp",
     *                           ".log")
     * @return total size in bytes (excluding specified file types)
     * @throws IllegalArgumentException if folder is null or not a directory
     */
    public static long getDirectorySize(File folder, Set<String> excludedExtensions) {
        // Input validation
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null");
        }
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + folder.getAbsolutePath());
        }

        Set<String> exclusions = (excludedExtensions != null) ? excludedExtensions : new HashSet<>();
        return getDirectorySizeHelper(folder, exclusions);
    }

    /**
     * Helper method for directory size calculation with filtering.
     * 
     * @param folder             the directory to analyze
     * @param excludedExtensions set of file extensions to exclude
     * @return total size in bytes
     */
    private static long getDirectorySizeHelper(File folder, Set<String> excludedExtensions) {
        long totalSize = 0;

        File[] files = folder.listFiles();
        if (files == null) {
            return 0;
        }

        for (File file : files) {
            if (file.isFile()) {
                // Check if file should be excluded based on extension
                if (!shouldExcludeFile(file, excludedExtensions)) {
                    totalSize += file.length();
                }
            } else if (file.isDirectory()) {
                // Recursive case
                totalSize += getDirectorySizeHelper(file, excludedExtensions);
            }
        }

        return totalSize;
    }

    /**
     * Checks if a file should be excluded based on its extension.
     * 
     * @param file               the file to check
     * @param excludedExtensions set of extensions to exclude
     * @return true if file should be excluded, false otherwise
     */
    private static boolean shouldExcludeFile(File file, Set<String> excludedExtensions) {
        String fileName = file.getName();
        for (String ext : excludedExtensions) {
            if (fileName.toLowerCase().endsWith(ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // ========== Mutual Recursion Implementation ==========

    /**
     * Statistics for mutual recursion traversal.
     */
    public static class TraversalStats {
        public long totalSize = 0;
        public int fileCount = 0;
        public int directoryCount = 0;
        public int excludedFileCount = 0;

        @Override
        public String toString() {
            return String.format(
                    "Total Size: %s (%d bytes)\n" +
                            "Files: %d\n" +
                            "Directories: %d\n" +
                            "Excluded Files: %d",
                    formatSize(totalSize), totalSize, fileCount, directoryCount, excludedFileCount);
        }
    }

    /**
     * Main entry point for mutual recursion approach.
     * Uses traverseFolder() and processFile() that call each other.
     * 
     * @param folder             the directory to analyze
     * @param excludedExtensions set of file extensions to exclude
     * @return statistics object containing size and counts
     */
    public static TraversalStats getDirectorySizeMutualRecursion(File folder, Set<String> excludedExtensions) {
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null");
        }
        if (!folder.exists()) {
            throw new IllegalArgumentException("Folder does not exist: " + folder.getAbsolutePath());
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + folder.getAbsolutePath());
        }

        TraversalStats stats = new TraversalStats();
        Set<String> exclusions = (excludedExtensions != null) ? excludedExtensions : new HashSet<>();
        traverseFolder(folder, exclusions, stats);
        return stats;
    }

    /**
     * Mutual recursion method 1: Handles folder traversal.
     * Iterates through directory contents and delegates to processFile().
     * 
     * @param folder             the folder to traverse
     * @param excludedExtensions set of extensions to exclude
     * @param stats              statistics accumulator
     */
    private static void traverseFolder(File folder, Set<String> excludedExtensions, TraversalStats stats) {
        stats.directoryCount++;

        File[] files = folder.listFiles();
        if (files == null) {
            return;
        }

        // Process each entry in the directory
        for (File file : files) {
            processFile(file, excludedExtensions, stats);
        }
    }

    /**
     * Mutual recursion method 2: Processes individual files.
     * If it's a file, adds its size. If it's a directory, calls traverseFolder().
     * 
     * @param file               the file or directory to process
     * @param excludedExtensions set of extensions to exclude
     * @param stats              statistics accumulator
     */
    private static void processFile(File file, Set<String> excludedExtensions, TraversalStats stats) {
        if (file.isFile()) {
            // Base case: it's a file
            if (shouldExcludeFile(file, excludedExtensions)) {
                stats.excludedFileCount++;
            } else {
                stats.totalSize += file.length();
                stats.fileCount++;
            }
        } else if (file.isDirectory()) {
            // Recursive case: it's a directory, call traverseFolder()
            // This creates mutual recursion: processFile() -> traverseFolder() ->
            // processFile()
            traverseFolder(file, excludedExtensions, stats);
        }
    }

    // ========== Utility Methods for Formatting ==========

    /**
     * Formats a size in bytes to a human-readable format.
     * 
     * @param bytes size in bytes
     * @return formatted string (e.g., "1.5 MB", "250 KB")
     */
    public static String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * Displays directory size information in a formatted way.
     * 
     * @param folder      the folder analyzed
     * @param sizeInBytes total size in bytes
     */
    public static void displayDirectorySize(File folder, long sizeInBytes) {
        System.out.println("Directory: " + folder.getAbsolutePath());
        System.out.println("Total Size: " + formatSize(sizeInBytes) + " (" + sizeInBytes + " bytes)");
    }

    /**
     * Displays directory size with exclusion information.
     * 
     * @param folder             the folder analyzed
     * @param sizeInBytes        total size in bytes
     * @param excludedExtensions extensions that were excluded
     */
    public static void displayDirectorySize(File folder, long sizeInBytes, Set<String> excludedExtensions) {
        System.out.println("Directory: " + folder.getAbsolutePath());
        System.out.println("Total Size: " + formatSize(sizeInBytes) + " (" + sizeInBytes + " bytes)");
        if (excludedExtensions != null && !excludedExtensions.isEmpty()) {
            System.out.println("Excluded extensions: " + excludedExtensions);
        }
    }

    /**
     * Main method for demonstration.
     */
    public static void main(String[] args) {
        System.out.println("=== Recursive File Size Calculator ===\n");

        // Use current directory for demonstration
        File currentDir = new File(".");

        try {
            System.out.println("--- Method 1: Basic Recursive Approach ---");
            long size1 = getDirectorySize(currentDir);
            displayDirectorySize(currentDir, size1);
            System.out.println();

            System.out.println("--- Method 2: With File Type Exclusion ---");
            Set<String> exclusions = new HashSet<>();
            exclusions.add(".class");
            exclusions.add(".tmp");
            exclusions.add(".log");
            long size2 = getDirectorySize(currentDir, exclusions);
            displayDirectorySize(currentDir, size2, exclusions);
            System.out.println();

            System.out.println("--- Method 3: Mutual Recursion with Statistics ---");
            TraversalStats stats = getDirectorySizeMutualRecursion(currentDir, exclusions);
            System.out.println("Directory: " + currentDir.getAbsolutePath());
            System.out.println(stats);
            System.out.println();

            // Compare the methods
            System.out.println("=== Comparison ===");
            System.out.println("Size without exclusions: " + formatSize(size1));
            System.out.println("Size with exclusions: " + formatSize(size2));
            System.out.println("Size difference: " + formatSize(size1 - size2));
            System.out.println("Files excluded: " + stats.excludedFileCount);

            // Demonstrate with a specific subdirectory if it exists
            File srcDir = new File("src");
            if (srcDir.exists() && srcDir.isDirectory()) {
                System.out.println("\n=== Analyzing 'src' Directory ===");
                long srcSize = getDirectorySize(srcDir);
                displayDirectorySize(srcDir, srcSize);

                TraversalStats srcStats = getDirectorySizeMutualRecursion(srcDir, exclusions);
                System.out.println("\nDetailed Statistics:");
                System.out.println(srcStats);
            }

            // Demonstrate with test directory if it exists
            File testDir = new File("test");
            if (testDir.exists() && testDir.isDirectory()) {
                System.out.println("\n=== Analyzing 'test' Directory ===");
                long testSize = getDirectorySize(testDir);
                displayDirectorySize(testDir, testSize);

                TraversalStats testStats = getDirectorySizeMutualRecursion(testDir, exclusions);
                System.out.println("\nDetailed Statistics:");
                System.out.println(testStats);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
