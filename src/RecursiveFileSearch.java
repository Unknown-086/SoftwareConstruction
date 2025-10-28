import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecursiveFileSearch - Core search logic for recursive file searching.
 * 
 * This class provides the core functionality to search for files within a
 * directory
 * and all of its subdirectories using a recursive algorithm.
 * 
 * This class contains ONLY the search logic - no CLI/display code.
 * For command-line interface, see RecursiveFileSearchCLI.
 * 
 * Features:
 * - Single and multiple file search
 * - Case-sensitive and case-insensitive search
 * - Occurrence counting
 * 
 * @author Lab Eight
 * @version 3.0 (Refactored - Core Logic Only)
 */
public class RecursiveFileSearch {

    /**
     * Recursively searches for a file in a directory and its subdirectories.
     * Case-sensitive search (calls overloaded method with caseSensitive=true).
     * 
     * Base cases:
     * - If directory is null, throw IllegalArgumentException
     * - If fileName is null or empty, throw IllegalArgumentException
     * - If directory doesn't exist or isn't a directory, throw
     * IllegalArgumentException
     * - If directory is empty or not readable, return empty list
     * 
     * Recursive step:
     * - For each item in the directory:
     * - If it's a file and matches the fileName, add to results
     * - If it's a directory, recursively search in that directory
     * 
     * @param directory the directory to search in (must be a valid directory)
     * @param fileName  the name of the file to search for (must not be null or
     *                  empty)
     * @return a List of File objects representing all matching files found
     * @throws IllegalArgumentException if directory is null, fileName is null or
     *                                  empty,
     *                                  or directory does not exist or is not a
     *                                  directory
     */
    public static List<File> searchFile(File directory, String fileName) {
        return searchFile(directory, fileName, true);
    }

    /**
     * Recursively searches for a file in a directory and its subdirectories
     * with configurable case sensitivity.
     * 
     * Base cases:
     * - If directory is null, throw IllegalArgumentException
     * - If fileName is null or empty, throw IllegalArgumentException
     * - If directory doesn't exist or isn't a directory, throw
     * IllegalArgumentException
     * - If directory is empty or not readable, return empty list
     * 
     * Recursive step:
     * - For each item in the directory:
     * - If it's a file and matches the fileName (considering case sensitivity), add
     * to results
     * - If it's a directory, recursively search in that directory
     * 
     * @param directory     the directory to search in (must be a valid directory)
     * @param fileName      the name of the file to search for (must not be null or
     *                      empty)
     * @param caseSensitive whether the search should be case-sensitive
     * @return a List of File objects representing all matching files found
     * @throws IllegalArgumentException if directory is null, fileName is null or
     *                                  empty,
     *                                  or directory does not exist or is not a
     *                                  directory
     */
    public static List<File> searchFile(File directory, String fileName, boolean caseSensitive) {
        // Base case 1: Validate inputs
        if (directory == null) {
            throw new IllegalArgumentException("Directory cannot be null");
        }

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        // Base case 2: Check if directory exists and is actually a directory
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + directory.getPath());
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + directory.getPath());
        }

        // Initialize result list
        List<File> foundFiles = new ArrayList<>();

        // Base case 3: Check if directory is readable
        if (!directory.canRead()) {
            System.err.println("Warning: Cannot read directory: " + directory.getPath());
            return foundFiles; // Return empty list
        }

        // Get all files and subdirectories in the current directory
        File[] contents = directory.listFiles();

        // Base case 4: Empty directory
        if (contents == null || contents.length == 0) {
            return foundFiles; // Return empty list
        }

        // Recursive step: Process each item in the directory
        for (File item : contents) {
            try {
                if (item.isFile()) {
                    // Check if this file matches the search criteria
                    if (matchesFileName(item.getName(), fileName, caseSensitive)) {
                        foundFiles.add(item);
                    }
                } else if (item.isDirectory()) {
                    // Recursively search in subdirectory
                    List<File> subResults = searchFile(item, fileName, caseSensitive);
                    foundFiles.addAll(subResults);
                }
            } catch (SecurityException e) {
                // Handle permission issues gracefully
                System.err.println("Warning: Access denied to: " + item.getPath());
            }
        }

        return foundFiles;
    }

    /**
     * Helper method to check if a file name matches the search criteria.
     * 
     * @param actualFileName the actual file name to check
     * @param searchFileName the file name being searched for
     * @param caseSensitive  whether the comparison should be case-sensitive
     * @return true if the file names match according to the case sensitivity
     *         setting
     */
    private static boolean matchesFileName(String actualFileName, String searchFileName, boolean caseSensitive) {
        if (actualFileName == null || searchFileName == null) {
            return false;
        }

        if (caseSensitive) {
            return actualFileName.equals(searchFileName);
        } else {
            return actualFileName.equalsIgnoreCase(searchFileName);
        }
    }

    /**
     * Recursively searches for multiple files in a directory and its
     * subdirectories.
     * 
     * @param directory     the directory to search in (must be a valid directory)
     * @param fileNames     a list of file names to search for (must not be null or
     *                      empty)
     * @param caseSensitive whether the search should be case-sensitive
     * @return a Map where keys are file names and values are Lists of Files where
     *         found
     * @throws IllegalArgumentException if directory is null, fileNames is null or
     *                                  empty,
     *                                  or directory does not exist or is not a
     *                                  directory
     */
    public static Map<String, List<File>> searchMultipleFiles(File directory,
            List<String> fileNames,
            boolean caseSensitive) {
        // Validate inputs
        if (directory == null) {
            throw new IllegalArgumentException("Directory cannot be null");
        }

        if (fileNames == null || fileNames.isEmpty()) {
            throw new IllegalArgumentException("File names list cannot be null or empty");
        }

        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + directory.getPath());
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + directory.getPath());
        }

        // Create result map
        Map<String, List<File>> results = new HashMap<>();

        // Search for each file
        for (String fileName : fileNames) {
            if (fileName != null && !fileName.trim().isEmpty()) {
                List<File> foundFiles = searchFile(directory, fileName.trim(), caseSensitive);
                results.put(fileName.trim(), foundFiles);
            }
        }

        return results;
    }

    /**
     * Counts the number of times a specific file appears in a directory and
     * subdirectories.
     * 
     * Base cases:
     * - If directory is null, throw IllegalArgumentException
     * - If fileName is null or empty, throw IllegalArgumentException
     * - If directory doesn't exist or isn't a directory, throw
     * IllegalArgumentException
     * - If directory is empty or not readable, return 0
     * 
     * Recursive step:
     * - Initialize count = 0
     * - For each item in the directory:
     * - If it's a file and matches fileName, increment count
     * - If it's a directory, count += recursive call on that directory
     * - Return total count
     * 
     * @param directory     the directory to search in (must be a valid directory)
     * @param fileName      the name of the file to count (must not be null or
     *                      empty)
     * @param caseSensitive whether the search should be case-sensitive
     * @return the count of matching files found
     * @throws IllegalArgumentException if directory is null, fileName is null or
     *                                  empty,
     *                                  or directory does not exist or is not a
     *                                  directory
     */
    public static int countFileOccurrences(File directory, String fileName, boolean caseSensitive) {
        // Use the searchFile method and return the size of the result list
        // This reuses the existing logic and ensures consistency
        List<File> foundFiles = searchFile(directory, fileName, caseSensitive);
        return foundFiles.size();
    }
}
